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

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import uk.co.qmunity.lib.helper.BlockPos;

public class ReturnDevice implements IRedstoneDevice, IBundledDevice {

    private RedstoneConnectionCache redstoneConnections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    private BundledConnectionCache bundledConnections = RedstoneApi.getInstance().createBundledConnectionCache(this);

    @Override
    public World getWorld() {

        return null;
    }

    @Override
    public int getX() {

        return 0;
    }

    @Override
    public int getY() {

        return 0;
    }

    @Override
    public int getZ() {

        return 0;
    }

    @Override
    public BlockPos getPos() {

        return new BlockPos(0, 0, 0);
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
    public boolean canConnect(ForgeDirection side, IRedstoneDevice dev, ConnectionType type) {

        return false;
    }

    @Override
    public boolean canConnect(ForgeDirection side, IBundledDevice dev, ConnectionType type) {

        return false;
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        return 0;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public byte[] getBundledOutput(ForgeDirection side) {

        return null;
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        return null;
    }

    @Override
    public void onBundledUpdate() {

    }

    @Override
    public MinecraftColor getBundledColor(ForgeDirection side) {

        return null;
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return false;
    }

}
