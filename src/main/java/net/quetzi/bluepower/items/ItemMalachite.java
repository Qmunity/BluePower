package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.references.Refs;

public class ItemMalachite extends Item {
    public ItemMalachite() {
        super();
        this.setUnlocalizedName(Refs.ITEMMALACHITE_NAME);
        this.setCreativeTab(BluePower.creativeTab);
    }

}
