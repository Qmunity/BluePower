package net.quetzi.bluepower.items;

import net.minecraft.item.ItemHoe;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemTungCarbHoe extends ItemHoe
{
	public ItemTungCarbHoe(ToolMaterial material, String name) {

        super(material);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerTools);
        this.setTextureName(Refs.MODID + ":" + name);
	}
}
