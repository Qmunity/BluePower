/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.tier2.TileWindmill;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 
 * @author TheFjong
 * 
 */
public class BlockWindmill extends BlockContainerBase implements EntityBlock {
    
    public BlockWindmill() {
        super(TileWindmill.class);
        BPBlocks.blockList.add(this);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TileWindmill(pos, state);
    }
}
