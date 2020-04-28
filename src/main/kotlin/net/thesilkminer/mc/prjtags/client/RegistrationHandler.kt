package net.thesilkminer.mc.prjtags.client

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.relauncher.Side
import net.thesilkminer.mc.boson.api.event.ConfigurationRegisterEvent
import net.thesilkminer.mc.prjtags.MOD_ID

@Mod.EventBusSubscriber(modid = MOD_ID, value = [Side.CLIENT])
@Suppress("unused")
object RegistrationHandler {
    @SubscribeEvent
    @JvmStatic
    fun onConfigurationRegister(event: ConfigurationRegisterEvent) {
        event.configurationRegistry.registerConfigurations(client)
    }
}
