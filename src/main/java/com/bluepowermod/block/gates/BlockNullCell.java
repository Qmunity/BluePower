/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.block.gates;

import com.bluepowermod.helper.DirectionHelper;
import com.bluepowermod.tile.tier1.TileGate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockNullCell extends BlockGateLogic {

    private boolean shouldSignal = true;

    public BlockNullCell() {
        setWIP(true);
    }

    enum Modes {
        STRAIGHT,
        LEFT_FRONT,
        RIGHT_FRONT,
        BOTH_FRONT,
        CROSSED
    }

    public Map<Side, Byte> getSidePower(BlockState state, TileGate gate){
        Map<Side, Byte> map = new HashMap<>();
        Direction[] dirs = DirectionHelper.ArrayFromDirection(state.getValue(FACING));

        Direction side_left = dirs[rotate(state.getValue(ROTATION), 1)];
        Direction side_right = dirs[rotate(state.getValue(ROTATION), 3)];
        Direction side_back = dirs[state.getValue(ROTATION)];
        Direction side_front = dirs[rotate(state.getValue(ROTATION), 2)];

        BlockPos pos_left = gate.getBlockPos().relative(side_left);
        BlockPos pos_right = gate.getBlockPos().relative(side_right);
        BlockPos pos_back = gate.getBlockPos().relative(side_back);
        BlockPos pos_front = gate.getBlockPos().relative(side_front);

        shouldSignal = false;

        byte left = gate.isPowered(Side.LEFT) ?  0 : (byte) gate.getLevel().getSignal(pos_left, side_right);
        byte right = gate.isPowered(Side.RIGHT) ?  0 : (byte) gate.getLevel().getSignal(pos_right, side_left);
        byte back = gate.isPowered(Side.BACK) ?  0 : (byte) gate.getLevel().getSignal(pos_back, side_front);
        byte front = gate.isPowered(Side.FRONT) ?  0 : (byte) gate.getLevel().getSignal(pos_front, side_back);

        shouldSignal = true;

        byte outLeft = computeRedstone(Side.LEFT, back, front, left, right);
        byte outRight = computeRedstone(Side.RIGHT, back, front, left, right);
        byte outBack = computeRedstone(Side.BACK, back, front, left, right);
        byte outFront = computeRedstone(Side.FRONT, back, front, left, right);

        map.put(Side.LEFT, outBack > 0 ? 0 : outLeft);
        map.put(Side.RIGHT, outFront > 0 ? 0 : outRight);
        map.put(Side.BACK, outLeft > 0 ? 0 : outBack);
        map.put(Side.FRONT, outRight > 0 ? 0 : outFront);

        return map;
    }

    public byte computeRedstone(Side side, byte back, byte front, byte left, byte right){
        return switch (side) {
            case FRONT -> right;
            case BACK -> left;
            case LEFT -> back;
            case RIGHT -> front;
        };
    }

    public boolean isSignalSource(BlockState pState) {
        return this.shouldSignal;
    }

    @Override
    public int getDirectSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side) {
        return getSignal(blockState, blockAccess, pos, side);
    }

    @Override
    public int getSignal(BlockState blockState, BlockGetter blockAccess, BlockPos pos, Direction side){
        TileGate gate = getGateTile(blockState, blockAccess.getBlockEntity(pos));
        if (gate == null) return 0;
        Direction[] dirs = DirectionHelper.ArrayFromDirection(blockState.getValue(FACING));

        Direction side_left = dirs[rotate(blockState.getValue(ROTATION), 1)];
        Direction side_right = dirs[rotate(blockState.getValue(ROTATION), 3)];
        Direction side_back = dirs[blockState.getValue(ROTATION)];
        Direction side_front = dirs[rotate(blockState.getValue(ROTATION), 2)];

        if(side == side_back) {
            return gate.isPowered(Side.FRONT) ? 16 : 0;
        }else if(side == side_front){
            return gate.isPowered(Side.BACK) ? 16 : 0;
        }else if(side == side_left){
            return gate.isPowered(Side.RIGHT) ? 16 : 0;
        }else if(side == side_right){
            return gate.isPowered(Side.LEFT) ? 16 : 0;
        }
        return 0;
    }
}
