package net.quetzi.bluepower.init;

import net.minecraft.item.Item;
import net.quetzi.bluepower.items.ItemGem;
import net.quetzi.bluepower.items.ItemGemAxe;
import net.quetzi.bluepower.items.ItemGemPickaxe;
import net.quetzi.bluepower.items.ItemGemSpade;
import net.quetzi.bluepower.items.ItemGemSword;
import net.quetzi.bluepower.items.ItemIngot;
import net.quetzi.bluepower.items.ItemNikolite;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.registry.GameRegistry;

public class BPItems{
    public static final Item malachite = new ItemGem(Refs.ITEMMALACHITE_NAME);
    public static final Item sapphire = new ItemGem(Refs.ITEMSAPPHIRE_NAME);
    public static final Item ruby = new ItemGem(Refs.ITEMRUBY_NAME);
    public static final Item nikolite = new ItemNikolite();
    public static final Item copper_ingot = new ItemIngot(Refs.ITEMCOPPERINGOT_NAME);
    public static final Item silver_ingot = new ItemIngot(Refs.ITEMSILVERINGOT_NAME);
    public static final Item tin_ingot = new ItemIngot(Refs.ITEMTININGOT_NAME);
    
    public static final Item ruby_axe = new ItemGemAxe(Refs.RUBYAXE_NAME);
    public static final Item ruby_sword = new ItemGemSword(Refs.RUBYSWORD_NAME);
    public static final Item ruby_pickaxe = new ItemGemPickaxe(Refs.RUBYPICKAXE_NAME);
    public static final Item ruby_spade = new ItemGemSpade(Refs.RUBYSPADE_NAME);    
    public static final Item sapphire_axe = new ItemGemAxe(Refs.SAPPHIREAXE_NAME);
    public static final Item sapphire_sword = new ItemGemSword(Refs.SAPPHIRESWORD_NAME);
    public static final Item sapphire_pickaxe = new ItemGemPickaxe(Refs.SAPPHIREPICKAXE_NAME);
    public static final Item sapphire_spade = new ItemGemSpade(Refs.SAPPHIRESPADE_NAME);
    public static final Item malachite_axe = new ItemGemAxe(Refs.RUBYAXE_NAME);
    public static final Item malachite_sword = new ItemGemSword(Refs.MALACHITESWORD_NAME);
    public static final Item malachite_pickaxe = new ItemGemPickaxe(Refs.MALACHITEPICKAXE_NAME);
    public static final Item malachite_spade = new ItemGemSpade(Refs.MALACHITESPADE_NAME);
    
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
        GameRegistry.registerItem(sapphire_axe, Refs.SAPPHIREAXE_NAME);
        GameRegistry.registerItem(sapphire_sword, Refs.SAPPHIRESWORD_NAME);
        GameRegistry.registerItem(sapphire_pickaxe, Refs.SAPPHIREPICKAXE_NAME);
        GameRegistry.registerItem(sapphire_spade, Refs.SAPPHIRESPADE_NAME);
        GameRegistry.registerItem(malachite_axe, Refs.MALACHITEAXE_NAME);
        GameRegistry.registerItem(malachite_sword, Refs.MALACHITESWORD_NAME);
        GameRegistry.registerItem(malachite_pickaxe, Refs.MALACHITEPICKAXE_NAME);
        GameRegistry.registerItem(malachite_spade, Refs.MALACHITESPADE_NAME);
        
    }
}
