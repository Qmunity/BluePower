/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

        package com.bluepowermod.init;

import com.bluepowermod.BluePower;
import com.bluepowermod.item.*;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.Dependencies;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;


@GameRegistry.ObjectHolder(Refs.MODID)
public class BPItems {

    public static Item amethyst_gem;
    public static Item sapphire_gem;
    public static Item ruby_gem;
    public static Item teslatite_dust;
    public static Item copper_ingot;
    public static Item silver_ingot;
    public static Item zinc_ingot;
    public static Item brass_ingot;
    public static Item blue_alloy_ingot;
    public static Item red_alloy_ingot;
    public static Item purple_alloy_ingot;
    public static Item tungsten_ingot;
    public static Item tungsten_nugget;
    public static Item zinc_ore_crushed;
    public static Item zinc_ore_purified;
    public static Item zinc_dust;
    public static Item zinc_tiny_dust;
    public static Item ruby_saw;
    public static Item sapphire_saw;
    public static Item amethyst_saw;
    public static Item wood_sickle;
    public static Item stone_sickle;
    public static Item iron_sickle;
    public static Item gold_sickle;
    public static Item diamond_sickle;
    public static Item flax_seeds;
    public static Item indigo_dye;
    public static Item silicon_boule;
    public static Item silicon_wafer;
    public static Item red_doped_wafer;
    public static Item blue_doped_wafer;
    public static Item infused_teslatite_dust;
    public static Item screwdriver;
    public static Item silky_screwdriver;
    public static Item athame;
    public static Item zincplate;
    public static Item stone_tile;
    public static Item bluestone_wire_tile;
    public static Item bluestone_anode_tile;
    public static Item bluestone_cathode_tile;
    public static Item bluestone_pointer_tile;
    public static Item silicon_chip_tile;
    public static Item tainted_silicon_chip_tile;
    public static Item quartz_resonator_tile;
    public static Item redstone_wire_tile;
    public static Item redstone_anode_tile;
    public static Item redstone_cathode_tile;
    public static Item redstone_pointer_tile;
    // public static Item stone_redwire;
    // public static Item plate_assembly;
    public static Item stone_bundle;
    public static Item screwdriver_handle;
    public static Item seed_bag;
    public static Item canvas_bag;
    public static Item canvas;
    public static Item lumar;
    public static Item wool_card;
    public static Item diamond_drawplate;
    public static Item copper_wire;
    public static Item iron_wire;
    public static Item paint_can;
    public static Item paint_brush;
    public static Item iron_saw;
    public static Item diamond_saw;
    public static Item ruby_axe;
    public static Item ruby_sword;
    public static Item ruby_pickaxe;
    public static Item ruby_shovel;
    public static Item ruby_hoe;
    public static Item ruby_sickle;
    public static Item sapphire_axe;
    public static Item sapphire_sword;
    public static Item sapphire_pickaxe;
    public static Item sapphire_shovel;
    public static Item sapphire_hoe;
    public static Item sapphire_sickle;
    public static Item amethyst_axe;
    public static Item amethyst_sword;
    public static Item amethyst_pickaxe;
    public static Item amethyst_shovel;
    public static Item amethyst_hoe;
    public static Item amethyst_sickle;
    public static Item multimeter;
    public static Item battery;
    public static Item sonicScrewdriver;

    public static void init() {

        if (!Loader.isModLoaded("ForgeMicroblock")) {// FMP already has an iron and diamond saw
            iron_saw = new ItemSaw(2, Refs.IRONSAW_NAME);
            diamond_saw = new ItemSaw(3, Refs.DIAMONDSAW_NAME);
        }

        initializeItems();
        registerItems();
    }

