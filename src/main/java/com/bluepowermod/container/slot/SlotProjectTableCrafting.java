/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container.slot;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;

import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.tile.tier1.TileProjectTable;

/**
 * @author MineMaarten
 */
public class SlotProjectTableCrafting extends SlotCrafting {
    
    /** The craft matrix inventory linked to this result slot. */
    private final IInventory       craftMatrix;
    private final TileProjectTable projectTable;
    
    public SlotProjectTableCrafting(TileProjectTable projectTable, EntityPlayer p_i1823_1_, InventoryCrafting craftMatrix, IInventory p_i1823_3_, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
    
        super(p_i1823_1_, craftMatrix, p_i1823_3_, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.projectTable = projectTable;
        this.craftMatrix = craftMatrix;
    }

    @Override
    public void onSlotChange(ItemStack p_82870_1_, ItemStack p_82870_2_) {
        ItemStack[] oldGrid = new ItemStack[9];
        for (int i = 0; i < 9; i++) {
            oldGrid[i] = craftMatrix.getStackInSlot(i);
        }

        super.onSlotChange(p_82870_1_, p_82870_2_);

        for (int i = 0; i < 9; i++) {
            if (craftMatrix.getStackInSlot(i).isEmpty() && oldGrid[i] != null) {
                oldGrid[i].setCount(1);
                ItemStack stackFromTable = ContainerProjectTable.extractStackFromTable(projectTable, oldGrid[i], false);
                craftMatrix.setInventorySlotContents(i, stackFromTable);
            }
        }
    }

    
}
