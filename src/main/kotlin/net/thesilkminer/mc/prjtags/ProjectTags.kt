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
import net.thesilkminer.mc.prjtags.common.update.startUpdateSchedule

@Mod(modid = MOD_ID, name = MOD_NAME, version = MOD_VERSION, dependencies = MOD_DEPENDENCIES,
        acceptedMinecraftVersions = MOD_MC_VERSION, certificateFingerprint = MOD_CERTIFICATE_FINGERPRINT,
        modLanguageAdapter = KOTLIN_LANGUAGE_ADAPTER, modLanguage = "kotlin")
object ProjectTags {
    private val l = L(MOD_NAME, "Main")

    @Mod.EventHandler
    fun onPreInitialization(e: FMLPreInitializationEvent) {
        l.info("PreInitialization")
        startUpdateSchedule()
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
