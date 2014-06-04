package net.quetzi.bluepower.items;

import net.minecraft.item.ItemSpade;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemGemSpade extends ItemSpade {
    public ItemGemSpade(ToolMaterial material, String name) {
        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerTools);
        this.setTextureName(Refs.MODID + ":" + name);
    }
}
