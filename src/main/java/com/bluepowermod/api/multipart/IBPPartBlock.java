/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.multipart;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;

import javax.annotation.Nullable;

/**
 * Blocks that can be used as part of a multipart should implement this.
 * @author MoreThanHidden
 */
public interface IBPPartBlock {

    /**
     * If this Part should block a given capability returns true.
     * For example covers block Blutricity, Redstone and Items where as hollow covers allow Items.
     * @param state
     * @param capability
     * @param side
     */
    default Boolean blockCapability (BlockState state, BlockCapability capability, @Nullable Direction side){
        return false;
    }

    /**
     * Return the occluding area given the BlockState, where other parts cannot be placed.
     * @param state
     */
    VoxelShape getOcclusionShape (BlockState state);

    /**
     * Separate onRemove for Multipart so that the BlockEntity isn't removed.
     * @param state
     * @param worldIn
     * @param pos
     * @param newState
     */
    default void onMultipartReplaced(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving){
    }

}
