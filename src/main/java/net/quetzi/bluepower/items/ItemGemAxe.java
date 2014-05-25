package net.quetzi.bluepower.items;

import net.minecraft.item.ItemAxe;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemAxe extends ItemAxe {
    public ItemGemAxe(ToolMaterial material, String name) {
        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }

}
