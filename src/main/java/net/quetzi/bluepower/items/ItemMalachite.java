package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemMalachite extends Item {
    public ItemMalachite() {
        super();
        this.setUnlocalizedName(Refs.ITEMMALACHITE_NAME);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }

}
