package net.quetzi.bluepower.init;

import net.minecraft.item.Item;
import net.quetzi.bluepower.items.ItemMalachite;
import net.quetzi.bluepower.items.ItemNikolite;
import net.quetzi.bluepower.items.ItemRuby;
import net.quetzi.bluepower.items.ItemSapphire;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.registry.GameRegistry;

public class Items{
    public static final Item malachite = new ItemMalachite();
    public static final Item sapphire = new ItemSapphire();
    public static final Item ruby = new ItemRuby();
    public static final Item nikolite = new ItemNikolite();

    public static void init(){
        GameRegistry.registerItem(malachite, Refs.ITEMMALACHITE_NAME);
        GameRegistry.registerItem(sapphire, Refs.ITEMSAPPHIRE_NAME);
        GameRegistry.registerItem(ruby, Refs.ITEMRUBY_NAME);
        GameRegistry.registerItem(nikolite, Refs.ITEMNIKOLITE_NAME);
    }
}
