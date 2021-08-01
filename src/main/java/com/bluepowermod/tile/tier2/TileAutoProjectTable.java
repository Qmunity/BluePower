package com.bluepowermod.tile.tier2;

import com.bluepowermod.container.inventory.InventoryProjectTableCrafting;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.Direction;
import net.minecraft.util.text.Component;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.stream.IntStream;

public class TileAutoProjectTable extends TileProjectTable {

    public TileAutoProjectTable() {
        super(BPBlockEntityType.AUTO_PROJECT_TABLE);
    }
    private final int OUTPUT_SLOT = 100;

    @Override
    public ItemStack removeItem(int slot, int amount) {
        if(slot == OUTPUT_SLOT) {
            InventoryProjectTableCrafting craftingInv = new InventoryProjectTableCrafting(null, this, 3, 3);
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, craftingInv, level);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                itemstack = icraftingrecipe.assemble(craftingInv);
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
                        ItemStack ptStack = getItem(ptSlot);
                        if (ptStack.getItem() == slotStack.getItem() && ptStack.getTag() == slotStack.getTag()) {
                            slotStack.setCount(2);
                            craftingGrid.set(i, slotStack);
                            removeItem(ptSlot, 1);
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
            return super.removeItem(slot, amount);
        }
    }

    @Override
    public ItemStack getItem(int i) {
        if(i == OUTPUT_SLOT){
            InventoryProjectTableCrafting craftingInv = new InventoryProjectTableCrafting(null, this, 3, 3);
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = level.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, craftingInv, level);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                itemstack = icraftingrecipe.assemble(craftingInv);
            }
            return itemstack;
        }else {
            return super.getItem(i);
        }
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if(i != OUTPUT_SLOT){
            super.setItem(i, itemStack);
        }
    }

    @Override
    public Component getDisplayName() {
        return new StringTextComponent(Refs.AUTOPROJECTTABLE_NAME);
    }


    @Override
    public int[] getSlotsForFace(Direction side) {
        if(side == Direction.DOWN) {
            return new int[]{OUTPUT_SLOT};
        }
        return IntStream.range(0, getContainerSize() - 1).toArray();
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return direction != Direction.DOWN;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return true;
    }

}
