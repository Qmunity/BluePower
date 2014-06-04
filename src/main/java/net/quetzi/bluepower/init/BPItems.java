package net.quetzi.bluepower.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.oredict.OreDictionary;
import net.quetzi.bluepower.items.ItemCropSeed;
import net.quetzi.bluepower.items.ItemGem;
import net.quetzi.bluepower.items.ItemGemAxe;
import net.quetzi.bluepower.items.ItemGemHoe;
import net.quetzi.bluepower.items.ItemGemPickaxe;
import net.quetzi.bluepower.items.ItemGemSpade;
import net.quetzi.bluepower.items.ItemGemSword;
import net.quetzi.bluepower.items.ItemIndigoDye;
import net.quetzi.bluepower.items.ItemIngot;
import net.quetzi.bluepower.items.ItemNikolite;
import net.quetzi.bluepower.items.ItemSickle;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.registry.GameRegistry;

public class BPItems{
    private static ToolMaterial gemMaterial = EnumHelper.addToolMaterial("GEM", 2, 750, 6.0F, 2.0F, 10);
    
    public static Item malachite;
    public static Item sapphire;
    public static Item ruby;
    public static Item nikolite;
    public static Item copper_ingot;
    public static Item silver_ingot;
    public static Item tin_ingot;
    
    public static Item ruby_axe;
    public static Item ruby_sword;
    public static Item ruby_pickaxe;
    public static Item ruby_spade;    
    public static Item ruby_hoe;
    public static Item ruby_sickle;
    
    public static Item sapphire_axe;
    public static Item sapphire_sword;
    public static Item sapphire_pickaxe;
    public static Item sapphire_spade;
    public static Item sapphire_hoe;
    public static Item sapphire_sickle;
    
    public static Item malachite_axe;
    public static Item malachite_sword;
    public static Item malachite_pickaxe;
    public static Item malachite_spade;
    public static Item malachite_hoe;
    public static Item malachite_sickle;
    
    public static Item wood_sickle;
    public static Item stone_sickle;
    public static Item iron_sickle;
    public static Item gold_sickle;
    public static Item diamond_sickle;
    
    public static Item flax_seed;
    public static Item indigo_dye;
    
    public static void init(){
    	malachite = new ItemGem(Refs.ITEMMALACHITE_NAME);
        sapphire = new ItemGem(Refs.ITEMSAPPHIRE_NAME);
        ruby = new ItemGem(Refs.ITEMRUBY_NAME);
        nikolite = new ItemNikolite();
        copper_ingot = new ItemIngot(Refs.ITEMCOPPERINGOT_NAME);
        silver_ingot = new ItemIngot(Refs.ITEMSILVERINGOT_NAME);
        tin_ingot = new ItemIngot(Refs.ITEMTININGOT_NAME);
        
        ruby_axe = new ItemGemAxe(gemMaterial, Refs.RUBYAXE_NAME);
        ruby_sword = new ItemGemSword(gemMaterial, Refs.RUBYSWORD_NAME);
        ruby_pickaxe = new ItemGemPickaxe(gemMaterial, Refs.RUBYPICKAXE_NAME);
        ruby_spade = new ItemGemSpade(gemMaterial, Refs.RUBYSPADE_NAME);    
        ruby_hoe = new ItemGemHoe(gemMaterial, Refs.RUBYHOE_NAME);
        ruby_sickle = new ItemSickle(gemMaterial, Refs.RUBYSICKLE_NAME);
        
        sapphire_axe = new ItemGemAxe(gemMaterial, Refs.SAPPHIREAXE_NAME);
        sapphire_sword = new ItemGemSword(gemMaterial, Refs.SAPPHIRESWORD_NAME);
        sapphire_pickaxe = new ItemGemPickaxe(gemMaterial, Refs.SAPPHIREPICKAXE_NAME);
        sapphire_spade = new ItemGemSpade(gemMaterial, Refs.SAPPHIRESPADE_NAME);
        sapphire_hoe = new ItemGemHoe(gemMaterial, Refs.SAPPHIREHOE_NAME);
        sapphire_sickle = new ItemSickle(gemMaterial, Refs.SAPPHIRESICKLE_NAME);
        
        malachite_axe = new ItemGemAxe(gemMaterial, Refs.MALACHITEAXE_NAME);
        malachite_sword = new ItemGemSword(gemMaterial, Refs.MALACHITESWORD_NAME);
        malachite_pickaxe = new ItemGemPickaxe(gemMaterial, Refs.MALACHITEPICKAXE_NAME);
        malachite_spade = new ItemGemSpade(gemMaterial, Refs.MALACHITESPADE_NAME);
        malachite_hoe = new ItemGemHoe(gemMaterial, Refs.MALACHITEHOE_NAME);
        malachite_sickle = new ItemSickle(gemMaterial, Refs.MALACHITESICKLE_NAME);
        
        wood_sickle = new ItemSickle(ToolMaterial.WOOD, Refs.WOODSICKLE_NAME);
        stone_sickle = new ItemSickle(ToolMaterial.STONE, Refs.STONESICKLE_NAME);
        iron_sickle = new ItemSickle(ToolMaterial.IRON, Refs.IRONSICKLE_NAME);
        gold_sickle = new ItemSickle(ToolMaterial.GOLD, Refs.GOLDSICKLE_NAME);
        diamond_sickle = new ItemSickle(ToolMaterial.EMERALD, Refs.DIAMONDSICKLE_NAME);
        
        flax_seed = new ItemCropSeed(BPBlocks.flax_crop, Blocks.farmland).setUnlocalizedName(Refs.FLAXSEED_NAME);
        indigo_dye = new ItemIndigoDye(Refs.INDIGODYE_NAME);
    	
    	registerItems();
    }
    
