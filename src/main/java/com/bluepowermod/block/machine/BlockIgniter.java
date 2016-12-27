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
import com.bluepowermod.tile.IRotatable;
import com.bluepowermod.tile.tier1.TileIgniter;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

;

public class BlockIgniter extends BlockContainerBase {
    
    public BlockIgniter() {
    
        super(Material.ROCK, TileIgniter.class);
        setRegistryName(Refs.BLOCKIGNITER_NAME);
    }

    @Override
    public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
        TileIgniter tile = (TileIgniter) world.getTileEntity(pos);
        boolean orientation = tile.getFacingDirection() == EnumFacing.UP;
        return orientation && tile.getIsRedstonePowered();
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, @Nullable EnumFacing side) {
        return true;
    }

    @Override
    public boolean rotateBlock(World worldObj, BlockPos pos, EnumFacing orDir) {
        TileEntity te = worldObj.getTileEntity(pos);
        if (te instanceof IRotatable) {
            IRotatable rotatable = (IRotatable) te;
            EnumFacing dir = rotatable.getFacingDirection();
            Block target = worldObj.getBlockState(pos.offset(dir)).getBlock();
            if (target == Blocks.FIRE || target == Blocks.PORTAL) {
                worldObj.setBlockToAir(pos.offset(dir));
            }
            dir = orDir;
            if (dir != EnumFacing.UP && dir != EnumFacing.DOWN || canRotateVertical()) {
                rotatable.setFacingDirection(dir);
                return true;
            }
        }
        return false;
    }
}
