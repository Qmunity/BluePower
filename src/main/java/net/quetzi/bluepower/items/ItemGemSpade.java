package net.quetzi.bluepower.items;

import net.minecraft.item.ItemSpade;
import net.quetzi.bluepower.init.CustomTabs;

public class ItemGemSpade extends ItemSpade {
    public ItemGemSpade(ToolMaterial material, String name) {
        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
    }
}
