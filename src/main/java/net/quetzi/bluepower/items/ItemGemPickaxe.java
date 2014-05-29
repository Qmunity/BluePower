package net.quetzi.bluepower.items;

import net.minecraft.item.ItemPickaxe;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemPickaxe extends ItemPickaxe {
    public ItemGemPickaxe(ToolMaterial material, String name) {
        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerTools);
    }
}
