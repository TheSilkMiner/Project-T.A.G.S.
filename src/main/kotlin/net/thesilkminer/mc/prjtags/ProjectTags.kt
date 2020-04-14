package net.thesilkminer.mc.prjtags

import com.aaronhowser1.dymm.api.ApiBindings
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.ProgressManager
import net.minecraftforge.fml.common.event.FMLFingerprintViolationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.thesilkminer.mc.boson.api.event.BosonPreAvailableEvent
import net.thesilkminer.mc.boson.api.fingerprint.logViolationMessage
import net.thesilkminer.mc.boson.api.log.L
import net.thesilkminer.mc.prjtags.common.dymm.TagDocumentationEntry

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES,
        acceptedMinecraftVersions = MOD_MC_VERSION, certificateFingerprint = MOD_CERTIFICATE_FINGERPRINT,
        modLanguageAdapter = KOTLIN_LANGUAGE_ADAPTER, modLanguage = "kotlin")
object ProjectTags {
    private val l = L(MOD_NAME, "Main")

    @Mod.EventHandler
    fun onPreInitialization(e: FMLPreInitializationEvent) {
        l.info("Preinit")
    }

    @Mod.EventHandler
    fun onBosonPreAvailable(e: BosonPreAvailableEvent) {
        l.info("Reloading tags")
        ProgressManager.push("Reloading tags", ApiBindings.getMainApi().documentationRegistry.valuesCollection.count()).let { bar ->
            ApiBindings.getMainApi()
                    .documentationRegistry
                    .valuesCollection
                    .asSequence()
                    .onEach { bar.step(it.registryName!!.toString()) }
                    .filter { it is TagDocumentationEntry }
                    .forEach { it.targets }
            ProgressManager.pop(bar)
        }
    }

    @Mod.EventHandler
    fun onFingerprintViolation(event: FMLFingerprintViolationEvent) {
        logViolationMessage(MOD_NAME, event)
    }
}
