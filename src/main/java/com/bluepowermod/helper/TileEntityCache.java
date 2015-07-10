/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import uk.co.qmunity.lib.helper.BlockPos;

/**
 * 
 * @author MineMaarten
 */

public class TileEntityCache extends LocationCache<TileEntity> {

    @Deprecated
    public TileEntityCache(World world, int x, int y, int z) {

        this(world, new BlockPos(x, y, z));
    }

    public TileEntityCache(World world, BlockPos pos) {

        super(world, pos);
    }

    @Override
    protected TileEntity getNewValue(World world, int x, int y, int z, Object... extraArgs) {

        return world.getTileEntity(x, y, z);
    }

    @Override
    protected TileEntity getNewValue(World world, BlockPos pos, Object... extraArgs) {

        return world.getTileEntity(pos.getX(), pos.getY(), pos.getZ());
    }

}
