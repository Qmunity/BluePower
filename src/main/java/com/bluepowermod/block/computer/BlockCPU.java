/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.computer;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileCPU;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class BlockCPU extends BlockContainerBase {


    public BlockCPU() {

        super(Material.IRON, TileCPU.class);
        setRegistryName(Refs.BLOCKCPU_NAME);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileCPU) {
        }
    }

    @Override
    public GuiIDs getGuiID() {
        return GuiIDs.CPU;
    }

    @Override
    public int tickRate(World world) {
        return 1;
    }

}
