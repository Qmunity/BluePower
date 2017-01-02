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

package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.tier1.TileProjectTable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;


public class BlockProjectTable extends BlockContainerBase {

    public BlockProjectTable() {

        super(Material.WOOD, TileProjectTable.class);
        setUnlocalizedName(Refs.PROJECTTABLE_NAME);
        setRegistryName(Refs.MODID, Refs.PROJECTTABLE_NAME);
    }

    public BlockProjectTable(Class<? extends TileBase> tileClass) {

        super(Material.WOOD, tileClass);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return true;
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }

}
