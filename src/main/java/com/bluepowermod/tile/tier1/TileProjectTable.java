/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.BluePower;
import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.tile.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author MineMaarten
 */
public class TileProjectTable extends TileBase implements IInventory, IGuiButtonSensitive {

    private NonNullList<ItemStack> inventory = NonNullList.withSize(19, ItemStack.EMPTY);
    protected NonNullList<ItemStack> craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);
    protected final IInventory craftResult = new InventoryCraftResult();
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
            stackListFieldInventoryCrafting.set(inventoryCrafting, craftingGrid);// Inject the array, so when stacks are being set to ItemStack.EMPTY by the
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
            if (!stack.isEmpty())
                drops.add(stack);
        for (ItemStack stack : craftingGrid)
            if (!stack.isEmpty())
                drops.add(stack);
        return drops;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);

        NBTTagList tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < inventory.size(); ++currentIndex) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte)currentIndex);
                inventory.get(currentIndex).writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
        }
        tag.setTag("Items", tagList);

        tagList = new NBTTagList();
        for (int currentIndex = 0; currentIndex < craftingGrid.size(); ++currentIndex) {
                NBTTagCompound tagCompound = new NBTTagCompound();
                tagCompound.setByte("Slot", (byte) currentIndex);
                craftingGrid.get(currentIndex).writeToNBT(tagCompound);
                tagList.appendTag(tagCompound);
        }
        tag.setTag("CraftingGrid", tagList);
        return tag;
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);

        NBTTagList tagList = tag.getTagList("Items", 10);
        inventory = NonNullList.withSize(19, ItemStack.EMPTY);
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < inventory.size()) {
                inventory.set(slot, new ItemStack(tagCompound));
            }
        }

        tagList = tag.getTagList("CraftingGrid", 10);
        craftingGrid = NonNullList.withSize(9, ItemStack.EMPTY);
        for (int i = 0; i < tagList.tagCount(); ++i) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            byte slot = tagCompound.getByte("Slot");
            if (slot >= 0 && slot < craftingGrid.size()) {
                craftingGrid.set(slot, new ItemStack(tagCompound));
            }
        }
    }

    @Override
    public int getSizeInventory() {

        return inventory.size();
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return inventory.get(i);
    }

    @Override
    public ItemStack decrStackSize(int slot, int amount) {

        ItemStack itemStack = getStackInSlot(slot);
        if (!itemStack.isEmpty()) {
            if (itemStack.getCount() <= amount) {
                setInventorySlotContents(slot, ItemStack.EMPTY);
            } else {
                itemStack = itemStack.splitStack(amount);
                if (itemStack.getCount() == 0) {
                    setInventorySlotContents(slot, ItemStack.EMPTY);
                }
            }
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStackFromSlot(int i) {
        ItemStack itemStack = getStackInSlot(i);
        if (!itemStack.isEmpty()) {
            setInventorySlotContents(i, ItemStack.EMPTY);
        }
        return itemStack;
    }

    @Override
    public void setInventorySlotContents(int i, ItemStack itemStack) {
        inventory.set(i, itemStack);
    }

    @Override
    public String getName() {

        return BPBlocks.project_table.getUnlocalizedName();
    }

    @Override
    public boolean hasCustomName() {

        return false;
    }

    @Override
    public int getInventoryStackLimit() {

        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return true;
    }

    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

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

    protected void updateCraftingGrid() {
        craftResult.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(getCraftingGrid(), getWorld()));
    }

    protected void craft() {
        // FMLCommonHandler.instance().firePlayerCraftingEvent(p_82870_1_, p_82870_2_, craftMatrix);

        for (int i = 0; i < craftingGrid.size(); ++i) {
            ItemStack itemstack1 = craftingGrid.get(i);

            if (!itemstack1.isEmpty()) {
                boolean pulledFromInventory = false;
                if (craftingGrid.get(i).getCount() == 1) {
                    ItemStack stackFromTable = ContainerProjectTable.extractStackFromTable(this, craftingGrid.get(i), false);
                    pulledFromInventory = !stackFromTable.isEmpty();
                }
                if (!pulledFromInventory) {
                    craftingGrid.get(i).setCount(craftingGrid.get(i).getCount() - 1);
                    if (craftingGrid.get(i).getCount() <= 0)
                        craftingGrid.set(i, ItemStack.EMPTY);
                }
                if (itemstack1.getItem().hasContainerItem(itemstack1)) {
                    ItemStack itemstack2 = itemstack1.getItem().getContainerItem(itemstack1);

                    if (!itemstack2.isEmpty() && itemstack2.isItemStackDamageable() && itemstack2.getItemDamage() > itemstack2.getMaxDamage()) {
                        continue;
                    }

                    //Removed remainder as this is now handled by minecraft.
                    IOHelper.insert(this, itemstack2, 0, false);

                }
            }
        }
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack itemstack : inventory ){
            if (!itemstack.isEmpty()){
                return false;
            }
        }
        return true;
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

}
