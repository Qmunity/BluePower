package com.bluepowermod.tile.tier2;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.bluepower.BluePowerTier;
import com.bluepowermod.api.bluepower.IBluePowered;
import com.bluepowermod.api.bluepower.IChargable;
import com.bluepowermod.api.bluepower.IPowerBase;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * @author Koen Beckers (K4Unl)
 */
public class TileBattery extends TileMachineBase implements IBluePowered, IInventory {

    private ItemStack[] inventory = new ItemStack[2];
    private IPowerBase handler;

    @Override
    public boolean isPowered() {

        return false;
    }

    @Override
    public BluePowerTier getTier() {

        return BluePowerTier.MEDIUMVOLTAGE;
    }

    @Override
    public IPowerBase getHandler() {

        if(handler == null){

            handler = BPApi.getInstance().getNewPowerHandler(this);
        }
        return handler;
    }

    @Override
    public boolean canConnectTo(ForgeDirection dir) {

        return true;
    }

    @Override public float getMaxStorage() {

        return 3000;
    }

    @Override
    public void updateEntity(){

        super.updateEntity();

        if(!getWorldObj().isRemote){

            getHandler().update();
        }

    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound){
        getHandler().readFromNBT(tagCompound);
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound){
        getHandler().writeToNBT(tagCompound);
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

        return itemToTest.getItem() instanceof IChargable;
    }
}
