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
import com.bluepowermod.init.BPRecipeTypes;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.Tag;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */

public class TileBlulectricAlloyFurnace extends TileMachineBase implements WorldlyContainer, MenuProvider {
    private final BlutricityStorage storage = new BlutricityStorage(1000, 100);
    private LazyOptional<IPowerBase> blutricityCap;
    private boolean isActive;
    private int currentProcessTime;
    public static final int SLOTS = 10;
    private NonNullList<ItemStack> inventory;
    private ItemStack outputInventory;
    private IAlloyFurnaceRecipe currentRecipe;
    private boolean updatingRecipe = true;

    public TileBlulectricAlloyFurnace(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.BLULECTRIC_ALLOY_FURNACE.get(), pos, state);
        this.inventory = NonNullList.withSize(9, ItemStack.EMPTY);
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


    /**
     * Function gets called every tick. Do not forget to call the super method!
     */
    public static void tickAlloyFurnace(Level level, BlockPos pos, BlockState state, BlockEntity blockEntity) {
        TileBlulectricAlloyFurnace tileAlloyFurnace = (TileBlulectricAlloyFurnace) blockEntity;
        TileBase.tickTileBase(level, pos, state, tileAlloyFurnace);

        if (level != null && !level.isClientSide) {
            tileAlloyFurnace.storage.resetCurrent();
            //Balance power of attached blulectric blocks.
            for (Direction facing : Direction.values()) {
                BlockEntity tile = level.getBlockEntity(pos.relative(facing));
                if (tile != null)
                    tile.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, facing.getOpposite()).ifPresent(
                            exStorage -> EnergyHelper.balancePower(exStorage, tileAlloyFurnace.storage));
            }
            if (tileAlloyFurnace.updatingRecipe) {
                if(level.getRecipeManager().getRecipeFor(BPRecipeTypes.ALLOY_SMELTING.get(), tileAlloyFurnace, level).isPresent()) {
                    tileAlloyFurnace.currentRecipe = (IAlloyFurnaceRecipe) level.getRecipeManager().getRecipeFor(BPRecipeTypes.ALLOY_SMELTING.get(), tileAlloyFurnace, level).get();
                    //Check output slot is empty and less then a stack of the same item.
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
                if((tileAlloyFurnace.storage.getEnergy() / tileAlloyFurnace.storage.getMaxEnergy()) > 0.5) {
                    tileAlloyFurnace.storage.addEnergy(-1, false);
                    tileAlloyFurnace.setIsActive(true);
                    //Check if progress completed
                    if (++tileAlloyFurnace.currentProcessTime >= (100 / (tileAlloyFurnace.storage.getEnergy() / tileAlloyFurnace.storage.getMaxEnergy()))) {
                        tileAlloyFurnace.currentProcessTime = 0;
                        if (!tileAlloyFurnace.outputInventory.isEmpty()) {
                            tileAlloyFurnace.outputInventory.setCount(tileAlloyFurnace.outputInventory.getCount() + tileAlloyFurnace.currentRecipe.assemble(tileAlloyFurnace.inventory, level.getRecipeManager()).getCount());
                        } else {
                            tileAlloyFurnace.outputInventory = tileAlloyFurnace.currentRecipe.assemble(tileAlloyFurnace.inventory, level.getRecipeManager()).copy();
                        }
                        tileAlloyFurnace.currentRecipe.useItems(tileAlloyFurnace.inventory, level.getRecipeManager());
                        tileAlloyFurnace.updatingRecipe = true;
                    }
                }else{
                    tileAlloyFurnace.setIsActive(false);
                }
            } else {
                tileAlloyFurnace.currentProcessTime = 0;
                tileAlloyFurnace.setIsActive(false);
            }
        }
    }

    protected final ContainerData fields = new ContainerData() {
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

        if (level != null && _isActive != isActive && level.getGameTime() % 4 == 0) {
            isActive = _isActive;
            BlockBlulectricAlloyFurnace.setState(isActive, level, worldPosition);
            sendUpdatePacket();
        }
    }

    /**
     * ************ IINVENTORY ****************
     */

    @Override
    public int getContainerSize() {

        return 9 + 1; // 9 inventory, 1 output
    }

    @Override
    public ItemStack getItem(int var1) {
        updatingRecipe = true;
        if (var1 == 0) {
            return outputInventory;
        } else if (var1 < 10) {
            return inventory.get(var1 - 1);
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
            outputInventory = itemStack;
        } else if (var1 < 10) {
            inventory.set(var1 - 1, itemStack);
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
    public boolean canPlaceItemThroughFace(int slot, ItemStack item, Direction direction) {
        return canPlaceItem(slot, item);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction direction) {
        return slot == 0;
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
        return Component.literal(Refs.BLULECTRICALLOYFURNACE_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
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
    public void invalidateCaps(){
        super.invalidateCaps();
        if( blutricityCap != null )
        {
            blutricityCap.invalidate();
            blutricityCap = null;
        }
    }

}
