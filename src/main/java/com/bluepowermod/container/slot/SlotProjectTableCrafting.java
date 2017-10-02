/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.container.slot;

import com.bluepowermod.helper.IOHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;

import com.bluepowermod.container.ContainerProjectTable;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;

/**
 * @author MineMaarten
 */
public class SlotProjectTableCrafting extends SlotCrafting {
    
    /** The craft matrix inventory linked to this result slot. */
    private final InventoryCrafting craftMatrix;

    public SlotProjectTableCrafting(EntityPlayer player, InventoryCrafting craftMatrix, InventoryCraftResult res, int p_i1823_4_, int p_i1823_5_, int p_i1823_6_) {
    
        super(player, craftMatrix, res, p_i1823_4_, p_i1823_5_, p_i1823_6_);
        this.craftMatrix = craftMatrix;
    }
}
