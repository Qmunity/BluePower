package com.bluepowermod.tileentities.tier3;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.compat.CompatibilityUtils;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.tube.PneumaticTube;
import com.bluepowermod.part.tube.TubeStack;
import com.bluepowermod.tileentities.IRejectAnimator;
import com.bluepowermod.tileentities.TileMachineBase;
import com.bluepowermod.util.Dependencies;

/**
 * @author MineMaarten
 */
public class TileManager extends TileMachineBase implements ISidedInventory, IGuiButtonSensitive, IRejectAnimator {
    
    protected final ItemStack[] inventory    = new ItemStack[24];
    public TubeColor            filterColor  = TubeColor.NONE;
    public int                  priority;
    public int                  mode;
    private int                 rejectTicker = -1;
    
    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from, boolean simulate) {
    
        if (from == getFacingDirection().getOpposite()) {
            //    if (!isBufferEmpty()) return stack;
            int itemsAccepted = acceptedItems(stack.stack);
            if (itemsAccepted > 0) {
                if (itemsAccepted >= stack.stack.stackSize) {
                    ItemStack rejectedStack = IOHelper.insert(getTileCache()[getFacingDirection().ordinal()].getTileEntity(), stack.stack, from, simulate);
                    if (rejectedStack == null || rejectedStack.stackSize != stack.stack.stackSize) {
                        if (!simulate) {
                            rejectTicker = 0;
                            sendUpdatePacket();
                        }
                    }
                    if (rejectedStack == null) {
                        return null;
                    } else {
                        stack.stack = rejectedStack;
                        return stack;
                    }
                }
                TubeStack injectedStack = stack.copy();
                stack.stack.stackSize -= itemsAccepted;
                
                injectedStack.stack.stackSize = itemsAccepted;
                ItemStack rejectedStack = IOHelper.insert(getTileCache()[getFacingDirection().ordinal()].getTileEntity(), injectedStack.stack, from, simulate);
                if (rejectedStack == null || rejectedStack.stackSize != injectedStack.stack.stackSize) {
                    if (!simulate) {
                        rejectTicker = 0;
                        sendUpdatePacket();
                    }
                }
                if (rejectedStack != null) {
                    stack.stack.stackSize += rejectedStack.stackSize;
                }
            }
        }
        stack.setTarget(null, null);
        return super.acceptItemFromTube(stack, from, simulate);
    }
    
    private int acceptedItems(ItemStack item) {
    
        if (item == null) return 0;
        int managerCount = IOHelper.getItemCount(item, this, ForgeDirection.UNKNOWN);
        if (mode == 1 && managerCount > 0) return item.stackSize;
        return managerCount - IOHelper.getItemCount(item, getTileCache()[getFacingDirection().ordinal()].getTileEntity(), getFacingDirection().getOpposite());
    }
    
    @Override
    public void updateEntity() {
    
        if (!worldObj.isRemote && getTicker() % BUFFER_EMPTY_INTERVAL == 0 && isBufferEmpty()) {
            dumpUnwantedItems();
            retrieveItemsFromManagers();
            setOutputtingRedstone(mode == 0 && shouldEmitRedstone());
        }
        if (rejectTicker >= 0) {
            if (++rejectTicker > ANIMATION_TIME) {
                rejectTicker = -1;
                markForRenderUpdate();
            }
        }
        super.updateEntity();
    }
    
    private boolean shouldEmitRedstone() {
    
        for (ItemStack stack : inventory) {
            if (stack != null && acceptedItems(stack) > 0) return false;
        }
        return true;
    }
    
    private void retrieveItemsFromManagers() {
    
        TileEntity extractingInventory = getTileCache()[getOutputDirection().ordinal()].getTileEntity();
        IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
        PneumaticTube tube = compat.getBPPart(extractingInventory, PneumaticTube.class);
        if (tube != null) {
            for (ItemStack stack : inventory) {
                int acceptedItems = acceptedItems(stack);
                if (acceptedItems > 0) {
                    ItemStack retrievingStack = stack.copy();
                    retrievingStack.stackSize = retrievingStack.getMaxStackSize();
                    if (tube.getLogic().retrieveStack(this, getOutputDirection(), retrievingStack, filterColor)) return;
                }
            }
        }
    }
    
    private void dumpUnwantedItems() {
    
        TileEntity te = getTileCache()[getFacingDirection().ordinal()].getTileEntity();
        IInventory inv = IOHelper.getInventoryForTE(te);
        int[] slots = IOHelper.getAccessibleSlotsForInventory(inv, getFacingDirection().getOpposite());
        for (int slot : slots) {
            ItemStack stack = inv.getStackInSlot(slot);
            int acceptedItems = acceptedItems(stack);
            if (acceptedItems < 0) {
                int rejectedItems = -acceptedItems;
                ItemStack rejectingStack = stack.copy();
                rejectingStack.stackSize = Math.min(rejectedItems, rejectingStack.getMaxStackSize());
                rejectingStack = IOHelper.extract(te, getFacingDirection().getOpposite(), rejectingStack, true, false);
                if (rejectingStack != null) {
                    this.addItemToOutputBuffer(rejectingStack, filterColor);
                }
            }
        }
    }
    
    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void readFromNBT(NBTTagCompound tCompound) {
    
        super.readFromNBT(tCompound);
        
        for (int i = 0; i < 24; i++) {
            NBTTagCompound tc = tCompound.getCompoundTag("inventory" + i);
            inventory[i] = ItemStack.loadItemStackFromNBT(tc);
        }
        filterColor = TubeColor.values()[tCompound.getByte("filterColor")];
        mode = tCompound.getByte("mode");
        priority = tCompound.getByte("priority");
    }
    
    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public void writeToNBT(NBTTagCompound tCompound) {
    
        super.writeToNBT(tCompound);
        
        for (int i = 0; i < 24; i++) {
            if (inventory[i] != null) {
                NBTTagCompound tc = new NBTTagCompound();
                inventory[i].writeToNBT(tc);
                tCompound.setTag("inventory" + i, tc);
            }
        }
        
        tCompound.setByte("filterColor", (byte) filterColor.ordinal());
        tCompound.setByte("mode", (byte) mode);
        tCompound.setByte("priority", (byte) priority);
    }
    
    @Override
    public void writeToPacketNBT(NBTTagCompound tag) {
    
        super.writeToPacketNBT(tag);
        tag.setByte("rejectAnimation", (byte) rejectTicker);
    }
    
    @Override
    public void readFromPacketNBT(NBTTagCompound tag) {
    
        super.readFromPacketNBT(tag);
        rejectTicker = tag.getByte("rejectAnimation");
    }
    
    @Override
    public int getSizeInventory() {
    
        return inventory.length;
    }
    
    @Override
    public ItemStack getStackInSlot(int i) {
    
        return inventory[i];
    }
    
    @Override
    public ItemStack decrStackSize(int slot, int amount) {
    
        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }
        
        return itemStack;
    }
    
    @Override
    public ItemStack getStackInSlotOnClosing(int i) {
    
        ItemStack itemStack = getStackInSlot(i);
        if (itemStack != null) {
            setInventorySlotContents(i, null);
        }
        return itemStack;
    }
    
    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
    
        inventory[i] = itemStack;
    }
    
    @Override
    public String getInventoryName() {
    
        return BPBlocks.manager.getUnlocalizedName();
    }
    
    @Override
    public boolean hasCustomInventoryName() {
    
        return false;
    }
    
    @Override
    public int getInventoryStackLimit() {
    
        return 64;
    }
    
    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
    
        return true;
    }
    
    @Override
    public void openInventory() {
    
    }
    
    @Override
    public void closeInventory() {
    
    }
    
    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
    
        return true;
    }
    
    @Override
    public List<ItemStack> getDrops() {
    
        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (stack != null) drops.add(stack);
        return drops;
    }
    
    @Override
    public int[] getAccessibleSlotsFromSide(int var1) {
    
        ForgeDirection direction = getFacingDirection();
        
        if (var1 == direction.ordinal() || var1 == direction.getOpposite().ordinal()) { return new int[] {}; }
        int[] slots = new int[inventory.length];
        for (int i = 0; i < slots.length; i++)
            slots[i] = i;
        return slots;
    }
    
    @Override
    public boolean canInsertItem(int slot, ItemStack itemStack, int side) {
    
        return true;
    }
    
    @Override
    public boolean canExtractItem(int slot, ItemStack itemStack, int side) {
    
        return true;
    }
    
    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {
    
        if (messageId == 0) {
            filterColor = TubeColor.values()[value];
        } else if (messageId == 1) {
            mode = value;
        } else {
            priority = value;
        }
    }
    
    @Override
    public boolean canConnectRedstone() {
    
        return true;
    }
    
    @Override
    public boolean isRejecting() {
    
        return rejectTicker >= 0;
    }
}
