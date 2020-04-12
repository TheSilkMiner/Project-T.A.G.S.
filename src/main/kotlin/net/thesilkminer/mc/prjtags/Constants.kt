@file:JvmName("C")

package net.thesilkminer.mc.prjtags

import net.thesilkminer.mc.boson.api.modid.BOSON
import net.thesilkminer.mc.boson.api.modid.DOCUMENT_YOUR_MOD_MOD
import net.thesilkminer.mc.boson.api.modid.JUST_ENOUGH_ITEMS
import net.thesilkminer.mc.boson.api.modid.PROJECT_TAGS

internal const val MOD_ID = PROJECT_TAGS
internal const val MOD_NAME = "Project T.A.G.S."
internal const val MOD_VERSION = "@PRJ_TAGS_VERSION@"
internal const val MOD_MC_VERSION = "1.12.2"
internal const val MOD_CERTIFICATE_FINGERPRINT = "@FINGERPRINT@"

internal const val MOD_DEPENDENCIES = "required-after:forge@[14.23.5.2847,);" +
        "required-after:$BOSON" + /*@[0.1.0,)" + */ ";" +
        "required-after:$DOCUMENT_YOUR_MOD_MOD" + /*@[2.0.3,)" + */ ";" +
        "required-after:$JUST_ENOUGH_ITEMS@[4.15.0.293,)"
// TODO("Move away from @VERSION@ in mods and use @${MOD_NAME}_VERSION@ instead

@Suppress("SpellCheckingInspection")
internal const val KOTLIN_LANGUAGE_ADAPTER = "net.shadowfacts.forgelin.KotlinAdapter"
