package net.quetzi.bluepower.items;

import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.Item.ToolMaterial;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemPickaxe extends ItemPickaxe {
    public ItemGemPickaxe(String name) {
        super(ToolMaterial.IRON);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
