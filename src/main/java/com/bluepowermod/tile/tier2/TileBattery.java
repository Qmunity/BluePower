package com.bluepowermod.tile.tier2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.api.power.IRechargeable;
import com.bluepowermod.api.power.PowerTier;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author Koen Beckers (K4Unl)
 */
public class TileBattery extends TileBase implements IPowered, IInventory {

    private ItemStack[] inventory = new ItemStack[2];

    private static int powerTransfer = 1; // The amount of Amps being transferred into or out of a battery, every tick
    private int textureIndex;

    private IPowerBase handler = BPApi.getInstance().getPowerApi().createPowerHandler(this);

    @SideOnly(Side.CLIENT)
    private float ampStored;
    @SideOnly(Side.CLIENT)
    private float maxAmp;

    @Override
    public boolean isPowered() {

        return false;
    }

    @Override
    public PowerTier getPowerTier() {

        return PowerTier.MEDIUMVOLTAGE;
    }

    @Override
    public IPowerBase getPowerHandler(ForgeDirection side) {

        return handler;
    }

    @Override
    public boolean canConnectPower(ForgeDirection side, IPowered dev, ConnectionType type) {

        return true;
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return true;
    }

    @Override
    public float getMaxPowerStorage() {

        return 3000;
    }

    @Override
    public void updateEntity() {

        super.updateEntity();

        if (!getWorldObj().isRemote) {

            handler.update();

            // Check if there's an item in the inventory
            if (inventory[0] != null) {
                // The slot for discharging
                if (inventory[0].getItem() instanceof IRechargeable) {
                    IRechargeable battery = ((IRechargeable) inventory[0].getItem());
                    if (battery.getAmpStored(inventory[0]) > powerTransfer) {
                        // Transfer power, with a certain rate, which we should maybe configurize?
                        float powerTransfered = handler.addEnergy(powerTransfer);
                        battery.removeEnergy(inventory[0], powerTransfered);
                    }
                }

            }
            if (inventory[1] != null) {
                // The slot for charging
                if (inventory[1].getItem() instanceof IRechargeable) {
                    IRechargeable battery = ((IRechargeable) inventory[1].getItem());
                    if (handler.getAmpHourStored() > powerTransfer) {
                        // Transfer power, with a certain rate, which we should maybe configurize?
                        float powerTransfered = battery.addEnergy(inventory[1], powerTransfer);
                        handler.removeEnergy(powerTransfered);
                    }
                }
            }
            recalculateTextureIndex();
        }

    }

    private void recalculateTextureIndex() {

        int newIndex = (int) Math.floor((handler.getAmpHourStored() / handler.getMaxAmpHour()) * 6.0);
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
        handler.readFromNBT(tagCompound);
        textureIndex = tagCompound.getInteger("textureIndex");
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {

        super.writeToNBT(tagCompound);
        handler.writeToNBT(tagCompound);
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

        return itemToTest.getItem() instanceof IRechargeable;
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

    public int getTextureIndex() {

        return textureIndex;
    }

    @Override
    public void invalidate() {

        super.invalidate();
        handler.disconnect();
    }

    @Override
    public void onNeighborBlockChanged() {

        super.onNeighborBlockChanged();
        handler.onNeighborUpdate();
    }

    @Override
    protected void onTileLoaded() {

        super.onTileLoaded();
        handler.onNeighborUpdate();
    }
}
