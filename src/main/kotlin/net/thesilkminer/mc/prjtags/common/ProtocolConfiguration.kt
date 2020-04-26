@file:JvmName("PC")

package net.thesilkminer.mc.prjtags.common

import net.thesilkminer.mc.boson.api.configuration.ConfigurationFormat
import net.thesilkminer.mc.boson.api.configuration.EntryType
import net.thesilkminer.mc.boson.api.configuration.configuration
import net.thesilkminer.mc.prjtags.MOD_ID

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
