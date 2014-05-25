package net.quetzi.bluepower.items;

import net.minecraft.item.ItemSword;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemSword extends ItemSword {
    public ItemGemSword(ToolMaterial material, String name) {
        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
