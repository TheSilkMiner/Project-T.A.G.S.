package net.thesilkminer.mc.prjtags.client.jei

import net.minecraft.client.util.ITooltipFlag
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.player.PlayerEvent

class JeiItemTooltipEvent(val stack: ItemStack, val player: EntityPlayer?, val tooltip: MutableList<String>, val flags: ITooltipFlag) : PlayerEvent(player)
