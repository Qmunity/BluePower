package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemRuby extends Item {
    public ItemRuby() {
        super();
        this.setUnlocalizedName(Refs.ITEMRUBY_NAME);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
