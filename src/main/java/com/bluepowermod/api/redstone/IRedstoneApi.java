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

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public interface IRedstoneApi {

    public byte[] getBundledOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

    public void registerRedstoneProvider(IRedstoneProvider provider);

    public IPropagator getPropagator();

    public boolean shouldWiresOutputPower();

    public void setWiresOutputPower(boolean shouldWiresOutputPower);

    public boolean shouldWiresHandleUpdates();

    public void setWiresHandleUpdates(boolean shouldWiresHandleUpdates);

}
