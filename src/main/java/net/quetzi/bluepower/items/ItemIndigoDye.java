package net.quetzi.bluepower.items;

import net.minecraft.item.ItemDye;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemIndigoDye extends ItemDye {
    public ItemIndigoDye(String name) {
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
