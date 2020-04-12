package net.thesilkminer.mc.prjtags.common.dymm

import com.aaronhowser1.dymm.api.documentation.DocumentationData
import net.thesilkminer.mc.boson.prefab.naming.toResourceLocation
import net.thesilkminer.mc.boson.api.id.NameSpacedString

internal class TagDocumentationData(private val type: NameSpacedString, private val data: List<String>) : DocumentationData {
    override fun getType() = this.type.toResourceLocation()
    override fun getData() = this.data
}
