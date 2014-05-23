package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemSapphire extends Item {
    public ItemSapphire() {
        super();
        this.setUnlocalizedName(Refs.ITEMSAPPHIRE_NAME);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
