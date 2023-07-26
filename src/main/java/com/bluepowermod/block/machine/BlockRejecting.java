/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.tile.TileBase;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;


/**
 * @author MineMaarten
 */
public class BlockRejecting extends BlockContainerBase {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty REJECTING = BooleanProperty.create("rejecting");

    public BlockRejecting(Class<? extends TileBase> tileEntityClass) {
        super(tileEntityClass);
        registerDefaultState(defaultBlockState().setValue(ACTIVE, false).setValue(POWERED, false).setValue(REJECTING, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder){
        builder.add(ACTIVE, POWERED, REJECTING);
    }
}
