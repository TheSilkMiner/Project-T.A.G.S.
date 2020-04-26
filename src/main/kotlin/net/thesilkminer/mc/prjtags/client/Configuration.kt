@file:JvmName("C")

package net.thesilkminer.mc.prjtags.client

import net.thesilkminer.mc.boson.api.configuration.ConfigurationFormat
import net.thesilkminer.mc.boson.api.configuration.EntryType
import net.thesilkminer.mc.boson.api.configuration.configuration
import net.thesilkminer.mc.boson.api.distribution.Distribution
import net.thesilkminer.mc.prjtags.MOD_ID

val client = configuration {
    owner = MOD_ID
    name = "client"
    type = ConfigurationFormat.FORGE_CONFIG
    targetDistribution = Distribution.CLIENT

    categories {
        "display" {
            comment = "Manages where and how the tags should be displayed"
            languageKey = "prjtags.configuration.client.display"

            entries {
                "show_tooltip"(EntryType.BOOLEAN) {
                    comment = "If this is enabled, tags will show on the tooltips of items"
                    languageKey = "prjtags.configuration.client.display.show_tooltip"
                    default = true
                }
                "tooltips_only_in_jei"(EntryType.BOOLEAN) {
                    comment = "If this is enabled, tooltips will be shown only in the JustEnoughItems GUI, instead of everywhere.\nRequires show_tooltip to be set to true"
                    languageKey = "prjtags.configuration.client.display.tooltips_only_in_jei"
                    default = true
                }
            }
        }
        "experimental" {
            comment = "A set of experimental features that may or may not work: enable at own risk"
            languageKey = "prjtags.configuration.client.experimental"

            entries {
                "allow_tag_search"(EntryType.BOOLEAN) {
                    comment = "If enabled, tags will be searchable in JEI with a `_` prefix: note that this won't disable tag tooltips in JEI, even if they're effectively useless"
                    languageKey = "prjtags.configuration.client.experimental.allow_tag_search"
                    default = false
                }
                "jei_index_shift"(EntryType.WHOLE_NUMBER) {
                    comment = "Indicates at which index the tags should appear on the tooltip.\nDefault: 1. Cannot be negative.\nRequires tooltips_only_in_jei in 'display' to be true"
                    languageKey = "prjtags.configuration.client.experimental.jei_index_shift"
                    default = 1
                    bounds(0, Integer.MAX_VALUE - 1)
                }
            }
        }
    }
}
