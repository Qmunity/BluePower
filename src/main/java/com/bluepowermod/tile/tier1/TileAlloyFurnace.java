/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.block.machine.BlockAlloyFurnace;
import com.bluepowermod.container.ContainerAlloyFurnace;
import com.bluepowermod.init.BPRecipeTypes;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 *
 * @author MineMaarten, Koen Beckers (K4Unl), amadornes
 */

public class TileAlloyFurnace extends TileBase implements WorldlyContainer, MenuProvider {

    private boolean isActive;
    private int currentBurnTime;
    private int currentProcessTime;
    private int maxBurnTime;
    public static final int SLOTS = 11;
    private NonNullList<ItemStack> inventory;
    private ItemStack fuelInventory;
    private ItemStack outputInventory;
    private IAlloyFurnaceRecipe currentRecipe;
    private boolean updatingRecipe = true;

    public TileAlloyFurnace(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.ALLOY_FURNACE.get(), pos, state);
        this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
        this.fuelInventory = ItemStack.EMPTY;
        this.outputInventory = ItemStack.EMPTY;
    }

    /*************** BASIC TE FUNCTIONS **************/

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void load(CompoundTag tCompound) {

        super.load(tCompound);

        for (int i = 0; i < 9; i++) {
            CompoundTag tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, ItemStack.of(tc));
        }
        fuelInventory = ItemStack.of(tCompound.getCompound("fuelInventory"));
        outputInventory = ItemStack.of(tCompound.getCompound("outputInventory"));

    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    protected void saveAdditional(CompoundTag tCompound) {

        super.saveAdditional(tCompound);

        for (int i = 0; i < 9; i++) {
            CompoundTag tc = new CompoundTag();
            inventory.get(i).save(tc);
            tCompound.put("inventory" + i, tc);
        }
        if (fuelInventory != null) {
            CompoundTag fuelCompound = new CompoundTag();
            fuelInventory.save(fuelCompound);
            tCompound.put("fuelInventory", fuelCompound);
        }

        if (outputInventory != null) {
            CompoundTag outputCompound = new CompoundTag();
            outputInventory.save(outputCompound);
            tCompound.put("outputInventory", outputCompound);
        }

    }

    @Override
    public void readFromPacketNBT(CompoundTag tag) {

        super.readFromPacketNBT(tag);
        isActive = tag.getBoolean("isActive");
        currentBurnTime = tag.getInt("currentBurnTime");
        currentProcessTime = tag.getInt("currentProcessTime");
        maxBurnTime = tag.getInt("maxBurnTime");
        markForRenderUpdate();
    }

    @Override
    public void writeToPacketNBT(CompoundTag tag) {

        super.writeToPacketNBT(tag);
        tag.putInt("currentBurnTime", currentBurnTime);
        tag.putInt("currentProcessTime", currentProcessTime);
        tag.putInt("maxBurnTime", maxBurnTime);
        tag.putBoolean("isActive", isActive);
    }

    /**
     * Function gets called every tick. Do not forget to call the super method!
     */
    public static void tickAlloyFurnace(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        TileAlloyFurnace tileAlloyFurnace = (TileAlloyFurnace) blockEntity;
        TileBase.tickTileBase(level, pos, state, tileAlloyFurnace);
        if (!level.isClientSide) {
            tileAlloyFurnace.setIsActive(tileAlloyFurnace.currentBurnTime > 0);
            if (tileAlloyFurnace.isActive) {
                tileAlloyFurnace.currentBurnTime--;
            }
            if (tileAlloyFurnace.updatingRecipe) {
                if(level.getRecipeManager().getRecipeFor(BPRecipeTypes.ALLOY_SMELTING.get(), tileAlloyFurnace, level).isPresent()) {
                    tileAlloyFurnace.currentRecipe = level.getRecipeManager().getRecipeFor(BPRecipeTypes.ALLOY_SMELTING.get(), tileAlloyFurnace, level).get();
                    //Check output slot is empty and less than a stack of the same item.
                    if(!(tileAlloyFurnace.outputInventory.getItem() == tileAlloyFurnace.currentRecipe.getResultItem().getItem()
                            && (tileAlloyFurnace.outputInventory.getCount() + tileAlloyFurnace.currentRecipe.assemble(tileAlloyFurnace.inventory, level.getRecipeManager()).getCount()) <= tileAlloyFurnace.outputInventory.getMaxStackSize())
                            && !tileAlloyFurnace.outputInventory.isEmpty()){
                        tileAlloyFurnace.currentRecipe = null;
                    }
                }else{
                    tileAlloyFurnace.currentRecipe = null;
                }
                tileAlloyFurnace.updatingRecipe = false;
            }
            if (tileAlloyFurnace.currentRecipe != null) {
                if (tileAlloyFurnace.currentBurnTime <= 0) {
                    if (FurnaceBlockEntity.getFuel().containsKey(tileAlloyFurnace.fuelInventory.getItem())) {
                        // Put new item in
                        tileAlloyFurnace.currentBurnTime = tileAlloyFurnace.maxBurnTime = FurnaceBlockEntity.getFuel().get(tileAlloyFurnace.fuelInventory.getItem());
                        if (!tileAlloyFurnace.fuelInventory.isEmpty()) {
                            tileAlloyFurnace.fuelInventory.setCount(tileAlloyFurnace.fuelInventory.getCount() - 1);
                            if (tileAlloyFurnace.fuelInventory.getCount() <= 0) {
                                tileAlloyFurnace.fuelInventory = tileAlloyFurnace.fuelInventory.getItem().getCraftingRemainingItem(tileAlloyFurnace.fuelInventory);
                            }
                        }
                    } else {
                        tileAlloyFurnace.currentProcessTime = 0;
                    }
                }

                //Check if progress completed
                if (++tileAlloyFurnace.currentProcessTime >= 200) {
                    tileAlloyFurnace.currentProcessTime = 0;
                    if (!tileAlloyFurnace.outputInventory.isEmpty()) {
                        tileAlloyFurnace.outputInventory.setCount(tileAlloyFurnace.outputInventory.getCount() + tileAlloyFurnace.currentRecipe.assemble(tileAlloyFurnace.inventory, level.getRecipeManager()).getCount());
                    } else {
                        tileAlloyFurnace.outputInventory = tileAlloyFurnace.currentRecipe.assemble(tileAlloyFurnace.inventory, level.getRecipeManager()).copy();
                    }
                    tileAlloyFurnace.currentRecipe.useItems(tileAlloyFurnace.inventory, level.getRecipeManager());
                    tileAlloyFurnace.updatingRecipe = true;
                }
            } else {
                tileAlloyFurnace.currentProcessTime = 0;
            }
        }
    }

    @Override
    protected void redstoneChanged(boolean newValue) {

        // setIsActive(newValue);
    }

    protected final ContainerData fields = new ContainerData() {
        public int get(int i) {
            switch (i) {
                case 0:
                    return TileAlloyFurnace.this.currentBurnTime;
                case 1:
                    return TileAlloyFurnace.this.currentProcessTime;
                case 2:
                    return TileAlloyFurnace.this.maxBurnTime;
                default:
                    return 0;
            }
        }

        public void set(int i, int value) {
            switch (i) {
                case 0:
                    TileAlloyFurnace.this.currentBurnTime = value;
                    break;
                case 1:
                    TileAlloyFurnace.this.currentProcessTime = value;
                    break;
                case 2:
                    TileAlloyFurnace.this.maxBurnTime = value;
            }

        }

        public int getCount() {
            return 3;
        }
    };

    /**
     * ************* ADDED FUNCTIONS *************
     */

    public boolean getIsActive() {

        return isActive;
    }

    public void setIsActive(boolean _isActive) {

        if (_isActive != isActive) {
            isActive = _isActive;
            BlockAlloyFurnace.setState(isActive, level, worldPosition);
            sendUpdatePacket();
        }
    }

    /**
     * ************ IINVENTORY ****************
     */

    @Override
    public int getContainerSize() {

        return 9 + 1 + 1; // 9 inventory, 1 fuel, 1 output
    }

    @Override
    public ItemStack getItem(int var1) {
        updatingRecipe = true;
        if (var1 == 0) {
            return fuelInventory;
        } else if (var1 == 1) {
            return outputInventory;
        } else if (var1 < 11) {
            return inventory.get(var1 - 2);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {

        ItemStack itemStack = getItem(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setItem(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setItem(slot, ItemStack.EMPTY);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return getItem(index);
    }

    @Override
    public void setItem(int var1, ItemStack itemStack) {

        if (var1 == 0) {
            fuelInventory = itemStack;
        } else if (var1 == 1) {
            outputInventory = itemStack;
        } else {
            inventory.set(var1 - 2, itemStack);
        }
        updatingRecipe = true;
    }

    @Override
    public int getMaxStackSize() {

        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        return player.blockPosition().closerThan(worldPosition, 64.0D);
    }

    @Override
    public void startOpen(Player player) {

    }

    @Override
    public void stopOpen(Player player) {

    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {

        if (slot == 0) {
            return FurnaceBlockEntity.isFuel(itemStack);
        } else if (slot == 1) { // Output slot
            return false;
        } else {
            return true;
        }
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        if (!fuelInventory.isEmpty())
            drops.add(fuelInventory);
        if (!outputInventory.isEmpty())
            drops.add(outputInventory);
        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
                drops.add(stack);
        return drops;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack item, Direction direction) {
        return canPlaceItem(slot, item);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == 1;
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void clearContent() {

    }

    @Override
    public Component getDisplayName() {
        return Component.literal(Refs.ALLOYFURNACE_NAME);
    }
    
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ContainerAlloyFurnace(id, inventory, this, fields);
    }
}
