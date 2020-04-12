package net.thesilkminer.mc.prjtags.asm;

import com.google.common.collect.ImmutableList;
import net.thesilkminer.mc.fermion.asm.api.LaunchPlugin;
import net.thesilkminer.mc.fermion.asm.api.MappingUtilities;
import net.thesilkminer.mc.fermion.asm.api.descriptor.ClassDescriptor;
import net.thesilkminer.mc.fermion.asm.api.descriptor.MethodDescriptor;
import net.thesilkminer.mc.fermion.asm.api.transformer.TransformerData;
import net.thesilkminer.mc.fermion.asm.prefab.transformer.SingleTargetMethodTransformer;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.BiFunction;

final class IngredientRendererTransformer extends SingleTargetMethodTransformer {
    IngredientRendererTransformer(@Nonnull final LaunchPlugin owner) {
        super(
                TransformerData.Builder.create()
                        .setOwningPlugin(owner)
                        .setName("item_stack_renderer")
                        .setDescription("Transforms ItemStackRenderer so that it fires an event to signal that the current tooltip is being rendered inside JEI")
                        .build(),
                ClassDescriptor.of("mezz.jei.plugins.vanilla.ingredients.item.ItemStackRenderer"),
                MethodDescriptor.of(
                        "getTooltip",
                        ImmutableList.of(
                                ClassDescriptor.of("net.minecraft.client.Minecraft"),
                                ClassDescriptor.of("net.minecraft.item.ItemStack"),
                                ClassDescriptor.of("net.minecraft.client.util.ITooltipFlag")),
                        ClassDescriptor.of(List.class)
                )
        );
    }

