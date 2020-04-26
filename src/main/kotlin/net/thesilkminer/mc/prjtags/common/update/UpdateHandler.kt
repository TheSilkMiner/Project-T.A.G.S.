@file:JvmName("UH")

package net.thesilkminer.mc.prjtags.common.update

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.ProgressManager
import net.thesilkminer.mc.boson.api.distribution.Distribution
import net.thesilkminer.mc.boson.api.distribution.onlyOn
import net.thesilkminer.mc.boson.api.log.L
import net.thesilkminer.mc.prjtags.MOD_NAME
import net.thesilkminer.mc.prjtags.common.main
import net.thesilkminer.mc.prjtags.common.sql.LAST_COMMIT_QUERY
import net.thesilkminer.mc.prjtags.common.sql.StatusVariables
import net.thesilkminer.mc.prjtags.common.sql.transaction
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.update
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL
import java.nio.channels.Channels
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.Locale
import kotlin.reflect.KClass
import kotlin.streams.asSequence

private data class HeadCommit(val sha: String, val type: String, val url: String)
private data class HeadData(val ref: String, @SerializedName("node_id") val nodeId: String, val url: String, @SerializedName("object") val latestCommit: HeadCommit)
private data class UpdateResponse(val hasUpdates: Boolean, val remoteData: HeadData?, val localCommitData: String?, val targetedBranch: String)

private val l = L(MOD_NAME, "Self-Update Routine")

internal fun startUpdateSchedule() {
    l.info("Setting up self-update checks")
    val domain = Paths.get(".").resolve("./resources/assets/prjtags/")
    prepareDirectories(domain)
    if (!main["update"]["enable"]().boolean) {
        l.info("Aborting self-update schedule as requested through configuration file")
        return
    }
    l.info("Beginning self-updating routine")
    val bar = ProgressManager.push("Self-update Routine", 10)
    bar.step("Checking for updates")
    val updateCheck = checkForUpdates()
    if (updateCheck.hasUpdates) {
        runUpdateRoutine(updateCheck, domain, bar)
    } else {
        l.info("Data is already up-to-date")
    }
    while (bar.step < bar.steps) bar.step("")
    ProgressManager.pop(bar)
    l.info("Self-update completed")
}

private fun prepareDirectories(domain: Path) {
    Files.createDirectories(domain.resolve("./documentation/repo"))
    Files.createDirectories(domain.resolve("./documentation/user"))
    Files.createDirectories(domain.resolve("./lang"))
    l.info("Data directories successfully set up")
}

private fun checkForUpdates(): UpdateResponse {
    l.info("Checking for availability of updates")

    val storedCommit = transaction {
        StatusVariables
                .slice(StatusVariables.name, StatusVariables.varcharValue)
                .select { StatusVariables.name eq LAST_COMMIT_QUERY }
                .single()[StatusVariables.varcharValue]
    }

    var targetedBranch = main["update"]["target_branch"]().string

    val githubQuery = runGitHubQuery(HeadData::class) {
        val isDefaultBranch = targetedBranch == GH_DEF_BRANCH
        val logFunction = { it: String -> l.bigWarn(it) }
        queryHeadsAndRefs(targetedBranch, logFunction) ?: if (isDefaultBranch) null else queryHeadsAndRefs(GH_DEF_BRANCH, logFunction).also { targetedBranch = GH_DEF_BRANCH }
    }

    return UpdateResponse(
            hasUpdates = storedCommit == null || (githubQuery != null && storedCommit != githubQuery.latestCommit.sha),
            remoteData = githubQuery,
            localCommitData = storedCommit,
            targetedBranch = targetedBranch
    )
}

private fun <T : Any> runGitHubQuery(responseClass: KClass<T>, query: () -> String?): T? = Gson().fromJson(query(), responseClass.java)

private fun queryHeadsAndRefs(queryBranch: String, onError: (String) -> Unit) =
        runGhUrlQuery("repos/$GH_USER/$GH_APP/git/refs/heads/$queryBranch") {
            onError("An error has occurred while querying HEAD for target repository '@' branch '$queryBranch'\n${it::class.qualifiedName}: ${it.message}")
        }

private fun runGhUrlQuery(url: String, onError: (IOException) -> Unit) = runUrlQuery("https://api.github.com/$url", onError)
private fun runUrlQuery(url: String, onError: (IOException) -> Unit) = try { URL(url).readText() } catch (e: IOException) { onError(e).let { null } }

