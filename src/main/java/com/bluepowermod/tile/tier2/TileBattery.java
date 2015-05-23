package com.bluepowermod.tile.tier2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.network.annotation.DescSynced;
import uk.co.qmunity.lib.network.annotation.GuiSynced;

import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.api.power.IRechargeable;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBluePowerBase;

/**
 * @author Koen Beckers (K4Unl)
 */
public class TileBattery extends TileBluePowerBase implements IPowered, IInventory {

    private final ItemStack[] inventory = new ItemStack[2];

    @DescSynced
    private int textureIndex;

    @GuiSynced
    private final IPowerBase powerBase = getPowerHandler(ForgeDirection.UNKNOWN);
    private final int powerTransfer = 1;

    @GuiSynced
    private int energyBuffer;

    public static final int MAX_ENERGY_BUFFER = 100;

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!getWorldObj().isRemote) {
            if (powerBase.getVoltage() < 0.9 * powerBase.getMaxVoltage() && energyBuffer > 0) {
                energyBuffer--;
                powerBase.addEnergy(1, false);
            } else if (powerBase.getVoltage() > 0.95 * powerBase.getMaxVoltage() && energyBuffer < MAX_ENERGY_BUFFER) {
                energyBuffer++;
                powerBase.addEnergy(-1, false);
            }

            // Check if there's an item in the inventory
            if (inventory[0] != null && inventory[0].getItem() instanceof IRechargeable) {
                IRechargeable battery = (IRechargeable) inventory[0].getItem();
                energyBuffer += -battery.addEnergy(inventory[0], -Math.min(powerTransfer, MAX_ENERGY_BUFFER - energyBuffer));
            }
            if (inventory[1] != null && inventory[1].getItem() instanceof IRechargeable) {
                IRechargeable battery = (IRechargeable) inventory[1].getItem();
                energyBuffer -= battery.addEnergy(inventory[1], Math.min(powerTransfer, energyBuffer));
            }
            if (worldObj.getWorldTime() % 20 == 0)
                recalculateTextureIndex();
        }

    }

    public double getBufferPercentage() {
        return (double) energyBuffer / MAX_ENERGY_BUFFER;
    }

    private void recalculateTextureIndex() {

        textureIndex = (int) Math.floor((double) energyBuffer / MAX_ENERGY_BUFFER * 6.0);
    }

    @Override
    public int getSizeInventory() {

        return 2;
    }

    @Override
    public ItemStack getStackInSlot(int index) {

        return inventory[index];
    }

    @Override
    public ItemStack decrStackSize(int index, int amount) {

        ItemStack itemStack = getStackInSlot(index);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(index, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(index, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {

        ItemStack itemStack = getStackInSlot(index);
        if (itemStack != null) {
            setInventorySlotContents(index, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack toSet) {

        inventory[index] = toSet;
    }

    @Override
    public String getInventoryName() {

        return BPBlocks.battery.getUnlocalizedName();
    }

    @Override
    public boolean hasCustomInventoryName() {

        return true;
    }

    @Override
    public int getInventoryStackLimit() {

        return 1;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer p_70300_1_) {

        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemToTest) {

        return itemToTest != null && itemToTest.getItem() instanceof IRechargeable;
    }

    public int getTextureIndex() {

        return textureIndex;
    }
}
