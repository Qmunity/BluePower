package net.quetzi.bluepower.items;

import net.minecraft.item.ItemSpade;
import net.minecraft.item.Item.ToolMaterial;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemSpade extends ItemSpade {
    public ItemGemSpade(String name) {
        super(ToolMaterial.IRON);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
