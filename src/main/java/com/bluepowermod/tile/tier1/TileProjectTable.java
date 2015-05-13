/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import com.bluepowermod.BluePower;
import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.tile.TileBase;

import cpw.mods.fml.relauncher.ReflectionHelper;

/**
 * @author MineMaarten
 */
public class TileProjectTable extends TileBase implements IInventory, IGuiButtonSensitive {

    private ItemStack[] inventory = new ItemStack[18];
    protected ItemStack[] craftingGrid = new ItemStack[9];
    public final IInventory craftResult = new InventoryCraftResult();
    private static Field stackListFieldInventoryCrafting;

    public InventoryCrafting getCraftingGrid(Container listener) {
        InventoryCrafting inventoryCrafting = new InventoryCrafting(listener, 3, 3) {
            @Override
            public void setInventorySlotContents(int slot, ItemStack stack) {
                super.setInventorySlotContents(slot, stack);
                updateCraftingGrid();
            }

            @Override
            public ItemStack decrStackSize(int p_70298_1_, int p_70298_2_) {
                ItemStack stack = super.decrStackSize(p_70298_1_, p_70298_2_);
                updateCraftingGrid();
                return stack;
            }
        };
        if (stackListFieldInventoryCrafting == null) {
            stackListFieldInventoryCrafting = ReflectionHelper.findField(InventoryCrafting.class, "field_70466_a", "stackList");
        }
        try {
            stackListFieldInventoryCrafting.set(inventoryCrafting, craftingGrid);// Inject the array, so when stacks are being set to null by the
                                                                                 // container it'll make it's way over to the actual stacks.
            return inventoryCrafting;
        } catch (Exception e) {
            BluePower.log.error("This is about to go wrong, Project Table getCraftingGrid:");
            e.printStackTrace();
            return null;
        }
    }

    public InventoryCrafting getCraftingGrid() {
        return getCraftingGrid(new Container() {
            @Override
            public boolean canInteractWith(EntityPlayer p_75145_1_) {

                return false;
            }

        });
    }

    @Override
    public List<ItemStack> getDrops() {

        List<ItemStack> drops = super.getDrops();
        for (ItemStack stack : inventory)
            if (stack != null)
                drops.add(stack);
        for (ItemStack stack : craftingGrid)
            if (stack != null)
                drops.add(stack);
        return drops;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.length; ++currentIndex) {
            if (inventory[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                inventory[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tag.setTag("Items", tagList);

        tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < craftingGrid.length; ++currentIndex) {
            if (craftingGrid[currentIndex] != null) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                craftingGrid[currentIndex].writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
            }
        }
        tag.setTag("CraftingGrid", tagList);

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = new ItemStack[18];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.length) {
                inventory[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }

        tagList = tag.getTagList("CraftingGrid", 10);
        craftingGrid = new ItemStack[9];
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < craftingGrid.length) {
                craftingGrid[slot] = ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
    }

    @Override
    public int getSizeInventory() {

        return inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {

        return i < inventory.length ? inventory[i] : craftResult.getStackInSlot(0);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (itemStack != null) {
            if (itemStack.stackSize <= amount) {
                setInventorySlotContents(slot, null);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.stackSize == 0) {
                    setInventorySlotContents(slot, null);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int i) {

        ItemStack itemStack = getStackInSlot(i);
        if (itemStack != null) {
            setInventorySlotContents(i, null);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {

        if (i < inventory.length) {
            inventory[i] = itemStack;
        } else {
            craftResult.setInventorySlotContents(0, itemStack);
            craft();
        }
        updateCraftingGrid();
    }

    @Override
    public String getInventoryName() {

        return BPBlocks.project_table.getUnlocalizedName();
    }

    @Override
    public boolean hasCustomInventoryName() {

        return false;
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {

        return true;
    }

    @Override
    public void openInventory() {

    }

    @Override
    public void closeInventory() {

    }

    @Override
    public boolean isItemValidForSlot(int p_94041_1_, ItemStack p_94041_2_) {

        return true;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {
        Container container = player.openContainer;
        if (container instanceof ContainerProjectTable) {
            ((ContainerProjectTable) container).clearCraftingGrid();
        }
    }

    private void updateCraftingGrid() {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(getCraftingGrid(), getWorldObj()));
    }

    private void craft() {
        // FMLCommonHandler.instance().firePlayerCraftingEvent(p_82870_1_, p_82870_2_, craftMatrix);

        for (int i = 0; i < craftingGrid.length; ++i) {
            ItemStack itemstack1 = craftingGrid[i];

            if (itemstack1 != null) {
                boolean pulledFromInventory = false;
                if (craftingGrid[i].stackSize == 1) {
                    ItemStack stackFromTable = ContainerProjectTable.extractStackFromTable(this, craftingGrid[i], false);
                    pulledFromInventory = stackFromTable != null;
                }
                if (!pulledFromInventory) {
                    craftingGrid[i].stackSize--;
                    if (craftingGrid[i].stackSize <= 0)
                        craftingGrid[i] = null;
                }
                if (itemstack1.getItem().hasContainerItem(itemstack1)) {
                    ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

                    if (itemstack2 != null && itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
                        continue;
                    }

                    if (!itemstack1.getItem().doesContainerItemLeaveCraftingGrid(itemstack1)) {
                        ItemStack remainder = IOHelper.insert(this, itemstack2, 0, false);
                        if (remainder != null) {
                            worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, remainder));
                        }
                    }
                }
            }
        }
    }

}
