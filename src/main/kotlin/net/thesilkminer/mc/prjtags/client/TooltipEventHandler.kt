package net.thesilkminer.mc.prjtags.client

import com.aaronhowser1.dymm.api.ApiBindings
import com.aaronhowser1.dymm.api.documentation.DocumentationEntry
import com.aaronhowser1.dymm.api.documentation.Target
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import net.minecraft.item.ItemStack
import net.minecraftforge.event.entity.player.ItemTooltipEvent
import net.minecraftforge.fml.client.event.ConfigChangedEvent
import net.minecraftforge.fml.common.eventhandler.EventPriority
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.thesilkminer.kotlin.commons.lang.reloadableLazy
import net.thesilkminer.mc.boson.api.id.NameSpacedString
import net.thesilkminer.mc.boson.api.locale.Color
import net.thesilkminer.mc.boson.api.locale.toLocale
import net.thesilkminer.mc.boson.api.log.L
import net.thesilkminer.mc.boson.prefab.naming.toNameSpacedString
import net.thesilkminer.mc.prjtags.MOD_NAME
import net.thesilkminer.mc.prjtags.client.jei.JeiItemTooltipEvent
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.ceil

object TooltipEventHandler {
    private val l = L(MOD_NAME, "Tooltip Handler")

    private val showTooltips = reloadableLazy { client["display"]["show_tooltip"]().boolean }
    private val showOnlyInJei = reloadableLazy { client["display"]["tooltips_only_in_jei"]().boolean }
    private val indexShift = reloadableLazy { client["experimental"]["jei_index_shift"]().int }

    private val targetEntries = mutableMapOf<Target, MutableList<Pair<Color, String>>>()

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
            .build(object: CacheLoader<ItemStack, List<Pair<Color, String>>>() {
                override fun load(key: ItemStack): List<Pair<Color, String>> {
                    val registryName = key.item.registryName?.toNameSpacedString() ?: return listOf()
                    val candidates = this@TooltipEventHandler.documentationCache[registryName]
                    val lines = mutableListOf<Pair<Color, String>>()

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
        this.indexShift.reload()
        l.info("Reloaded configuration")
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onGenericTooltipEvent(e: ItemTooltipEvent) = if (this.showTooltips.value && !this.showOnlyInJei.value) this.onTooltipEvent(e.itemStack, e.toolTip) else Unit

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    fun onJeiTooltipEvent(e: JeiItemTooltipEvent) = if (this.showTooltips.value && this.showOnlyInJei.value) this.onTooltipEvent(e.stack, e.tooltip, this.indexShift.value) else Unit

    private fun onTooltipEvent(stack: ItemStack, lines: MutableList<String>, index: Int = -1) {
        val tags = this.findFormattedTagNamesFor(stack)
        val additionalLines = Array(ceil(tags.count().toDouble() / 3.0).toInt()) { "" }
        tags.forEachIndexed { i, tag -> additionalLines[i / 3] += "$tag " }
        if (index < 0) {
            lines += additionalLines.toList()
            return
        }
        additionalLines.reversed().forEach { lines.add(index, it) }
    }

    private fun findTagsFor(stack: ItemStack) = this.tagsCache[stack]
    @Suppress("unused") fun findPlainTagNamesFor(stack: ItemStack) = this.findTagsFor(stack).map { it.second.toLocale().toLowerCase(Locale.ENGLISH) }
    @Suppress("WeakerAccess") internal fun findFormattedTagNamesFor(stack: ItemStack) =
            this.findTagsFor(stack).map { "#%s".toLocale(it.second.toLocale(), color = it.first) }

    internal fun populateWithData(data: String, color: String, targets: Set<Target>) {
        targets.forEach { this.targetEntries.computeIfAbsent(it) { mutableListOf() } += Pair(color.convert(), data) }
    }

    private fun String.convert() = when (this) {
        "aqua" -> Color.AQUA
        "black" -> Color.BLACK
        "blue" -> Color.BLUE
        "dark_aqua" -> Color.DARK_AQUA
        "dark_blue" -> Color.DARK_BLUE
        "dark_gray" -> Color.DARK_GRAY
        "dark_green" -> Color.DARK_GREEN
        "dark_purple" -> Color.DARK_PURPLE
        "dark_red" -> Color.DARK_RED
        "gold" -> Color.GOLD
        "gray" -> Color.GRAY
        "green" -> Color.GREEN
        "purple" -> Color.PURPLE
        "red" -> Color.RED
        "white" -> Color.WHITE
        "yellow" -> Color.YELLOW
        "light_purple" -> Color.PURPLE.also { l.warn("Color 'light_purple' is deprecated: use 'purple' instead!") }
        else -> Color.DEFAULT
    }
}
