/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.PartInfo;
import com.bluepowermod.part.PartManager;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.tile.IGUITextFieldSensitive;
import com.bluepowermod.tile.TileBase;

/**
 * @author MineMaarten
 */
public class TileCircuitTable extends TileBase implements IInventory, IGuiButtonSensitive, IGUITextFieldSensitive {

    protected ItemStack[] inventory = new ItemStack[18];
    public final InventoryBasic circuitInventory = new InventoryBasic("circuitInventory", false, 24);
    public int slotsScrolled;
    private String textboxString = "";

    public TileCircuitTable() {

        updateGateInventory();
    }

    public void updateGateInventory() {
        //       if (worldObj == null || !worldObj.isRemote) {
        List<ItemStack> gates = getApplicableItems();
        Iterator<ItemStack> iterator = gates.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().getDisplayName().toLowerCase().contains(textboxString.toLowerCase()))
                iterator.remove();
        }
        slotsScrolled = Math.max(0, Math.min(slotsScrolled, (gates.size() - 17) / 8));
        for (int i = 0; i < circuitInventory.getSizeInventory(); i++) {
            ItemStack stack = gates.size() > slotsScrolled * 8 + i ? gates.get(slotsScrolled * 8 + i) : null;
            circuitInventory.setInventorySlotContents(i, stack);
        }
        //   }
    }

    protected List<ItemStack> getApplicableItems() {

        List<ItemStack> gates = new ArrayList<ItemStack>();
        List<PartInfo> registeredParts = PartManager.getRegisteredParts();
        for (PartInfo part : registeredParts) {
            if (part.getExample() instanceof GateBase<?, ?, ?, ?, ?, ?>
            && ((GateBase<?, ?, ?, ?, ?, ?>) part.getExample()).isCraftableInCircuitTable()) {
                ItemStack partStack = part.getStack().copy();
                gates.add(partStack);
            }
        }
        return gates;
    }

    public int getRequiredScrollStates() {
        return (getApplicableItems().size() - 17) / 8;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {
        if (messageId == 0) {
            slotsScrolled = value;
        }
        updateGateInventory();
        ((ICrafting) player).sendContainerAndContentsToPlayer(player.openContainer, player.openContainer.getInventory());
    }

    @Override
    public void setText(int textFieldID, String text) {

        textboxString = text;
        updateGateInventory();
    }

    @Override
    public String getText(int textFieldID) {

        return textboxString;
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (stack != null)
                drops.add(stack);
        return drops;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tag.setTag("Items", tagList);

        tag.setString("textboxString", textboxString);

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = new ItemStack[18];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

        textboxString = tag.getString("textboxString");
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

        return BPBlocks.circuit_table.getUnlocalizedName();
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
return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
//        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {

        return true;
    }

}
