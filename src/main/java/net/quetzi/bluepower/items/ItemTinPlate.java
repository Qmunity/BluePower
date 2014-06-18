package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

public class ItemTinPlate extends Item {
    
    public ItemTinPlate() {
    
        this.setUnlocalizedName(Refs.TINPLATE_NAME);
        this.setCreativeTab(CustomTabs.tabBluePowerItems);
        this.setTextureName(Refs.MODID + ":" + Refs.TINPLATE_NAME);
    }
    
}
