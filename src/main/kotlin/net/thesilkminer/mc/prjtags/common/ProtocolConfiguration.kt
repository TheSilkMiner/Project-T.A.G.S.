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

@file:JvmName("PC")

package net.thesilkminer.mc.prjtags.common

import net.thesilkminer.mc.boson.api.configuration.ConfigurationFormat
import net.thesilkminer.mc.boson.api.configuration.EntryType
import net.thesilkminer.mc.boson.api.configuration.configuration
import net.thesilkminer.mc.boson.api.log.L
import net.thesilkminer.mc.prjtags.MOD_ID
import net.thesilkminer.mc.prjtags.MOD_NAME
import java.util.Base64
import kotlin.IllegalArgumentException

// OKAY, so, I figured I might as well address this due to the constant modding community drama I've seen floating
// around the interwebs.
// I've seen PlusTiC getting banned from CurseForge, and subsequently LandMaster, for good reasons, and that was fine by
// me.
// I've seen Funwayguy receiving backlash for lightly obfuscated code inside Better Questing that, as they openly
// admitted, it was just to protect and ban users that abused a bug in Better Questing's networking protocol. That was
// not fine by me at all: they had all the rights to do so and to stop bug abusing. Not only that, they had all the
// rights to try fixing the damage caused, via deopping, inventory clearing, quest reset etc. What I don't approve is
// the IP ban measures. But then again, they were doing that in good faith, and they received backlash.
// At this point, I am removing any instance of Base64 from this mod so that nobody can come to me and complain that I
// am obfuscating something that EVERYBODY can CHANGE in a GODDAMN CONFIGURATION FILE!
// Do you want to know the defaults? Grab a Base 64 decoder and read it! These are all references to GitHub.
// If you don't believe me, I've left the original code as a comment under here: check it with git blame, you'll see
// this hasn't been touched and it won't be touched ever again.
// I've added a Base64-config to a non-Base64-config now, so that even the previous mod versions will be updated to the
// GLORIOUS plain text.
// And now to my reasons for wanting Base64 for this file to be a thing in the first place:
// - only users that know what they're doing will mess around with the protocol data of this mod, which has a specific
//   format, mind you.
// - people that use a fork instead of the main repository will be less, otherwise the point of having a central
//   repository is defeated. TheSilkMiner/Project-T.A.G.S.-Repository is supposed to be the Maven Central for tags, but
//   whatever.
// - users can't come and complain that the mod broke because they changed api.github.com to github.com because they
//   thought it would be correct, and blame me for that. Deleting Base64 is more destructive, I know, but at this point
//   the error would be "invalid base64" instead of any other error, which means I have to spend less time fixing that
//   shit.
// - Base64 conveys the idea of DO NOT TOUCH THIS UNLESS YOU KNOW WHAT YOU ARE DOING better than any written sign.
//   People DON'T READ AT ALL: they can't even read an error that says "this mod caused the crash", imagine something
//   that isn't blocking their way and just appears on a tooltip sometimes. Base64 would guide them away because "I
//   don't know what this shit means".
// Now, you may argue security through obscurity is bad, but at this point, that is another topic and something I am not
// trying to do here. So whatever, enjoy your Base64-free code.
/*
val protocol = configuration {
    owner = MOD_ID
    name = "protocol"
    type = ConfigurationFormat.JSON

    categories {
        "protocol_data" {
            comment = "A set of data that identifies the protocol used for the updating system.\n\nDO NOT TOUCH UNLESS YOU KNOW WHAT YOU ARE DOING"
            languageKey = "prjtags.configuration.protocol.protocol_data"

            entries {
                "entry_point"(EntryType.STRING) {
                    comment = "The API entry point"
                    languageKey = "prjtags.configuration.protocol.protocol_data.entry_point"
                    default = "aHR0cHM6Ly9hcGkuZ2l0aHViLmNvbS8="
                }
                "query_data"(EntryType.STRING) {
                    comment = "The query data"
                    languageKey = "prjtags.configuration.protocol.protocol_data.query_data"
                    default = "cmVwb3MvOnVzZXIvOmFwcC9naXQvcmVmcy9oZWFkcy86dGFyZ2V0"
                }
                "allow_fallback"(EntryType.BOOLEAN) {
                    comment = "Allow Fallback?"
                    languageKey = "prjtags.configuration.protocol.protocol_data.allow_fallback"
                    default = true
                }
                "protocol_fallback"(EntryType.STRING) {
                    comment = "Protocol Fallback Policy"
                    languageKey = "prjtags.configuration.protocol.protocol_data.protocol_fallback"
                    default = "bWFzdGVy"
                }
                "protocol_connection_data"(EntryType.STRING) {
                    comment = "Protocol Connection Data"
                    languageKey = "prjtags.configuration.protocol.protocol_data.protocol_fallback"
                    default = "VGhlU2lsa01pbmVyJVByb2plY3QtVC5BLkcuUy4tUmVwb3NpdG9yeQ=="
                }
                "update_package"(EntryType.STRING) {
                    comment = "Update Package"
                    languageKey = "prjtags.configuration.protocol.protocol_data.update_package"
                    default = "aHR0cHM6Ly9naXRodWIuY29tLzp1c2VyLzphcHAvYXJjaGl2ZS86YnJhbmNoLnppcA=="
                }
            }
        }
    }
}
*/

