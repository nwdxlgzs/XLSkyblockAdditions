package com.nwdxlgzs.skyadd;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLootTableProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.recipe.RecipeExporter;
import net.minecraft.data.recipe.RecipeGenerator;
import net.minecraft.data.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static com.nwdxlgzs.skyadd.XLSkyblockAdditions.MOD_ID;

public class XLSkyblockAdditionsDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(XRecipeProvider::new);
    }

    //配方
    public static class XRecipeProvider extends FabricRecipeProvider {
        public XRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        @Override
        protected RecipeGenerator getRecipeGenerator(RegistryWrapper.WrapperLookup registryLookup, RecipeExporter exporter) {
            return new RecipeGenerator(registryLookup, exporter) {
                @Override
                public void generate() {
                    //腐肉合成地狱岩
                    NineToOne(RecipeCategory.BUILDING_BLOCKS, Items.ROTTEN_FLESH, Items.NETHERRACK, "netherrack_compose");
                    //绯红菌+地狱岩合成绯红菌岩
                    createShapeless(RecipeCategory.BUILDING_BLOCKS, Items.CRIMSON_NYLIUM, 1)
                            .input(Items.CRIMSON_FUNGUS)
                            .input(Items.NETHERRACK)
                            .criterion(hasItem(Items.CRIMSON_FUNGUS), conditionsFromItem(Items.CRIMSON_FUNGUS))
                            .criterion(hasItem(Items.NETHERRACK), conditionsFromItem(Items.NETHERRACK))
                            .offerTo(exporter, "crimson_nylium_implantation");
                    //诡异菌+地狱岩合成诡异菌岩
                    createShapeless(RecipeCategory.BUILDING_BLOCKS, Items.WARPED_NYLIUM, 1)
                            .input(Items.WARPED_FUNGUS)
                            .input(Items.NETHERRACK)
                            .criterion(hasItem(Items.WARPED_FUNGUS), conditionsFromItem(Items.WARPED_FUNGUS))
                            .criterion(hasItem(Items.NETHERRACK), conditionsFromItem(Items.NETHERRACK))
                            .offerTo(exporter, "warped_nylium_implantation");
                    //石英块分解为石英
                    createShapeless(RecipeCategory.REDSTONE, Items.QUARTZ, 4)
                            .input(Items.QUARTZ_BLOCK)
                            .criterion(hasItem(Items.QUARTZ_BLOCK), conditionsFromItem(Items.QUARTZ_BLOCK))
                            .offerTo(exporter, "quartz_decompose");
                    offerStonecuttingRecipe(RecipeCategory.REDSTONE, Items.QUARTZ, Items.QUARTZ_BLOCK, 4);
                    //滴水石切割回滴水石锥
                    createShapeless(RecipeCategory.DECORATIONS, Items.POINTED_DRIPSTONE, 4)
                            .input(Items.DRIPSTONE_BLOCK)
                            .criterion(hasItem(Items.DRIPSTONE_BLOCK), conditionsFromItem(Items.DRIPSTONE_BLOCK))
                            .offerTo(exporter, "pointed_dripstone_decompose");
                    offerStonecuttingRecipe(RecipeCategory.DECORATIONS, Items.POINTED_DRIPSTONE, Items.DRIPSTONE_BLOCK, 4);
                    //锁链甲配方
                    createShaped(RecipeCategory.COMBAT, Items.CHAINMAIL_HELMET, 1)
                            .pattern("XXX")
                            .pattern("X X")
                            .input('X', Items.CHAIN)
                            .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                            .offerTo(exporter);
                    createShaped(RecipeCategory.COMBAT, Items.CHAINMAIL_CHESTPLATE, 1)
                            .pattern("X X")
                            .pattern("XXX")
                            .pattern("XXX")
                            .input('X', Items.CHAIN)
                            .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                            .offerTo(exporter);
                    createShaped(RecipeCategory.COMBAT, Items.CHAINMAIL_LEGGINGS, 1)
                            .pattern("XXX")
                            .pattern("X X")
                            .pattern("X X")
                            .input('X', Items.CHAIN)
                            .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                            .offerTo(exporter);
                    createShaped(RecipeCategory.COMBAT, Items.CHAINMAIL_BOOTS, 1)
                            .pattern("X X")
                            .pattern("X X")
                            .input('X', Items.CHAIN)
                            .criterion(hasItem(Items.CHAIN), conditionsFromItem(Items.CHAIN))
                            .offerTo(exporter);
                    //皮甲拆解（任意皮甲拆解为1个皮革）
                    ArmorDisassembly(Items.LEATHER_HELMET, Items.LEATHER_CHESTPLATE, Items.LEATHER_LEGGINGS, Items.LEATHER_BOOTS, Items.LEATHER);
                    //铁甲拆解（任意铁甲拆解为1个铁锭）
                    ArmorDisassembly(Items.IRON_HELMET, Items.IRON_CHESTPLATE, Items.IRON_LEGGINGS, Items.IRON_BOOTS, Items.IRON_INGOT);
                    //锁链甲拆解（任意锁链甲拆解为1个锁链）
                    ArmorDisassembly(Items.CHAINMAIL_HELMET, Items.CHAINMAIL_CHESTPLATE, Items.CHAINMAIL_LEGGINGS, Items.CHAINMAIL_BOOTS, Items.CHAIN);
                    //金甲拆解（任意金甲拆解为1个金锭）
                    ArmorDisassembly(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS, Items.GOLD_INGOT);
                    //钻石甲拆解（任意钻石甲拆解为1个钻石）
                    ArmorDisassembly(Items.DIAMOND_HELMET, Items.DIAMOND_CHESTPLATE, Items.DIAMOND_LEGGINGS, Items.DIAMOND_BOOTS, Items.DIAMOND);
                    //传统的附魔金苹果配方（苹果周围一圈金块）
                    createShaped(RecipeCategory.FOOD, Items.ENCHANTED_GOLDEN_APPLE, 1)
                            .pattern("GGG")
                            .pattern("GAG")
                            .pattern("GGG")
                            .input('G', Items.GOLD_BLOCK)
                            .input('A', Items.APPLE)
                            .criterion(hasItem(Items.GOLD_BLOCK), conditionsFromItem(Items.GOLD_BLOCK))
                            .criterion(hasItem(Items.APPLE), conditionsFromItem(Items.APPLE))
                            .offerTo(exporter);
                    //合成方式合成黑曜石（8岩浆桶+1水桶）
                    createShaped(RecipeCategory.MISC, Items.OBSIDIAN, 8)
                            .pattern("LLL")
                            .pattern("LWL")
                            .pattern("LLL")
                            .input('L', Items.LAVA_BUCKET)
                            .input('W', Items.WATER_BUCKET)
                            .criterion(hasItem(Items.LAVA_BUCKET), conditionsFromItem(Items.LAVA_BUCKET))
                            .criterion(hasItem(Items.WATER_BUCKET), conditionsFromItem(Items.WATER_BUCKET))
                            .offerTo(exporter, "bucket_way_obsidian");
                    //9根箭合成去皮竹块（方便前期刷怪塔产生建筑方块以及后期刷怪塔能让箭有用途）
                    NineToOne(RecipeCategory.BUILDING_BLOCKS, Items.ARROW, Items.STRIPPED_BAMBOO_BLOCK, "arrow_way_bamboo");
                    //消耗一把剪刀把箭剪断获得燧石（什么？你说你用岩浆点的地狱门？这么好的方法不用？）
                    createShapeless(RecipeCategory.MISC, Items.FLINT, 1)
                            .input(Items.ARROW)
                            .input(Items.SHEARS)
                            .criterion(hasItem(Items.ARROW), conditionsFromItem(Items.ARROW))
                            .criterion(hasItem(Items.SHEARS), conditionsFromItem(Items.SHEARS))
                            .offerTo(exporter, "shears_way_flint");
                    //海龟壳分解成海龟鳞甲（1个）
                    ItemDisassembly(RecipeCategory.MISC, Items.TURTLE_HELMET, Items.TURTLE_SCUTE);
                    //便携的骨头直接合成骨块
                    createShapeless(RecipeCategory.MISC, Items.BONE_BLOCK, 1)
                            .input(Items.BONE).input(Items.BONE).input(Items.BONE)
                            .criterion(hasItem(Items.BONE), conditionsFromItem(Items.BONE))
                            .offerTo(exporter, "fast_bone_block");
                    //珊瑚块合成（4珊瑚/9珊瑚扇合成1珊瑚块）
                    FourToOne(RecipeCategory.BUILDING_BLOCKS, Items.TUBE_CORAL, Items.TUBE_CORAL_BLOCK, "tube_coral_block_compose");
                    NineToOne(RecipeCategory.BUILDING_BLOCKS, Items.TUBE_CORAL_FAN, Items.TUBE_CORAL_BLOCK, "tube_coral_block_compose_funway");
                    FourToOne(RecipeCategory.BUILDING_BLOCKS, Items.BRAIN_CORAL, Items.BRAIN_CORAL_BLOCK, "brain_coral_block_compose");
                    NineToOne(RecipeCategory.BUILDING_BLOCKS, Items.BRAIN_CORAL_FAN, Items.BRAIN_CORAL_BLOCK, "brain_coral_block_compose_funway");
                    FourToOne(RecipeCategory.BUILDING_BLOCKS, Items.BUBBLE_CORAL, Items.BUBBLE_CORAL_BLOCK, "bubble_coral_block_compose");
                    NineToOne(RecipeCategory.BUILDING_BLOCKS, Items.BUBBLE_CORAL_FAN, Items.BUBBLE_CORAL_BLOCK, "bubble_coral_block_compose_funway");
                    FourToOne(RecipeCategory.BUILDING_BLOCKS, Items.FIRE_CORAL, Items.FIRE_CORAL_BLOCK, "fire_coral_block_compose");
                    NineToOne(RecipeCategory.BUILDING_BLOCKS, Items.FIRE_CORAL_FAN, Items.FIRE_CORAL_BLOCK, "fire_coral_block_compose_funway");
                    FourToOne(RecipeCategory.BUILDING_BLOCKS, Items.HORN_CORAL, Items.HORN_CORAL_BLOCK, "horn_coral_block_compose");
                    NineToOne(RecipeCategory.BUILDING_BLOCKS, Items.HORN_CORAL_FAN, Items.HORN_CORAL_BLOCK, "horn_coral_block_compose_funway");
                    //蜘蛛线合成蜘蛛网
                    NineToOne(RecipeCategory.MISC, Items.STRING, Items.COBWEB, "cobweb_compose");
                    //灵魂沙合成（灵魂沙=生命+沙子，这很合理吧）
                    createShaped(RecipeCategory.BUILDING_BLOCKS, Items.SOUL_SAND, 1)
                            .pattern("EEE")
                            .pattern("ESE")
                            .pattern("EEE")
                            .input('E', Items.EGG)
                            .input('S', Items.SAND)
                            .criterion(hasItem(Items.EGG), conditionsFromItem(Items.EGG))
                            .criterion(hasItem(Items.SAND), conditionsFromItem(Items.SAND))
                            .offerTo(exporter, "egg_way_soul_sand");
                    //哭泣黑曜石（黑曜石+恶魂之泪）
                    createShapeless(RecipeCategory.BUILDING_BLOCKS, Items.CRYING_OBSIDIAN, 1)
                            .input(Items.OBSIDIAN)
                            .input(Items.GHAST_TEAR)
                            .criterion(hasItem(Items.OBSIDIAN), conditionsFromItem(Items.OBSIDIAN))
                            .criterion(hasItem(Items.GHAST_TEAR), conditionsFromItem(Items.GHAST_TEAR))
                            .offerTo(exporter, "ghast_tear_way_crying_obsidian");
                    //便捷深板岩原石合成（两个圆石）
                    createShapeless(RecipeCategory.BUILDING_BLOCKS, Items.COBBLED_DEEPSLATE, 1)
                            .input(Items.COBBLESTONE).input(Items.COBBLESTONE)
                            .criterion(hasItem(Items.COBBLESTONE), conditionsFromItem(Items.COBBLESTONE))
                            .offerTo(exporter, "easy_cobbled_deepslate");
                    //无损西瓜分解
                    createShapeless(RecipeCategory.FOOD, Items.MELON_SLICE, 9)
                            .input(Items.MELON)
                            .criterion(hasItem(Items.MELON), conditionsFromItem(Items.MELON))
                            .offerTo(exporter, "split_melon");
                    //铜块兑换（三叉戟+铜锭+包裹一圈腐肉，通过消耗稀有的三叉戟兑换铜，毕竟铜产能太低了，农场效率可能不如主世界下矿快……）
                    createShaped(RecipeCategory.BUILDING_BLOCKS, Items.COPPER_BLOCK,64)
                            .pattern("RRR")
                            .pattern("RSR")
                            .pattern("RCR")
                            .input('R', Items.ROTTEN_FLESH)
                            .input('S', Items.TRIDENT)
                            .input('C', Items.COPPER_INGOT)
                            .criterion(hasItem(Items.ROTTEN_FLESH), conditionsFromItem(Items.ROTTEN_FLESH))
                            .criterion(hasItem(Items.TRIDENT), conditionsFromItem(Items.TRIDENT))
                            .criterion(hasItem(Items.COPPER_INGOT), conditionsFromItem(Items.COPPER_INGOT))
                            .offerTo(exporter, "exchange_copper_block");
                    
                }

                private void NineToOne(RecipeCategory category, Item input, Item output, String name) {
                    createShaped(category, output, 1)
                            .pattern("CCC")
                            .pattern("CCC")
                            .pattern("CCC")
                            .input('C', input)
                            .criterion(hasItem(input), conditionsFromItem(input))
                            .offerTo(exporter, name);
                }

                private void FourToOne(RecipeCategory category, Item input, Item output, String name) {
                    createShaped(category, output, 1)
                            .pattern("CC")
                            .pattern("CC")
                            .input('C', input)
                            .criterion(hasItem(input), conditionsFromItem(input))
                            .offerTo(exporter, name);
                }

                private void ItemDisassembly(RecipeCategory category, Item input, Item output) {
                    createShapeless(category, output, 1)
                            .input(input)
                            .criterion(hasItem(input), conditionsFromItem(input))
                            .offerTo(exporter, "dis-" + input.getTranslationKey());
                }

                private void ArmorDisassembly(Item helmet, Item chestplate, Item leggings, Item boots, Item output) {
                    ItemDisassembly(RecipeCategory.COMBAT, helmet, output);
                    ItemDisassembly(RecipeCategory.COMBAT, chestplate, output);
                    ItemDisassembly(RecipeCategory.COMBAT, leggings, output);
                    ItemDisassembly(RecipeCategory.COMBAT, boots, output);
                }

            };
        }

        @Override
        public String getName() {
            return "XRecipeProvider";
        }
    }
}
