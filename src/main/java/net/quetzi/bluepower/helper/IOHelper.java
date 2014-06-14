package net.quetzi.bluepower.helper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.compat.CompatibilityUtils;
import net.quetzi.bluepower.compat.fmp.IMultipartCompat;
import net.quetzi.bluepower.part.tube.PneumaticTube;
import net.quetzi.bluepower.references.Dependencies;

public class IOHelper {
    
    public static ItemStack extract(IInventory inventory, ForgeDirection direction) {
    
        if (inventory instanceof ISidedInventory) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] accessibleSlotsFromSide = isidedinventory.getAccessibleSlotsFromSide(direction.ordinal());
            
            for (int anAccessibleSlotsFromSide : accessibleSlotsFromSide) {
                ItemStack stack = extract(inventory, direction, anAccessibleSlotsFromSide);
                if (stack != null) return stack;
            }
        } else {
            int j = inventory.getSizeInventory();
            
            for (int k = 0; k < j; ++k) {
                ItemStack stack = extract(inventory, direction, k);
                if (stack != null) return stack;
            }
        }
        return null;
    }
    
    public static ItemStack extract(IInventory inventory, ForgeDirection direction, int slot) {
    
        ItemStack itemstack = inventory.getStackInSlot(slot);
        
        if (itemstack != null && canExtractItemFromInventory(inventory, itemstack, slot, direction.ordinal())) {
            inventory.setInventorySlotContents(slot, null);
            return itemstack;
        }
        return null;
    }
    
    public static ItemStack insert(TileEntity tile, ItemStack itemStack, ForgeDirection direction, boolean simulate) {
    
        if (tile instanceof IInventory) { return insert((IInventory) tile, itemStack, direction.ordinal(), simulate); }
        return itemStack;
    }
    
    public static ItemStack insert(IInventory inventory, ItemStack itemStack, int side, boolean simulate) {
    
        if (inventory instanceof ISidedInventory && side > -1) {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] aint = isidedinventory.getAccessibleSlotsFromSide(side);
            
            for (int j = 0; j < aint.length && itemStack != null && itemStack.stackSize > 0; ++j) {
                itemStack = insert(inventory, itemStack, aint[j], side, simulate);
            }
        } else {
            int k = inventory.getSizeInventory();
            
            for (int l = 0; l < k && itemStack != null && itemStack.stackSize > 0; ++l) {
                itemStack = insert(inventory, itemStack, l, side, simulate);
            }
        }
        
        if (itemStack != null && itemStack.stackSize == 0) {
            itemStack = null;
        }
        
        return itemStack;
    }
    
    public static ItemStack insert(IInventory inventory, ItemStack itemStack, int slot, int side, boolean simulate) {
    
        ItemStack itemstack1 = inventory.getStackInSlot(slot);
        
        if (canInsertItemToInventory(inventory, itemStack, slot, side)) {
            boolean flag = false;
            
            if (itemstack1 == null) {
                int max = Math.min(itemStack.getMaxStackSize(), inventory.getInventoryStackLimit());
                if (max >= itemStack.stackSize) {
                    if (!simulate) {
                        inventory.setInventorySlotContents(slot, itemStack);
                        flag = true;
                    }
                    itemStack = null;
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
                if (max > itemstack1.stackSize) {
                    int l = Math.min(itemStack.stackSize, max - itemstack1.stackSize);
                    itemStack.stackSize -= l;
                    if (!simulate) {
                        itemstack1.stackSize += l;
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
    
        return inventory.isItemValidForSlot(slot, itemStack) && (!(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canInsertItem(slot, itemStack, side));
    }
    
    public static boolean canExtractItemFromInventory(IInventory inventory, ItemStack itemStack, int slot, int side) {
    
        return !(inventory instanceof ISidedInventory) || ((ISidedInventory) inventory).canExtractItem(slot, itemStack, side);
    }
    
    public static void dropInventory(World world, int x, int y, int z) {
    
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        
        if (!(tileEntity instanceof IInventory)) { return; }
        
        IInventory inventory = (IInventory) tileEntity;
        
        for (int i = 0; i < inventory.getSizeInventory(); i++) {
            ItemStack itemStack = inventory.getStackInSlot(i);
            
            if (itemStack != null && itemStack.stackSize > 0) {
                spawnItemInWorld(world, itemStack, x, y, z);
            }
        }
    }
    
    public static void spawnItemInWorld(World world, ItemStack itemStack, double x, double y, double z) {
    
        if (world.isRemote) return;
        float dX = world.rand.nextFloat() * 0.8F + 0.1F;
        float dY = world.rand.nextFloat() * 0.8F + 0.1F;
        float dZ = world.rand.nextFloat() * 0.8F + 0.1F;
        
        EntityItem entityItem = new EntityItem(world, x + dX, y + dY, z + dZ, new ItemStack(itemStack.getItem(), itemStack.stackSize, itemStack.getItemDamage()));
        
        if (itemStack.hasTagCompound()) {
            entityItem.getEntityItem().setTagCompound((NBTTagCompound) itemStack.getTagCompound().copy());
        }
        
        float factor = 0.05F;
        entityItem.motionX = world.rand.nextGaussian() * factor;
        entityItem.motionY = world.rand.nextGaussian() * factor + 0.2F;
        entityItem.motionZ = world.rand.nextGaussian() * factor;
        world.spawnEntityInWorld(entityItem);
        itemStack.stackSize = 0;
    }
    
    public static boolean canInterfaceWith(TileEntity tile, ForgeDirection direction) {
    
        IMultipartCompat compat = (IMultipartCompat) CompatibilityUtils.getModule(Dependencies.FMP);
        PneumaticTube tube = compat.getBPPart(tile, PneumaticTube.class);
        if (tube != null && tube.isConnected(direction)) return true;
        if (tile instanceof IInventory) { return !(tile instanceof ISidedInventory) || ((ISidedInventory) tile).getAccessibleSlotsFromSide(direction.ordinal()).length > 0; }
        return false;
    }
}
