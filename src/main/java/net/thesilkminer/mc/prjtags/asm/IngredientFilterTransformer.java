package net.thesilkminer.mc.prjtags.asm;

import net.thesilkminer.mc.fermion.asm.api.LaunchPlugin;
import net.thesilkminer.mc.fermion.asm.api.descriptor.ClassDescriptor;
import net.thesilkminer.mc.fermion.asm.api.transformer.TransformerData;
import net.thesilkminer.mc.fermion.asm.prefab.AbstractTransformer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BiFunction;

final class IngredientFilterTransformer extends AbstractTransformer {
    @SuppressWarnings("SpellCheckingInspection")
    private static final class ConstructorVisitor extends MethodVisitor {
        //  // access flags 0x1
        //  public <init>(Lmezz/jei/ingredients/IngredientBlacklistInternal;)V
        //   L0
        //    LINENUMBER 57 L0
        //    ALOAD 0
        //    INVOKESPECIAL java/lang/Object.<init> ()V
        //   L1
        //    LINENUMBER 48 L1
        //    ALOAD 0
        //    NEW it/unimi/dsi/fastutil/chars/Char2ObjectOpenHashMap
        //    DUP
        //    INVOKESPECIAL it/unimi/dsi/fastutil/chars/Char2ObjectOpenHashMap.<init> ()V
        //    PUTFIELD mezz/jei/ingredients/IngredientFilter.prefixedSearchTrees : Lit/unimi/dsi/fastutil/chars/Char2ObjectMap;
        //   L2
        //    LINENUMBER 54 L2
        //    ALOAD 0
        //    INVOKESTATIC java/util/Collections.emptyList ()Ljava/util/List;
        //    PUTFIELD mezz/jei/ingredients/IngredientFilter.ingredientListCached : Ljava/util/List;
        //   L3
        //    LINENUMBER 55 L3
        //    ALOAD 0
        //    NEW java/util/ArrayList
        //    DUP
        //    INVOKESPECIAL java/util/ArrayList.<init> ()V
        //    PUTFIELD mezz/jei/ingredients/IngredientFilter.listeners : Ljava/util/List;
        //   L4
        //    LINENUMBER 58 L4
        //    ALOAD 0
        //    ALOAD 1
        //    PUTFIELD mezz/jei/ingredients/IngredientFilter.blacklist : Lmezz/jei/ingredients/IngredientBlacklistInternal;
        //   L5
        //    LINENUMBER 59 L5
        //    ALOAD 0
        //    INVOKESTATIC net/minecraft/util/NonNullList.func_191196_a ()Lnet/minecraft/util/NonNullList;
        //    PUTFIELD mezz/jei/ingredients/IngredientFilter.elementList : Lnet/minecraft/util/NonNullList;
        //   L6
        //    LINENUMBER 60 L6
        //    ALOAD 0
        //    NEW mezz/jei/suffixtree/GeneralizedSuffixTree
        //    DUP
        //    INVOKESPECIAL mezz/jei/suffixtree/GeneralizedSuffixTree.<init> ()V
        //    PUTFIELD mezz/jei/ingredients/IngredientFilter.searchTree : Lmezz/jei/suffixtree/GeneralizedSuffixTree;
        //   L7
        //    LINENUMBER 61 L7
        //    ALOAD 0
        //    BIPUSH 64
        //    INVOKEDYNAMIC getMode()Lmezz/jei/ingredients/PrefixedSearchTree$IModeGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Lmezz/jei/config/Config$SearchMode;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/config/Config.getModNameSearchMode()Lmezz/jei/config/Config$SearchMode;,
        //      ()Lmezz/jei/config/Config$SearchMode;
        //    ]
        //    INVOKEDYNAMIC getStrings()Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      // handle kind 0x9 : INVOKEINTERFACE
        //      mezz/jei/gui/ingredients/IIngredientListElement.getModNameStrings()Ljava/util/Set; itf,
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;
        //    ]
        //    INVOKESPECIAL mezz/jei/ingredients/IngredientFilter.createPrefixedSearchTree (CLmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;)V
        //   L8
        //    LINENUMBER 62 L8
        //    ALOAD 0
        //    BIPUSH 35
        //    INVOKEDYNAMIC getMode()Lmezz/jei/ingredients/PrefixedSearchTree$IModeGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Lmezz/jei/config/Config$SearchMode;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/config/Config.getTooltipSearchMode()Lmezz/jei/config/Config$SearchMode;,
        //      ()Lmezz/jei/config/Config$SearchMode;
        //    ]
        //    INVOKEDYNAMIC getStrings()Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      // handle kind 0x9 : INVOKEINTERFACE
        //      mezz/jei/gui/ingredients/IIngredientListElement.getTooltipStrings()Ljava/util/List; itf,
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;
        //    ]
        //    INVOKESPECIAL mezz/jei/ingredients/IngredientFilter.createPrefixedSearchTree (CLmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;)V
        //   L9
        //    LINENUMBER 63 L9
        //    ALOAD 0
        //    BIPUSH 36
        //    INVOKEDYNAMIC getMode()Lmezz/jei/ingredients/PrefixedSearchTree$IModeGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Lmezz/jei/config/Config$SearchMode;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/config/Config.getOreDictSearchMode()Lmezz/jei/config/Config$SearchMode;,
        //      ()Lmezz/jei/config/Config$SearchMode;
        //    ]
        //    INVOKEDYNAMIC getStrings()Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      // handle kind 0x9 : INVOKEINTERFACE
        //      mezz/jei/gui/ingredients/IIngredientListElement.getOreDictStrings()Ljava/util/Collection; itf,
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;
        //    ]
        //    INVOKESPECIAL mezz/jei/ingredients/IngredientFilter.createPrefixedSearchTree (CLmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;)V
        //   L10
        //    LINENUMBER 64 L10
        //    ALOAD 0
        //    BIPUSH 37
        //    INVOKEDYNAMIC getMode()Lmezz/jei/ingredients/PrefixedSearchTree$IModeGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Lmezz/jei/config/Config$SearchMode;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/config/Config.getCreativeTabSearchMode()Lmezz/jei/config/Config$SearchMode;,
        //      ()Lmezz/jei/config/Config$SearchMode;
        //    ]
        //    INVOKEDYNAMIC getStrings()Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      // handle kind 0x9 : INVOKEINTERFACE
        //      mezz/jei/gui/ingredients/IIngredientListElement.getCreativeTabsStrings()Ljava/util/Collection; itf,
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;
        //    ]
        //    INVOKESPECIAL mezz/jei/ingredients/IngredientFilter.createPrefixedSearchTree (CLmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;)V
        //   L11
        //    LINENUMBER 65 L11
        //    ALOAD 0
        //    BIPUSH 94
        //    INVOKEDYNAMIC getMode()Lmezz/jei/ingredients/PrefixedSearchTree$IModeGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Lmezz/jei/config/Config$SearchMode;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/config/Config.getColorSearchMode()Lmezz/jei/config/Config$SearchMode;,
        //      ()Lmezz/jei/config/Config$SearchMode;
        //    ]
        //    INVOKEDYNAMIC getStrings()Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      // handle kind 0x9 : INVOKEINTERFACE
        //      mezz/jei/gui/ingredients/IIngredientListElement.getColorStrings()Ljava/util/Collection; itf,
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;
        //    ]
        //    INVOKESPECIAL mezz/jei/ingredients/IngredientFilter.createPrefixedSearchTree (CLmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;)V
        //   L12
        //    LINENUMBER 66 L12
        //    ALOAD 0
        //    BIPUSH 38
        //    INVOKEDYNAMIC getMode()Lmezz/jei/ingredients/PrefixedSearchTree$IModeGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Lmezz/jei/config/Config$SearchMode;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/config/Config.getResourceIdSearchMode()Lmezz/jei/config/Config$SearchMode;,
        //      ()Lmezz/jei/config/Config$SearchMode;
        //    ]
        //    INVOKEDYNAMIC getStrings()Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/ingredients/IngredientFilter.lambda$new$0(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;
        //    ]
        //    INVOKESPECIAL mezz/jei/ingredients/IngredientFilter.createPrefixedSearchTree (CLmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;)V
        // <<< INJECTION BEGIN
        //   L800
        //    LINENUMBER 67 L800
        //    ALOAD 0
        //    BIPUSH 95
        //    INVOKEDYNAMIC getMode()Lmezz/jei/ingredients/PrefixedSearchTree$IModeGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      ()Lmezz/jei/config/Config$SearchMode;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/ingredients/IngredientFilter.<fermion-lambda:new:10>()Lmezz/jei/config/Config$SearchMode;,
        //      ()Lmezz/jei/config/Config$SearchMode;
        //    ]
        //    INVOKEDYNAMIC getStrings()Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter; [
        //      // handle kind 0x6 : INVOKESTATIC
        //      java/lang/invoke/LambdaMetafactory.metafactory(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;
        //      // arguments:
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      // handle kind 0x6 : INVOKESTATIC
        //      mezz/jei/ingredients/IngredientFilter.<fermion-lambda:new:11>(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;,
        //      (Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;
        //    ]
        //    INVOKESPECIAL mezz/jei/ingredients/IngredientFilter.createPrefixedSearchTree (CLmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;)V
        // >>> INJECTION END
        //   L13
        //    LINENUMBER 68 L13
        //    ALOAD 0
        //    ALOAD 0
        //    GETFIELD mezz/jei/ingredients/IngredientFilter.searchTree : Lmezz/jei/suffixtree/GeneralizedSuffixTree;
        //    ALOAD 0
        //    GETFIELD mezz/jei/ingredients/IngredientFilter.prefixedSearchTrees : Lit/unimi/dsi/fastutil/chars/Char2ObjectMap;
        //    INVOKEINTERFACE it/unimi/dsi/fastutil/chars/Char2ObjectMap.values ()Lit/unimi/dsi/fastutil/objects/ObjectCollection; (itf)
        //    INVOKESTATIC mezz/jei/ingredients/IngredientFilter.buildCombinedSearchTrees (Lmezz/jei/suffixtree/ISearchTree;Ljava/util/Collection;)Lmezz/jei/suffixtree/CombinedSearchTrees;
        //    PUTFIELD mezz/jei/ingredients/IngredientFilter.combinedSearchTrees : Lmezz/jei/suffixtree/CombinedSearchTrees;
        //   L14
        //    LINENUMBER 69 L14
        //    ALOAD 0
        //    NEW mezz/jei/ingredients/IngredientFilterBackgroundBuilder
        //    DUP
        //    ALOAD 0
        //    GETFIELD mezz/jei/ingredients/IngredientFilter.prefixedSearchTrees : Lit/unimi/dsi/fastutil/chars/Char2ObjectMap;
        //    ALOAD 0
        //    GETFIELD mezz/jei/ingredients/IngredientFilter.elementList : Lnet/minecraft/util/NonNullList;
        //    INVOKESPECIAL mezz/jei/ingredients/IngredientFilterBackgroundBuilder.<init> (Lit/unimi/dsi/fastutil/chars/Char2ObjectMap;Lnet/minecraft/util/NonNullList;)V
        //    PUTFIELD mezz/jei/ingredients/IngredientFilter.backgroundBuilder : Lmezz/jei/ingredients/IngredientFilterBackgroundBuilder;
        //   L15
        //    LINENUMBER 70 L15
        //    RETURN
        //   L16
        //    LOCALVARIABLE this Lmezz/jei/ingredients/IngredientFilter; L0 L16 0
        //    LOCALVARIABLE blacklist Lmezz/jei/ingredients/IngredientBlacklistInternal; L0 L16 1
        //    MAXSTACK = 5
        //    MAXLOCALS = 2

