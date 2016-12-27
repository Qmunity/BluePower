package com.bluepowermod.tile.tier2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;;

import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.tier1.TileProjectTable;

public class TileAutoProjectTable extends TileProjectTable implements ISidedInventory {
    private static int[] slots;
    protected ItemStack craftBuffer;
    private boolean markedForBufferFill = true;

    static {
        slots = new int[19];
        for (int i = 0; i < slots.length; i++)
            slots[i] = i;
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        if (craftBuffer != null)
            drops.add(craftBuffer);
        return drops;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);

        if (craftBuffer != null) {
            NBTTagCompound bufferTag = new NBTTagCompound();
            craftBuffer.writeToNBT(bufferTag);
            tag.setTag("craftBuffer", bufferTag);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("craftBuffer")) {
            craftBuffer = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("craftBuffer"));
        } else {
            craftBuffer = null;
        }
    }

    @Override
    protected void updateCraftingGrid() {
        super.updateCraftingGrid();
        markedForBufferFill = true;
    }

    @Override
    public int getSizeInventory() {

        return super.getSizeInventory() + 1;
    }

    @Override
    public int[] getAccessibleSlotsFromSide(int p_94128_1_) {
        return slots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack stack, int side) {
        return slot < 18;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack extractedStack, int side) {
        if (slot == 18) {
            return true;
        } else {
            return side > 5;
        }
    }

    private void addItem(Map<ItemStack, Integer> collection, ItemStack stack) {
        for (Map.Entry<ItemStack, Integer> entry : collection.entrySet()) {
            ItemStack s = entry.getKey();
            if (s.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(stack, s)) {
                entry.setValue(entry.getValue() + 1);
                return;
            }
        }
        collection.put(stack, 1);
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        markedForBufferFill = true;
        return slot < super.getSizeInventory() ? super.getStackInSlot(slot) : craftBuffer;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot < super.getSizeInventory()) {
            super.setInventorySlotContents(slot, stack);
        } else {
            craftBuffer = stack;
        }
    }

    @Override
    public void updateEntity() {
        super.updateEntity();
        if (markedForBufferFill) {
            updateCraftingGrid();
            tryFillCraftBuffer();
            markedForBufferFill = false;
        }
    }

    private void tryFillCraftBuffer() {
        if (craftBuffer == null && craftResult.getStackInSlot(0) != null && !worldObj.isRemote) {
            Map<ItemStack, Integer> recipeItems = new HashMap<ItemStack, Integer>();
            for (ItemStack s : craftingGrid) {
                if (s != null) {
                    addItem(recipeItems, s);
                }
            }
            boolean canCraft = true;
            for (Map.Entry<ItemStack, Integer> entry : recipeItems.entrySet()) {
                ItemStack s = entry.getKey().copy();
                s.stackSize = entry.getValue();
                ItemStack extracted = IOHelper.extract(this, EnumFacing.UNKNOWN, s, true, true);
                if (extracted == null) {
                    canCraft = false;
                    break;
                }
            }
            if (canCraft) {
                craftBuffer = craftResult.getStackInSlot(0).copy();
                craft();
            }
        }
    }

    @Override
    public String getInventoryName() {

        return BPBlocks.auto_project_table.getUnlocalizedName();
    }

}
