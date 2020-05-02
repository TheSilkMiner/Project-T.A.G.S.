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