        private boolean foundBiPushForAmpersand;
        private boolean foundConfigIndyForResourceId;
        private boolean foundSpecialSearchIndy;
        private boolean injected;

        private ConstructorVisitor(final int version, @Nonnull final MethodVisitor parent) {
            super(version, parent);
        }

        @Override
        public void visitIntInsn(final int opcode, final int operand) {
            super.visitIntInsn(opcode, operand);
            if (this.injected) return;
            if (opcode == Opcodes.BIPUSH && operand == 38) {
                this.foundBiPushForAmpersand = true; // 38 == '&'
                LOGGER.info("Found 'BIPUSH 38'");
            }
        }

        @Override
        public void visitInvokeDynamicInsn(@Nonnull final String name, @Nonnull final String desc, @Nonnull final Handle bsm, @Nonnull final Object... bsmArgs) {
            super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
            if (this.injected) return;
            if (!this.foundBiPushForAmpersand) return;
            if (this.foundConfigIndyForResourceId && this.isIndyForSearch(bsmArgs)) {
                this.foundSpecialSearchIndy = true;
                LOGGER.info("Found INVOKEDYNAMIC instruction for 'IngredientFilter.lambda$new$0'");
                return;
            }
            if (this.isIndyForConfig(bsmArgs)) {
                this.foundConfigIndyForResourceId = true;
                LOGGER.info("Found INVOKEDYNAMIC instruction for 'Config.getResourceIdSearchMode'");
            }
        }

