package com.bluepowermod.items;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.bluepowermod.init.BPItems;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * This class is used to convert parts using the metadata system to the Item system. TODO Remove in MC 1.8.
 * 
 * @author MineMaarten
 */
public class ItemPartLegacy extends Item {
    @Override
    public String getUnlocalizedName(ItemStack item) {

        return "part." + Refs.MODID + ":" + PartRegistry.getInstance().getPartIdFromItem(item);
    }

    @Override
    public String getItemStackDisplayName(ItemStack item) {
        return EnumChatFormatting.RED + super.getItemStackDisplayName(item);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List list, boolean p_77624_4_) {
        list.add(EnumChatFormatting.RED + I18n.format("message.oldpart"));
    }

    @Override
    public void onUpdate(ItemStack stack, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        convertLegacy(((EntityPlayer) p_77663_3_).inventory, p_77663_4_);
    }

    public static void convertLegacy(IInventory inv, int index) {
        inv.setInventorySlotContents(
                index,
                PartRegistry.getInstance().getItemForPart(PartRegistry.getInstance().getPartIdFromItem(inv.getStackInSlot(index)),
                        inv.getStackInSlot(index).stackSize));
    }

    public static int convertLegacy(IInventory inv) {
        int count = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            if (inv.getStackInSlot(i) != null && inv.getStackInSlot(i).getItem() == BPItems.item_multipart_legacy) {
                convertLegacy(inv, i);
                count++;
            }
        }
        return count;
    }
}
