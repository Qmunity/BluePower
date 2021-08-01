/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.inventory.*;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;

import com.bluepowermod.tile.tier1.TileProjectTable;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultContainer;

/**
 * @author MineMaarten
 */
public class SlotProjectTableCrafting extends ResultSlot {
    
    /** The craft matrix inventory linked to this result slot. */
    private final CraftingContainer craftMatrix;
    private final Container projectTable;

    public SlotProjectTableCrafting(Container projectTable, Player player, CraftingContainer craftMatrix, ResultContainer res, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
    
        super(player, craftMatrix, res, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.craftMatrix = craftMatrix;
        this.projectTable = projectTable;
    }

    @Override
    public void onQuickCraft(ItemStack p_75220_1_, ItemStack p_75220_2_) {

    }

    @Override
    public ItemStack onTake(Player thePlayer, ItemStack stack) {
            extractedFromTable();
            return super.onTake(thePlayer, stack);
    }

    private boolean extractedFromTable(){
        boolean remaining = true;
        for (int i = 0; i < 10; i++) {
            ItemStack itemStack = craftMatrix.getItem(i + 18);
            if (itemStack.getCount() == 1) {
                itemStack = extractFromTable(itemStack);
                craftMatrix.setItem(i + 18, itemStack);
            }
            if (itemStack.getCount() == 1) {
                remaining  =  false;
            }
        }
        return remaining;
    }

    private ItemStack extractFromTable(ItemStack itemStack){
        for (int j = 18; j < projectTable.getContainerSize() + 18; j++) {
            if (projectTable.getItem(j + 18).getItem().equals(itemStack.getItem())) {
                projectTable.removeItem(j + 18, 1);
                itemStack.setCount(itemStack.getCount() + 1);
            }
        }
        return itemStack;
    }

}
