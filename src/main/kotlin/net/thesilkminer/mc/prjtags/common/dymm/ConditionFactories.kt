/*
 * Copyright (C) 2020  TheSilkMiner
 *
 * This file is part of Project T.A.G.S..
 *
 * Project T.A.G.S. is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Project T.A.G.S. is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Project T.A.G.S..  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact information:
 * E-mail: thesilkminer <at> outlook <dot> com
 */

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
