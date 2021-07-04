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
 *     @author Lumien
 */

package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPContainerType;
import com.bluepowermod.container.slot.SlotLocked;
import com.bluepowermod.item.ItemCanvasBag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

//@ChestContainer
public class ContainerCanvasBag extends Container {

    private final ItemStackHandler canvasBagInvHandler;
    private Hand activeHand;

    public ContainerCanvasBag(int windowId, PlayerInventory playerInventory) {
    super(BPContainerType.CANVAS_BAG, windowId);
        canvasBagInvHandler = new ItemStackHandler(27);

        //Get Active hand
        activeHand = Hand.MAIN_HAND;
        ItemStack canvasbag = playerInventory.player.getItemInHand(activeHand);
        if(!(canvasbag.getItem() instanceof ItemCanvasBag)){
            canvasbag = playerInventory.player.getOffhandItem();
            activeHand = Hand.OFF_HAND;
        }

        //Get Items from the NBT Handler
        if (canvasbag.hasTag()) canvasBagInvHandler.deserializeNBT(canvasbag.getTag().getCompound("inv"));

        //Create Item Slots
        int i = -1 * 18;
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                addSlot(new SlotItemHandler(canvasBagInvHandler, k + j * 9, 8 + k * 18, 18 + j * 18){
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return super.mayPlace(stack) && !(stack.getItem() instanceof ItemCanvasBag);
                    }
                });
            }
        }

        //Create Player Slots
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }

        //Lock the Current Item
        for (int j = 0; j < 9; ++j) {
            if (playerInventory.selected == j) {
                addSlot(new SlotLocked(playerInventory, j, 8 + j * 18, 161 + i));
            } else {
                addSlot(new Slot(playerInventory, j, 8 + j * 18, 161 + i));
            }
        }
    }

    @Override
    public boolean stillValid(PlayerEntity player) {
        return player.getItemInHand(activeHand).getItem() instanceof ItemCanvasBag;
    }

    @Override
    public ItemStack clicked(int par1, int par2, ClickType par3, PlayerEntity player) {

        if (par3.ordinal() != 2 || player.inventory.selected != par2) {
            return super.clicked(par1, par2, par3, player);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void removed(PlayerEntity playerIn) {
        //Update items in the NBT
        ItemStack canvasBag = playerIn.getItemInHand(activeHand);
        if (!canvasBag.hasTag())
            canvasBag.setTag(new CompoundNBT());
        if (canvasBag.getTag() != null) {
            canvasBag.getTag().put("inv", canvasBagInvHandler.serializeNBT());
        }
        super.removed(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity par1EntityPlayer, int par2) {
    
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(par2);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            if (par2 < 27) {
                if (!moveItemStackTo(itemstack1, 27, 63, true)) { return ItemStack.EMPTY; }
            } else if (!moveItemStackTo(itemstack1, 0, 27, false)) { return ItemStack.EMPTY; }
            
            if (itemstack1.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            
            if (itemstack1.getCount() == itemstack.getCount()) { return ItemStack.EMPTY; }
            
            slot.onQuickCraft(itemstack, itemstack1);
        }
        
        return itemstack;
    }

    /*TODO: Inventory Tweaks Support find @Optional.Method alternative
    @Optional.Method(modid = Dependencies.INVTWEAKS)
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSections() {

        Map<ContainerSection, List<Slot>> sections = new HashMap<ContainerSection, List<Slot>>();
        List<Slot> slotsChest = new ArrayList<Slot>();
        List<Slot> slotsInventory = new ArrayList<Slot>();
        for (int i = 0; i < 27; i++) {
            slotsChest.add(i, (Slot) inventorySlots.get(i));
        }
        for (int i = 0; i < 36; i++) {
            slotsInventory.add(0, (Slot) inventorySlots.get(i + 27));
        }
        sections.put(ContainerSection.CHEST, slotsChest);
        sections.put(ContainerSection.INVENTORY, slotsInventory);
        return sections;
    }*/
}