    public static void initializeItems() {

        amethyst_gem = new ItemCrafting(Refs.AMETHYST_NAME);
        sapphire_gem = new ItemCrafting(Refs.SAPPHIRE_NAME);
        ruby_gem = new ItemCrafting(Refs.RUBY_NAME);
        teslatite_dust = new ItemCrafting(Refs.TESLATITE_NAME);
        copper_ingot = new ItemCrafting(Refs.COPPERINGOT_NAME);
        silver_ingot = new ItemCrafting(Refs.SILVERINGOT_NAME);
        zinc_ingot = new ItemCrafting(Refs.ZINCINGOT_NAME);
        brass_ingot = new ItemCrafting(Refs.BRASSINGOT_NAME);
        blue_alloy_ingot = new ItemCrafting(Refs.BLUEALLOYINGOT_NAME);
        red_alloy_ingot = new ItemCrafting(Refs.REDALLOYINGOT_NAME);
        purple_alloy_ingot = new ItemCrafting(Refs.PURPLEALLOYINGOT_NAME);
        tungsten_ingot = new ItemCrafting(Refs.TUNGSTENINGOT_NAME);
        tungsten_nugget = new ItemCrafting(Refs.TUNGSTENNUGGET_NAME);
        zinc_ore_crushed = new ItemBase().setUnlocalizedName(Refs.ZINC_ORE_CRUSHED_NAME)
                .setTextureName(Refs.MODID + ":" + Refs.ZINC_ORE_CRUSHED_NAME).setCreativeTab(BPCreativeTabs.items);
        zinc_ore_purified = new ItemBase().setUnlocalizedName(Refs.ZINC_ORE_CRUSHED_PURIFIED_NAME)
                .setTextureName(Refs.MODID + ":" + Refs.ZINC_ORE_CRUSHED_PURIFIED_NAME).setCreativeTab(BPCreativeTabs.items);
        zinc_dust = new ItemBase().setUnlocalizedName(Refs.ZINCDUST_NAME).setTextureName(Refs.MODID + ":" + Refs.ZINCDUST_NAME)
                .setCreativeTab(BPCreativeTabs.items);
        zinc_tiny_dust = new ItemBase().setUnlocalizedName(Refs.ZINCDUST_TINY_NAME).setTextureName(Refs.MODID + ":" + Refs.ZINCDUST_TINY_NAME)
                .setCreativeTab(BPCreativeTabs.items);
        ruby_saw = new ItemSaw(2, Refs.RUBYSAW_NAME);
        sapphire_saw = new ItemSaw(2, Refs.SAPPHIRESAW_NAME);
        amethyst_saw = new ItemSaw(2, Refs.AMETHYSTSAW_NAME);
        wood_sickle = new ItemSickle(ToolMaterial.WOOD, Refs.WOODSICKLE_NAME, Item.getItemFromBlock(Blocks.planks));
        stone_sickle = new ItemSickle(ToolMaterial.STONE, Refs.STONESICKLE_NAME, Item.getItemFromBlock(Blocks.cobblestone));
        iron_sickle = new ItemSickle(ToolMaterial.IRON, Refs.IRONSICKLE_NAME, Items.iron_ingot);
        gold_sickle = new ItemSickle(ToolMaterial.GOLD, Refs.GOLDSICKLE_NAME, Items.gold_ingot);
        diamond_sickle = new ItemSickle(ToolMaterial.EMERALD, Refs.DIAMONDSICKLE_NAME, Items.diamond);
        flax_seeds = new ItemCropSeed(BPBlocks.flax_crop, Blocks.farmland).setUnlocalizedName(Refs.FLAXSEED_NAME);
        indigo_dye = new ItemIndigoDye(Refs.INDIGODYE_NAME);
        silicon_boule = new ItemCrafting(Refs.SILICONBOULE_NAME).setCreativeTab(BPCreativeTabs.items);
        silicon_wafer = new ItemCrafting(Refs.SILICONWAFER_NAME).setCreativeTab(BPCreativeTabs.items);
        red_doped_wafer = new ItemCrafting(Refs.REDDOPEDWAFER_NAME).setCreativeTab(BPCreativeTabs.items);
        blue_doped_wafer = new ItemCrafting(Refs.BLUEDOPEDWAFER_NAME).setCreativeTab(BPCreativeTabs.items);
        infused_teslatite_dust = new ItemCrafting(Refs.INFUSEDTESLATITEDUST_NAME);
        screwdriver = new ItemScrewdriver();
        silky_screwdriver = new ItemSilkyScrewdriver();
        athame = new ItemAthame();
        zincplate = new ItemCrafting(Refs.ZINCPLATE_NAME);
        stone_tile = new ItemCrafting(Refs.STONETILE_NAME);
        bluestone_wire_tile = new ItemCrafting(Refs.BLUESTONEWIRETILE_NAME);
        bluestone_anode_tile = new ItemCrafting(Refs.BLUESTONEANODETILE_NAME);
        bluestone_cathode_tile = new ItemCrafting(Refs.BLUESTONECATHODE_NAME);
        bluestone_pointer_tile = new ItemCrafting(Refs.BLUESTONEPOINTER_NAME);
        silicon_chip_tile = new ItemCrafting(Refs.SILICONCHIP_NAME);
        tainted_silicon_chip_tile = new ItemCrafting(Refs.TAINTEDSILICONCHIP_NAME);
        quartz_resonator_tile = new ItemCrafting(Refs.QUARTZRESONATOR_NAME);
        redstone_wire_tile = new ItemCrafting(Refs.REDSTONEWIRETILE_NAME);
        redstone_anode_tile = new ItemCrafting(Refs.REDSTONEANODETILE_NAME);
        redstone_cathode_tile = new ItemCrafting(Refs.REDSTONECATHODE_NAME);
        redstone_pointer_tile = new ItemCrafting(Refs.REDSTONEPOINTER_NAME);
        // stone_redwire = new ItemCrafting(Refs.STONEREDWIRE_NAME);
        // plate_assembly = new ItemCrafting(Refs.PLATEASSEMBLY_NAME);
        stone_bundle = new ItemCrafting(Refs.STONEBUNDLE_NAME);
        screwdriver_handle = new ItemCrafting(Refs.SCREWDRIVERHANDLE_NAME);
        seed_bag = new ItemSeedBag(Refs.SEEDBAG_NAME);
        canvas_bag = new ItemCanvasBag(Refs.CANVASBAG_NAME);
        canvas = new ItemCrafting(Refs.CANVAS_NAME);
        lumar = new ItemLumar();
        wool_card = new ItemLimitedCrafting(Refs.WOOLCARD_NAME, 64);
        diamond_drawplate = new ItemLimitedCrafting(Refs.DIAMONDDRAWPLATE_NAME, 256);
        copper_wire = new ItemCrafting(Refs.COPPERWIRE_NAME);
        iron_wire = new ItemCrafting(Refs.IRONWIRE_NAME);
        paint_can = new ItemPaintCan(Refs.PAINTCAN_NAME);
        paint_brush = new ItemPaintBrush(Refs.PAINTBRUSH_NAME);
        ruby_axe = new ItemGemAxe(BluePower.gemMaterial, Refs.RUBYAXE_NAME, BPItems.ruby_gem);
        ruby_sword = new ItemGemSword(BluePower.gemMaterial, Refs.RUBYSWORD_NAME, BPItems.ruby_gem);
        ruby_pickaxe = new ItemGemPickaxe(BluePower.gemMaterial, Refs.RUBYPICKAXE_NAME, BPItems.ruby_gem);
        ruby_shovel = new ItemGemSpade(BluePower.gemMaterial, Refs.RUBYSPADE_NAME, BPItems.ruby_gem);
        ruby_hoe = new ItemGemHoe(BluePower.gemMaterial, Refs.RUBYHOE_NAME, BPItems.ruby_gem);
        ruby_sickle = new ItemSickle(BluePower.gemMaterial, Refs.RUBYSICKLE_NAME, BPItems.ruby_gem);
        sapphire_axe = new ItemGemAxe(BluePower.gemMaterial, Refs.SAPPHIREAXE_NAME, BPItems.sapphire_gem);
        sapphire_sword = new ItemGemSword(BluePower.gemMaterial, Refs.SAPPHIRESWORD_NAME, BPItems.sapphire_gem);
        sapphire_pickaxe = new ItemGemPickaxe(BluePower.gemMaterial, Refs.SAPPHIREPICKAXE_NAME, BPItems.sapphire_gem);
        sapphire_shovel = new ItemGemSpade(BluePower.gemMaterial, Refs.SAPPHIRESPADE_NAME, BPItems.sapphire_gem);
        sapphire_hoe = new ItemGemHoe(BluePower.gemMaterial, Refs.SAPPHIREHOE_NAME, BPItems.sapphire_gem);
        sapphire_sickle = new ItemSickle(BluePower.gemMaterial, Refs.SAPPHIRESICKLE_NAME, BPItems.sapphire_gem);
        amethyst_axe = new ItemGemAxe(BluePower.gemMaterial, Refs.AMETHYSTAXE_NAME, BPItems.amethyst_gem);
        amethyst_sword = new ItemGemSword(BluePower.gemMaterial, Refs.AMETHYSTSWORD_NAME, BPItems.amethyst_gem);
        amethyst_pickaxe = new ItemGemPickaxe(BluePower.gemMaterial, Refs.AMETHYSTPICKAXE_NAME, BPItems.amethyst_gem);
        amethyst_shovel = new ItemGemSpade(BluePower.gemMaterial, Refs.AMETHYSTSPADE_NAME, BPItems.amethyst_gem);
        amethyst_hoe = new ItemGemHoe(BluePower.gemMaterial, Refs.AMETHYSTHOE_NAME, BPItems.amethyst_gem);
        amethyst_sickle = new ItemSickle(BluePower.gemMaterial, Refs.AMETHYSTSICKLE_NAME, BPItems.amethyst_gem);

        multimeter = new ItemMultimeter();
        battery = new ItemBattery(1000);
        sonicScrewdriver = new ItemSonicScrewdriver();
    }

