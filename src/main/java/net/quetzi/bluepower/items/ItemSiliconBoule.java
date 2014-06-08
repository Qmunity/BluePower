package net.quetzi.bluepower.items;

import net.minecraft.item.Item;
import net.quetzi.bluepower.references.Refs;

public class ItemSiliconBoule extends Item {
    
    public ItemSiliconBoule(String name) {
    
        this.setUnlocalizedName(name);
        this.setTextureName(Refs.MODID + ":" + name);
    }
}
