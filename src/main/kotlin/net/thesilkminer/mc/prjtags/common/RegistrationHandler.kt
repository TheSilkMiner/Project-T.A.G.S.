package net.thesilkminer.mc.prjtags.common

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.thesilkminer.mc.boson.api.event.ConfigurationRegisterEvent
import net.thesilkminer.mc.prjtags.MOD_ID

@Mod.EventBusSubscriber(modid = MOD_ID)
object RegistrationHandler {
    @SubscribeEvent
    @JvmStatic
    fun onConfigurationRegister(event: ConfigurationRegisterEvent) {
        event.configurationRegistry.registerConfigurations(main)
    }
}
