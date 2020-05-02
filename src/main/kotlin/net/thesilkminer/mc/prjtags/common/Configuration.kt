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

package net.thesilkminer.mc.prjtags.common

import net.thesilkminer.mc.boson.api.configuration.ConfigurationFormat
import net.thesilkminer.mc.boson.api.configuration.EntryType
import net.thesilkminer.mc.boson.api.configuration.configuration
import net.thesilkminer.mc.prjtags.MOD_ID

val common = configuration {
    owner = MOD_ID
    name = "common"
    type = ConfigurationFormat.FORGE_CONFIG

    categories {
        "pack" {
            comment = "Manages settings for modpacks"
            languageKey = "prjtags.configuration.common.pack"

            entries {
                "slug"(EntryType.STRING) {
                    comment = "The slug that uniquely identifies this pack. Leave empty for no slug.\nBy convention, this corresponds to the CurseForge slug"
                    languageKey = "prjtags.configuration.common.pack.slug"
                    default = ""
                    requiresGameRestart()
                }
                "valid_slugs"(EntryType.LIST_OF_STRINGS) {
                    comment = "A set of other slugs that this pack wants to load even if they don't correspond to the actual slug.\nThere is no need to specify the pack slug again."
                    languageKey = "prjtags.configuration.common.pack.valid_slugs"
                    default = listOf<String>()
                    requiresGameRestart()
                }
            }
        }
        "update" {
            comment = "Manages automatic content updates from the central repository"
            languageKey = "prjtags.configuration.common.update"

            entries {
                "enable"(EntryType.BOOLEAN) {
                    comment = "Whether the automatic updates should be enabled or not. This setting must be set to true for it to work."
                    languageKey = "prjtags.configuration.common.update.enable"
                    default = true
                    requiresGameRestart()
                }
                "target_branch"(EntryType.STRING) {
                    comment = "Which branch the automatic updater should target when querying for updates.\nTo change repository, refer to `protocol.json`."
                    languageKey = "prjtags.configuration.common.update.target_branch"
                    default = "master"
                    requiresGameRestart()
                }
            }
        }
    }
}
