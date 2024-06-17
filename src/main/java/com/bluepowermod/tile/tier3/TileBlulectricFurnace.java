/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.tile.tier3;

import com.bluepowermod.api.power.BlutricityStorage;
import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.block.power.BlockBlulectricFurnace;
import com.bluepowermod.container.ContainerBlulectricFurnace;
import com.bluepowermod.helper.EnergyHelper;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */

public class TileBlulectricFurnace extends TileMachineBase implements WorldlyContainer, MenuProvider {
    public final BlutricityStorage storage = new BlutricityStorage(1000, 100);
    private boolean isActive;
    private int currentProcessTime;
    public static final int SLOTS = 2;
    private ItemStack inventory;
    private ItemStack outputInventory;
    private SmeltingRecipe currentRecipe;
    private boolean updatingRecipe = true;


    public TileBlulectricFurnace(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.BLULECTRIC_FURNACE.get(), pos, state);
        this.inventory = ItemStack.EMPTY;
        this.outputInventory = ItemStack.EMPTY;
    }

    public static void tickFurnace(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        TileBlulectricFurnace tileFurnace = (TileBlulectricFurnace) blockEntity;
        if (level != null && !level.isClientSide) {
            tileFurnace.storage.resetCurrent();
            //Balance power of attached blulectric blocks.
            for (Direction facing : Direction.values()) {
                IPowerBase exStorage = level.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, pos.relative(facing), facing.getOpposite());
                if (exStorage != null){
                    EnergyHelper.balancePower(exStorage, tileFurnace.storage);
                }
            }
            if (tileFurnace.updatingRecipe) {
                if(level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, tileFurnace, level).isPresent()) {
                    tileFurnace.currentRecipe = level.getRecipeManager().getRecipeFor(RecipeType.SMELTING, tileFurnace, level).get().value();
                    //Check output slot is empty and less than a stack of the same item.
                    if(!(tileFurnace.outputInventory.getItem() == tileFurnace.currentRecipe.getResultItem(level.registryAccess()).getItem()
                            && (tileFurnace.outputInventory.getCount() + tileFurnace.currentRecipe.assemble(tileFurnace, level.registryAccess()).getCount()) <= tileFurnace.outputInventory.getMaxStackSize())
                            && !tileFurnace.outputInventory.isEmpty()){
                        tileFurnace.currentRecipe = null;
                    }
                }else{
                    tileFurnace.currentRecipe = null;
                }
                tileFurnace.updatingRecipe = false;
            }
            if (tileFurnace.currentRecipe != null) {
                if((tileFurnace.storage.getEnergy() / tileFurnace.storage.getMaxEnergy()) > 0.5) {
                    tileFurnace.storage.addEnergy(-1, false);
                    tileFurnace.setIsActive(true);
                    //Check if progress completed.
                    if (++tileFurnace.currentProcessTime >= (100 / (tileFurnace.storage.getEnergy() / tileFurnace.storage.getMaxEnergy()))) {
                        tileFurnace.currentProcessTime = 0;
                        if (!tileFurnace.outputInventory.isEmpty()) {
                            tileFurnace.outputInventory.setCount(tileFurnace.outputInventory.getCount() + tileFurnace.currentRecipe.assemble(tileFurnace, level.registryAccess()).getCount());
                        } else {
                            tileFurnace.outputInventory = tileFurnace.currentRecipe.assemble(tileFurnace, level.registryAccess()).copy();
                        }
                        tileFurnace.removeItem(0, 1);
                        tileFurnace.updatingRecipe = true;
                    }
                }else{
                    tileFurnace.setIsActive(false);
                }
            } else {
                tileFurnace.currentProcessTime = 0;
                tileFurnace.setIsActive(false);
            }
        }

    }

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void loadAdditional(CompoundTag tCompound, HolderLookup.Provider provider) {
        super.loadAdditional(tCompound, provider);
        inventory = ItemStack.parseOptional(provider,tCompound.getCompound("inventory"));
        outputInventory = ItemStack.parseOptional(provider, tCompound.getCompound("outputInventory"));
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    protected void saveAdditional(CompoundTag tCompound, HolderLookup.Provider provider) {
        super.saveAdditional(tCompound, provider);
        tCompound.put("inventory", inventory.saveOptional(provider));
        if (outputInventory != null) {
            tCompound.put("outputInventory", outputInventory.saveOptional(provider));
        }
    }

    @Override
    public void readFromPacketNBT(CompoundTag tag) {
        super.readFromPacketNBT(tag);
        isActive = tag.getBoolean("isActive");
        currentProcessTime = tag.getInt("currentProcessTime");
        markForRenderUpdate();
        if(tag.contains("energy")) {
            Tag nbtstorage = tag.get("energy");
            CapabilityBlutricity.readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
    }

    @Override
    public void writeToPacketNBT(CompoundTag tag) {
        super.writeToPacketNBT(tag);
        tag.putInt("currentProcessTime", currentProcessTime);
        tag.putBoolean("isActive", isActive);
        Tag nbtstorage = CapabilityBlutricity.writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
        tag.put("energy", nbtstorage);
    }

    protected final ContainerData fields = new ContainerData() {
        public int get(int i) {
            switch (i) {
                case 0:
                    return (int) TileBlulectricFurnace.this.storage.getEnergy();
                case 1:
                    return TileBlulectricFurnace.this.currentProcessTime;
                case 2:
                    return (int) TileBlulectricFurnace.this.storage.getMaxEnergy();
                default:
                    return 0;
            }
        }

        public void set(int i, int value) {
            switch (i) {
                case 0:
                    break;
                case 1:
                    TileBlulectricFurnace.this.currentProcessTime = value;
                    break;
                case 2:
                    break;
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

    private void setIsActive(boolean _isActive) {
        if (level != null && _isActive != isActive && level.getGameTime() % 4 == 0) {
            isActive = _isActive;
            BlockBlulectricFurnace.setState(isActive, level, worldPosition);
            sendUpdatePacket();
        }
    }

    /**
     * ************ IINVENTORY ****************
     */

    @Override
    public int getContainerSize() {

        return 2; // 1 inventory, 1 output
    }

    @Override
    public ItemStack getItem(int var1) {
        updatingRecipe = true;
        if (var1 == 0) {
            return inventory;
        } else if (var1 == 1) {
            return outputInventory;
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
            inventory = itemStack;
        } else {
            outputInventory = itemStack;
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
            return true;
        } else {// Output slot
            return false;
        }
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        if (!outputInventory.isEmpty())
            drops.add(outputInventory);
        if (!inventory.isEmpty())
            drops.add(inventory);
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
        return Component.literal(Refs.BLULECTRICFURNACE_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
        return new ContainerBlulectricFurnace(id, inventory, this, fields);
    }

}
