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

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;

import com.bluepowermod.BluePower;
import com.bluepowermod.compat.fmp.ItemBPMultipart;
import com.bluepowermod.items.ItemAthame;
import com.bluepowermod.items.ItemBase;
import com.bluepowermod.items.ItemCanvasBag;
import com.bluepowermod.items.ItemCrafting;
import com.bluepowermod.items.ItemCropSeed;
import com.bluepowermod.items.ItemGemAxe;
import com.bluepowermod.items.ItemGemHoe;
import com.bluepowermod.items.ItemGemPickaxe;
import com.bluepowermod.items.ItemGemSpade;
import com.bluepowermod.items.ItemGemSword;
import com.bluepowermod.items.ItemIndigoDye;
import com.bluepowermod.items.ItemLimitedCrafting;
import com.bluepowermod.items.ItemLumar;
import com.bluepowermod.items.ItemPaintBrush;
import com.bluepowermod.items.ItemPaintCan;
import com.bluepowermod.items.ItemSaw;
import com.bluepowermod.items.ItemScrewdriver;
import com.bluepowermod.items.ItemSeedBag;
import com.bluepowermod.items.ItemSickle;
import com.bluepowermod.items.ItemSilkyScrewdriver;
import com.bluepowermod.part.ItemBPPart;
import com.bluepowermod.util.Dependencies;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Refs.MODID)
public class BPItems {
    
