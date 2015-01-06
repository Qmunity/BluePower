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

package com.bluepowermod.api.redstone;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.misc.MinecraftColor;

public interface IRedstoneDevice extends IWorldLocation {

    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device);

    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device);

    public void onConnect(ForgeDirection side, IRedstoneDevice device);

    public void onDisconnect(ForgeDirection side);

    public IRedstoneDevice getDeviceOnSide(ForgeDirection side);

    public byte getRedstonePower(ForgeDirection side);

    public void setRedstonePower(ForgeDirection side, byte power);

    public void onRedstoneUpdate();

    public MinecraftColor getInsulationColor();

    public boolean isNormalBlock();

}
