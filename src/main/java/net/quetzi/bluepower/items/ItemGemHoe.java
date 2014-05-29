package net.quetzi.bluepower.items;

import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemHoe;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemHoe extends ItemHoe {
    public ItemGemHoe(ToolMaterial material, String name) {
        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }

}