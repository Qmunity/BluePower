package com.bluepowermod.tileentities.tier2;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.tileentities.tier1.TileFilter;
import com.bluepowermod.util.Dependencies;

public class TileRetriever extends TileFilter {
    
    public int slotIndex;
    public int mode;
    
    @Override
    protected void pullItem() {
    
        if (isBufferEmpty()) {
            TileEntity extractingInventory = getTileCache()[getFacingDirection().ordinal()].getTileEntity();
            IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
            PneumaticTube tube = compat.getBPPart(extractingInventory, PneumaticTube.class);
            if (tube != null) {
                boolean everythingNull = true;
                for (int i = 0; i < inventory.length; i++) {
                    if (mode == 1 || slotIndex == i) {
                        ItemStack stack = inventory[i];
                        if (stack != null) {
                            if (tube.getLogic().retrieveStack(this, getFacingDirection(), stack)) {
                                if (mode == 0) {
                                    slotIndex++;
                                    while (slotIndex != i) {
                                        if (inventory[slotIndex] != null) break;
                                        if (++slotIndex >= inventory.length) slotIndex = 0;
                                    }
                                }
                                return;
                            }
                            everythingNull = false;
                        }
                    }
                }
                if (everythingNull) {
                    tube.getLogic().retrieveStack(this, getFacingDirection(), null);
                    slotIndex = 0;
                }
            } else {
                super.pullItem();
            }
        }
    }
    
    @Override
    public void onButtonPress(int messageId, int value) {
    
        if (messageId == 1) {
            mode = value;
        } else {
            super.onButtonPress(messageId, value);
        }
    }
    
    @Override
    public void writeToNBT(NBTTagCompound tag) {
    
        super.writeToNBT(tag);
        tag.setByte("slotIndex", (byte) slotIndex);
        tag.setByte("mode", (byte) mode);
    }
    
    @Override
    public void readFromNBT(NBTTagCompound tag) {
    
        super.readFromNBT(tag);
        slotIndex = tag.getByte("slotIndex");
        mode = tag.getByte("mode");
    }
}