        @Override
        public void visitMethodInsn(final int opcode, @Nonnull final String owner, @Nonnull final String name, @Nonnull final String desc, final boolean itf) {
            super.visitMethodInsn(opcode, owner, name, desc, itf);
            if (this.injected) return;
            if (!this.foundBiPushForAmpersand) return;
            if (!this.foundConfigIndyForResourceId) return;
            if (!this.foundSpecialSearchIndy) return;
            this.injected = true;
            this.inject();
        }

        private boolean isIndyForSearch(@Nonnull final Object... bsmArgs) {
            if (bsmArgs.length < 2) return false;
            final Object secondArg = bsmArgs[1];
            if (!(secondArg instanceof Handle)) return false;
            final Handle targetHandle = (Handle) secondArg;
            return Opcodes.H_INVOKESTATIC == targetHandle.getTag()
                    && "mezz/jei/ingredients/IngredientFilter".equals(targetHandle.getOwner())
                    && "lambda$new$0".equals(targetHandle.getName())
                    && "(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;".equals(targetHandle.getDesc());
        }

        private boolean isIndyForConfig(@Nonnull final Object... bsmArgs) {
            if (bsmArgs.length < 2) return false;
            final Object secondArg = bsmArgs[1];
            if (!(secondArg instanceof Handle)) return false;
            final Handle targetHandle = (Handle) secondArg;
            return Opcodes.H_INVOKESTATIC == targetHandle.getTag()
                    && "mezz/jei/config/Config".equals(targetHandle.getOwner())
                    && "getResourceIdSearchMode".equals(targetHandle.getName())
                    && "()Lmezz/jei/config/Config$SearchMode;".equals(targetHandle.getDesc());
        }

