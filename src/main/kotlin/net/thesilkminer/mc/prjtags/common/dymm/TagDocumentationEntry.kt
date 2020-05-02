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
import com.aaronhowser1.dymm.api.documentation.Dependency
import com.aaronhowser1.dymm.api.documentation.DocumentationData
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry
import com.aaronhowser1.dymm.api.documentation.Target
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParseException
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSyntaxException
import net.minecraft.util.ResourceLocation
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.registries.IForgeRegistryEntry
import net.thesilkminer.mc.boson.api.id.NameSpacedString
import net.thesilkminer.mc.prjtags.MOD_ID

internal class TagDocumentationEntry(targetsArray: JsonArray, tagName: String, tagColor: String) : IForgeRegistryEntry.Impl<DocumentationEntry>(), DocumentationEntry {
    @get:JvmName("$") private val targets by lazy { targetsArray.parseTargets() }
    private val data by lazy { tagName.createData(tagColor) }

    override fun getTargets(): Set<Target> = this.targets
    override fun getDocumentationData(): Set<DocumentationData> = this.data
    override fun getDependencies(): Set<Dependency> = setOf()

    private fun JsonArray.parseTargets(): Set<Target> {
        if (this.count() < 1) throw JsonParseException("A tag declaration must have at least one target!")
        return this.asSequence().map { it.convertToTarget() }.flatten().toSet()
    }

    private fun JsonElement.convertToTarget() = when {
        this.isJsonPrimitive -> this.asJsonPrimitive.convertToTarget()
        this.isJsonObject -> this.asJsonObject.convertToTarget()
        this.isJsonArray -> throw JsonSyntaxException("A target must either be a string or a JSON object, but instead an array was found!")
        this.isJsonNull -> throw JsonSyntaxException("Targets cannot be null!")
        else -> throw JsonParseException("Unable to discern the type for the JsonElement '$this': target cannot be parsed")
    }

    private fun JsonPrimitive.convertToTarget() =
            if (this.isString) this.asString.convertToTarget() else throw JsonSyntaxException("A target must either be a string or a JSON object, not a '$this'!")

    private fun String.convertToTarget() = when {
        this.startsWith('#') -> this.convertTagString()
        this.startsWith('@') -> this.convertOreString()
        else -> this.convertPlainString()
    }

    private fun String.convertPlainString(): Set<Target> {
        val lastData = this.splitToSequence(':').last()
        if (lastData.isEmpty()) throw JsonSyntaxException("For string '$this': a target string cannot end with a colon")
        if (lastData == "*") return this.removeSuffix(":*").convertWildcard()

        val metadata = lastData.toIntOrNull()
        return convertToItemObject(this.extractRegistryName(metadata), metadata).convertToTarget()
    }

    private fun String.convertWildcard(): Set<Target> {
        val obj = JsonObject()
        obj.addProperty("type", "minecraft:dynamic_nbt_item")
        obj.addProperty("registry_name", this)
        return obj.convertToTarget()
    }

    private fun String.extractRegistryName(metadata: Int?) = if (metadata == null) this else this.removeSuffix(":$metadata")

    private fun convertToItemObject(registryName: String, metadata: Int?): JsonObject {
        val obj = JsonObject()
        obj.addProperty("type", "minecraft:item")
        obj.addProperty("registry_name", registryName)
        if (metadata != null) obj.addProperty("metadata", metadata)
        return obj
    }

    private fun String.convertOreString(): Set<Target> = OreDictionary.getOres(this.removePrefix("@")).map { OreTagTarget(it) }.toSet()

    private fun String.convertTagString(): Set<Target> {
        val obj = JsonObject()
        obj.addProperty("type", "boson:tag")
        obj.addProperty("tag", this)
        return obj.convertToTarget()
    }

    private fun JsonObject.convertToTarget() =
            this@TagDocumentationEntry.globalState().let { it.getTargetFactory(ResourceLocation(JsonUtilities.getString(this, "type"))).fromJson(it, this) }.toSet()

    private fun String.createData(color: String): Set<DocumentationData> = setOf(this.createGenericData(color), this.createTooltipData(color))
    private fun String.createGenericData(color: String): DocumentationData = TagDocumentationData(NameSpacedString(MOD_ID, "tag_data"), listOf(this, color))
    private fun String.createTooltipData(color: String): DocumentationData = TagDocumentationData(NameSpacedString(MOD_ID, "tooltip"), listOf("prjtags.tag.$this.name", color))

    private fun globalState() = ApiBindings.getMainApi().currentLoadingState!!
}
