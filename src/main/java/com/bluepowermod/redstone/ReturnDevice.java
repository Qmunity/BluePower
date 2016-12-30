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

package com.bluepowermod.redstone;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;

public class ReturnDevice implements IRedstoneDevice, IBundledDevice {

    private RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    private BundledConnectionCache bundledConnections = RedstoneApi.getInstance().createBundledConnectionCache(this);

    @Override
    public World getWorld() {

        return null;
    }

    @Override
    public BlockPos getPos() {
        return new BlockPos(0,0,0);
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {

        return redstoneConnections;
    }

    @Override
    public IConnectionCache<? extends IBundledDevice> getBundledConnectionCache() {

        return bundledConnections;
    }

    @Override
    public boolean canConnect(EnumFacing side, IRedstoneDevice dev, ConnectionType type) {

        return false;
    }

    @Override
    public boolean canConnect(EnumFacing side, IBundledDevice dev, ConnectionType type) {

        return false;
    }

    @Override
    public byte getRedstonePower(EnumFacing side) {

        return 0;
    }

    @Override
    public void setRedstonePower(EnumFacing side, byte power) {

    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public byte[] getBundledOutput(EnumFacing side) {

        return null;
    }

    @Override
    public void setBundledPower(EnumFacing side, byte[] power) {

    }

    @Override
    public byte[] getBundledPower(EnumFacing side) {

        return null;
    }

    @Override
    public void onBundledUpdate() {

    }

    @Override
    public MinecraftColor getBundledColor(EnumFacing side) {

        return null;
    }

    @Override
    public boolean isNormalFace(EnumFacing side) {

        return false;
    }

}
