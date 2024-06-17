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

import com.bluepowermod.container.ContainerEjector;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TileEjector extends TileMachineBase implements Container, MenuProvider {

    public static final int SLOTS = 10;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public TileEjector(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.EJECTOR.get(), pos, state);
    }

    @Override
    protected void redstoneChanged(boolean newValue) {

        super.redstoneChanged(newValue);

        if (!level.isClientSide && newValue) {
            for (int i = 0; i < inventory.size(); i++) {
                if (!inventory.get(i).isEmpty() && inventory.get(i).getCount() > 0) {
                    ItemStack output = inventory.get(i).copy();
                    output.setCount(1);
                    addItemToOutputBuffer(output);
                    inventory.get(i).setCount(inventory.get(i).getCount() - 1);
                    if (inventory.get(i).getCount() == 0)
                        inventory.set(i, ItemStack.EMPTY);
                    break;
                }
            }
        }
    }

    /**
     * This function gets called whenever the world/chunk loads
     */
    @Override
    public void loadAdditional(CompoundTag tCompound, HolderLookup.Provider provider) {
        super.loadAdditional(tCompound, provider);
        ContainerHelper.loadAllItems(tCompound.getCompound("inventory"), inventory, provider);
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    protected void saveAdditional(CompoundTag tCompound, HolderLookup.Provider provider) {
        super.saveAdditional(tCompound, provider);
        CompoundTag tc = new CompoundTag();
        ContainerHelper.saveAllItems(tc, inventory, provider);
        tCompound.put("inventory", tc);
    }

    /**
     * Returns the number of slots in the inventory.
     */
    @Override
    public int getContainerSize() {

        return inventory.size();
    }

    /**
     * Returns the stack in slot i
     * 
     * @param slot
     */
    @Override
    public ItemStack getItem(int slot) {

        return inventory.get(slot);
    }

    /**
     * Removes from an inventory slot (first arg) up to a specified number (second arg) of items and returns them in a new stack.
     * 
     * @param slot
     * @param amount
     */
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

    /**
     * When some containers are closed they call this on each slot, then drop whatever it returns as an EntityItem - like when you close a workbench
     * GUI.
     * 
     * @param slot
     */
    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return getItem(slot);
    }

    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     * 
     * @param slot
     * @param itemStack
     */
    @Override
    public void setItem(int slot, ItemStack itemStack) {

        inventory.set(slot, itemStack);
    }

    /**
     * Returns the maximum stack size for a inventory slot.
     */
    @Override
    public int getMaxStackSize() {

        return 64;
    }

    /**
     * Do not make give this method the name stillValid because it clashes with Container
     * 
     * @param player
     */
    @Override
    public boolean stillValid(Player player) {
        return player.blockPosition().closerThan(worldPosition,64.0D);
    }

    @Override
    public void startOpen(Player player) {

    }

    @Override
    public void stopOpen(Player player) {

    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     * 
     * @param var1
     * @param var2
     */
    @Override
    public boolean canPlaceItem(int var1, ItemStack var2) {

        return true;
    }

    @Override
    public NonNullList<ItemStack> getDrops() {

        NonNullList<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (!stack.isEmpty())
                drops.add(stack);
        return drops;
    }

    @Override
    public boolean canConnectRedstone() {

        return true;
    }

    //Todo Fields
    @Override
    public boolean isEmpty() {
        return inventory.size() == 0;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public Component getDisplayName() {
        return Component.literal(Refs.EJECTOR_NAME);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player playerEntity) {
        return new ContainerEjector(id, inventory, this);
    }
}