private fun runUpdateRoutine(updateResponse: UpdateResponse, domain: Path, bar: ProgressManager.ProgressBar) {
    if (updateResponse.localCommitData == null) {
        l.warn("Unable to find local commit data: running update routine anyway")
    }
    if (updateResponse.remoteData == null) {
        l.bigError("Unable to query the latest commit data! Are you connected to the Internet?\nThe self-updating procedure cannot be run!")
        return
    }

    l.info("Update routine found non-up-to-date data: upgrading from local commit '${updateResponse.localCommitData}' to remote commit '${updateResponse.remoteData.latestCommit.sha}'")

    bar.step("Downloading update package")
    val zipFileLocation = downloadRepositoryZip(domain, updateResponse.targetedBranch) ?: return

    bar.step("Clearing current repository")
    clearRepository(domain)

    bar.step("Saving local language files")
    val languages = saveLanguages(domain)

    bar.step("Extracting update package")
    val extractedLanguages = extractRepositoryDataToDomain(zipFileLocation, domain, updateResponse.targetedBranch)

    bar.step("Deleting update package")
    deleteZip(zipFileLocation)

    bar.step("Patching language files")
    val mergedLanguages = mergeLanguageChanges(extractedLanguages, languages)

    bar.step("Saving language files")
    saveLanguageFiles(mergedLanguages, domain)

    bar.step("Reloading language manager")
    triggerLanguageReloading()

    bar.step("Updating database entry")
    updateDatabaseEntry(updateResponse.remoteData.latestCommit.sha)

    l.info("Update routine completed")
}

private fun clearRepository(domain: Path) {
    val repo = domain.resolve("./documentation/repo").toAbsolutePath()
    l.info("Clearing '$repo' directory: your files WON'T BE TOUCHED!")
    repo.deleteRecursively()
}

private fun saveLanguages(domain: Path): Map<String, JsonObject> {
    val lang = domain.resolve("./lang").toAbsolutePath()
    l.info("Saving language JSONs saved in '$lang': these will be patched later on")
    val map = mutableMapOf<String, JsonObject>()
    Files.walk(lang).use { tree ->
        tree.asSequence()
                .filter { Files.isRegularFile(it) }
                .filter { it.fileName.toString().endsWith(".json") }
                .forEach {
                    val fileName = it.fileName.toString().removeSuffix(".json").toLowerCase(Locale.ENGLISH)
                    val fileContent = Files.newBufferedReader(it).use { reader -> reader.lineSequence().joinToString(separator = "\n") }
                    val jsonObject = Gson().fromJson(fileContent, JsonObject::class.java)
                    map[fileName] = jsonObject
                }
    }
    l.info("Successfully saved a total of '${map.count()}' languages: clearing directory")
    lang.deleteRecursively()
    return map.toMap()
}

private fun downloadRepositoryZip(domain: Path, targetedBranch: String): Path? = try {
    val temporaryPath = domain.resolve("./documentation/update_package.zip").toAbsolutePath()
    deleteZip(temporaryPath)
    l.info("Beginning download of update package: file will be saved in '$temporaryPath'")
    val bytes = URL("https://github.com/$GH_USER/$GH_APP/archive/$targetedBranch.zip").openStream().use { urlStream ->
        Channels.newChannel(urlStream).use { remoteChannel ->
            FileOutputStream(temporaryPath.toFile()).use { output ->
                output.channel.transferFrom(remoteChannel, 0, Long.MAX_VALUE)
            }
        }
    }
    l.info("Successfully downloaded a total of $bytes from the remote source into the target file")
    temporaryPath
} catch (e: IOException) {
    l.bigError("An error has occurred while downloading the update package! The updating process will be terminated!\n${e::class.qualifiedName}: ${e.message}")
    null
}