    @Nonnull
    @Override
    @SuppressWarnings("SpellCheckingInspection")
    protected BiFunction<Integer, MethodVisitor, MethodVisitor> getMethodVisitorCreator() {
        return (v, mv) -> new MethodVisitor(v, mv) {
            //  // access flags 0x1
            //  // signature (Lnet/minecraft/client/Minecraft;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/ITooltipFlag;)Ljava/util/List<Ljava/lang/String;>;
            //  // declaration: java.util.List<java.lang.String> getTooltip(net.minecraft.client.Minecraft, net.minecraft.item.ItemStack, net.minecraft.client.util.ITooltipFlag)
            //  public getTooltip(Lnet/minecraft/client/Minecraft;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/ITooltipFlag;)Ljava/util/List;
            //    TRYCATCHBLOCK L0 L1 L2 java/lang/RuntimeException
            //    TRYCATCHBLOCK L0 L1 L2 java/lang/LinkageError
            //    TRYCATCHBLOCK L3 L4 L5 java/lang/RuntimeException
            //    TRYCATCHBLOCK L3 L4 L5 java/lang/LinkageError
            //   L6
            //    LINENUMBER 39 L6
            //    ALOAD 1
            //    GETFIELD net/minecraft/client/Minecraft.field_71439_g : Lnet/minecraft/client/entity/EntityPlayerSP;
            //    ASTORE 4
            //   L0
            //    LINENUMBER 42 L0
            //    ALOAD 2
            //    ALOAD 4
            //    ALOAD 3
            //    INVOKEVIRTUAL net/minecraft/item/ItemStack.func_82840_a (Lnet/minecraft/entity/player/EntityPlayer;Lnet/minecraft/client/util/ITooltipFlag;)Ljava/util/List;
            //    ASTORE 5
            // <<< INJECTION BEGIN
            //   L800
            //    LINENUMBER 43 L800
            //    GETSTATIC net/minecraftforge/common/MinecraftForge.EVENT_BUS : Lnet/minecraftforge/fml/common/eventhandler/EventBus;
            //    NEW net/thesilkminer/mc/prjtags/client/jei/JeiItemTooltipEvent
            //    DUP
            //    ALOAD 2
            //    ALOAD 4
            //    ALOAD 5
            //    ALOAD 3
            //    INVOKESPECIAL net/thesilkminer/mc/prjtags/client/jei/JeiItemTooltipEvent.<init> (Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Ljava/util/List;Lnet/minecraft/client/util/ITooltipFlag;)V
            //    CHECKCAST net/minecraftforge/fml/common/eventhandler/Event
            //    INVOKEVIRTUAL net/minecraftforge/fml/common/eventhandler/EventBus.post (Lnet/minecraftforge/fml/common/eventhandler/Event;)Z
            //    POP
            // >>> INJECTION END
            //   L1
            //    LINENUMBER 49 L1
            //    GOTO L3
            //   L2
            //    LINENUMBER 43 L2
            //   FRAME FULL [mezz/jei/plugins/vanilla/ingredients/item/ItemStackRenderer net/minecraft/client/Minecraft net/minecraft/item/ItemStack net/minecraft/client/util/ITooltipFlag net/minecraft/entity/player/EntityPlayer] [java/lang/Throwable]
            //    ASTORE 6
            //   L7
            //    LINENUMBER 44 L7
            //    ALOAD 2
            //    INVOKESTATIC mezz/jei/util/ErrorUtil.getItemStackInfo (Lnet/minecraft/item/ItemStack;)Ljava/lang/String;
            //    ASTORE 7
            //   L8
            //    LINENUMBER 45 L8
            //    INVOKESTATIC mezz/jei/util/Log.get ()Lorg/apache/logging/log4j/Logger;
            //    LDC "Failed to get tooltip: {}"
            //    ALOAD 7
            //    ALOAD 6
            //    INVOKEINTERFACE org/apache/logging/log4j/Logger.error (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V (itf)
            //   L9
            //    LINENUMBER 46 L9
            //    NEW java/util/ArrayList
            //    DUP
            //    INVOKESPECIAL java/util/ArrayList.<init> ()V
            //    ASTORE 5
            //   L10
            //    LINENUMBER 47 L10
            //    ALOAD 5
            //    NEW java/lang/StringBuilder
            //    DUP
            //    INVOKESPECIAL java/lang/StringBuilder.<init> ()V
            //    GETSTATIC net/minecraft/util/text/TextFormatting.RED : Lnet/minecraft/util/text/TextFormatting;
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    LDC "jei.tooltip.error.crash"
            //    INVOKESTATIC mezz/jei/util/Translator.translateToLocal (Ljava/lang/String;)Ljava/lang/String;
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;
            //    INVOKEINTERFACE java/util/List.add (Ljava/lang/Object;)Z (itf)
            //    POP
            //   L11
            //    LINENUMBER 48 L11
            //    ALOAD 5
            //    ARETURN
            //   L3
            //    LINENUMBER 53 L3
            //   FRAME APPEND [java/util/List]
            //    ALOAD 2
            //    INVOKEVIRTUAL net/minecraft/item/ItemStack.func_77973_b ()Lnet/minecraft/item/Item;
            //    ALOAD 2
            //    INVOKEVIRTUAL net/minecraft/item/Item.getForgeRarity (Lnet/minecraft/item/ItemStack;)Lnet/minecraftforge/common/IRarity;
            //    ASTORE 6
            //   L4
            //    LINENUMBER 58 L4
            //    GOTO L12
            //   L5
            //    LINENUMBER 54 L5
            //   FRAME SAME1 java/lang/Throwable
            //    ASTORE 7
            //   L13
            //    LINENUMBER 55 L13
            //    ALOAD 2
            //    INVOKESTATIC mezz/jei/util/ErrorUtil.getItemStackInfo (Lnet/minecraft/item/ItemStack;)Ljava/lang/String;
            //    ASTORE 8
            //   L14
            //    LINENUMBER 56 L14
            //    INVOKESTATIC mezz/jei/util/Log.get ()Lorg/apache/logging/log4j/Logger;
            //    LDC "Failed to get rarity: {}"
            //    ALOAD 8
            //    ALOAD 7
            //    INVOKEINTERFACE org/apache/logging/log4j/Logger.error (Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V (itf)
            //   L15
            //    LINENUMBER 57 L15
            //    GETSTATIC net/minecraft/item/EnumRarity.COMMON : Lnet/minecraft/item/EnumRarity;
            //    ASTORE 6
            //   L12
            //    LINENUMBER 60 L12
            //   FRAME APPEND [net/minecraftforge/common/IRarity]
            //    ICONST_0
            //    ISTORE 7
            //   L16
            //   FRAME APPEND [I]
            //    ILOAD 7
            //    ALOAD 5
            //    INVOKEINTERFACE java/util/List.size ()I (itf)
            //    IF_ICMPGE L17
            //   L18
            //    LINENUMBER 61 L18
            //    ILOAD 7
            //    IFNE L19
            //   L20
            //    LINENUMBER 62 L20
            //    ALOAD 5
            //    ILOAD 7
            //    NEW java/lang/StringBuilder
            //    DUP
            //    INVOKESPECIAL java/lang/StringBuilder.<init> ()V
            //    ALOAD 6
            //    INVOKEINTERFACE net/minecraftforge/common/IRarity.getColor ()Lnet/minecraft/util/text/TextFormatting; (itf)
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    ALOAD 5
            //    ILOAD 7
            //    INVOKEINTERFACE java/util/List.get (I)Ljava/lang/Object; (itf)
            //    CHECKCAST java/lang/String
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;
            //    INVOKEINTERFACE java/util/List.set (ILjava/lang/Object;)Ljava/lang/Object; (itf)
            //    POP
            //    GOTO L21
            //   L19
            //    LINENUMBER 64 L19
            //   FRAME SAME
            //    ALOAD 5
            //    ILOAD 7
            //    NEW java/lang/StringBuilder
            //    DUP
            //    INVOKESPECIAL java/lang/StringBuilder.<init> ()V
            //    GETSTATIC net/minecraft/util/text/TextFormatting.GRAY : Lnet/minecraft/util/text/TextFormatting;
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/Object;)Ljava/lang/StringBuilder;
            //    ALOAD 5
            //    ILOAD 7
            //    INVOKEINTERFACE java/util/List.get (I)Ljava/lang/Object; (itf)
            //    CHECKCAST java/lang/String
            //    INVOKEVIRTUAL java/lang/StringBuilder.append (Ljava/lang/String;)Ljava/lang/StringBuilder;
            //    INVOKEVIRTUAL java/lang/StringBuilder.toString ()Ljava/lang/String;
            //    INVOKEINTERFACE java/util/List.set (ILjava/lang/Object;)Ljava/lang/Object; (itf)
            //    POP
            //   L21
            //    LINENUMBER 60 L21
            //   FRAME SAME
            //    IINC 7 1
            //    GOTO L16
            //   L17
            //    LINENUMBER 68 L17
            //   FRAME CHOP 1
            //    ALOAD 5
            //    ARETURN
            //   L22
            //    LOCALVARIABLE list Ljava/util/List; L1 L2 5
            //    // signature Ljava/util/List<Ljava/lang/String;>;
            //    // declaration: list extends java.util.List<java.lang.String>
            //    LOCALVARIABLE itemStackInfo Ljava/lang/String; L8 L3 7
            //    LOCALVARIABLE e Ljava/lang/Throwable; L7 L3 6
            //    LOCALVARIABLE rarity Lnet/minecraftforge/common/IRarity; L4 L5 6
            //    LOCALVARIABLE itemStackInfo Ljava/lang/String; L14 L12 8
            //    LOCALVARIABLE e Ljava/lang/Throwable; L13 L12 7
            //    LOCALVARIABLE k I L16 L17 7
            //    LOCALVARIABLE this Lmezz/jei/plugins/vanilla/ingredients/item/ItemStackRenderer; L6 L22 0
            //    LOCALVARIABLE minecraft Lnet/minecraft/client/Minecraft; L6 L22 1
            //    LOCALVARIABLE ingredient Lnet/minecraft/item/ItemStack; L6 L22 2
            //    LOCALVARIABLE tooltipFlag Lnet/minecraft/client/util/ITooltipFlag; L6 L22 3
            //    LOCALVARIABLE player Lnet/minecraft/entity/player/EntityPlayer; L0 L22 4
            //    LOCALVARIABLE list Ljava/util/List; L10 L22 5
            //    // signature Ljava/util/List<Ljava/lang/String;>;
            //    // declaration: list extends java.util.List<java.lang.String>
            //    LOCALVARIABLE rarity Lnet/minecraftforge/common/IRarity; L12 L22 6
            // <<< OVERWRITE BEGIN
            //    MAXSTACK = 5
            // === OVERWRITE WITH
            //    MAXSTACK = 7
            // >>> OVERWRITE END
            //    MAXLOCALS = 9

            private boolean foundGetTooltip = false;
            private boolean injected = false;

            @Override
            public void visitMethodInsn(final int opcode, @Nonnull final String owner, @Nonnull final String name, @Nonnull final String desc, final boolean itf) {
                super.visitMethodInsn(opcode, owner, name, desc, itf);
                if (this.injected) return;
                if (opcode == Opcodes.INVOKEVIRTUAL && "net/minecraft/item/ItemStack".equals(owner) && MappingUtilities.INSTANCE.mapMethod("func_82840_a").equals(name)) {
                    this.foundGetTooltip = true;
                }
            }

            @Override
            public void visitVarInsn(final int opcode, final int var) {
                super.visitVarInsn(opcode, var);
                if (this.injected) return;
                if (!this.foundGetTooltip) return;
                this.injected = true;

                final Label l800 = new Label();
                super.visitLabel(l800);
                super.visitLineNumber(4 * 10 + 3, l800);
                super.visitFieldInsn(Opcodes.GETSTATIC, "net/minecraftforge/common/MinecraftForge", "EVENT_BUS", "Lnet/minecraftforge/fml/common/eventhandler/EventBus;");
                super.visitTypeInsn(Opcodes.NEW, "net/thesilkminer/mc/prjtags/client/jei/JeiItemTooltipEvent");
                super.visitInsn(Opcodes.DUP);
                super.visitVarInsn(Opcodes.ALOAD, 2);
                super.visitVarInsn(Opcodes.ALOAD, 4);
                super.visitVarInsn(Opcodes.ALOAD, 5);
                super.visitVarInsn(Opcodes.ALOAD, 3);
                super.visitMethodInsn(Opcodes.INVOKESPECIAL, "net/thesilkminer/mc/prjtags/client/jei/JeiItemTooltipEvent", "<init>", "(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/player/EntityPlayer;Ljava/util/List;Lnet/minecraft/client/util/ITooltipFlag;)V", false);
                super.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraftforge/fml/common/eventhandler/Event");
                super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/minecraftforge/fml/common/eventhandler/EventBus", "post", "(Lnet/minecraftforge/fml/common/eventhandler/Event;)Z", false);
                super.visitInsn(Opcodes.POP);
            }

            @Override
            public void visitMaxs(final int maxStack, final int maxLocals) {
                super.visitMaxs(7, maxLocals);
            }
        };
    }
}
