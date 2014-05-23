package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemNikolite extends Item {
    public ItemNikolite() {
        super();
        this.setUnlocalizedName(Refs.ITEMNIKOLITE_NAME);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
