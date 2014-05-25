package net.quetzi.bluepower.items;

import net.minecraft.item.ItemAxe;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemAxe extends ItemAxe {
    public ItemGemAxe(String name) {
        super(ToolMaterial.IRON);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }

}
