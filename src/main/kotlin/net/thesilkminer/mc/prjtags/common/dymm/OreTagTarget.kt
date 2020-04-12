package net.thesilkminer.mc.prjtags.common.dymm

import com.aaronhowser1.dymm.api.documentation.Target
import net.minecraft.item.ItemStack

internal class OreTagTarget(private val target: ItemStack) : Target {
    override fun obtainTarget() = this.target
}
