/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockBase;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileWindmill;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.block.RenderShape;
import net.minecraft.util.math.AABB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockGetter;
import net.minecraft.world.World;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.EntityBlock;

/**
 * 
 * @author TheFjong
 * 
 */
public class BlockWindmill extends BlockContainerBase implements EntityBlock {
    
    public BlockWindmill() {
        super(Material.METAL, TileWindmill.class);
        setRegistryName(Refs.MODID, Refs.WINDMILL_NAME);
        BPBlocks.blockList.add(this);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockGetter iBlockReader) {
        return new TileWindmill();
    }
}