    public static void registerItems() {

        GameRegistry.registerItem(amethyst_gem, Refs.AMETHYST_NAME);
        GameRegistry.registerItem(sapphire_gem, Refs.SAPPHIRE_NAME);
        GameRegistry.registerItem(ruby_gem, Refs.RUBY_NAME);
        GameRegistry.registerItem(teslatite_dust, Refs.TESLATITE_NAME);
        GameRegistry.registerItem(copper_ingot, Refs.COPPERINGOT_NAME);
        GameRegistry.registerItem(silver_ingot, Refs.SILVERINGOT_NAME);
        GameRegistry.registerItem(zinc_ingot, Refs.ZINCINGOT_NAME);
        GameRegistry.registerItem(tungsten_ingot, Refs.TUNGSTENINGOT_NAME);
        GameRegistry.registerItem(brass_ingot, Refs.BRASSINGOT_NAME);
        GameRegistry.registerItem(blue_alloy_ingot, Refs.BLUEALLOYINGOT_NAME);
        GameRegistry.registerItem(red_alloy_ingot, Refs.REDALLOYINGOT_NAME);
        GameRegistry.registerItem(purple_alloy_ingot, Refs.PURPLEALLOYINGOT_NAME);

        GameRegistry.registerItem(zinc_dust, Refs.ZINCDUST_NAME);
        GameRegistry.registerItem(zinc_ore_crushed, Refs.ZINC_ORE_CRUSHED_NAME);
        GameRegistry.registerItem(zinc_ore_purified, Refs.ZINC_ORE_CRUSHED_PURIFIED_NAME);
        GameRegistry.registerItem(zinc_tiny_dust, Refs.ZINCDUST_TINY_NAME);
        GameRegistry.registerItem(tungsten_nugget, Refs.TUNGSTENNUGGET_NAME);

        GameRegistry.registerItem(ruby_axe, Refs.RUBYAXE_NAME);
        GameRegistry.registerItem(ruby_sword, Refs.RUBYSWORD_NAME);
        GameRegistry.registerItem(ruby_pickaxe, Refs.RUBYPICKAXE_NAME);
        GameRegistry.registerItem(ruby_shovel, Refs.RUBYSPADE_NAME);
        GameRegistry.registerItem(ruby_hoe, Refs.RUBYHOE_NAME);
        GameRegistry.registerItem(ruby_sickle, Refs.RUBYSICKLE_NAME);

        GameRegistry.registerItem(sapphire_axe, Refs.SAPPHIREAXE_NAME);
        GameRegistry.registerItem(sapphire_sword, Refs.SAPPHIRESWORD_NAME);
        GameRegistry.registerItem(sapphire_pickaxe, Refs.SAPPHIREPICKAXE_NAME);
        GameRegistry.registerItem(sapphire_shovel, Refs.SAPPHIRESPADE_NAME);
        GameRegistry.registerItem(sapphire_hoe, Refs.SAPPHIREHOE_NAME);
        GameRegistry.registerItem(sapphire_sickle, Refs.SAPPHIRESICKLE_NAME);

        GameRegistry.registerItem(amethyst_axe, Refs.AMETHYSTAXE_NAME);
        GameRegistry.registerItem(amethyst_sword, Refs.AMETHYSTSWORD_NAME);
        GameRegistry.registerItem(amethyst_pickaxe, Refs.AMETHYSTPICKAXE_NAME);
        GameRegistry.registerItem(amethyst_shovel, Refs.AMETHYSTSPADE_NAME);
        GameRegistry.registerItem(amethyst_hoe, Refs.AMETHYSTHOE_NAME);
        GameRegistry.registerItem(amethyst_sickle, Refs.AMETHYSTSICKLE_NAME);

        GameRegistry.registerItem(wood_sickle, Refs.WOODSICKLE_NAME);
        GameRegistry.registerItem(stone_sickle, Refs.STONESICKLE_NAME);
        GameRegistry.registerItem(iron_sickle, Refs.IRONSICKLE_NAME);
        GameRegistry.registerItem(gold_sickle, Refs.GOLDSICKLE_NAME);
        GameRegistry.registerItem(diamond_sickle, Refs.DIAMONDSICKLE_NAME);

        if (!Loader.isModLoaded(Dependencies.FMP)) {
            GameRegistry.registerItem(iron_saw, Refs.IRONSAW_NAME);
            GameRegistry.registerItem(diamond_saw, Refs.DIAMONDSAW_NAME);
        }
        GameRegistry.registerItem(ruby_saw, Refs.RUBYSAW_NAME);
        GameRegistry.registerItem(sapphire_saw, Refs.SAPPHIRESAW_NAME);
        GameRegistry.registerItem(amethyst_saw, Refs.AMETHYSTSAW_NAME);

        GameRegistry.registerItem(flax_seeds, Refs.FLAXSEED_NAME);
        GameRegistry.registerItem(indigo_dye, Refs.INDIGODYE_NAME);
        GameRegistry.registerItem(silicon_boule, Refs.SILICONBOULE_NAME);
        GameRegistry.registerItem(silicon_wafer, Refs.SILICONWAFER_NAME);
        GameRegistry.registerItem(blue_doped_wafer, Refs.BLUEDOPEDWAFER_NAME);
        GameRegistry.registerItem(red_doped_wafer, Refs.REDDOPEDWAFER_NAME);
        GameRegistry.registerItem(screwdriver, Refs.SCREWDRIVER_NAME);
        GameRegistry.registerItem(silky_screwdriver, Refs.SILKYSCREWDRIVER_NAME);
        GameRegistry.registerItem(athame, Refs.ATHAME_NAME);
        GameRegistry.registerItem(zincplate, Refs.ZINCPLATE_NAME);
        GameRegistry.registerItem(infused_teslatite_dust, Refs.INFUSEDTESLATITEDUST_NAME);

        GameRegistry.registerItem(stone_tile, Refs.STONETILE_NAME);
        GameRegistry.registerItem(bluestone_wire_tile, Refs.BLUESTONEWIRETILE_NAME);
        GameRegistry.registerItem(bluestone_anode_tile, Refs.BLUESTONEANODETILE_NAME);
        GameRegistry.registerItem(bluestone_cathode_tile, Refs.BLUESTONECATHODE_NAME);
        GameRegistry.registerItem(bluestone_pointer_tile, Refs.BLUESTONEPOINTER_NAME);
        GameRegistry.registerItem(silicon_chip_tile, Refs.SILICONCHIP_NAME);
        GameRegistry.registerItem(tainted_silicon_chip_tile, Refs.TAINTEDSILICONCHIP_NAME);
        GameRegistry.registerItem(quartz_resonator_tile, Refs.QUARTZRESONATOR_NAME);
        GameRegistry.registerItem(redstone_wire_tile, Refs.REDSTONEWIRETILE_NAME);
        GameRegistry.registerItem(redstone_anode_tile, Refs.REDSTONEANODETILE_NAME);
        GameRegistry.registerItem(redstone_cathode_tile, Refs.REDSTONECATHODE_NAME);
        GameRegistry.registerItem(redstone_pointer_tile, Refs.REDSTONEPOINTER_NAME);
        // GameRegistry.registerItem(stone_redwire, Refs.STONEREDWIRE_NAME);
        // GameRegistry.registerItem(plate_assembly, Refs.PLATEASSEMBLY_NAME);
        GameRegistry.registerItem(stone_bundle, Refs.STONEBUNDLE_NAME);
        GameRegistry.registerItem(screwdriver_handle, Refs.SCREWDRIVERHANDLE_NAME);
        GameRegistry.registerItem(seed_bag, Refs.SEEDBAG_NAME);
        GameRegistry.registerItem(canvas_bag, Refs.CANVASBAG_NAME);
        GameRegistry.registerItem(canvas, Refs.CANVAS_NAME);
        GameRegistry.registerItem(lumar, Refs.LUMAR_NAME);
        GameRegistry.registerItem(wool_card, Refs.WOOLCARD_NAME);
        GameRegistry.registerItem(diamond_drawplate, Refs.DIAMONDDRAWPLATE_NAME);

        GameRegistry.registerItem(paint_can, Refs.PAINTCAN_NAME);
        GameRegistry.registerItem(paint_brush, Refs.PAINTBRUSH_NAME);

        GameRegistry.registerItem(copper_wire, Refs.COPPERWIRE_NAME);
        GameRegistry.registerItem(iron_wire, Refs.IRONWIRE_NAME);

        GameRegistry.registerItem(multimeter, Refs.MULTIMETER_NAME);
        GameRegistry.registerItem(battery, Refs.BATTERY_ITEM_NAME);

        MinecraftForge.addGrassSeed(new ItemStack(flax_seeds), 5);

        GameRegistry.registerItem(sonicScrewdriver, Refs.SONIC_SCREWDRIVER_NAME);
    }
}

