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
 *     
 *     @author Quetzi
 */

package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.tube.IPneumaticTube.TubeColor;
import com.bluepowermod.helper.IOHelper;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class TileTransposer extends TileMachineBase {

    public TileTransposer(BlockPos pos, BlockState state) {
        super(BPBlockEntityType.TRANSPOSER, pos, state);
    }

    public static void tickTransposer(Level level, BlockPos pos, BlockState state, TileTransposer tileTransposer) {
        TileBase.tickTileBase(level, pos, state, tileTransposer);
        if (!level.isClientSide) {
            tileTransposer.suckEntity();
        }

    }

    @Override
    protected void redstoneChanged(boolean newValue) {

        super.redstoneChanged(newValue);
        Direction direction = getFacingDirection();

        if (!level.isClientSide && newValue) {
            if (level.isEmptyBlock(worldPosition.relative(direction))) {
                suckItems();
            } else {
                pullItem();
            }
        }

    }

    protected void pullItem() {

        Direction dir = getOutputDirection().getOpposite();
        BlockEntity inputTE = getTileCache(dir);
        ItemStack extractedStack = IOHelper.extractOneItem(inputTE, dir.getOpposite());
        if (!extractedStack.isEmpty())
            addItemToOutputBuffer(extractedStack);
    }

    private static AABB[] ITEM_SUCK_AABBS;
    static {
        ITEM_SUCK_AABBS = new AABB[6];
        ITEM_SUCK_AABBS[0] = new AABB(-1, -1, -1, 2, 0, 2);
        ITEM_SUCK_AABBS[1] = new AABB(-1, 1, -1, 2, 2, 2);
        ITEM_SUCK_AABBS[2] = new AABB(-1, -1, -1, 2, 2, 0);
        ITEM_SUCK_AABBS[3] = new AABB(-1, -1, 1, 2, 2, 2);
        ITEM_SUCK_AABBS[4] = new AABB(-1, -1, -1, 0, 2, 2);
        ITEM_SUCK_AABBS[5] = new AABB(1, -1, -1, 2, 2, 2);
    }

    private void suckItems() {

        for (ItemEntity entity : (List<ItemEntity>) level.getEntitiesOfClass(ItemEntity.class, ITEM_SUCK_AABBS[getFacingDirection().ordinal()].move(worldPosition))) {
            ItemStack stack = entity.getItem();
            if (isItemAccepted(stack) && entity.isAlive()) {
                addItemToOutputBuffer(stack, getAcceptedItemColor(stack));
                entity.remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    private void suckEntity() {

        Direction direction = getFacingDirection();
        AABB box = new AABB(worldPosition.getX() + direction.getStepX(), worldPosition.getY() + direction.getStepY(), worldPosition.getZ() + direction.getStepZ(), worldPosition.getX()
                + direction.getStepX() + 1, worldPosition.getY() + direction.getStepY() + 1, worldPosition.getZ() + direction.getStepZ() + 1);
        for (ItemEntity entity : (List<ItemEntity>) level.getEntitiesOfClass(ItemEntity.class, box)) {
            ItemStack stack = entity.getItem();
            if (isItemAccepted(stack) && entity.isAlive()) {
                addItemToOutputBuffer(stack, getAcceptedItemColor(stack));
                entity.remove(Entity.RemovalReason.KILLED);
            }
        }
    }

    protected boolean isItemAccepted(ItemStack item) {

        return true;
    }

    protected TubeColor getAcceptedItemColor(ItemStack item) {

        return TubeColor.NONE;
    }

    @Override
    public boolean canConnectRedstone() {

        return true;
    }
}
