package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemIndigoDye extends Item {
    public ItemIndigoDye(String name) {
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerItems);
        this.setTextureName(Refs.MODID + ":" + name);
    }
}
