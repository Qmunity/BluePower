package net.quetzi.bluepower.init;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
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
    
    public static final Item malachite = new ItemGem(Refs.ITEMMALACHITE_NAME);
    public static final Item sapphire = new ItemGem(Refs.ITEMSAPPHIRE_NAME);
    public static final Item ruby = new ItemGem(Refs.ITEMRUBY_NAME);
    public static final Item nikolite = new ItemNikolite();
    public static final Item copper_ingot = new ItemIngot(Refs.ITEMCOPPERINGOT_NAME);
    public static final Item silver_ingot = new ItemIngot(Refs.ITEMSILVERINGOT_NAME);
    public static final Item tin_ingot = new ItemIngot(Refs.ITEMTININGOT_NAME);
    
    public static final Item ruby_axe = new ItemGemAxe(gemMaterial, Refs.RUBYAXE_NAME);
    public static final Item ruby_sword = new ItemGemSword(gemMaterial, Refs.RUBYSWORD_NAME);
    public static final Item ruby_pickaxe = new ItemGemPickaxe(gemMaterial, Refs.RUBYPICKAXE_NAME);
    public static final Item ruby_spade = new ItemGemSpade(gemMaterial, Refs.RUBYSPADE_NAME);    
    public static final Item ruby_hoe = new ItemGemHoe(gemMaterial, Refs.RUBYHOE_NAME);
    public static final Item ruby_sickle = new ItemSickle(gemMaterial, Refs.RUBYSICKLE_NAME);
    
    public static final Item sapphire_axe = new ItemGemAxe(gemMaterial, Refs.SAPPHIREAXE_NAME);
    public static final Item sapphire_sword = new ItemGemSword(gemMaterial, Refs.SAPPHIRESWORD_NAME);
    public static final Item sapphire_pickaxe = new ItemGemPickaxe(gemMaterial, Refs.SAPPHIREPICKAXE_NAME);
    public static final Item sapphire_spade = new ItemGemSpade(gemMaterial, Refs.SAPPHIRESPADE_NAME);
    public static final Item sapphire_hoe = new ItemGemHoe(gemMaterial, Refs.SAPPHIREHOE_NAME);
    public static final Item sapphire_sickle = new ItemSickle(gemMaterial, Refs.SAPPHIRESICKLE_NAME);
    
    public static final Item malachite_axe = new ItemGemAxe(gemMaterial, Refs.MALACHITEAXE_NAME);
    public static final Item malachite_sword = new ItemGemSword(gemMaterial, Refs.MALACHITESWORD_NAME);
    public static final Item malachite_pickaxe = new ItemGemPickaxe(gemMaterial, Refs.MALACHITEPICKAXE_NAME);
    public static final Item malachite_spade = new ItemGemSpade(gemMaterial, Refs.MALACHITESPADE_NAME);
    public static final Item malachite_hoe = new ItemGemHoe(gemMaterial, Refs.MALACHITEHOE_NAME);
    public static final Item malachite_sickle = new ItemSickle(gemMaterial, Refs.MALACHITESICKLE_NAME);
    
    public static final Item wood_sickle = new ItemSickle(ToolMaterial.WOOD, Refs.WOODSICKLE_NAME);
    public static final Item stone_sickle = new ItemSickle(ToolMaterial.STONE, Refs.STONESICKLE_NAME);
    public static final Item iron_sickle = new ItemSickle(ToolMaterial.IRON, Refs.IRONSICKLE_NAME);
    public static final Item gold_sickle = new ItemSickle(ToolMaterial.GOLD, Refs.GOLDSICKLE_NAME);
    public static final Item diamond_sickle = new ItemSickle(ToolMaterial.EMERALD, Refs.DIAMONDSICKLE_NAME);
    
    public static final Item flax_seed = new ItemCropSeed(BPBlocks.flax_crop, Blocks.farmland).setUnlocalizedName(Refs.FLAXSEED_NAME);
    public static final Item indigo_dye = new ItemIndigoDye(Refs.INDIGODYE_NAME);
    
    public static void init(){
        GameRegistry.registerItem(malachite, Refs.ITEMMALACHITE_NAME);
        GameRegistry.registerItem(sapphire, Refs.ITEMSAPPHIRE_NAME);
        GameRegistry.registerItem(ruby, Refs.ITEMRUBY_NAME);
        GameRegistry.registerItem(nikolite, Refs.ITEMNIKOLITE_NAME);
        GameRegistry.registerItem(copper_ingot, Refs.ITEMCOPPERINGOT_NAME);
        GameRegistry.registerItem(silver_ingot, Refs.ITEMSILVERINGOT_NAME);
        GameRegistry.registerItem(tin_ingot, Refs.ITEMTININGOT_NAME);
        
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
        
    }
}
