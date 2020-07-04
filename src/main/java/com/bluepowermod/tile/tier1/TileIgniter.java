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
import com.bluepowermod.tile.BPTileEntityType;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

import com.bluepowermod.tile.IEjectAnimator;
import com.bluepowermod.tile.TileBase;
import net.minecraft.world.World;

import static com.bluepowermod.block.machine.BlockIgniter.ACTIVE;
import static com.bluepowermod.block.machine.BlockIgniter.FACING;

public class TileIgniter extends TileBase implements IEjectAnimator {

    public TileIgniter() {
        super(BPTileEntityType.IGNITER);
    }

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
        Direction facing = getBlockState().get(FACING);
        if (world.getRedstonePowerFromNeighbors(pos) > 0 && world.isAirBlock(pos.offset(facing)) && world.getBlockState(pos.offset(facing)).isAir()) {
            world.setBlockState(pos.offset(facing), Blocks.FIRE.getDefaultState());
        }
    }

    private void extinguish() {
        Direction facing = getBlockState().get(FACING);
        Block target = world.getBlockState(pos.offset(facing)).getBlock();
        if (world.getRedstonePowerFromNeighbors(pos) == 0 && (target == Blocks.FIRE || target == Blocks.NETHER_PORTAL)) {
            world.setBlockState(pos.offset(facing), Blocks.AIR.getDefaultState());
        }
    }


    @Override
    public void tick() {

        if (getTicker() % 5 == 0) {
            ignite();
        }
        super.tick();
    }

    @Override
    public boolean isEjecting() {

        return getBlockState().get(ACTIVE);
    }
}
