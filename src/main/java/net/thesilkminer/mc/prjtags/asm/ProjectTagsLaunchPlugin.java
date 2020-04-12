package net.thesilkminer.mc.prjtags.asm;

import com.google.common.collect.ImmutableSet;
import net.thesilkminer.mc.fermion.asm.api.PluginMetadata;
import net.thesilkminer.mc.fermion.asm.prefab.AbstractLaunchPlugin;

import javax.annotation.Nonnull;
import java.util.Set;

public final class ProjectTagsLaunchPlugin extends AbstractLaunchPlugin {
    public ProjectTagsLaunchPlugin() {
        super("prjtags.asm");
        this.registerTransformers();
    }

    @Override
    public void populateMetadata(@Nonnull final PluginMetadata.Builder builder) {
        builder.setName("Project T.A.G.S. ASM")
                .setDescription("Set of tweaks for Project T.A.G.S. that allow for more fine-tuned control with JEI")
                .setVersion("1.0.0")
                .addAuthor("TheSilkMiner")
                .setCredits("CritFlaw for the idea, Minecraft Pack Development for the ongoing support, aaronhowser1 for DYMM")
                .setLogoPath("project_tags.png");
    }

    @Nonnull
    @Override
    public Set<String> getRootPackages() {
        return ImmutableSet.of("net.thesilkminer.mc.prjtags.asm");
    }

    private void registerTransformers() {
        this.registerTransformer(new IngredientRendererTransformer(this));
    }
}
