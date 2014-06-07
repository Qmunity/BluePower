package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemGem extends Item {
    public ItemGem(String name) {
        super();
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerItems);
        this.setTextureName(Refs.MODID + ":" + name);
    }
}
