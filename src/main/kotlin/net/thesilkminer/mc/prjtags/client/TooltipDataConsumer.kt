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
import net.minecraftforge.common.MinecraftForge
import net.thesilkminer.mc.boson.api.distribution.Distribution
import net.thesilkminer.mc.boson.api.distribution.onlyOn
import net.thesilkminer.mc.boson.api.id.NameSpacedString
import net.thesilkminer.mc.boson.prefab.naming.toNameSpacedString
import net.thesilkminer.mc.boson.prefab.naming.toResourceLocation
import net.thesilkminer.mc.prjtags.MOD_ID

internal class TooltipDataConsumer : DocumentationDataConsumer {
    private companion object {
        private val TOOLTIP = NameSpacedString(MOD_ID, "tooltip")
        private val TAG_DATA = NameSpacedString(MOD_ID, "tag_data")
    }

    override fun getCompatibleTypes() = listOf(
            TOOLTIP.toResourceLocation(), // tooltip based on configuration
            TAG_DATA.toResourceLocation() // tag Data
    )

    override fun consumeData(data: DocumentationData, targets: Set<Target>) {
        if (data.type.toNameSpacedString() == TOOLTIP) this.consumeTooltip(data.data, targets)
        if (data.type.toNameSpacedString() == TAG_DATA) this.consumeTagData(data.data, targets)
    }

    override fun onCreation() {
        onlyOn(Distribution.CLIENT) { { MinecraftForge.EVENT_BUS.register(TooltipEventHandler) } }
    }

    private fun consumeTooltip(data: List<String>, targets: Set<Target>) {
        onlyOn(Distribution.CLIENT) { { TooltipEventHandler.populateWithData(data[0], data[1], targets) } }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun consumeTagData(data: List<String>, targets: Set<Target>) {
        // No-op for now: keeping it just in case it becomes useful
    }
}
