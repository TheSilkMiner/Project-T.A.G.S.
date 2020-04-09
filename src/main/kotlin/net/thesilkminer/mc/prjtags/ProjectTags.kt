package net.thesilkminer.mc.prjtags

import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.thesilkminer.mc.boson.api.fingerprint.logViolationMessage
import net.thesilkminer.mc.boson.api.log.L

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES,
        acceptedMinecraftVersions = MOD_MC_VERSION, certificateFingerprint = MOD_CERTIFICATE_FINGERPRINT,
        modLanguageAdapter = KOTLIN_LANGUAGE_ADAPTER, modLanguage = "kotlin")
object ProjectTags {
    private val l = L(MOD_NAME, "Main")

    @Mod.EventHandler
    fun onPreInitialization(event: FMLPreInitializationEvent) {
        l.info("Preinit")
    }

    @Mod.EventHandler
    fun onFingerprintViolation(event: FMLFingerprintViolationEvent) {
        logViolationMessage(MOD_NAME, event)
    }
}
