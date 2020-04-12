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
        onlyOn(Distribution.CLIENT) { { TooltipEventHandler.populateWithData(data[0], targets) } }
    }

    @Suppress("UNUSED_PARAMETER")
    private fun consumeTagData(data: List<String>, targets: Set<Target>) {
        // No-op for now: keeping it just in case it becomes useful
    }
}
