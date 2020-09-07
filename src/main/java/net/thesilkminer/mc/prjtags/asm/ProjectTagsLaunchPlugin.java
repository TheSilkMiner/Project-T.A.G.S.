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
                .setVersion("1.0.2")
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
        this.registerTransformer(new IngredientFilterTransformer(this));
        this.registerTransformer(new IngredientRendererTransformer(this));
    }
}
