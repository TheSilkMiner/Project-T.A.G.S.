package net.thesilkminer.mc.prjtags.common.dymm

import com.aaronhowser1.dymm.JsonUtilities
import com.aaronhowser1.dymm.api.documentation.Condition
import com.aaronhowser1.dymm.api.loading.GlobalLoadingState
import com.aaronhowser1.dymm.api.loading.factory.ConditionFactory
import com.aaronhowser1.dymm.module.base.BasicCondition
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonSyntaxException
import net.thesilkminer.mc.prjtags.MOD_MC_VERSION
import net.thesilkminer.mc.prjtags.common.common

@Suppress("unused")
internal class PlatformVersionConditionFactory : ConditionFactory {
    override fun fromJson(state: GlobalLoadingState, `object`: JsonObject): Condition = BasicCondition(MOD_MC_VERSION == JsonUtilities.getString(`object`, "version"))
}

@Suppress("unused")
internal class SlugConditionFactory : ConditionFactory {
    override fun fromJson(state: GlobalLoadingState, `object`: JsonObject): Condition {
        val slugs = `object`.slugArray
        val allSlugs = sequenceOf(common["pack"]["slug"]().string).plus(common["pack"]["valid_slugs"]().asList()).toList()
        return BasicCondition(allSlugs.any { it in slugs })
    }

    private val JsonObject.slugArray get() = when {
        this.has("slug") -> this.readSingleSlug()
        this.has("slugs") -> this.readProbableSlugs()
        else -> throw JsonSyntaxException("Condition must have either a 'slug' or a 'slugs' property!")
    }

    private fun JsonObject.readSingleSlug() = this.readSingleSlug("slug")
    private fun JsonObject.readSingleSlug(propertyName: String) = listOf(JsonUtilities.getString(this, propertyName))
    private fun JsonObject.readProbableSlugs() = this["slugs"].let { if (it.isJsonArray) it.readSlugsArray() else this.readSingleSlug("slugs") }
    private fun JsonElement.readSlugsArray() = JsonUtilities.asJsonArray(this, "slugs").readSlugsArray()

    private fun JsonArray.readSlugsArray(): List<String> {
        val list = mutableListOf<String>()
        this.forEachIndexed { index, element -> list += JsonUtilities.asString(element, "slugs[$index]") }
        if (list.count() < 0) throw JsonParseException("Slug list must contain at least one slug")
        return list.toList()
    }
}
