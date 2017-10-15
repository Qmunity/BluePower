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

package com.bluepowermod.tile.tier1;

import com.bluepowermod.block.machine.BlockIgniter;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;;

import com.bluepowermod.tile.IEjectAnimator;
import com.bluepowermod.tile.TileBase;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.bluepowermod.block.machine.BlockIgniter.ACTIVE;
import static com.bluepowermod.block.machine.BlockIgniter.FACING;

public class TileIgniter extends TileBase implements IEjectAnimator {

    @Override
    protected void redstoneChanged(boolean newValue) {
        super.redstoneChanged(newValue);
        BlockIgniter.setState(newValue, world, pos);
        sendUpdatePacket();
        if (getIsRedstonePowered()) {
            ignite();
        } else {
            extinguish();
        }
    }

    private void ignite() {
        EnumFacing facing = world.getBlockState(pos).getValue(FACING);
        if (world.isBlockIndirectlyGettingPowered(pos) > 0 && world.isAirBlock(pos.offset(facing)) && Blocks.FIRE.canPlaceBlockAt(world, pos.offset(facing))) {
            world.setBlockState(pos.offset(facing), Blocks.FIRE.getDefaultState());
        }
    }

    private void extinguish() {
        EnumFacing facing = world.getBlockState(pos).getValue(FACING);
        Block target = world.getBlockState(pos.offset(facing)).getBlock();
        if (world.isBlockIndirectlyGettingPowered(pos) == 0 && (target == Blocks.FIRE || target == Blocks.PORTAL)) {
            world.setBlockToAir(pos.offset(facing));
        }
    }


    @Override
    public void update() {

        if (getTicker() % 5 == 0) {
            ignite();
        }
        super.update();
    }

    @Override
    public boolean isEjecting() {

        return world.getBlockState(pos).getValue(ACTIVE);
    }
}