    public static final Item amethyst                  = new ItemCrafting(Refs.AMETHYST_NAME);
    public static final Item sapphire                  = new ItemCrafting(Refs.SAPPHIRE_NAME);
    public static final Item ruby                      = new ItemCrafting(Refs.RUBY_NAME);
    public static final Item teslatite                 = new ItemCrafting(Refs.TESLATITE_NAME);
    public static final Item copper_ingot              = new ItemCrafting(Refs.COPPERINGOT_NAME);
    public static final Item silver_ingot              = new ItemCrafting(Refs.SILVERINGOT_NAME);
    public static final Item zinc_ingot                = new ItemCrafting(Refs.ZINCINGOT_NAME);
    public static final Item brass_ingot               = new ItemCrafting(Refs.BRASSINGOT_NAME);
    public static final Item blue_alloy_ingot          = new ItemCrafting(Refs.BLUEALLOYINGOT_NAME);
    //    public static final Item red_alloy_ingot = new ItemCrafting(Refs.REDALLOYINGOT_NAME);
    public static final Item tungsten_ingot            = new ItemCrafting(Refs.TUNGSTENINGOT_NAME);
    public static final Item zinc_ore_crushed          = new ItemBase().setUnlocalizedName(Refs.ZINC_ORE_CRUSHED_NAME).setTextureName(Refs.MODID + ":" + Refs.ZINC_ORE_CRUSHED_NAME).setCreativeTab(CustomTabs.tabBluePowerItems);
    public static final Item zinc_ore_crushed_purified = new ItemBase().setUnlocalizedName(Refs.ZINC_ORE_CRUSHED_PURIFIED_NAME).setTextureName(Refs.MODID + ":" + Refs.ZINC_ORE_CRUSHED_PURIFIED_NAME).setCreativeTab(CustomTabs.tabBluePowerItems);
    public static final Item zinc_dust                 = new ItemBase().setUnlocalizedName(Refs.ZINCDUST_NAME).setTextureName(Refs.MODID + ":" + Refs.ZINCDUST_NAME).setCreativeTab(CustomTabs.tabBluePowerItems);
    public static final Item zinc_dust_tiny            = new ItemBase().setUnlocalizedName(Refs.ZINCDUST_TINY_NAME).setTextureName(Refs.MODID + ":" + Refs.ZINCDUST_TINY_NAME).setCreativeTab(CustomTabs.tabBluePowerItems);
    public static final Item ruby_saw                  = new ItemSaw(2, Refs.RUBYSAW_NAME);
    public static final Item sapphire_saw              = new ItemSaw(2, Refs.SAPPHIRESAW_NAME);
    public static final Item amethyst_saw              = new ItemSaw(2, Refs.AMETHYSTSAW_NAME);
    public static final Item wood_sickle               = new ItemSickle(ToolMaterial.WOOD, Refs.WOODSICKLE_NAME);
    public static final Item stone_sickle              = new ItemSickle(ToolMaterial.STONE, Refs.STONESICKLE_NAME);
    public static final Item iron_sickle               = new ItemSickle(ToolMaterial.IRON, Refs.IRONSICKLE_NAME);
    public static final Item gold_sickle               = new ItemSickle(ToolMaterial.GOLD, Refs.GOLDSICKLE_NAME);
    public static final Item diamond_sickle            = new ItemSickle(ToolMaterial.EMERALD, Refs.DIAMONDSICKLE_NAME);
    public static final Item flax_seed                 = new ItemCropSeed(BPBlocks.flax_crop, Blocks.farmland).setUnlocalizedName(Refs.FLAXSEED_NAME);
    public static final Item indigo_dye                = new ItemIndigoDye(Refs.INDIGODYE_NAME);
    public static final Item silicon_boule             = new ItemCrafting(Refs.SILICONBOULE_NAME).setCreativeTab(CustomTabs.tabBluePowerItems);
    public static final Item silicon_wafer             = new ItemCrafting(Refs.SILICONWAFER_NAME).setCreativeTab(CustomTabs.tabBluePowerItems);
    public static final Item red_doped_wafer           = new ItemCrafting(Refs.REDDOPEDWAFER_NAME).setCreativeTab(CustomTabs.tabBluePowerItems);
    public static final Item blue_doped_wafer          = new ItemCrafting(Refs.BLUEDOPEDWAFER_NAME).setCreativeTab(CustomTabs.tabBluePowerItems);
    public static final Item screwdriver               = new ItemScrewdriver();
    public static final Item silky_screwdriver         = new ItemSilkyScrewdriver();
    public static final Item athame                    = new ItemAthame();
    public static final Item zincplate                 = new ItemCrafting(Refs.ZINCPLATE_NAME);
    public static final Item stone_tile                = new ItemCrafting(Refs.STONETILE_NAME);
    public static final Item stone_wire                = new ItemCrafting(Refs.STONEWIRE_NAME);
    public static final Item stone_anode               = new ItemCrafting(Refs.STONEANODE_NAME);
    public static final Item stone_cathode             = new ItemCrafting(Refs.STONECATHODE_NAME);
    public static final Item stone_pointer             = new ItemCrafting(Refs.STONEPOINTER_NAME);
    public static final Item silicon_chip              = new ItemCrafting(Refs.SILICONCHIP_NAME);
    public static final Item taintedsilicon_chip       = new ItemCrafting(Refs.TAINTEDSILICONCHIP_NAME);
    public static final Item stone_redwire             = new ItemCrafting(Refs.STONEREDWIRE_NAME);
    public static final Item plate_assembly            = new ItemCrafting(Refs.PLATEASSEMBLY_NAME);
    public static final Item stone_bundle              = new ItemCrafting(Refs.STONEBUNDLE_NAME);
    public static final Item screwdriver_handle        = new ItemCrafting(Refs.SCREWDRIVERHANDLE_NAME);
    public static final Item seedBag                   = new ItemSeedBag(Refs.SEEDBAG_NAME);
    public static final Item canvas_bag                = new ItemCanvasBag(Refs.CANVASBAG_NAME);
    public static final Item canvas                    = new ItemCrafting(Refs.CANVAS_NAME);
    public static final Item lumar                     = new ItemLumar();
    public static final Item wool_card                 = new ItemLimitedCrafting(Refs.WOOLCARD_NAME, 64);
    public static final Item diamond_drawplate         = new ItemLimitedCrafting(Refs.DIAMONDDRAWPLATE_NAME, 256);
    public static final Item copper_wire               = new ItemCrafting(Refs.COPPERWIRE_NAME);
    public static final Item iron_wire                 = new ItemCrafting(Refs.IRONWIRE_NAME);
    public static final Item paint_can                 = new ItemPaintCan(Refs.PAINTCAN_NAME);
    public static final Item paint_brush               = new ItemPaintBrush(Refs.PAINTBRUSH_NAME);
    public static Item       iron_saw;
    public static Item       diamond_saw;
    public static Item       multipart;
    public static final Item ruby_axe                  = new ItemGemAxe(BluePower.gemMaterial, Refs.RUBYAXE_NAME);
    public static final Item ruby_sword                = new ItemGemSword(BluePower.gemMaterial, Refs.RUBYSWORD_NAME);
    public static final Item ruby_pickaxe              = new ItemGemPickaxe(BluePower.gemMaterial, Refs.RUBYPICKAXE_NAME);
    public static final Item ruby_spade                = new ItemGemSpade(BluePower.gemMaterial, Refs.RUBYSPADE_NAME);
    public static final Item ruby_hoe                  = new ItemGemHoe(BluePower.gemMaterial, Refs.RUBYHOE_NAME);
    public static final Item ruby_sickle               = new ItemSickle(BluePower.gemMaterial, Refs.RUBYSICKLE_NAME);
    public static final Item sapphire_axe              = new ItemGemAxe(BluePower.gemMaterial, Refs.SAPPHIREAXE_NAME);
    public static final Item sapphire_sword            = new ItemGemSword(BluePower.gemMaterial, Refs.SAPPHIRESWORD_NAME);
    public static final Item sapphire_pickaxe          = new ItemGemPickaxe(BluePower.gemMaterial, Refs.SAPPHIREPICKAXE_NAME);
    public static final Item sapphire_spade            = new ItemGemSpade(BluePower.gemMaterial, Refs.SAPPHIRESPADE_NAME);
    public static final Item sapphire_hoe              = new ItemGemHoe(BluePower.gemMaterial, Refs.SAPPHIREHOE_NAME);
    public static final Item sapphire_sickle           = new ItemSickle(BluePower.gemMaterial, Refs.SAPPHIRESICKLE_NAME);
    public static final Item amethyst_axe              = new ItemGemAxe(BluePower.gemMaterial, Refs.AMETHYSTAXE_NAME);
    public static final Item amethyst_sword            = new ItemGemSword(BluePower.gemMaterial, Refs.AMETHYSTSWORD_NAME);
    public static final Item amethyst_pickaxe          = new ItemGemPickaxe(BluePower.gemMaterial, Refs.AMETHYSTPICKAXE_NAME);
    public static final Item amethyst_spade            = new ItemGemSpade(BluePower.gemMaterial, Refs.AMETHYSTSPADE_NAME);
    public static final Item amethyst_hoe              = new ItemGemHoe(BluePower.gemMaterial, Refs.AMETHYSTHOE_NAME);
    public static final Item amethyst_sickle           = new ItemSickle(BluePower.gemMaterial, Refs.AMETHYSTSICKLE_NAME);
    
