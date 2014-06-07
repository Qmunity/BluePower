package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemSaw extends Item
{
    private int sawLevel;

    public ItemSaw(int sawLevel, String name)
    {
        this.setCreativeTab(CustomTabs.tabBluePowerTools);
        this.sawLevel = sawLevel;
        this.setTextureName(Refs.MODID + ":" + name);
        this.setUnlocalizedName(name);
    }

    public int getSawLevel()
    {
        return sawLevel;
    }
}
