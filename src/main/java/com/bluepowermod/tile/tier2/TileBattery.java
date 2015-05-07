package com.bluepowermod.tile.tier2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.power.IBluePowered;
import com.bluepowermod.api.power.IChargeable;
import com.bluepowermod.api.power.IPowerHandler;
import com.bluepowermod.api.power.PowerTier;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl)
 */
public class TileBattery extends TileBase implements IBluePowered, IInventory {

    private ItemStack[] inventory = new ItemStack[2];

    private static float powerTransfer = 1; // The amount of Amps being transfered into or out of a battery, every tick
    private int textureIndex;

    private IPowerHandler handler;

    @SideOnly(Side.CLIENT)
    private float ampStored;
    @SideOnly(Side.CLIENT)
    private float maxAmp;

    @Override
    public PowerTier getPowerTier() {

        return PowerTier.MEDIUMVOLTAGE;
    }

    @Override
    public IPowerHandler getPowerHandler(ForgeDirection side) {

        return getPowerHandler();
    }

    public IPowerHandler getPowerHandler() {

        if (worldObj.isRemote)
            throw new IllegalStateException("Handler can only be accessed from the server!");

        if (handler == null)
            handler = BPApi.getInstance().getNewPowerHandler(this, 3000);

        return handler;
    }

    @Override
    public boolean canConnectPower(ForgeDirection side, IBluePowered dev, ConnectionType type) {

        return true;
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return true;
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!getWorldObj().isRemote) {

            getPowerHandler().update();

            // Check if there's an item in the inventory
            if (inventory[0] != null) {
                // The slot for discharging
                if (inventory[0].getItem() instanceof IChargeable) {
                    IChargeable battery = ((IChargeable) inventory[0].getItem());

                    float discharged = battery.drainEnergy(inventory[0],
                            Math.min(powerTransfer, getPowerHandler().getMaxAmps() - getPowerHandler().getAmpsStored()), true);

                    battery.drainEnergy(inventory[0], powerTransfer, false);
                    getPowerHandler().injectEnergy(discharged, false);
                }

            }
            if (inventory[1] != null) {
                // The slot for charging
                if (inventory[1].getItem() instanceof IChargeable) {
                    IChargeable battery = ((IChargeable) inventory[0].getItem());

                    float charged = battery.injectEnergy(inventory[0], Math.min(powerTransfer, getPowerHandler().getAmpsStored()), true);

                    battery.injectEnergy(inventory[0], powerTransfer, false);
                    getPowerHandler().drainEnergy(charged, false);
                }
            }
            recalculateTextureIndex();
        }

    }

    private void recalculateTextureIndex() {

        int newIndex = (int) Math.floor((getPowerHandler().getAmpsStored() / getPowerHandler().getMaxAmps()) * 6.0);
        if (newIndex != textureIndex) {
            textureIndex = newIndex;
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }

    @Override
    protected void readFromPacketNBT(NBTTagCompound tCompound) {

        super.readFromPacketNBT(tCompound);
        textureIndex = tCompound.getInteger("textureIndex");
    }

    @Override
    protected void writeToPacketNBT(NBTTagCompound tCompound) {

        super.writeToPacketNBT(tCompound);
        tCompound.setInteger("textureIndex", textureIndex);
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {

        super.readFromNBT(tagCompound);
        getPowerHandler().readFromNBT(tagCompound);
        textureIndex = tagCompound.getInteger("textureIndex");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        getPowerHandler().writeToNBT(tagCompound);
        tagCompound.setInteger("textureIndex", textureIndex);
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

        return itemToTest.getItem() instanceof IChargeable;
    }

    @SideOnly(Side.CLIENT)
    public void setAmpStored(float newAmp) {

        ampStored = newAmp;
    }

    @SideOnly(Side.CLIENT)
    public void setMaxAmp(float newAmp) {

        maxAmp = newAmp;
    }

    @SideOnly(Side.CLIENT)
    public float getAmpStored() {

        return ampStored;
    }

    @SideOnly(Side.CLIENT)
    public float getMaxAmp() {

        return maxAmp;
    }

    @Override
    public void invalidate() {

        super.invalidate();
        if (!getWorldObj().isRemote) {
            getPowerHandler().invalidate();
        }
    }

    public int getTextureIndex() {

        return textureIndex;
    }
}
