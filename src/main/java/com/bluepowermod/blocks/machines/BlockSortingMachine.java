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

package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier2.TileSortingMachine;

/**
 * 
 * @author MineMaarten
 */

public class BlockSortingMachine extends BlockContainerBase {
    
    public BlockSortingMachine() {
    
        super(Material.rock);
        setBlockName(Refs.SORTING_MACHINE_NAME);
    }
    
    @Override
    protected Class<? extends TileEntity> getTileEntity() {
    
        return TileSortingMachine.class;
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.SORTING_MACHINE;
    }
}
