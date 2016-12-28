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

package com.bluepowermod.tile.tier1;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;;

import com.bluepowermod.tile.IEjectAnimator;
import com.bluepowermod.tile.TileBase;

public class TileIgniter extends TileBase implements IEjectAnimator {

    private boolean isActive;

    @Override
    protected void redstoneChanged(boolean newValue) {

        super.redstoneChanged(newValue);
        isActive = newValue;
        sendUpdatePacket();
        EnumFacing direction = getFacingDirection();
        if (getIsRedstonePowered()) {
            ignite(direction);
        } else {
            extinguish(direction);
        }
    }

    @Override
    public void writeToPacketNBT(NBTTagCompound tCompound) {

        super.writeToPacketNBT(tCompound);
        tCompound.setBoolean("isActive", isActive);
    }

    @Override
    public void readFromPacketNBT(NBTTagCompound tCompound) {

        super.readFromPacketNBT(tCompound);
        isActive = tCompound.getBoolean("isActive");
        if (world != null)
            markForRenderUpdate();
    }

    private void ignite(EnumFacing direction) {

        if (getIsRedstonePowered() && world.isAirBlock(pos.offset(direction))) {
            world.setBlockState(pos.offset(direction), Blocks.FIRE.getDefaultState());
        }
    }

    private void extinguish(EnumFacing direction) {

        Block target = world.getBlockState(pos.offset(direction)).getBlock();
        if (!getIsRedstonePowered() && (target == Blocks.FIRE || target == Blocks.PORTAL)) {
            world.setBlockToAir(pos.offset(direction));
        }
    }

    @Override
    public void update() {

        if (getTicker() % 5 == 0) {
            ignite(getFacingDirection());
        }
        super.update();
    }

    @Override
    public boolean isEjecting() {

        return isActive;
    }

    @Override
    public boolean canConnectRedstone() {

        return true;
    }
}