private fun extractRepositoryDataToDomain(zip: Path, domain: Path, branch: String): Map<String, JsonObject> {
    val repo = domain.resolve("./documentation/repo").toAbsolutePath()
    Files.createDirectories(repo)
    l.info("Extracting ZIP file in repository directory")
    val languagesMap = mutableMapOf<String, JsonObject>()
    FileSystems.newFileSystem(zip, null).use { fs ->
        val root = fs.getPath("/$GH_APP-${branch.replace('/', '-')}")
        val tags = root.resolve("./tags").normalize()
        val lang = root.resolve("./lang").normalize()
        Files.walk(tags).asSequence()
                .filter { Files.isRegularFile(it) }
                .forEach {
                    val relativePath = tags.relativize(it).normalize()
                    val outputPath = repo.resolve(relativePath.toString()).toAbsolutePath().normalize()
                    Files.newBufferedReader(it).use { input ->
                        Files.newBufferedWriter(outputPath, StandardOpenOption.CREATE_NEW).use { output ->
                            output.write(input.lineSequence().joinToString(separator = "\n"))
                        }
                    }
                }
        l.info("Successfully extracted all tags contents: attempting to read language JSONs")
        Files.walk(lang).asSequence()
                .filter { Files.isRegularFile(it) }
                .filter { it.fileName.toString().endsWith(".json") }
                .forEach {
                    val fileName = it.fileName.toString().removeSuffix(".json").toLowerCase(Locale.ENGLISH)
                    val fileContent = Files.newBufferedReader(it).use { reader -> reader.lineSequence().joinToString(separator = "\n") }
                    val jsonObject = Gson().fromJson(fileContent, JsonObject::class.java)
                    languagesMap[fileName] = jsonObject
                }
    }
    l.info("Successfully stored ${languagesMap.count()} language entries for subsequent merging")
    return languagesMap.toMap()
}

private fun deleteZip(path: Path) {
    path.deleteSafely()
    l.info("Deleted temporary ZIP file")
}

private fun mergeLanguageChanges(remote: Map<String, JsonObject>, local: Map<String, JsonObject>): Map<String, JsonObject> {
    val storage = mutableMapOf<String, JsonObject>()
    remote.forEach { (name, contents) -> storage[name] = mergeLanguageChanges(name, contents, local[name]) }
    local.asSequence().filterNot { storage.containsKey(it.key) }.forEach { (name, contents) -> storage[name] = mergeLanguageChanges(name, null, contents) }
    return storage.toMap()
}

private fun mergeLanguageChanges(name: String, remote: JsonObject?, local: JsonObject?): JsonObject {
    val returningJson = JsonObject()
    l.info("Beginning language merging for file '$name'")
    remote?.let { it.entrySet().forEach { (key, value) -> returningJson.add(key, value) } }
    local?.let {
        it.entrySet().forEachIndexed { index, (key, value) ->
            val actualKey = if (returningJson.has(key) && returningJson.get(key) != value) {
                val newName = "__conflict_resolution_$index.$key"
                l.warn("The entry '$key' is already present in the file '$name': probably an accepted PR? Renaming key to '$newName'")
                newName
            } else {
                key
            }
            returningJson.add(actualKey, value)
        }
    }
    l.info("Successfully merged all language entries for '$name'")
    return returningJson
}

private fun saveLanguageFiles(languageMap: Map<String, JsonObject>, domain: Path) {
    val languageDirectory = domain.resolve("./lang")
    Files.createDirectories(languageDirectory)
    languageMap.forEach { (name, content) -> saveLanguageFile(name, content, languageDirectory) }
}

private fun saveLanguageFile(name: String, content: JsonObject, languageDirectory: Path) {
    val targetPath = languageDirectory.resolve("./$name.json")
    val jsonWriter = GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create()
    l.info("Saving contents for language '$name'")
    Files.newBufferedWriter(targetPath, StandardOpenOption.CREATE_NEW).use {
        it.write(jsonWriter.toJson(content))
        it.write("\n")
    }
    l.info("Successfully saved contents for language '$name'")
}

private fun triggerLanguageReloading() {
    l.info("Reloading languages to account for changes: this may take some time")
    onlyOn(Distribution.CLIENT) {
        {
            Minecraft.getMinecraft().let { it.languageManager.onResourceManagerReload(it.resourceManager) }
        }
    }
    l.info("Reload completed")
}

private fun updateDatabaseEntry(remoteSha: String) {
    transaction {
        StatusVariables.update(where = { StatusVariables.name eq LAST_COMMIT_QUERY }) {
            it[this.varcharValue] = remoteSha
        }
    }
    l.info("Successfully updated database entry to remote commit target '$remoteSha'")
}

private fun Path.deleteRecursively() = Files.walk(this).use { it.asSequence().sortedDescending().forEach { path -> path.deleteSafely() } }
private fun Path.deleteSafely() = try { Files.delete(this) } catch (e: IOException) { l.error("Unable to delete '$this' due to '${e::class.qualifiedName}: ${e.message}'") }
