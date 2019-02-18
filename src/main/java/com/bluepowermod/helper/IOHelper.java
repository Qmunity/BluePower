/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.api.tube.ITubeConnection;

/**
 * @author MineMaarten
 * @author Dynious
 */
public class IOHelper {

    public static IInventory getInventoryForTE(TileEntity te) {

        if (te instanceof IInventory) {
            IInventory inv = (IInventory) te;
            Block block = te.getBlockType();
            if (block instanceof BlockChest) {
                inv = ((BlockChest) block).getContainer(te.getWorld(), te.getPos() ,false);
            }
            return inv;
        } else {
            return null;
        }
    }

    public static ItemStack extract(TileEntity inventory, EnumFacing direction, boolean simulate) {

        IInventory inv = getInventoryForTE(inventory);
        if (inv != null)
            return extract(inv, direction, simulate);
        return ItemStack.EMPTY;
    }

    public static ItemStack extract(IInventory inventory, EnumFacing direction, boolean simulate) {

        if (inventory instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] accessibleSlotsFromSide = isidedinventory.getSlotsForFace(direction);

            for (int anAccessibleSlotsFromSide : accessibleSlotsFromSide) {
                ItemStack stack = extract(inventory, direction, anAccessibleSlotsFromSide, simulate);
                if (!stack.isEmpty())
                    return stack;
            }
        } else {
            int j = inventory.getSizeInventory();

            for (int k = 0; k < j; ++k) {
                ItemStack stack = extract(inventory, direction, k, simulate);
                if (!stack.isEmpty())
                    return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack extract(IInventory inventory, EnumFacing direction, int slot, boolean simulate) {

        ItemStack itemstack = inventory.getStackInSlot(slot);

        if (!itemstack.isEmpty() && canExtractItemFromInventory(inventory, itemstack, slot, direction.ordinal())) {
            if (!simulate)
                inventory.setInventorySlotContents(slot, ItemStack.EMPTY);
            return itemstack;
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack extract(TileEntity tile, EnumFacing direction, ItemStack requestedStack, boolean useItemCount, boolean simulate) {

        return extract(tile, direction, requestedStack, useItemCount, simulate, 0);
    }

    public static int[] getAccessibleSlotsForInventory(IInventory inv, EnumFacing side) {

        int[] accessibleSlots;
        if (inv != null) {
            if (inv instanceof ISidedInventory) {
                accessibleSlots = ((ISidedInventory) inv).getSlotsForFace(side);
            } else {
                accessibleSlots = new int[inv.getSizeInventory()];
                for (int i = 0; i < accessibleSlots.length; i++)
                    accessibleSlots[i] = i;
            }
            return accessibleSlots;
        } else {
            return new int[0];
        }
    }

    public static int getItemCount(ItemStack type, TileEntity inv, EnumFacing side, int fuzzySetting) {

        IInventory inventory = getInventoryForTE(inv);
        int[] slots = getAccessibleSlotsForInventory(inventory, side);
        int count = 0;
        for (int slot : slots) {
            ItemStack invStack = inventory.getStackInSlot(slot);
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
    public static ItemStack extract(TileEntity tile, EnumFacing direction, ItemStack requestedStack, boolean useItemCount, boolean simulate,
            int fuzzySetting) {

        if (requestedStack.isEmpty())
            return requestedStack;
        IInventory inv = getInventoryForTE(tile);
        if (!inv.isEmpty()) {
            int[] accessibleSlots;
            if (inv instanceof ISidedInventory) {
                accessibleSlots = ((ISidedInventory) inv).getSlotsForFace(direction);
            } else {
                accessibleSlots = new int[inv.getSizeInventory()];
                for (int i = 0; i < accessibleSlots.length; i++)
                    accessibleSlots[i] = i;
            }
            int itemsFound = 0;
            for (int slot : accessibleSlots) {
                ItemStack stack = inv.getStackInSlot(slot);
                if (!stack.isEmpty() && ItemStackHelper.areStacksEqual(stack, requestedStack, fuzzySetting)
                        && IOHelper.canExtractItemFromInventory(inv, requestedStack, slot, direction.ordinal())) {
                    if (!useItemCount) {
                        if (!simulate) {
                            inv.setInventorySlotContents(slot, ItemStack.EMPTY);
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
                    ItemStack stack = inv.getStackInSlot(slot);
                    if (!stack.isEmpty() && ItemStackHelper.areStacksEqual(stack, requestedStack, fuzzySetting)
                            && IOHelper.canExtractItemFromInventory(inv, requestedStack, slot, direction.ordinal())) {
                        int itemsSubstracted = Math.min(itemsNeeded, stack.getCount());
                        if (itemsSubstracted > 0)
                            exportedStack = stack;
                        itemsNeeded -= itemsSubstracted;
                        if (!simulate) {
                            stack.setCount(stack.getCount() - itemsSubstracted);
                            if (stack.getCount() == 0)
                                inv.setInventorySlotContents(slot, ItemStack.EMPTY);
                            tile.markDirty();
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

    public static ItemStack extractOneItem(TileEntity tile, EnumFacing dir) {

        IInventory inv = getInventoryForTE(tile);
        if (!inv.isEmpty()) {
            int[] accessibleSlots;
            if (inv instanceof ISidedInventory) {
                accessibleSlots = ((ISidedInventory) inv).getSlotsForFace(dir);
            } else {
                accessibleSlots = new int[inv.getSizeInventory()];
                for (int i = 0; i < accessibleSlots.length; i++)
                    accessibleSlots[i] = i;
            }
            for (int slot : accessibleSlots) {
                ItemStack stack = inv.getStackInSlot(slot);
                ItemStack retrievingStack = stack.isEmpty() ? ItemStack.EMPTY : stack.copy().splitStack(1);
                if (!stack.isEmpty() && IOHelper.canExtractItemFromInventory(inv, retrievingStack, slot, dir.ordinal())) {
                    ItemStack ret = stack.splitStack(1);
                    if (stack.getCount() == 0)
                        inv.setInventorySlotContents(slot, ItemStack.EMPTY);
                    tile.markDirty();
                    return ret;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public static ItemStack insert(TileEntity tile, ItemStack itemStack, EnumFacing direction, boolean simulate) {

        return insert(tile, itemStack, direction, TubeColor.NONE, simulate);
    }

    public static ItemStack insert(TileEntity tile, ItemStack itemStack, EnumFacing direction, TubeColor color, boolean simulate) {

        if (tile == null || itemStack.isEmpty())
            return itemStack;

       /* if (tile instanceof ITubeConnection) {
            TubeStack tubeStack = ((ITubeConnection) tile).acceptItemFromTube(new TubeStack(itemStack, direction.getOpposite(), color), direction,
                    simulate);
            if (tubeStack == null)
                return ItemStack.EMPTY;
            return tubeStack.stack;
        }
        IInventory inv = getInventoryForTE(tile);
        if (!inv.isEmpty())
            return insert(inv, itemStack, direction.ordinal(), simulate);
        PneumaticTube tube = MultipartCompatibility.getPart(tile.getWorld(), tile.getPos(), PneumaticTube.class);
        if (tube != null) {// we don't need to check connections, that's catched earlier.
            TubeLogic logic = tube.getLogic();
            return logic.injectStack(itemStack, direction.getOpposite(), color, simulate) ? ItemStack.EMPTY : itemStack;
        }*/
        return itemStack;
    }

    public static ItemStack insert(IInventory inventory, ItemStack itemStack, int side, boolean simulate) {

        if (inventory instanceof ISidedInventory && side > -1) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] aint = isidedinventory.getSlotsForFace(EnumFacing.byIndex(side));

            for (int j = 0; j < aint.length && !itemStack.isEmpty() && itemStack.getCount() > 0; ++j) {
                itemStack = insert(inventory, itemStack, aint[j], side, simulate);
            }
        } else {
            int k = inventory.getSizeInventory();

            for (int l = 0; l < k && !itemStack.isEmpty() && itemStack.getCount() > 0; ++l) {
                itemStack = insert(inventory, itemStack, l, side, simulate);
            }
        }

        if (!itemStack.isEmpty() && itemStack.getCount() == 0) {
            itemStack = ItemStack.EMPTY;
        }

        return itemStack;
    }

    public static ItemStack insert(IInventory inventory, ItemStack itemStack, int slot, int side, boolean simulate) {

        ItemStack itemstack1 = inventory.getStackInSlot(slot);

        if (canInsertItemToInventory(inventory, itemStack, slot, side)) {
            boolean flag = false;

            if (itemstack1.isEmpty()) {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max >= itemStack.getCount()) {
                    if (!simulate) {
                        inventory.setInventorySlotContents(slot, itemStack);
                        flag = true;
                    }
                    itemStack = ItemStack.EMPTY;
                } else {
                    if (!simulate) {
                        inventory.setInventorySlotContents(slot, itemStack.splitStack(max));
                        flag = true;
                    } else {
                        itemStack.splitStack(max);
                    }
                }
            } else if (ItemStackHelper.areItemStacksEqual(itemstack1, itemStack)) {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
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
                inventory.markDirty();
            }
        }

        return itemStack;
    }

    public static boolean canInsertItemToInventory(IInventory inventory, ItemStack itemStack, int slot, int side) {

        return inventory.isItemValidForSlot(slot, itemStack)
                && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(slot, itemStack, EnumFacing.byIndex(side)));
    }

    public static boolean canExtractItemFromInventory(IInventory inventory, ItemStack itemStack, int slot, int side) {

        return !(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canExtractItem(slot, itemStack, EnumFacing.byIndex(side));
    }

    public static void dropInventory(World world, BlockPos pos) {

        TileEntity tileEntity = world.getTileEntity(pos);

        if (!(tileEntity instanceof IInventory)) {
            return;
        }

        IInventory inventory = (IInventory) tileEntity;

        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);

            if (!itemStack.isEmpty() && itemStack.getCount() > 0) {
                spawnItemInWorld(world, itemStack, pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }

    public static void spawnItemInWorld(World world, ItemStack itemStack, BlockPos pos) {
        spawnItemInWorld(world, itemStack, pos.getX(), pos.getY(), pos.getZ());
    }


    public static void spawnItemInWorld(World world, ItemStack itemStack, double x, double y, double z) {

        if (world.isRemote)
            return;
        float dX = world.rand.nextFloat() * 0.8F + 0.1F;
        float dY = world.rand.nextFloat() * 0.8F + 0.1F;
        float dZ = world.rand.nextFloat() * 0.8F + 0.1F;

        EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.getItem(), itemStack.getCount(),
                itemStack.getItemDamage()));

        if (itemStack.hasTagCompound()) {
            entityItem.getItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
        }

        float factor = 0.05F;
        entityItem.motionX = world.rand.nextGaussian() * factor;
        entityItem.motionY = world.rand.nextGaussian() * factor + 0.2F;
        entityItem.motionZ = world.rand.nextGaussian() * factor;
        world.spawnEntity(entityItem);
        itemStack.setCount(0);
    }

    public static boolean canInterfaceWith(TileEntity tile, EnumFacing direction) {

        return canInterfaceWith(tile, direction, true);
    }

   public static boolean canInterfaceWith(TileEntity tile, EnumFacing direction, boolean canInterfaceWithIInventory) {

       // PneumaticTube tube = tile != null ? MultipartCompatibility.getPart(tile.getWorld(), tile.getPos(),
                //PneumaticTube.class) : null;
        //if (tube != null && tube.isConnected(direction, requester))
            //return true;
        if (!canInterfaceWithIInventory)
            return false;
        if (tile instanceof ITubeConnection) {
            return true;
        }
        if (tile instanceof IInventory) {
            return !(tile instanceof ISidedInventory) || ((ISidedInventory) tile).getSlotsForFace(direction).length > 0;
        }
        return false;
    }
}
