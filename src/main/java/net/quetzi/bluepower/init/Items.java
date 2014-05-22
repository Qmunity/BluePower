package net.quetzi.bluepower.init;

import net.minecraft.item.Item;
import net.quetzi.bluepower.items.ItemMalachite;
import net.quetzi.bluepower.items.ItemNikolite;
import net.quetzi.bluepower.items.ItemRuby;
import net.quetzi.bluepower.items.ItemSapphire;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.registry.GameRegistry;


public class Items {
    public static void init() {
        GameRegistry.registerItem(new ItemMalachite(), Refs.ITEMMALACHITE_NAME);
        GameRegistry.registerItem(new ItemSapphire(), Refs.ITEMSAPPHIRE_NAME);
        GameRegistry.registerItem(new ItemRuby(), Refs.ITEMRUBY_NAME);
        GameRegistry.registerItem(new ItemNikolite(), Refs.ITEMNIKOLITE_NAME);
    }
    
    public static final Item malachite = (Item)Item.itemRegistry.getObject(Refs.ITEMMALACHITE_NAME);
    public static final Item sapphire = (Item)Item.itemRegistry.getObject(Refs.ITEMSAPPHIRE_NAME);
    public static final Item ruby = (Item)Item.itemRegistry.getObject(Refs.ITEMRUBY_NAME);
    public static final Item nikolite = (Item)Item.itemRegistry.getObject(Refs.ITEMNIKOLITE_NAME);
}
