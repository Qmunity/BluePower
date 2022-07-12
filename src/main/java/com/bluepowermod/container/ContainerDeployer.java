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

package com.bluepowermod.container;

import com.bluepowermod.client.gui.BPMenuType;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import com.bluepowermod.tile.tier1.TileDeployer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * @author MineMaarten
 */
public class ContainerDeployer extends AbstractContainerMenu {
    
    private final Container deployer;

    public ContainerDeployer(int windowId, Inventory invPlayer, Container inventory) {
        super(BPMenuType.DEPLOYER.get(), windowId);
        this.deployer = inventory;
        
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 3; ++j) {
                addSlot(new Slot(deployer, j + i * 3, 62 + j * 18, 17 + i * 18));
            }
        }
        bindPlayerInventory(invPlayer);
    }

    public ContainerDeployer( int id, Inventory player )    {
        this( id, player, new SimpleContainer( TileDeployer.SLOTS ));
    }


    protected void bindPlayerInventory(Inventory invPlayer) {
    
        // Render inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(invPlayer, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        
        // Render hotbar
        for (int j = 0; j < 9; j++) {
            addSlot(new Slot(invPlayer, j, 8 + j * 18, 142));
        }
        
    }
    
    @Override
    public boolean stillValid(Player player) {
    
        return deployer.stillValid(player);
    }
    
    @Override
    public ItemStack quickMoveStack(Player par1EntityPlayer, int par2) {
    
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) slots.get(par2);
        
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            
            if (par2 < 9) {
                if (!moveItemStackTo(itemstack1, 9, 45, true)) { return ItemStack.EMPTY; }
            } else if (!moveItemStackTo(itemstack1, 0, 9, false)) { return ItemStack.EMPTY; }
            
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
    
    @Override
    public boolean moveItemStackTo(ItemStack par1ItemStack, int par2, int par3, boolean par4) {
    
        boolean flag1 = false;
        int k = par2;
        
        if (par4) {
            k = par3 - 1;
        }
        
        Slot slot;
        ItemStack itemstack1;
        
        if (par1ItemStack.isStackable()) {
            while (par1ItemStack.getCount() > 0 && (!par4 && k < par3 || par4 && k >= par2)) {
                slot = (Slot) slots.get(k);
                itemstack1 = slot.getItem();
                
                if (!itemstack1.isEmpty() && itemstack1.getItem() == par1ItemStack.getItem() && (!par1ItemStack.hasCraftingRemainingItem() || par1ItemStack.getDamageValue() == itemstack1.getDamageValue()) && ItemStack.tagMatches(par1ItemStack, itemstack1) && slot.mayPlace(par1ItemStack)) {
                    int l = itemstack1.getCount() + par1ItemStack.getCount();
                    
                    if (l <= par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.setCount(0);
                        itemstack1.setCount(l);
                        slot.setChanged();
                        flag1 = true;
                    } else if (itemstack1.getCount() < par1ItemStack.getMaxStackSize()) {
                        par1ItemStack.setCount(par1ItemStack.getCount() - par1ItemStack.getMaxStackSize() - itemstack1.getCount());
                        itemstack1.setCount(par1ItemStack.getMaxStackSize());
                        slot.setChanged();
                        flag1 = true;
                    }
                }
                
                if (par4) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        
        if (par1ItemStack.getCount() > 0) {
            if (par4) {
                k = par3 - 1;
            } else {
                k = par2;
            }
            
            while (!par4 && k < par3 || par4 && k >= par2) {
                slot = (Slot) slots.get(k);
                itemstack1 = slot.getItem();
                
                if (itemstack1.isEmpty() && slot.mayPlace(par1ItemStack)) {
                    if (1 < par1ItemStack.getCount()) {
                        ItemStack copy = par1ItemStack.copy();
                        copy.setCount(1);
                        slot.set(copy);

                        par1ItemStack.setCount(par1ItemStack.getCount() - 1);
                        flag1 = true;
                        break;
                    } else {
                        slot.set(par1ItemStack.copy());
                        slot.setChanged();
                        par1ItemStack.setCount(0);
                        flag1 = true;
                        break;
                    }
                }
                
                if (par4) {
                    --k;
                } else {
                    ++k;
                }
            }
        }
        return flag1;
    }
}
