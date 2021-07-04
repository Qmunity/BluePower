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
 *
 *     @author Quetzi
 */

package com.bluepowermod.tile.tier1;

import com.bluepowermod.container.ContainerRelay;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nullable;

public class TileRelay extends TileMachineBase implements IInventory, INamedContainerProvider {

    public static final int SLOTS = 10;
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(SLOTS, ItemStack.EMPTY);

    public TileRelay() {
        super(BPTileEntityType.RELAY);
    }

    @Override
    public void tick() {

        super.tick();

        if (!level.isClientSide) {
            for (int i = 0; i < inventory.size(); i++) {
                if (!inventory.get(i).isEmpty() && inventory.get(i).getCount() > 0) {
                    addItemToOutputBuffer(inventory.get(i));
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
    public void load(BlockState blockState, CompoundNBT tCompound) {
        super.load(blockState, tCompound);

        for (int i = 0; i < 9; i++) {
            CompoundNBT tc = tCompound.getCompound("inventory" + i);
            inventory.set(i, new ItemStack((IItemProvider) tc));
        }
    }

    /**
     * This function gets called whenever the world/chunk is saved
     */
    @Override
    public CompoundNBT save(CompoundNBT tCompound) {

        super.save(tCompound);

        for (int i = 0; i < 9; i++) {
                CompoundNBT tc = new CompoundNBT();
                inventory.get(i).save(tc);
                tCompound.put("inventory" + i, tc);
        }
        return tCompound;
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
    public boolean stillValid(PlayerEntity player) {
        return player.blockPosition().closerThan(worldPosition, 64.0D);
    }

    @Override
    public void startOpen(PlayerEntity player) {

    }

    @Override
    public void stopOpen(PlayerEntity player) {

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
    public ITextComponent getDisplayName() {
        return new StringTextComponent(Refs.RELAY_NAME);
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory inventory, PlayerEntity playerEntity) {
        return new ContainerRelay(id, inventory, this);
    }
}
