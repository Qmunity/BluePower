package com.bluepowermod.tileentities.tier2;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.PartRegistry;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.tileentities.IGUITextFieldSensitive;
import com.bluepowermod.tileentities.TileBase;

public class TileCircuitTable extends TileBase implements IInventory, IGuiButtonSensitive, IGUITextFieldSensitive {
    
    private ItemStack[]         inventory        = new ItemStack[18];
    public final InventoryBasic circuitInventory = new InventoryBasic("circuitInventory", false, 24);
    private int                 slotsScrolled;
    private String              textboxString    = "";
    
    public TileCircuitTable() {
    
        updateGateInventory();
    }
    
    public void updateGateInventory() {
    
        List<ItemStack> gates = getApplicableGates();
        slotsScrolled = Math.max(0, Math.min(slotsScrolled, (gates.size() - 17) / 8));
        for (int i = 0; i < circuitInventory.getSizeInventory(); i++) {
            ItemStack stack = gates.size() > slotsScrolled * 8 + i ? gates.get(slotsScrolled * 8 + i) : null;
            circuitInventory.setInventorySlotContents(i, stack);
        }
    }
    
    private List<ItemStack> getApplicableGates() {
    
        List<ItemStack> gates = new ArrayList<ItemStack>();
        List<String> registeredParts = PartRegistry.getInstance().getRegisteredParts();
        for (String partId : registeredParts) {
            BPPart part = PartRegistry.getInstance().createPart(partId);
            if (part instanceof GateBase && ((GateBase) part).isCraftableInCircuitTable()) {
                ItemStack partStack = PartRegistry.getInstance().getItemForPart(partId);
                if (partStack.getDisplayName().toLowerCase().contains(textboxString.toLowerCase())) {
                    gates.add(partStack);
                }
            }
        }
        return gates;
    }
    
    @Override
    public void onButtonPress(int messageId, int value) {
    
        slotsScrolled = value;
        updateGateInventory();
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
            if (stack != null) drops.add(stack);
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
    
        return true;
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
