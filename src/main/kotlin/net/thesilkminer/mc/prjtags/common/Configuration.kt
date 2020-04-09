@file:JvmName("C")

package net.thesilkminer.mc.prjtags.common

import net.thesilkminer.mc.boson.api.configuration.ConfigurationFormat
import net.thesilkminer.mc.boson.api.configuration.EntryType
import net.thesilkminer.mc.boson.api.configuration.configuration
import net.thesilkminer.mc.prjtags.MOD_ID

val main = configuration {
    owner = MOD_ID
    name = "main"
    type = ConfigurationFormat.FORGE_CONFIG

    categories {
        "display" {
            comment = "Manages where and how the tags should be displayed"
            languageKey = "prjtags.configuration.common.display"

            entries {
                "show_tooltip"(EntryType.BOOLEAN) {
                    comment = "If this is enabled, tags will show on the tooltips of items"
                    languageKey = "prjtags.configuration.common.display.show_tooltip"
                    default = true
                }
                "tooltips_only_in_jei"(EntryType.BOOLEAN) {
                    comment = "If this is enabled, tooltips will be shown only in the JustEnoughItems GUI, instead of everywhere.\nRequires show_tooltip to be set to true"
                    languageKey = "prjtags.configuration.common.display.tooltips_only_in_jei"
                    default = false
                }
            }
        }
        "experimental" {
            comment = "A set of experimental features that may or may not work: enable at own risk"
            languageKey = "prjtags.configuration.common.experimental"

            entries {
                "allow_tag_search"(EntryType.BOOLEAN) {
                    comment = "If enabled, tags will be searchable in JEI with a `_` prefix: note that this won't disable tag tooltips in JEI, even if they're effectively useless"
                    languageKey = "prjtags.configuration.common.experimental.allow_tag_search"
                    default = false
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
                }
            }
        }
    }
}
