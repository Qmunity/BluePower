/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.multipart;

import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.common.capabilities.Capability;

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
    default Boolean blockCapability (BlockState state, Capability capability, @Nullable Direction side){
        return false;
    }

    /**
     * Return the occluding area given the BlockState
     * @param state
     */
    VoxelShape getOcclusionShape (BlockState state);

}
