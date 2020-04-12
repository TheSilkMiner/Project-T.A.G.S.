package net.thesilkminer.mc.prjtags.common.dymm

import com.aaronhowser1.dymm.JsonUtilities
import com.aaronhowser1.dymm.api.ApiBindings
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry
import com.aaronhowser1.dymm.api.loading.DocumentationLoader
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import net.minecraft.util.ResourceLocation
import net.minecraft.util.text.TextFormatting
import net.thesilkminer.mc.prjtags.MOD_ID

internal class TagDocumentationLoader : DocumentationLoader {
    private companion object {
        private val tagNamePattern = Regex("[a-z_]{2,}")
    }

    override fun getIdentifier() = ResourceLocation(MOD_ID, "tag")

    override fun loadFromJson(`object`: JsonObject) = this.load(`object`) // Bad naming, I don't want to deal with back-ticks everywhere

    override fun onGlobalLoadingStateUnbinding() {
        ApiBindings.getMainApi()
                .documentationRegistry
                .valuesCollection
                .asSequence()
                .filter { it is TagDocumentationEntry }
                .forEach { it.targets }
    }

    private fun load(jsonObject: JsonObject): DocumentationEntry? {
        if (!this.doConditionsPass(JsonUtilities.getJsonArrayOrElse(jsonObject, "conditions", ::JsonArray))) return null
        val targets = JsonUtilities.getJsonArray(jsonObject, "targets")
        val tagName = JsonUtilities.getString(jsonObject, "tag").checkName()
        val tagColor = JsonUtilities.getStringOrElse(jsonObject, "color") { "dark_aqua" }.checkColor()
        return TagDocumentationEntry(targets, tagName, tagColor)
    }

    private fun doConditionsPass(conditions: JsonArray) = JsonUtilities.checkEntriesAsJsonObjects(conditions, "conditions", this::doesConditionPass)

    private fun doesConditionPass(condition: JsonObject) =
            this.globalState().let { it.getConditionFactory(ResourceLocation(JsonUtilities.getString(condition, "type"))).fromJson(it, condition).canParse() }

    private fun String.checkName(): String {
        if (!this.matches(tagNamePattern)) {
            throw JsonSyntaxException("The tag name '$this' is not a valid tag name: must be all lowercase, contain no spaces, no digits, and has to be at least two characters long")
        }
        return this
    }

    private fun String.checkColor(): String {
        val textFormatting = TextFormatting.getValueByName(this) ?: throw JsonParseException("Unable to find color '$this'!")
        return when (textFormatting) {
            TextFormatting.OBFUSCATED, TextFormatting.BOLD, TextFormatting.STRIKETHROUGH, TextFormatting.UNDERLINE, TextFormatting.ITALIC, TextFormatting.RESET ->
                throw JsonParseException("The string '$this' does not represent a valid color")
            else -> this
        }
    }

    private fun globalState() = ApiBindings.getMainApi().currentLoadingState!!
}
