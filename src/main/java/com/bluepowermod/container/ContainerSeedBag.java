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

import com.bluepowermod.client.gui.BPMenuType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import com.bluepowermod.container.slot.SlotLocked;
import com.bluepowermod.container.slot.SlotSeedBag;
import com.bluepowermod.item.ItemSeedBag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.items.ItemStackHandler;

public class ContainerSeedBag extends AbstractContainerMenu {

    private final ItemStackHandler seedBagInvHandler;
    private InteractionHand activeHand;

    public ContainerSeedBag(int windowId, Inventory playerInventory) {
        super(BPMenuType.SEEDBAG.get(), windowId);
        seedBagInvHandler = new ItemStackHandler(9);
        
        //Get Active hand
        activeHand = InteractionHand.MAIN_HAND;
        ItemStack seedBag = playerInventory.player.getItemInHand(activeHand);
        if(!(seedBag.getItem() instanceof ItemSeedBag)){
            seedBag = playerInventory.player.getOffhandItem();
            activeHand = InteractionHand.OFF_HAND;
        }

        //Get Items from the NBT Handler
        if (seedBag.has(DataComponents.CUSTOM_DATA)) seedBagInvHandler.deserializeNBT(playerInventory.player.level().registryAccess(), seedBag.get(DataComponents.CUSTOM_DATA).copyTag().getCompound("inv"));
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                this.addSlot(new SlotSeedBag(seedBagInvHandler, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        for (int i = 0; i < 9; ++i) {
            if (playerInventory.selected == i) {
                this.addSlot(new SlotLocked(playerInventory, i, 8 + i * 18, 142));
            } else {
                this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
            }
        }

    }
    
    @Override
    public boolean stillValid(Player player) {
    
        return !player.getMainHandItem().isEmpty() && player.getMainHandItem().getItem() instanceof ItemSeedBag;
    }

    @Override
    public void removed(Player playerIn) {
        //Update items in the NBT
        ItemStack seedBag = playerIn.getItemInHand(activeHand);
        CompoundTag tag = new CompoundTag();
        tag.put("inv", seedBagInvHandler.serializeNBT(playerIn.level().registryAccess()));
        seedBag.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
        super.removed(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2) {

        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(par2);

        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();

            if (par2 < 9) {
                if (!this.moveItemStackTo(itemstack1, 9, 45, true)) { return ItemStack.EMPTY; }
            } else if (!this.moveItemStackTo(itemstack1, 0, 9, false)) { return ItemStack.EMPTY; }

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

}