val protocol = configuration {
    owner = MOD_ID
    name = "protocol"
    type = ConfigurationFormat.JSON

    categories {
        "protocol_data" {
            comment = "A set of data that identifies the protocol used for the updating system.\n\nDO NOT TOUCH UNLESS YOU KNOW WHAT YOU ARE DOING"
            languageKey = "prjtags.configuration.protocol.protocol_data"

            entries {
                "entry_point"(EntryType.STRING) {
                    comment = "The API entry point"
                    languageKey = "prjtags.configuration.protocol.protocol_data.entry_point"
                    default = "https://api.github.com/"
                }
                "query_data"(EntryType.STRING) {
                    comment = "The query data"
                    languageKey = "prjtags.configuration.protocol.protocol_data.query_data"
                    default = "repos/:user/:app/git/refs/heads/:target"
                }
                "allow_fallback"(EntryType.BOOLEAN) {
                    comment = "Allow Fallback?"
                    languageKey = "prjtags.configuration.protocol.protocol_data.allow_fallback"
                    default = true
                }
                "protocol_fallback"(EntryType.STRING) {
                    comment = "Protocol Fallback Policy"
                    languageKey = "prjtags.configuration.protocol.protocol_data.protocol_fallback"
                    default = "master"
                }
                "protocol_connection_data"(EntryType.STRING) {
                    comment = "Protocol Connection Data"
                    languageKey = "prjtags.configuration.protocol.protocol_data.protocol_fallback"
                    default = "TheSilkMiner%Project-T.A.G.S.-Repository"
                }
                "update_package"(EntryType.STRING) {
                    comment = "Update Package"
                    languageKey = "prjtags.configuration.protocol.protocol_data.update_package"
                    default = "https://github.com/:user/:app/archive/:branch.zip"
                }
                "b64"(EntryType.BOOLEAN) {
                    comment = "Base 64-decoded?"
                    languageKey = "prjtags.configuration.protocol.protocol_data.b64"
                    default = false
                }
            }
        }
    }
}

internal fun rewriteBase64Config() {
    val l = L(MOD_NAME, "Base64Config")
    val b64 = protocol["protocol_data"]["b64"]
    if (b64().boolean) return
    protocol["protocol_data"].entries.asSequence()
            .filter { it.type == EntryType.STRING }
            .forEach {
                try {
                    val data = it().string
                    if (data == it.default) {
                        // on first run, the configuration file will only have default data associated, which we know
                        // isn't b64-encoded.
                        throw IllegalArgumentException()
                    }
                    val deEncodedData = String(Base64.getDecoder().decode(data.toByteArray()))
                    l.info("De-encoded data from original Base 64 '$data' to actual value '$deEncodedData'")
                    it.currentValue = deEncodedData
                } catch (e: IllegalArgumentException) {
                    //
                }
            }
    b64.currentValue = true
    protocol.save()
    protocol.load()
}