        private void inject() {
            LOGGER.info("Found target injection point: beginning injection");

            final Label l800 = new Label();
            super.visitLabel(l800);
            super.visitLineNumber(6 * 10 + 7, l800);
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitIntInsn(Opcodes.BIPUSH, 95);
            super.visitInvokeDynamicInsn("getMode", "()Lmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;",
                    new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false),
                    Type.getType("()Lmezz/jei/config/Config$SearchMode;"),
                    new Handle(Opcodes.H_INVOKESTATIC, THIS, CONFIG_LAMBDA, "()Lmezz/jei/config/Config$SearchMode;", false),
                    Type.getType("()Lmezz/jei/config/Config$SearchMode;"));
            super.visitInvokeDynamicInsn("getStrings", "()Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;",
                    new Handle(Opcodes.H_INVOKESTATIC, "java/lang/invoke/LambdaMetafactory", "metafactory", "(Ljava/lang/invoke/MethodHandles$Lookup;Ljava/lang/String;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodType;Ljava/lang/invoke/MethodHandle;Ljava/lang/invoke/MethodType;)Ljava/lang/invoke/CallSite;", false),
                    Type.getType("(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;"),
                    new Handle(Opcodes.H_INVOKESTATIC, THIS, STRINGS_LAMBDA, "(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;", false),
                    Type.getType("(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;"));
            super.visitMethodInsn(Opcodes.INVOKESPECIAL, THIS, "createPrefixedSearchTree", "(CLmezz/jei/ingredients/PrefixedSearchTree$IModeGetter;Lmezz/jei/ingredients/PrefixedSearchTree$IStringsGetter;)V", false);
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static final class ModeGetterMethodVisitor extends MethodVisitor {
        //  // access flags 0x101A
        //  private static final synthetic <fermion-lambda:new:10>()Lmezz/jei/config/Config$SearchMode;
        //   L0
        //    LINENUMBER 450 L0
        //    INVOKESTATIC net/thesilkminer/mc/prjtags/client/C.getClient ()Lnet/thesilkminer/mc/boson/api/configuration/Configuration;
        //    LDC "experimental"
        //    ICONST_0
        //    ANEWARRAY java/lang/String
        //    INVOKEINTERFACE net/thesilkminer/mc/boson/api/configuration/Configuration.get (Ljava/lang/String;[Ljava/lang/String;)Lnet/thesilkminer/mc/boson/api/configuration/Category; (itf)
        //    LDC "allow_tag_search"
        //    INVOKEINTERFACE net/thesilkminer/mc/boson/api/configuration/Category.get (Ljava/lang/String;)Lnet/thesilkminer/mc/boson/api/configuration/Entry; (itf)
        //    INVOKEINTERFACE net/thesilkminer/mc/boson/api/configuration/Entry.invoke ()Lnet/thesilkminer/mc/boson/api/configuration/Entry$View; (itf)
        //    INVOKEVIRTUAL net/thesilkminer/mc/boson/api/configuration/Entry$View.getBoolean ()Z
        //    IFEQ L1
        //   L2
        //    GETSTATIC mezz/jei/config/Config$SearchMode.REQUIRE_PREFIX : Lmezz/jei/config/Config$SearchMode;
        //    GOTO L3
        //   L1
        //   FRAME SAME
        //    GETSTATIC mezz/jei/config/Config$SearchMode.DISABLED : Lmezz/jei/config/Config$SearchMode;
        //   L3
        //   FRAME SAME1 mezz/jei/config/Config$SearchMode
        //    ARETURN
        //   L4
        //   MAXSTACK = 3
        //   MAXLOCALS = 0

        private ModeGetterMethodVisitor(final int version, @Nonnull final MethodVisitor parent) {
            super(version, parent);
        }

        @Override
        public void visitCode() {
            super.visitCode();

            final Label l0 = new Label();
            final Label l1 = new Label();
            super.visitLabel(l0);
            super.visitLineNumber(4 * 100 + 5 * 10, l0);
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "net/thesilkminer/mc/prjtags/client/C", "getClient", "()Lnet/thesilkminer/mc/boson/api/configuration/Configuration;", false);
            super.visitLdcInsn("experimental");
            super.visitInsn(Opcodes.ICONST_0);
            super.visitTypeInsn(Opcodes.ANEWARRAY, "java/lang/String");
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "net/thesilkminer/mc/boson/api/configuration/Configuration", "get", "(Ljava/lang/String;[Ljava/lang/String;)Lnet/thesilkminer/mc/boson/api/configuration/Category;", true);
            super.visitLdcInsn("allow_tag_search");
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "net/thesilkminer/mc/boson/api/configuration/Category", "get", "(Ljava/lang/String;)Lnet/thesilkminer/mc/boson/api/configuration/Entry;", true);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "net/thesilkminer/mc/boson/api/configuration/Entry", "invoke", "()Lnet/thesilkminer/mc/boson/api/configuration/Entry$View;", true);
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/thesilkminer/mc/boson/api/configuration/Entry$View", "getBoolean", "()Z", false);
            super.visitJumpInsn(Opcodes.IFEQ, l1);

            final Label l2 = new Label();
            final Label l3 = new Label();
            super.visitLabel(l2);
            super.visitFieldInsn(Opcodes.GETSTATIC, "mezz/jei/config/Config$SearchMode", "REQUIRE_PREFIX", "Lmezz/jei/config/Config$SearchMode;");
            super.visitJumpInsn(Opcodes.GOTO, l3);

            super.visitLabel(l1);
            super.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
            super.visitFieldInsn(Opcodes.GETSTATIC, "mezz/jei/config/Config$SearchMode", "DISABLED", "Lmezz/jei/config/Config$SearchMode;");

            super.visitLabel(l3);
            super.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "mezz/jei/config/Config$SearchMode" });
            super.visitInsn(Opcodes.ARETURN);

            final Label l4 = new Label();
            super.visitLabel(l4);

            super.visitMaxs(3, 0);
            super.visitEnd();
        }
    }

    @SuppressWarnings("SpellCheckingInspection")
    private static final class StringsGetterMethodVisitor extends MethodVisitor {
        //  // access flags 0x101A
        //  // signature (Lmezz/jei/gui/ingredients/IIngredientListElement<*>;)Ljava/util/Collection<Ljava/lang/String;>;
        //  // declaration: java.util.Collection<java.lang.String> <fermion-lambda:new:11>(mezz.jei.gui.ingredients.IIngredientListElement<?>)
        //  private static final synthetic <fermion-lambda:new:11>(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;
        //   L0
        //    LINENUMBER 455 L0
        //    ALOAD 0
        //    INVOKEINTERFACE mezz/jei/gui/ingredients/IIngredientListElement.getIngredient ()Ljava/lang/Object; (itf)
        //    ASTORE 1
        //   L1
        //    LINENUMBER 456 L1
        //    ALOAD 1
        //    INSTANCEOF net/minecraft/item/ItemStack
        //    IFNE L2
        //    INVOKESTATIC java/util/Collections.emptyList ()Ljava/util/List;
        //    CHECKCAST java/util/Collection
        //    GOTO L3
        //   L2
        //    LINENUMBER 457 L2
        //   FRAME APPEND [java/lang/Object]
        //    GETSTATIC net/thesilkminer/mc/prjtags/client/TooltipEventHandler.INSTANCE : Lnet/thesilkminer/mc/prjtags/client/TooltipEventHandler;
        //    ALOAD 1
        //    CHECKCAST net/minecraft/item/ItemStack
        //    INVOKEVIRTUAL net/thesilkminer/mc/prjtags/client/TooltipEventHandler.findPlainTagNamesFor (Lnet/minecraft/item/ItemStack;)Ljava/util/List;
        //    CHECKCAST java/util/Collection
        //   L3
        //   FRAME FULL [mezz/jei/gui/ingredients/IIngredientListElement java/lang/Object] [java/util/Collection]
        //    ARETURN
        //   L4
        //    LOCALVARIABLE element Lmezz/jei/gui/ingredients/IIngredientListElement; L0 L4 0
        //    // signature Lmezz/jei/gui/ingredients/IIngredientListElement<*>;
        //    // declaration: element extends mezz.jei.gui.ingredients.IIngredientListElement<?>
        //    LOCALVARIABLE ingredient Ljava/lang/Object; L1 L4 1
        //    MAXSTACK = 2
        //    MAXLOCALS = 2

        private StringsGetterMethodVisitor(final int version, @Nonnull final MethodVisitor parent) {
            super(version, parent);
        }

        @Override
        public void visitCode() {
            super.visitCode();

            final Label l0 = new Label();
            super.visitLabel(l0);
            super.visitLineNumber(4 * 100 + 5 * 10 + 5, l0);
            super.visitVarInsn(Opcodes.ALOAD, 0);
            super.visitMethodInsn(Opcodes.INVOKEINTERFACE, "mezz/jei/gui/ingredients/IIngredientListElement", "getIngredient", "()Ljava/lang/Object;", true);
            super.visitVarInsn(Opcodes.ASTORE, 1);

            final Label l1 = new Label();
            final Label l2 = new Label();
            final Label l3 = new Label();
            super.visitLabel(l1);
            super.visitLineNumber(4 * 100 + 5 * 10 + 6, l1);
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitTypeInsn(Opcodes.INSTANCEOF, "net/minecraft/item/ItemStack");
            super.visitJumpInsn(Opcodes.IFNE, l2);
            super.visitMethodInsn(Opcodes.INVOKESTATIC, "java/util/Collections", "emptyList", "()Ljava/util/List;", false);
            super.visitTypeInsn(Opcodes.CHECKCAST, "java/util/Collection");
            super.visitJumpInsn(Opcodes.GOTO, l3);

            super.visitLabel(l2);
            super.visitLineNumber(4 * 100 + 5 * 10 + 7, l2);
            super.visitFrame(Opcodes.F_APPEND, 1, new Object[] { "java/lang/Object" }, 0, null);
            super.visitFieldInsn(Opcodes.GETSTATIC, "net/thesilkminer/mc/prjtags/client/TooltipEventHandler", "INSTANCE", "Lnet/thesilkminer/mc/prjtags/client/TooltipEventHandler;");
            super.visitVarInsn(Opcodes.ALOAD, 1);
            super.visitTypeInsn(Opcodes.CHECKCAST, "net/minecraft/item/ItemStack");
            super.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "net/thesilkminer/mc/prjtags/client/TooltipEventHandler", "findPlainTagNamesFor", "(Lnet/minecraft/item/ItemStack;)Ljava/util/List;", false);
            super.visitTypeInsn(Opcodes.CHECKCAST, "java/util/Collection");

            super.visitLabel(l3);
            super.visitFrame(Opcodes.F_FULL, 2, new Object[] { "mezz/jei/gui/ingredients/IIngredientListElement", "java/lang/Object" }, 1, new Object[] { "java/util/Collection" });
            super.visitInsn(Opcodes.ARETURN);

            final Label l4 = new Label();
            super.visitLabel(l4);

            super.visitLocalVariable("element", "Lmezz/jei/gui/ingredients/IIngredientListElement;", "Lmezz/jei/gui/ingredients/IIngredientListElement<*>;", l0, l4, 0);
            super.visitLocalVariable("ingredient", "Ljava/lang/Object;", null, l1, l4, 1);

            super.visitMaxs(2,2);
            super.visitEnd();
        }
    }

    private static final Logger LOGGER = LogManager.getLogger("Project T.A.G.S. ASM/Ingredient Renderer Transformer");
    private static final String THIS = "mezz/jei/ingredients/IngredientFilter";
    private static final String CONFIG_LAMBDA = "fermion$$lambda$$new$$generated$$00_10_03_1122";
    private static final String STRINGS_LAMBDA = "fermion$$lambda$$new$$generated$$00_11_03_1122";

    @SuppressWarnings("ResultOfMethodCallIgnored") // Classloading because weird shit happens otherwise
    IngredientFilterTransformer(@Nonnull final LaunchPlugin owner) {
        super(
                TransformerData.Builder.create()
                        .setOwningPlugin(owner)
                        .setName("ingredient_filter")
                        .setDescription("Transforms JEI's Ingredient Filter to allow for a custom prefix to be added in order to search for tags")
                        .build(),
                ClassDescriptor.of(THIS)
        );
        ConstructorVisitor.class.toString();
        ModeGetterMethodVisitor.class.toString();
        StringsGetterMethodVisitor.class.toString();
    }

    @Nonnull
    @Override
    public BiFunction<Integer, ClassVisitor, ClassVisitor> getClassVisitorCreator() {
        return (v, cw) -> new ClassVisitor(v, cw) {
            {
                LOGGER.info("First time I try to inject with INDYs in the mix: I guess it's time to have fun");
            }

            @Override
            public MethodVisitor visitMethod(final int access, @Nonnull final String name, @Nonnull final String desc, @Nullable final String signature, @Nullable final String[] exceptions) {
                final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
                if ("<init>".equals(name) && "(Lmezz/jei/ingredients/IngredientBlacklistInternal;)V".equals(desc)) {
                    LOGGER.info("Identified class constructor: injecting new search tree call (with INDYs...)");
                    return new ConstructorVisitor(v, parent);
                }
                return parent;
            }

            @Override
            public void visitEnd() {
                LOGGER.info("Injecting config lambda");
                new ModeGetterMethodVisitor(
                        v,
                        super.visitMethod(
                                Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_SYNTHETIC,
                                CONFIG_LAMBDA,
                                "()Lmezz/jei/config/Config$SearchMode;",
                                null,
                                null
                        )
                ).visitCode();

                LOGGER.info("Injecting strings lambda");
                new StringsGetterMethodVisitor(
                        v,
                        super.visitMethod(
                                Opcodes.ACC_PRIVATE | Opcodes.ACC_STATIC | Opcodes.ACC_FINAL | Opcodes.ACC_SYNTHETIC,
                                STRINGS_LAMBDA,
                                "(Lmezz/jei/gui/ingredients/IIngredientListElement;)Ljava/util/Collection;",
                                "(Lmezz/jei/gui/ingredients/IIngredientListElement<*>;)Ljava/util/Collection<Ljava/lang/String;>;",
                                null
                        )
                ).visitCode();

                super.visitEnd();
            }
        };
    }
}