    public static void registerItems(){
        GameRegistry.registerItem(malachite, Refs.ITEMMALACHITE_NAME);
        GameRegistry.registerItem(sapphire, Refs.ITEMSAPPHIRE_NAME);
        GameRegistry.registerItem(ruby, Refs.ITEMRUBY_NAME);
        GameRegistry.registerItem(nikolite, Refs.ITEMNIKOLITE_NAME);
        GameRegistry.registerItem(copper_ingot, Refs.ITEMCOPPERINGOT_NAME);
        GameRegistry.registerItem(silver_ingot, Refs.ITEMSILVERINGOT_NAME);
        GameRegistry.registerItem(tin_ingot, Refs.ITEMTININGOT_NAME);
        //Todo: Add Ore dictionary here. ingotTin, ingotSilver etc. Also add blocks
        //OreDictionary.registerOre(name, ore);
        
        
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
        
        GameRegistry.registerItem(malachite_axe, Refs.MALACHITEAXE_NAME);
        GameRegistry.registerItem(malachite_sword, Refs.MALACHITESWORD_NAME);
        GameRegistry.registerItem(malachite_pickaxe, Refs.MALACHITEPICKAXE_NAME);
        GameRegistry.registerItem(malachite_spade, Refs.MALACHITESPADE_NAME);
        GameRegistry.registerItem(malachite_hoe, Refs.MALACHITEHOE_NAME);
        GameRegistry.registerItem(malachite_sickle, Refs.MALACHITESICKLE_NAME);
        
        GameRegistry.registerItem(wood_sickle, Refs.WOODSICKLE_NAME);
        GameRegistry.registerItem(stone_sickle, Refs.STONESICKLE_NAME);
        GameRegistry.registerItem(iron_sickle, Refs.IRONSICKLE_NAME);
        GameRegistry.registerItem(gold_sickle, Refs.GOLDSICKLE_NAME);
        GameRegistry.registerItem(diamond_sickle, Refs.DIAMONDSICKLE_NAME);
        
        GameRegistry.registerItem(flax_seed,  Refs.FLAXSEED_NAME);
        GameRegistry.registerItem(indigo_dye, Refs.INDIGODYE_NAME);
        MinecraftForge.addGrassSeed(new ItemStack(flax_seed), 5);
    }
}
