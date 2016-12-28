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
import com.bluepowermod.part.tube.TubeStack;
import com.bluepowermod.tile.TileMachineBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

;

public class TileTransposer extends TileMachineBase {

    @Override
    public void update() {

        super.update();
        if (isBufferEmpty() && !world.isRemote) {
            suckEntity();
        }

    }

    @Override
    protected void redstoneChanged(boolean newValue) {

        super.redstoneChanged(newValue);
        EnumFacing direction = getFacingDirection();

        if (!world.isRemote && isBufferEmpty() && newValue) {
            if (world.isAirBlock(pos.offset(direction))) {
                suckItems();
            } else {
                pullItem();
            }
        }

    }

    @Override
    public TubeStack acceptItemFromTube(TubeStack stack, EnumFacing from, boolean simulate) {

        if (from == getFacingDirection() && getIsRedstonePowered())
            return stack;
        return super.acceptItemFromTube(stack, from, simulate);

    }

    protected void pullItem() {

        EnumFacing dir = getOutputDirection().getOpposite();
        TileEntity inputTE = getTileCache(dir);
        ItemStack extractedStack = IOHelper.extractOneItem(inputTE, dir.getOpposite());
        if (extractedStack != null)
            addItemToOutputBuffer(extractedStack);
    }

    private static AxisAlignedBB[] ITEM_SUCK_AABBS;
    static {
        ITEM_SUCK_AABBS = new AxisAlignedBB[6];
        ITEM_SUCK_AABBS[0] = new AxisAlignedBB(-1, -1, -1, 2, 0, 2);
        ITEM_SUCK_AABBS[1] = new AxisAlignedBB(-1, 1, -1, 2, 2, 2);
        ITEM_SUCK_AABBS[2] = new AxisAlignedBB(-1, -1, -1, 2, 2, 0);
        ITEM_SUCK_AABBS[3] = new AxisAlignedBB(-1, -1, 1, 2, 2, 2);
        ITEM_SUCK_AABBS[4] = new AxisAlignedBB(-1, -1, -1, 0, 2, 2);
        ITEM_SUCK_AABBS[5] = new AxisAlignedBB(1, -1, -1, 2, 2, 2);
    }

    private void suckItems() {

        for (EntityItem entity : (List<EntityItem>) world.getEntitiesWithinAABB(EntityItem.class, ITEM_SUCK_AABBS[getFacingDirection().ordinal()].offset(pos))) {
            ItemStack stack = entity.getEntityItem();
            if (isItemAccepted(stack) && !entity.isDead) {
                addItemToOutputBuffer(stack, getAcceptedItemColor(stack));
                entity.setDead();
            }
        }
    }

    private void suckEntity() {

        EnumFacing direction = getFacingDirection();
        AxisAlignedBB box = new AxisAlignedBB(pos.getX() + direction.getFrontOffsetX(), pos.getY() + direction.getFrontOffsetY(), pos.getZ() + direction.getFrontOffsetZ(), pos.getX()
                + direction.getFrontOffsetX() + 1, pos.getY() + direction.getFrontOffsetY() + 1, pos.getZ() + direction.getFrontOffsetZ() + 1);
        for (EntityItem entity : (List<EntityItem>) world.getEntitiesWithinAABB(EntityItem.class, box)) {
            ItemStack stack = entity.getEntityItem();
            if (isItemAccepted(stack) && !entity.isDead) {
                addItemToOutputBuffer(stack, getAcceptedItemColor(stack));
                entity.setDead();
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
