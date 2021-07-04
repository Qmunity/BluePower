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
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIntArray;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */

public class TileBlulectricFurnace extends TileMachineBase implements ISidedInventory, INamedContainerProvider {
    private final BlutricityStorage storage = new BlutricityStorage(1000, 100);
    private LazyOptional<IPowerBase> blutricityCap;
    private boolean isActive;
    private int currentProcessTime;
    public static final int SLOTS = 2;
    private ItemStack inventory;
    private ItemStack outputInventory;
    private FurnaceRecipe currentRecipe;
    private boolean updatingRecipe = true;


    public TileBlulectricFurnace() {
        super(BPTileEntityType.BLULECTRIC_FURNACE);
        this.inventory = ItemStack.EMPTY;
        this.outputInventory = ItemStack.EMPTY;
    }

    @Override
    public void tick() {
        if (level != null && !level.isClientSide) {
            storage.resetCurrent();
            //Balance power of attached blulectric blocks.
            for (Direction facing : Direction.values()) {
                TileEntity tile = level.getBlockEntity(worldPosition.relative(facing));
                if (tile != null)
                    tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).ifPresent(
                            exStorage -> EnergyHelper.balancePower(exStorage, storage));
            }
            if (updatingRecipe) {
                if(this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).isPresent()) {
                    currentRecipe = this.level.getRecipeManager().getRecipeFor(IRecipeType.SMELTING, this, this.level).get();
                }else{
                    currentRecipe = null;
                }
                updatingRecipe = false;
            }
            if (currentRecipe != null) {
                if((storage.getEnergy() / storage.getMaxEnergy()) > 0.5) {
                    storage.addEnergy(-1, false);
                    this.setIsActive(true);
                    //Check if progress completed, and output slot is empty and less then a stack of the same item.
                    if (++currentProcessTime >= (100 / (storage.getEnergy() / storage.getMaxEnergy())) && ((outputInventory.getItem() == currentRecipe.getResultItem().getItem()
                            && (outputInventory.getCount() + currentRecipe.assemble(this).getCount()) <= 64)
                            || outputInventory.isEmpty())) {
                        currentProcessTime = 0;
                        if (!outputInventory.isEmpty()) {
                            outputInventory.setCount(outputInventory.getCount() + currentRecipe.assemble(this).getCount());
                        } else {
                            outputInventory = currentRecipe.assemble(this).copy();
                        }
                        this.removeItem(0, 1);
                        updatingRecipe = true;
                    }
                }else{
                    this.setIsActive(false);
                }
            } else {
                currentProcessTime = 0;
                this.setIsActive(false);
            }
        }

    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityBlutricity.BLUTRICITY_CAPABILITY) {
            if( blutricityCap == null ) blutricityCap = LazyOptional.of( () -> storage );
            return blutricityCap.cast();
        }
        return LazyOptional.empty();
    }

    @Override
    protected void invalidateCaps(){
        super.invalidateCaps();
        if( blutricityCap != null )
        {
            blutricityCap.invalidate();
            blutricityCap = null;
        }
    }


    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void load(BlockState blockState, CompoundNBT tCompound) {
        super.load(blockState, tCompound);
        CompoundNBT tc = tCompound.getCompound("inventory");
        inventory = ItemStack.of(tc);
        outputInventory = ItemStack.of(tCompound.getCompound("outputInventory"));
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public CompoundNBT save(CompoundNBT tCompound) {
        super.save(tCompound);

        CompoundNBT tc = new CompoundNBT();
        inventory.save(tc);
        tCompound.put("inventory", tc);

        if (outputInventory != null) {
            CompoundNBT outputCompound = new CompoundNBT();
            outputInventory.save(outputCompound);
            tCompound.put("outputInventory", outputCompound);
        }
        return tCompound;
    }

    @Override
    public void readFromPacketNBT(CompoundNBT tag) {

        super.readFromPacketNBT(tag);
        isActive = tag.getBoolean("isActive");
        currentProcessTime = tag.getInt("currentProcessTime");
        markForRenderUpdate();
        if(tag.contains("energy")) {
            INBT nbtstorage = tag.get("energy");
            CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().readNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null, nbtstorage);
        }
    }

    @Override
    public void writeToPacketNBT(CompoundNBT tag) {

        super.writeToPacketNBT(tag);
        tag.putInt("currentProcessTime", currentProcessTime);
        tag.putBoolean("isActive", isActive);
        INBT nbtstorage = CapabilityBlutricity.BLUTRICITY_CAPABILITY.getStorage().writeNBT(CapabilityBlutricity.BLUTRICITY_CAPABILITY, storage, null);
        tag.put("energy", nbtstorage);
    }

    protected final IIntArray fields = new IIntArray() {
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
    public boolean stillValid(PlayerEntity player) {
        return player.blockPosition().closerThan(worldPosition, 64.0D);
    }

    @Override
    public void startOpen(PlayerEntity player) {

    }

    @Override
    public void stopOpen(PlayerEntity player) {

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
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.BLULECTRICFURNACE_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new ContainerBlulectricFurnace(id, inventory, this, fields);
    }

}
