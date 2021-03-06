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

package net.thesilkminer.mc.prjtags.client

import com.aaronhowser1.dymm.api.consume.DocumentationDataConsumer
import com.aaronhowser1.dymm.api.documentation.DocumentationData
import com.aaronhowser1.dymm.api.documentation.Target
import net.thesilkminer.mc.boson.api.distribution.Distribution
import net.thesilkminer.mc.boson.api.distribution.onlyOn
import net.thesilkminer.mc.boson.api.id.NameSpacedString
import net.thesilkminer.mc.boson.prefab.naming.toNameSpacedString
import net.thesilkminer.mc.boson.prefab.naming.toResourceLocation
import net.thesilkminer.mc.prjtags.MOD_ID

internal class RemovalTooltipDataConsumer : DocumentationDataConsumer {
    private companion object {
        private val REMOVAL_TAG_DATA = NameSpacedString(MOD_ID, "removal_tag_data")
        private val TAG_REMOVAL = NameSpacedString(MOD_ID, "tag_removal")
    }

    override fun getCompatibleTypes() = listOf(REMOVAL_TAG_DATA.toResourceLocation(), TAG_REMOVAL.toResourceLocation())

    override fun consumeData(data: DocumentationData, targets: Set<Target>) {
        data.type.let {
            if (it.toNameSpacedString() == REMOVAL_TAG_DATA) this.consumeData(data.data, targets)
            if (it.toNameSpacedString() == TAG_REMOVAL) this.identifyRemoval(data.data, targets)
        }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun consumeData(data: List<String>, targets: Set<Target>) {
        // No-op for now: keeping it just in case it becomes useful
    }

    private fun identifyRemoval(data: List<String>, targets: Set<Target>) {
        val (tagName, jsonValue) = data
        if (jsonValue == "null" && targets.count() > 0) throw IllegalStateException("Malformed data! $data $targets")
        if (tagName == "\$\$any") return this.removeByTargets(targets)
        this.removeByTagName(targets, tagName)
    }

    private fun removeByTagName(targets: Set<Target>, name: String) {
        onlyOn(Distribution.CLIENT) { { TooltipEventHandler.removeDataFor(name, targets) } }
    }

    private fun removeByTargets(targets: Set<Target>) {
        onlyOn(Distribution.CLIENT) { { TooltipEventHandler.removeDataFor("\$\$any", targets) } }
    }
}
