package net.thesilkminer.mc.prjtags.client

import com.aaronhowser1.dymm.api.ApiBindings
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry
import com.aaronhowser1.dymm.api.documentation.Target
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import net.minecraft.client.resources.I18n
import net.minecraft.item.ItemStack
import net.minecraft.util.text.TextFormatting
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.thesilkminer.kotlin.commons.lang.reloadableLazy
import net.thesilkminer.mc.boson.api.id.NameSpacedString
import net.thesilkminer.mc.boson.api.log.L
import net.thesilkminer.mc.boson.prefab.naming.toNameSpacedString
import net.thesilkminer.mc.prjtags.MOD_NAME
import net.thesilkminer.mc.prjtags.client.jei.JeiItemTooltipEvent
import net.thesilkminer.mc.prjtags.common.main
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

object TooltipEventHandler {
    private val l = L(MOD_NAME, "Tooltip Handler")

    private val showTooltips = reloadableLazy { main["display"]["show_tooltip"]().boolean }
    private val showOnlyInJei = reloadableLazy { main["display"]["tooltips_only_in_jei"]().boolean }

    private val targetEntries = mutableMapOf<Target, MutableList<Pair<TextFormatting, String>>>()

    private val documentationCache = CacheBuilder.newBuilder()
            .expireAfterAccess(5, TimeUnit.MINUTES)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(object: CacheLoader<NameSpacedString, List<DocumentationEntry>>() {
                override fun load(key: NameSpacedString): List<DocumentationEntry> =
                        ApiBindings.getMainApi()
                                .documentationRegistry
                                .valuesCollection
                                .filter { entry ->
                                    entry.targets.asSequence()
                                            .map(Target::obtainTarget)
                                            .map(ItemStack::getItem)
                                            .map { it.registryName?.toNameSpacedString() }
                                            .filterNotNull()
                                            .any { key == it }
                                }
            })

    private val tagsCache = CacheBuilder.newBuilder()
            .expireAfterAccess(3, TimeUnit.SECONDS)
            .expireAfterWrite(5, TimeUnit.SECONDS)
            .build(object: CacheLoader<ItemStack, List<Pair<TextFormatting, String>>>() {
                override fun load(key: ItemStack): List<Pair<TextFormatting, String>> {
                    val registryName = key.item.registryName?.toNameSpacedString() ?: return listOf()
                    val candidates = this@TooltipEventHandler.documentationCache[registryName]
                    val lines = mutableListOf<Pair<TextFormatting, String>>()

                    candidates.forEach { candidate ->
                        var matches = false
                        for (target in candidate.targets) {
                            if (matches) break
                            val stack = target.obtainTarget()

                            // First we check the items
                            matches = ItemStack.areItemsEqual(stack, key)

                            if (matches && stack.hasTagCompound()) {
                                // This item stack has a tag compound: we need to go deeper
                                matches = ItemStack.areItemStackTagsEqual(stack, key)

                                if (!matches) {
                                    // We are going to match if and only if the "difference"
                                    // between the two is just the display name, unless the
                                    // second stack has itself a display name
                                    if (key.hasDisplayName() && stack.hasDisplayName()) continue
                                    if (!key.hasDisplayName()) continue

                                    stack.setStackDisplayName(key.displayName)

                                    matches = ItemStack.areItemStackTagsEqual(stack, key)
                                }
                            }

                            if (matches) this@TooltipEventHandler.targetEntries[target]?.let { lines += it }
                        }
                    }

                    return lines
                }
            })

    init {
        l.info("Successfully initialized and registered tooltip event handler")
    }

    @SubscribeEvent
    fun onConfigReload(e: ConfigChangedEvent) {
        this.showTooltips.reload()
        this.showOnlyInJei.reload()
        l.info("Reloaded configuration")
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onGenericTooltipEvent(e: ItemTooltipEvent) = if (this.showTooltips.value && !this.showOnlyInJei.value) this.onTooltipEvent(e.itemStack, e.toolTip) else Unit

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onJeiTooltipEvent(e: JeiItemTooltipEvent) = if (this.showTooltips.value && this.showOnlyInJei.value) this.onTooltipEvent(e.stack, e.tooltip) else Unit

    private fun onTooltipEvent(stack: ItemStack, lines: MutableList<String>) {
        val tags = this.tagsCache[stack].map { "${it.first}#${I18n.format(it.second)}${TextFormatting.RESET}" }
        val additionalLines = Array(ceil(tags.count().toDouble() / 3.0).toInt()) { "" }
        tags.forEachIndexed { index, tag -> additionalLines[index / 3] += "$tag " }
        lines += additionalLines.toList()
        return
    }

    internal fun populateWithData(data: String, color: String, targets: Set<Target>) {
        targets.forEach { this.targetEntries.computeIfAbsent(it) { mutableListOf() } += Pair(color.convert(), data) }
    }

    private fun String.convert() = TextFormatting.getValueByName(this) ?: TextFormatting.DARK_AQUA
}
