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

package com.bluepowermod.block.worldgen;

import com.bluepowermod.block.BlockBase;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;

public class BlockItemOre extends BlockBase {

    protected Random rand = new Random();

    public BlockItemOre(String type) {
        super(Properties.of(Material.STONE).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.STONE));
        this.setRegistryName(Refs.MODID + ":" + type);
    }

    @Override
    public int getExpDrop(BlockState state, net.minecraft.world.IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        return silktouch == 0 ? 1 + MathHelper.nextInt(rand, 3, 7) : 0;
    }

}
