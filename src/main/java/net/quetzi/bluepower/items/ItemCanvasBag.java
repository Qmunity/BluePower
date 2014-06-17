package net.quetzi.bluepower.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.quetzi.bluepower.BluePower;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.GuiIDs;
import net.quetzi.bluepower.references.Refs;

public class ItemCanvasBag extends Item {
    
    public ItemCanvasBag(String name) {
    
        this.setCreativeTab(CustomTabs.tabBluePowerItems);
        this.setUnlocalizedName(name);
        this.setTextureName(Refs.MODID + ":" + name);
    }
    
    @Override
    public ItemStack onItemRightClick(ItemStack itemstack, World worldObj, EntityPlayer playerEntity) {
    
        if (!worldObj.isRemote) {
            playerEntity.openGui(BluePower.instance, GuiIDs.CANVAS_BAG.ordinal(), worldObj, (int) playerEntity.posX, (int) playerEntity.posY,
                    (int) playerEntity.posZ);
        }
        return itemstack;
    }
}
