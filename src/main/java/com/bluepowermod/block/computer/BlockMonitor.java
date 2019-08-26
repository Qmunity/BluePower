/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.computer;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileMonitor;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class BlockMonitor extends BlockContainerBase {

    public BlockMonitor() {

        super(Material.IRON, TileMonitor.class);
        setRegistryName(Refs.BLOCKMONITOR_NAME);
    }

    @Override
    public void tick(BlockState state, World world, BlockPos pos, Random rand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileMonitor) {
            // ((TileCPU)tileEntity).updateEntity();
            // Logs.log(Level.INFO, "[BluePowerControl] CPU TE ticked");
        }
    }

    @Override
    public int tickRate(IWorldReader p_149738_1_) {
        return 1;
    }

}
