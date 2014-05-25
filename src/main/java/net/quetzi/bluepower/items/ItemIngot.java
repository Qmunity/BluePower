package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemIngot extends Item {
    public ItemIngot(String name) {
        super();
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
