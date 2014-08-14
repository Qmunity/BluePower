package com.bluepowermod.tileentities.tier3;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.BluePower;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier2.TileCircuitTable;

public class TileCircuitDatabase extends TileCircuitTable {
    
    public IInventory       copyInventory        = new InventoryBasic("copy inventory", false, 2);
    public int              clientCurrentTab;
    public int              curUploadProgress;
    public int              curCopyProgress;
    public int              selectedShareOption;
    public static final int UPLOAD_AND_COPY_TIME = 200;
    
    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {
    
        switch (messageId) {
            case 1:
                player.openGui(BluePower.instance, value == 0 ? GuiIDs.CIRCUITDATABASE_MAIN_ID.ordinal() : GuiIDs.CIRCUITDATABASE_SHARING_ID.ordinal(), worldObj, xCoord, yCoord, zCoord);
                break;
            case 2:
                selectedShareOption = value;
                if (selectedShareOption > 0) {
                    curUploadProgress = 0;
                } else {
                    curUploadProgress = -1;
                }
                break;
            case 3:
                curCopyProgress = curCopyProgress >= 0 ? -1 : 0;
                break;
        
        }
        super.onButtonPress(player, messageId, value);
    }
    
    @Override
    public void updateEntity() {
    
        super.updateEntity();
        if (!worldObj.isRemote) {
            if (copyInventory.getStackInSlot(0) != null) {
                if (curCopyProgress >= 0) {
                    if (++curCopyProgress > UPLOAD_AND_COPY_TIME) {
                        curCopyProgress = -1;
                    }
                }
                
                if (curUploadProgress >= 0) {
                    if (++curUploadProgress > UPLOAD_AND_COPY_TIME) {
                        curUploadProgress = -1;
                    }
                }
            } else {
                curCopyProgress = -1;
                curUploadProgress = -1;
                selectedShareOption = 0;
            }
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
    
        super.writeToNBT(tag);
        
        if (copyInventory.getStackInSlot(0) != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            copyInventory.getStackInSlot(0).writeToNBT(stackTag);
            tag.setTag("copyTemplateStack", stackTag);
        }
        if (copyInventory.getStackInSlot(1) != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            copyInventory.getStackInSlot(1).writeToNBT(stackTag);
            tag.setTag("copyOutputStack", stackTag);
        }
        
        tag.setInteger("curUploadProgress", curUploadProgress);
        tag.setInteger("curCopyProgress", curCopyProgress);
        tag.setByte("selectedShareOption", (byte) selectedShareOption);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
    
        super.readFromNBT(tag);
        
        if (tag.hasKey("copyTemplateStack")) {
            copyInventory.setInventorySlotContents(0, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("copyTemplateStack")));
        } else {
            copyInventory.setInventorySlotContents(0, null);
        }
        
        if (tag.hasKey("copyOutputStack")) {
            copyInventory.setInventorySlotContents(1, ItemStack.loadItemStackFromNBT(tag.getCompoundTag("copyOutputStack")));
        } else {
            copyInventory.setInventorySlotContents(1, null);
        }
        
        curUploadProgress = tag.getInteger("curUploadProgress");
        curCopyProgress = tag.getInteger("curCopyProgress");
        selectedShareOption = tag.getByte("selectedShareOption");
    }
    
    @Override
    public String getInventoryName() {
    
        return BPBlocks.circuit_database.getUnlocalizedName();
    }
}
