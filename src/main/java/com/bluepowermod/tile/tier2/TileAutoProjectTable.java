package com.bluepowermod.tile.tier2;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.tier1.TileProjectTable;

public class TileAutoProjectTable extends TileProjectTable implements ISidedInventory {
    private static int[] slots;

    static {
        slots = new int[19];
        for (int i = 0; i < slots.length; i++)
            slots[i] = i;
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
            if (extractedStack != null && getStackInSlot(18) != null && getStackInSlot(18).stackSize != extractedStack.stackSize)
                return false;
            Map<ItemStack, Integer> recipeItems = new HashMap<ItemStack, Integer>();
            for (ItemStack stack : craftingGrid) {
                if (stack != null) {
                    addItem(recipeItems, stack);
                }
            }
            for (Map.Entry<ItemStack, Integer> entry : recipeItems.entrySet()) {
                ItemStack stack = entry.getKey().copy();
                stack.stackSize = entry.getValue();
                ItemStack extracted = IOHelper.extract(this, ForgeDirection.UNKNOWN, stack, true, true);
                if (extracted == null)
                    return false;
            }
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
    public String getInventoryName() {

        return BPBlocks.auto_project_table.getUnlocalizedName();
    }

}