    public static void init() {
    
        if (!Loader.isModLoaded("ForgeMicroblock")) {// FMP already has an iron and diamond saw
            iron_saw = new ItemSaw(2, Refs.IRONSAW_NAME);
            diamond_saw = new ItemSaw(3, Refs.DIAMONDSAW_NAME);
        }
        
        if (!Loader.isModLoaded(Dependencies.FMP)) {
            multipart = new ItemBPPart();
        } else {
            initFMP();
        }
        
        registerItems();
    }
    
    @Optional.Method(modid = Dependencies.FMP)
    private static void initFMP() {
    
        multipart = new ItemBPMultipart();
    }
    
    public static void registerItems() {
    
        GameRegistry.registerItem(amethyst, Refs.AMETHYST_NAME);
        GameRegistry.registerItem(sapphire, Refs.SAPPHIRE_NAME);
        GameRegistry.registerItem(ruby, Refs.RUBY_NAME);
        GameRegistry.registerItem(teslatite, Refs.TESLATITE_NAME);
        GameRegistry.registerItem(copper_ingot, Refs.COPPERINGOT_NAME);
        GameRegistry.registerItem(silver_ingot, Refs.SILVERINGOT_NAME);
        GameRegistry.registerItem(zinc_ingot, Refs.ZINCINGOT_NAME);
        GameRegistry.registerItem(tungsten_ingot, Refs.TUNGSTENINGOT_NAME);
        GameRegistry.registerItem(brass_ingot, Refs.BRASSINGOT_NAME);
        GameRegistry.registerItem(blue_alloy_ingot, Refs.BLUEALLOYINGOT_NAME);
        // GameRegistry.registerItem(red_alloy_ingot, Refs.REDALLOYINGOT_NAME);
        
        GameRegistry.registerItem(zinc_dust, Refs.ZINCDUST_NAME);
        GameRegistry.registerItem(zinc_ore_crushed, Refs.ZINC_ORE_CRUSHED_NAME);
        GameRegistry.registerItem(zinc_ore_crushed_purified, Refs.ZINC_ORE_CRUSHED_PURIFIED_NAME);
        GameRegistry.registerItem(zinc_dust_tiny, Refs.ZINCDUST_TINY_NAME);
        
        GameRegistry.registerItem(ruby_axe, Refs.RUBYAXE_NAME);
        GameRegistry.registerItem(ruby_sword, Refs.RUBYSWORD_NAME);
        GameRegistry.registerItem(ruby_pickaxe, Refs.RUBYPICKAXE_NAME);
        GameRegistry.registerItem(ruby_spade, Refs.RUBYSPADE_NAME);
        GameRegistry.registerItem(ruby_hoe, Refs.RUBYHOE_NAME);
        GameRegistry.registerItem(ruby_sickle, Refs.RUBYSICKLE_NAME);
        
        GameRegistry.registerItem(sapphire_axe, Refs.SAPPHIREAXE_NAME);
        GameRegistry.registerItem(sapphire_sword, Refs.SAPPHIRESWORD_NAME);
        GameRegistry.registerItem(sapphire_pickaxe, Refs.SAPPHIREPICKAXE_NAME);
        GameRegistry.registerItem(sapphire_spade, Refs.SAPPHIRESPADE_NAME);
        GameRegistry.registerItem(sapphire_hoe, Refs.SAPPHIREHOE_NAME);
        GameRegistry.registerItem(sapphire_sickle, Refs.SAPPHIRESICKLE_NAME);
        
        GameRegistry.registerItem(amethyst_axe, Refs.AMETHYSTAXE_NAME);
        GameRegistry.registerItem(amethyst_sword, Refs.AMETHYSTSWORD_NAME);
        GameRegistry.registerItem(amethyst_pickaxe, Refs.AMETHYSTPICKAXE_NAME);
        GameRegistry.registerItem(amethyst_spade, Refs.AMETHYSTSPADE_NAME);
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
        
        GameRegistry.registerItem(flax_seed, Refs.FLAXSEED_NAME);
        GameRegistry.registerItem(indigo_dye, Refs.INDIGODYE_NAME);
        GameRegistry.registerItem(silicon_boule, Refs.SILICONBOULE_NAME);
        GameRegistry.registerItem(silicon_wafer, Refs.SILICONWAFER_NAME);
        GameRegistry.registerItem(blue_doped_wafer, Refs.BLUEDOPEDWAFER_NAME);
        GameRegistry.registerItem(red_doped_wafer, Refs.REDDOPEDWAFER_NAME);
        GameRegistry.registerItem(screwdriver, Refs.SCREWDRIVER_NAME);
        GameRegistry.registerItem(silky_screwdriver, Refs.SILKYSCREWDRIVER_NAME);
        GameRegistry.registerItem(athame, Refs.ATHAME_NAME);
        GameRegistry.registerItem(zincplate, Refs.ZINCPLATE_NAME);
        
        GameRegistry.registerItem(stone_tile, Refs.STONETILE_NAME);
        GameRegistry.registerItem(stone_wire, Refs.STONEWIRE_NAME);
        GameRegistry.registerItem(stone_anode, Refs.STONEANODE_NAME);
        GameRegistry.registerItem(stone_cathode, Refs.STONECATHODE_NAME);
        GameRegistry.registerItem(stone_pointer, Refs.STONEPOINTER_NAME);
        GameRegistry.registerItem(silicon_chip, Refs.SILICONCHIP_NAME);
        GameRegistry.registerItem(taintedsilicon_chip, Refs.TAINTEDSILICONCHIP_NAME);
        //  GameRegistry.registerItem(stone_redwire, Refs.STONEREDWIRE_NAME);
        //   GameRegistry.registerItem(plate_assembly, Refs.PLATEASSEMBLY_NAME);
        GameRegistry.registerItem(stone_bundle, Refs.STONEBUNDLE_NAME);
        GameRegistry.registerItem(screwdriver_handle, Refs.SCREWDRIVERHANDLE_NAME);
        GameRegistry.registerItem(seedBag, Refs.SEEDBAG_NAME);
        GameRegistry.registerItem(canvas_bag, Refs.CANVASBAG_NAME);
        GameRegistry.registerItem(canvas, Refs.CANVAS_NAME);
        GameRegistry.registerItem(lumar, Refs.LUMAR_NAME);
        GameRegistry.registerItem(wool_card, Refs.WOOLCARD_NAME);
        GameRegistry.registerItem(diamond_drawplate, Refs.DIAMONDDRAWPLATE_NAME);
        
        GameRegistry.registerItem(paint_can, Refs.PAINTCAN_NAME);
        GameRegistry.registerItem(paint_brush, Refs.PAINTBRUSH_NAME);
        
        GameRegistry.registerItem(copper_wire, Refs.COPPERWIRE_NAME);
        GameRegistry.registerItem(iron_wire, Refs.IRONWIRE_NAME);
        
        MinecraftForge.addGrassSeed(new ItemStack(flax_seed), 5);
        
        GameRegistry.registerItem(multipart, Refs.MULTIPART_NAME);
    }
}
