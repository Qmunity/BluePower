/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import com.bluepowermod.tile.tier2.TileTube;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.Container;
import net.minecraft.world.level.block.entity.BlockEntity;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.api.tube.ITubeConnection;

/**
 * @author MineMaarten
 * @author Dynious
 */
public class IOHelper {

    public static Container getInventoryForTE(BlockEntity te) {

        if (te instanceof Container) {
            return (Container) te;
        } else {
            return null;
        }
    }

    public static ItemStack extract(BlockEntity inventory, Direction direction, boolean simulate) {

        Container inv = getInventoryForTE(inventory);
        if (inv != null)
            return extract(inv, direction, simulate);
        return ItemStack.EMPTY;
    }

    public static ItemStack extract(Container inventory, Direction direction, boolean simulate) {

        if (inventory instanceof WorldlyContainer) {
            WorldlyContainer isidedinventory = (WorldlyContainer) inventory;
            int[] accessibleSlotsFromSide = isidedinventory.getSlotsForFace(direction);

            for (int anAccessibleSlotsFromSide : accessibleSlotsFromSide) {
                ItemStack stack = extract(inventory, direction, anAccessibleSlotsFromSide, simulate);
                if (!stack.isEmpty())
                    return stack;
            }
        } else {
            int j = inventory.getContainerSize();

            for (int k = 0; k < j; ++k) {
                ItemStack stack = extract(inventory, direction, k, simulate);
                if (!stack.isEmpty())
                    return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack extract(Container inventory, Direction direction, int slot, boolean simulate) {

        ItemStack itemstack = inventory.getItem(slot);

        if (!itemstack.isEmpty() && canTakeItemThroughFaceFromInventory(inventory, itemstack, slot, direction.ordinal())) {
            if (!simulate)
                inventory.setItem(slot, ItemStack.EMPTY);
            return itemstack;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack extract(BlockEntity tile, Direction direction, ItemStack requestedStack, boolean useItemCount, boolean simulate) {

        return extract(tile, direction, requestedStack, useItemCount, simulate, 0);
    }

    public static int[] getAccessibleSlotsForInventory(Container inv, Direction side) {

        int[] accessibleSlots;
        if (inv != null) {
            if (inv instanceof WorldlyContainer) {
                accessibleSlots = ((WorldlyContainer) inv).getSlotsForFace(side);
            } else {
                accessibleSlots = new int[inv.getContainerSize()];
                for (int i = 0; i < accessibleSlots.length; i++)
                    accessibleSlots[i] = i;
            }
            return accessibleSlots;
        } else {
            return new int[0];
        }
    }

    public static int getItemCount(ItemStack type, BlockEntity inv, Direction side, int fuzzySetting) {

        Container inventory = getInventoryForTE(inv);
        int[] slots = getAccessibleSlotsForInventory(inventory, side);
        int count = 0;
        for (int slot : slots) {
            ItemStack invStack = inventory.getItem(slot);
            if (!invStack.isEmpty()) {
                if (ItemStackHelper.areStacksEqual(invStack, type, fuzzySetting)) {
                    count += invStack.getCount();
                }
            }
        }
        return count;
    }

    /**
     * Retrieves an item from the specified inventory. This item can be specified.
     *
     * @param tile
     * @param direction
     * @param requestedStack
     * @param useItemCount
     *            if true, it'll only retrieve the stack of the exact item count given. it'll look in multiple slots of the inventory. if false, the
     *            first matching stack, ignoring item count, will be returned.
     * @param simulate
     * @param fuzzySetting
     *            ,
     * @return
     */
    public static ItemStack extract(BlockEntity tile, Direction direction, ItemStack requestedStack, boolean useItemCount, boolean simulate,
                                    int fuzzySetting) {

        if (requestedStack.isEmpty())
            return requestedStack;
        Container inv = getInventoryForTE(tile);
        if (!inv.isEmpty()) {
            int[] accessibleSlots;
            if (inv instanceof WorldlyContainer) {
                accessibleSlots = ((WorldlyContainer) inv).getSlotsForFace(direction);
            } else {
                accessibleSlots = new int[inv.getContainerSize()];
                for (int i = 0; i < accessibleSlots.length; i++)
                    accessibleSlots[i] = i;
            }
            int itemsFound = 0;
            for (int slot : accessibleSlots) {
                ItemStack stack = inv.getItem(slot);
                if (!stack.isEmpty() && ItemStackHelper.areStacksEqual(stack, requestedStack, fuzzySetting)
                        && IOHelper.canTakeItemThroughFaceFromInventory(inv, requestedStack, slot, direction.ordinal())) {
                    if (!useItemCount) {
                        if (!simulate) {
                            inv.setItem(slot, ItemStack.EMPTY);
                        }
                        return stack;
                    }
                    itemsFound += stack.getCount();
                }
            }
            if (itemsFound >= requestedStack.getCount()) {
                ItemStack exportedStack = ItemStack.EMPTY;
                int itemsNeeded = requestedStack.getCount();
                for (int slot : accessibleSlots) {
                    ItemStack stack = inv.getItem(slot);
                    if (!stack.isEmpty() && ItemStackHelper.areStacksEqual(stack, requestedStack, fuzzySetting)
                            && IOHelper.canTakeItemThroughFaceFromInventory(inv, requestedStack, slot, direction.ordinal())) {
                        int itemsSubstracted = Math.min(itemsNeeded, stack.getCount());
                        if (itemsSubstracted > 0)
                            exportedStack = stack;
                        itemsNeeded -= itemsSubstracted;
                        if (!simulate) {
                            stack.setCount(stack.getCount() - itemsSubstracted);
                            if (stack.getCount() == 0)
                                inv.setItem(slot, ItemStack.EMPTY);
                            tile.setChanged();
                        }
                    }
                }
                exportedStack = exportedStack.copy();
                exportedStack.setCount(requestedStack.getCount());
                return exportedStack;
            }
        }
        return ItemStack.EMPTY;

    }

    public static ItemStack extractOneItem(BlockEntity tile, Direction dir) {

        Container inv = getInventoryForTE(tile);
        if (inv != null && !inv.isEmpty()) {
            int[] accessibleSlots;
            if (inv instanceof WorldlyContainer) {
                accessibleSlots = ((WorldlyContainer) inv).getSlotsForFace(dir);
            } else {
                accessibleSlots = new int[inv.getContainerSize()];
                for (int i = 0; i < accessibleSlots.length; i++)
                    accessibleSlots[i] = i;
            }
            for (int slot : accessibleSlots) {
                ItemStack stack = inv.getItem(slot);
                ItemStack retrievingStack = stack.isEmpty() ? ItemStack.EMPTY : stack.copy().split(1);
                if (!stack.isEmpty() && IOHelper.canTakeItemThroughFaceFromInventory(inv, retrievingStack, slot, dir.ordinal())) {
                    ItemStack ret = stack.split(1);
                    if (stack.getCount() == 0)
                        inv.setItem(slot, ItemStack.EMPTY);
                    tile.setChanged();
                    return ret;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack insert(BlockEntity tile, ItemStack itemStack, Direction direction, boolean simulate) {

        return insert(tile, itemStack, direction, TubeColor.NONE, simulate);
    }

    public static ItemStack insert(BlockEntity tile, ItemStack itemStack, Direction direction, TubeColor color, boolean simulate) {

        if (tile == null || itemStack.isEmpty())
            return itemStack;

       /* if (tile instanceof ITubeConnection) {
            TubeStack tubeStack = ((ITubeConnection) tile).acceptItemFromTube(new TubeStack(itemStack, direction.getOpposite(), color), direction,
                    simulate);
            if (tubeStack == null)
                return ItemStack.EMPTY;
            return tubeStack.stack;
        }

        if (tile.getCapability(ForgeCapabilities.ITEM_HANDLER, direction).isPresent()) {
            IItemHandler handler = tile.getCapability(ForgeCapabilities.ITEM_HANDLER, direction).orElse(null);
            if (handler != null) {
                return ItemHandlerHelper.insertItem(handler, itemStack, simulate);
            }
            return itemStack;
        }*/

        return itemStack;
    }

    public static ItemStack insert(Container inventory, ItemStack itemStack, int side, boolean simulate) {

        if (inventory instanceof WorldlyContainer && side > -1) {
            WorldlyContainer isidedinventory = (WorldlyContainer) inventory;
            int[] aint = isidedinventory.getSlotsForFace(Direction.from3DDataValue(side));

            for (int j = 0; j < aint.length && !itemStack.isEmpty() && itemStack.getCount() > 0; ++j) {
                itemStack = insert(inventory, itemStack, aint[j], side, simulate);
            }
        } else {
            int k = inventory.getContainerSize();

            for (int l = 0; l < k && !itemStack.isEmpty() && itemStack.getCount() > 0; ++l) {
                itemStack = insert(inventory, itemStack, l, side, simulate);
            }
        }

        if (!itemStack.isEmpty() && itemStack.getCount() == 0) {
            itemStack = ItemStack.EMPTY;
        }

        return itemStack;
    }

    public static ItemStack insert(Container inventory, ItemStack itemStack, int slot, int side, boolean simulate) {

        ItemStack itemstack1 = inventory.getItem(slot);

        if (canPlaceItemThroughFaceToInventory(inventory, itemStack, slot, side)) {
            boolean flag = false;

            if (itemstack1.isEmpty()) {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getMaxStackSize());
                if (max >= itemStack.getCount()) {
                    if (!simulate) {
                        inventory.setItem(slot, itemStack);
                        flag = true;
                    }
                    itemStack = ItemStack.EMPTY;
                } else {
                    if (!simulate) {
                        inventory.setItem(slot, itemStack.split(max));
                        flag = true;
                    } else {
                        itemStack.split(max);
                    }
                }
            } else if (ItemStackHelper.areItemStacksEqual(itemstack1, itemStack)) {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getMaxStackSize());
                if (max > itemstack1.getCount()) {
                    int l = Math.min(itemStack.getCount(), max - itemstack1.getCount());
                    itemStack.setCount(itemStack.getCount() - l);
                    if (!simulate) {
                        itemstack1.setCount(itemstack1.getCount() + l);
                        flag = l > 0;
                    }
                }
            }
            if (flag) {
                inventory.setChanged();
            }
        }

        return itemStack;
    }

    public static boolean canPlaceItemThroughFaceToInventory(Container inventory, ItemStack itemStack, int slot, int side) {

        return inventory.canPlaceItem(slot, itemStack)
                && (!(inventory instanceof WorldlyContainer) || ((WorldlyContainer) inventory).canPlaceItemThroughFace(slot, itemStack, Direction.from3DDataValue(side)));
    }

    public static boolean canTakeItemThroughFaceFromInventory(Container inventory, ItemStack itemStack, int slot, int side) {

        return !(inventory instanceof WorldlyContainer) || ((WorldlyContainer) inventory).canTakeItemThroughFace(slot, itemStack, Direction.from3DDataValue(side));
    }

    public static void dropInventory(Level world, BlockPos pos) {

        BlockEntity tileEntity = world.getBlockEntity(pos);

        if (!(tileEntity instanceof Container)) {
            return;
        }

        Container inventory = (Container) tileEntity;

        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack itemStack = inventory.getItem(i);

            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                spawnItemInWorld(world, itemStack, pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }

    public static void spawnItemInWorld(Level world, ItemStack itemStack, BlockPos pos) {
        spawnItemInWorld(world, itemStack, pos.getX(), pos.getY(), pos.getZ());
    }


    public static void spawnItemInWorld(Level world, ItemStack itemStack, double x, double y, double z) {

        if (world.isClientSide)
            return;
        float dX = world.random.nextFloat() * 0.8F + 0.1F;
        float dY = world.random.nextFloat() * 0.8F + 0.1F;
        float dZ = world.random.nextFloat() * 0.8F + 0.1F;

        ItemEntity entityItem = new ItemEntity(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.getItem(), itemStack.getCount()));
        if (itemStack.has(DataComponents.CUSTOM_DATA)) {
            entityItem.getItem().set(DataComponents.CUSTOM_DATA, itemStack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY));
        }

        float factor = 0.05F;
        entityItem.setDeltaMovement(world.random.nextGaussian() * factor, world.random.nextGaussian() * factor + 0.2F, world.random.nextGaussian() * factor);
        world.addFreshEntity(entityItem);
        itemStack.setCount(0);
    }

    public static boolean canInterfaceWith(BlockEntity tile, Direction direction) {

        return canInterfaceWith(tile, direction, true);
    }

   public static boolean canInterfaceWith(BlockEntity tile, Direction direction, boolean canInterfaceWithContainer) {

       if (tile instanceof TileTube)
            return true;
        if (!canInterfaceWithContainer)
            return false;
        if (tile instanceof ITubeConnection) {
            return true;
        }
        if (tile instanceof Container) {
            return !(tile instanceof WorldlyContainer) || ((WorldlyContainer) tile).getSlotsForFace(direction).length > 0;
        }
        return false;
    }
}
