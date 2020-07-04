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
import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.block.power.BlockBlulectricAlloyFurnace;
import com.bluepowermod.container.ContainerBlulectricAlloyFurnace;
import com.bluepowermod.helper.EnergyHelper;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
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

public class TileBlulectricAlloyFurnace extends TileMachineBase implements ISidedInventory, INamedContainerProvider {
    private final BlutricityStorage storage = new BlutricityStorage(1000, 100);
    private LazyOptional<IPowerBase> blutricityCap;
    private boolean isActive;
    private int currentProcessTime;
    public static final int SLOTS = 10;
    private NonNullList<ItemStack> inventory;
    private ItemStack outputInventory;
    private IAlloyFurnaceRecipe currentRecipe;
    private boolean updatingRecipe = true;

    public TileBlulectricAlloyFurnace() {
        super(BPTileEntityType.BLULECTRIC_ALLOY_FURNACE);
        this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
        this.outputInventory = ItemStack.EMPTY;
    }

    /*************** BASIC TE FUNCTIONS **************/

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void read(BlockState blockState, CompoundNBT tCompound) {

        super.read(blockState, tCompound);

        for (int i = 0; i < 9; i++) {
            CompoundNBT tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, ItemStack.read(tc));
        }
        outputInventory = ItemStack.read(tCompound.getCompound("outputInventory"));

    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public CompoundNBT write(CompoundNBT tCompound) {

        super.write(tCompound);

        for (int i = 0; i < 9; i++) {
            CompoundNBT tc = new CompoundNBT();
            inventory.get(i).write(tc);
            tCompound.put("inventory" + i, tc);
        }

        if (outputInventory != null) {
            CompoundNBT outputCompound = new CompoundNBT();
            outputInventory.write(outputCompound);
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


    /**
     * Function gets called every tick. Do not forget to call the super method!
     */
    @Override
    public void tick() {
        super.tick();

        if (world != null && !world.isRemote) {
            storage.resetCurrent();
            //Balance power of attached blulectric blocks.
            for (Direction facing : Direction.values()) {
                TileEntity tile = world.getTileEntity(pos.offset(facing));
                if (tile != null)
                    tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).ifPresent(
                            exStorage -> EnergyHelper.balancePower(exStorage, storage));
            }
            if (updatingRecipe) {
                if(this.world.getRecipeManager().getRecipe(AlloyFurnaceRegistry.ALLOYFURNACE_RECIPE, this, this.world).isPresent()) {
                    currentRecipe = (IAlloyFurnaceRecipe) this.world.getRecipeManager().getRecipe(AlloyFurnaceRegistry.ALLOYFURNACE_RECIPE, this, this.world).get();
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
                    if (++currentProcessTime >= (100 / (storage.getEnergy() / storage.getMaxEnergy())) && ((outputInventory.getItem() == currentRecipe.getRecipeOutput().getItem()
                            && (outputInventory.getCount() + currentRecipe.getCraftingResult(inventory).getCount()) <= 64)
                            || outputInventory.isEmpty())) {
                        currentProcessTime = 0;
                        if (!outputInventory.isEmpty()) {
                            outputInventory.setCount(outputInventory.getCount() + currentRecipe.getCraftingResult(inventory).getCount());
                        } else {
                            outputInventory = currentRecipe.getCraftingResult(inventory).copy();
                        }
                        currentRecipe.useItems(inventory);
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

    protected final IIntArray fields = new IIntArray() {
        public int get(int i) {
            switch (i) {
                case 0:
                    return (int) TileBlulectricAlloyFurnace.this.storage.getEnergy();
                case 1:
                    return TileBlulectricAlloyFurnace.this.currentProcessTime;
                case 2:
                    return (int) TileBlulectricAlloyFurnace.this.storage.getMaxEnergy();
                default:
                    return 0;
            }
        }

        public void set(int i, int value) {
            switch (i) {
                case 0:
                    break;
                case 1:
                    TileBlulectricAlloyFurnace.this.currentProcessTime = value;
                    break;
                case 2:
                    break;
            }

        }

        public int size() {
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

        if (world != null && _isActive != isActive && world.getGameTime() % 4 == 0) {
            isActive = _isActive;
            BlockBlulectricAlloyFurnace.setState(isActive, world, pos);
            sendUpdatePacket();
        }
    }

    /**
     * ************ IINVENTORY ****************
     */

    @Override
    public int getSizeInventory() {

        return 9 + 1; // 9 inventory, 1 output
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        updatingRecipe = true;
        if (var1 == 0) {
            return outputInventory;
        } else if (var1 < 10) {
            return inventory.get(var1 - 1);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.split(amount);
                if (itemStack.getCount() == 0) {
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return getStackInSlot(index);
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack itemStack) {

        if (var1 == 0) {
            outputInventory = itemStack;
        } else if (var1 < 10) {
            inventory.set(var1 - 1, itemStack);
        }
        updatingRecipe = true;
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        return player.getPosition().withinDistance(pos, 64.0D);
    }

    @Override
    public void openInventory(PlayerEntity player) {

    }

    @Override
    public void closeInventory(PlayerEntity player) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack itemStack) {

        // Output slot
        return slot != 0 && slot <= 9;
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
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
    public boolean canInsertItem(int slot, ItemStack item, Direction direction) {
        return isItemValidForSlot(slot, item);
    }

    @Override
    public boolean canExtractItem(int slot, ItemStack stack, Direction direction) {
        return slot == 0;
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public void clear() {

    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.BLULECTRICALLOYFURNACE_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity player) {
        return new ContainerBlulectricAlloyFurnace(id, inventory, this, fields);
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

}
