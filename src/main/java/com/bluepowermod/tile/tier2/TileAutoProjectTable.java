package com.bluepowermod.tile.tier2;

import com.bluepowermod.container.inventory.InventoryProjectTableCrafting;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;

public class TileAutoProjectTable extends TileProjectTable {

    public TileAutoProjectTable() {
        super(BPTileEntityType.AUTO_PROJECT_TABLE);
    }
    private final int OUTPUT_SLOT = 100;

    @Override
    public ItemStack decrStackSize(int slot, int amount) {
        if(slot == OUTPUT_SLOT) {
            InventoryProjectTableCrafting craftingInv = new InventoryProjectTableCrafting(null, this, 3, 3);
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingInv, world);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                itemstack = icraftingrecipe.getCraftingResult(craftingInv);
            }

            for (int i = 0; i < 9; i++) {
                ItemStack slotStack = craftingGrid.get(i);
                if (slotStack.getCount() == 1) {
                    //Get items from the Inventory to Craft
                    for (int ptSlot = 0; ptSlot < 19; ++ptSlot) {
                        if (ptSlot == 18) {
                            //No available items so end here to keep template
                            return ItemStack.EMPTY;
                        }
                        ItemStack ptStack = getStackInSlot(ptSlot);
                        if (ptStack.getItem() == slotStack.getItem() && ptStack.getTag() == slotStack.getTag()) {
                            slotStack.setCount(2);
                            craftingGrid.set(i, slotStack);
                            decrStackSize(ptSlot, 1);
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < 9; i++) {
                ItemStack currentItem = craftingGrid.get(i);
                currentItem.setCount(currentItem.getCount() - 1);
                craftingGrid.set(i, currentItem);
            }

            return itemstack;
        }else{
            return super.decrStackSize(slot, amount);
        }
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        if(i == OUTPUT_SLOT){
            InventoryProjectTableCrafting craftingInv = new InventoryProjectTableCrafting(null, this, 3, 3);
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipe(IRecipeType.CRAFTING, craftingInv, world);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                itemstack = icraftingrecipe.getCraftingResult(craftingInv);
            }
            return itemstack;
        }else {
            return super.getStackInSlot(i);
        }
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        if(i != OUTPUT_SLOT){
            super.setInventorySlotContents(i, itemStack);
        }
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.AUTOPROJECTTABLE_NAME);
    }


    @Override
    public int[] getSlotsForFace(Direction side) {
        if(side == Direction.DOWN) {
            return new int[]{OUTPUT_SLOT};
        }
        return IntStream.range(0, getSizeInventory() - 1).toArray();
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return direction != Direction.DOWN;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return true;
    }

}
