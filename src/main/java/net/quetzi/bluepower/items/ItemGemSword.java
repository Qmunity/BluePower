package net.quetzi.bluepower.items;

import net.minecraft.item.ItemSword;
import net.minecraft.item.Item.ToolMaterial;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemSword extends ItemSword {
    public ItemGemSword(String name) {
        super(ToolMaterial.IRON);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
