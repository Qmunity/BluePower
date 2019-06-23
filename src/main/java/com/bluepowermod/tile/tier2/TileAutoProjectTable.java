package com.bluepowermod.tile.tier2;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;

import java.util.List;
import java.util.Map;

;

public class TileAutoProjectTable extends TileProjectTable implements ISidedInventory {
    private static int[] slots;
    protected ItemStack craftBuffer = ItemStack.EMPTY;
    private boolean markedForBufferFill = true;

    static {
        slots = new int[19];
        for (int i = 0; i < slots.length; i++)
            slots[i] = i;
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        if (!craftBuffer.isEmpty())
            drops.add(craftBuffer);
        return drops;
    }

    @Override
    public CompoundNBT writeToNBT(CompoundNBT tag) {
        super.writeToNBT(tag);
            CompoundNBT bufferTag = new CompoundNBT();
            craftBuffer.writeToNBT(bufferTag);
            tag.setTag("craftBuffer", bufferTag);

        return tag;
    }

    @Override
    public void readFromNBT(CompoundNBT tag) {
        super.readFromNBT(tag);

        if (tag.hasKey("craftBuffer")) {
            craftBuffer = new ItemStack(tag.getCompoundTag("craftBuffer"));
        } else {
            craftBuffer = ItemStack.EMPTY;
        }
    }

    @Override
    public int getSizeInventory() {

        return super.getSizeInventory() + 1;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[0];
    }

    public static int[] getSlots() {
        return slots;
    }

    @Override
    public boolean canInsertItem(int slot, ItemStack itemStackIn, Direction direction) {
        return slot < 18;
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, Direction side) {
        if (slot == 18) {
            return true;
        } else {
            return side.ordinal() > 5;
        }
    }

    private void addItem(Map<ItemStack, Integer> collection, ItemStack stack) {
        for (Map.Entry<ItemStack, Integer> entry : collection.entrySet()) {
            ItemStack s = entry.getKey();
            if (s.isItemEqual(stack) && ItemStack.areItemStackTagsEqual(stack, s)) {
                entry.setValue(entry.get() + 1);
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
    public void update() {
        super.update();
        if (markedForBufferFill) {
            markedForBufferFill = false;
        }
    }


    @Override
    public String getName() {

        return BPBlocks.auto_project_table.getTranslationKey();
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return craftBuffer == ItemStack.EMPTY;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

}
