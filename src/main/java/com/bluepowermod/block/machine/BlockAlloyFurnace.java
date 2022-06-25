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

import com.bluepowermod.block.BlockContainerHorizontalFacingBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.tier1.TileAlloyFurnace;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockAlloyFurnace extends BlockContainerHorizontalFacingBase {

    public BlockAlloyFurnace() {
        super(Material.STONE, TileAlloyFurnace.class, BPBlockEntityType.ALLOY_FURNACE);
        setRegistryName(Refs.MODID, Refs.ALLOYFURNACE_NAME);
    }


    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : TileAlloyFurnace::tickAlloyFurnace;
    }

    @Override
    public void tick(BlockState stateIn, ServerLevel world, BlockPos pos, Random rnd) {
        if (stateIn.getValue(ACTIVE)) {
            int l = stateIn.getValue(FACING).ordinal();
            float f = pos.getX() + 0.5F;
            float f1 = pos.getY() + 0.0F + rnd.nextFloat() * 6.0F / 16.0F;
            float f2 = pos.getZ() + 0.5F;
            float f3 = 0.52F;
            float f4 = rnd.nextFloat() * 0.6F - 0.3F;

            if (l == 4) {
                world.addParticle(ParticleTypes.SMOKE, f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, f - f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 5) {
                world.addParticle(ParticleTypes.SMOKE, f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, f + f3, f1, f2 + f4, 0.0D, 0.0D, 0.0D);
            } else if (l == 2) {
                world.addParticle(ParticleTypes.SMOKE, f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, f + f4, f1, f2 - f3, 0.0D, 0.0D, 0.0D);
            } else if (l == 3) {
                world.addParticle(ParticleTypes.SMOKE, f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
                world.addParticle(ParticleTypes.FLAME, f + f4, f1, f2 + f3, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
        return state.getValue(ACTIVE) ? 13 : 0;
    }

    @Override
    protected boolean canRotateVertical() {

        return false;
    }
}
