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
 */

package com.bluepowermod.container;

import com.bluepowermod.container.inventory.InventoryProjectTableCrafting;
import com.bluepowermod.container.slot.SlotProjectTableCrafting;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tile.tier1.TileProjectTable;
import com.bluepowermod.util.Dependencies;
import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author MineMaarten
 */
@ChestContainer
public class ContainerProjectTable extends Container {

    private final EntityPlayer player;
    private final InventoryCrafting craftingGrid;
    private final InventoryCraftResult craftResult;

    public ContainerProjectTable(InventoryPlayer invPlayer, TileProjectTable projectTable) {
        craftResult =  new InventoryCraftResult();
        craftingGrid = new InventoryProjectTableCrafting(this, projectTable, 3, 3);;
        player = invPlayer.player;

        //Output
        addSlotToContainer(new SlotProjectTableCrafting(projectTable, player, craftingGrid, craftResult, 0, 127, 34));

        //Crafting Grid
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                //When changing the 34 and 16, this will break the NEI shift clicking the question mark. See NEIPluginInitConfig
                addSlotToContainer(new Slot(craftingGrid, j + i * 3, 34 + j * 18, 16 + i * 18));
            }
        }

        //Chest
        for (int i = 0; i < 2; ++i) {
            for (int j = 0; j < 9; ++j) {
                addSlotToContainer(new Slot(projectTable, j + i * 9, 8 + j * 18, 79 + i * 18));
            }
        }

        bindPlayerInventory(invPlayer);
        this.onCraftMatrixChanged(this.craftingGrid);
    }

    protected void bindPlayerInventory(InventoryPlayer invPlayer) {

        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlotToContainer(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 126 + i * 18));
            }
        }

        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlotToContainer(new Slot(invPlayer, j, 8 + j * 18, 184));
        }
    }

    /**
     * Callback for when the crafting matrix is changed.
     */
    @Override
    public void onCraftMatrixChanged(IInventory p_75130_1_) {
        this.slotChangedCraftingGrid(player.getEntityWorld(), this.player, this.craftingGrid, this.craftResult);
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return true;
    }

    public void clearCraftingGrid() {
        for (int i = 1; i < 10; i++) {
            Slot slot = (Slot) inventorySlots.get(i);
            if (slot.getHasStack()) {
                mergeItemStack(slot.getStack(), 10, 28, false);
                if (slot.getStack().getCount() <= 0)
                    slot.putStack(ItemStack.EMPTY);
            }
        }
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }

    public InventoryCrafting getCraftingGrid() {
        return craftingGrid;
    }

    /*
     * 0 result, 1-9 matrix,  10 - 27 inventory, 28 - 63 player inv.
     */
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = inventorySlots.get(par2);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if (0 < par2 && par2 < 10) {
                if (!mergeItemStack(itemstack1, 10, 28, false))
                    return ItemStack.EMPTY;
            } else if (par2 < 28) {
                if (!mergeItemStack(itemstack1, 28, 64, false))
                    return ItemStack.EMPTY;
            } else {
                if (!mergeItemStack(itemstack1, 10, 28, false))
                    return ItemStack.EMPTY;
            }
            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            if (itemstack1.getCount() != itemstack.getCount()) {
                slot.onSlotChange(itemstack, itemstack1);
            } else {

                this.onCraftMatrixChanged(this.craftingGrid);
                return ItemStack.EMPTY;
            }
            ItemStack itemstack2 = slot.onTake(player, itemstack1);

            if (par2 == 0)
            {
                player.dropItem(itemstack2, false);
            }
        }

        this.onCraftMatrixChanged(this.craftingGrid);
        return itemstack;
    }

    @Optional.Method(modid = Dependencies.INVTWEAKS)
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSections() {

        Map<ContainerSection, List<Slot>> sections = new HashMap<ContainerSection, List<Slot>>();
        List<Slot> slotsCraftingIn = new ArrayList<Slot>();
        List<Slot> slotsCraftingOut = new ArrayList<Slot>();
        List<Slot> slotsChest = new ArrayList<Slot>();
        List<Slot> slotsInventory = new ArrayList<Slot>();
        List<Slot> slotsInventoryHotbar = new ArrayList<Slot>();
        for (int i = 0; i < 9; i++) {
            slotsCraftingIn.add(i, (Slot) inventorySlots.get(i));
        }
        slotsCraftingOut.add(0, (Slot) inventorySlots.get(9));
        for (int i = 0; i < 18; i++) {
            slotsChest.add(i, (Slot) inventorySlots.get(i + 9));
        }
        for (int i = 0; i < 27; i++) {
            slotsInventory.add(0, (Slot) inventorySlots.get(i + 27));
        }
        for (int i = 0; i < 9; i++) {
            slotsInventoryHotbar.add(0, (Slot) inventorySlots.get(i + 54));
        }
        sections.put(ContainerSection.CRAFTING_IN, slotsCraftingIn);
        sections.put(ContainerSection.CRAFTING_OUT, slotsCraftingOut);
        sections.put(ContainerSection.CHEST, slotsChest);
        sections.put(ContainerSection.INVENTORY, slotsInventory);
        sections.put(ContainerSection.INVENTORY_HOTBAR, slotsInventoryHotbar);
        return sections;
    }
}
