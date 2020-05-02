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
import com.aaronhowser1.dymm.api.ApiBindings
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry
import com.aaronhowser1.dymm.api.loading.DocumentationLoader
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonSyntaxException
import net.minecraft.util.ResourceLocation
import net.thesilkminer.mc.prjtags.MOD_ID

internal class TagRemovalDocumentationLoader : DocumentationLoader {
    private data class RemovalTarget(val tag: String?, val targets: JsonArray?)

    private companion object {
        private val tagNamePattern = Regex("[a-z_]{2,}")
    }

    override fun getIdentifier() = ResourceLocation(MOD_ID, "remove")

    override fun loadFromJson(`object`: JsonObject) = this.load(`object`)

    private fun load(jsonObject: JsonObject): DocumentationEntry? {
        if (!this.doConditionsPass(JsonUtilities.getJsonArrayOrElse(jsonObject, "conditions", ::JsonArray))) return null
        val (tag, targets) = this.identifyRemovalTarget(jsonObject)
        return TagRemovalDocumentationEntry(targets, tag)
    }

    private fun doConditionsPass(conditions: JsonArray) = JsonUtilities.checkEntriesAsJsonObjects(conditions, "conditions", this::doesConditionPass)

    private fun doesConditionPass(condition: JsonObject) =
            this.globalState().let { it.getConditionFactory(ResourceLocation(JsonUtilities.getString(condition, "type"))).fromJson(it, condition).canParse() }

    private fun identifyRemovalTarget(jsonObject: JsonObject): RemovalTarget {
        val tagName = if (jsonObject.has("tag")) JsonUtilities.getString(jsonObject, "tag").checkName() else null
        val tagArray = if (jsonObject.has("targets")) JsonUtilities.getJsonArray(jsonObject, "targets").checkSize() else null
        return RemovalTarget(tagName, tagArray)
    }

    private fun String.checkName(): String {
        if (!this.matches(tagNamePattern)) {
            throw JsonSyntaxException("The tag name '$this' is not a valid tag name: must be all lowercase, contain no spaces, no digits, and has to be at least two characters long")
        }
        return this
    }

    private fun JsonArray.checkSize(): JsonArray {
        if (this.count() <= 0) throw JsonSyntaxException("Target amount must be positive, otherwise the removal is useless!")
        return this
    }

    private fun globalState() = ApiBindings.getMainApi().currentLoadingState!!
}
