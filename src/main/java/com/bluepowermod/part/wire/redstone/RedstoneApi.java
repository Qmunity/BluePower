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

package com.bluepowermod.part.wire.redstone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IPropagator;
import com.bluepowermod.api.redstone.IRedstoneApi;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneProvider;
import com.bluepowermod.part.wire.redstone.propagation.WirePropagator;

public class RedstoneApi implements IRedstoneApi {

    private static RedstoneApi INSTANCE = new RedstoneApi();

    private static Object returnDevice = new ReturnDevice();

    private RedstoneApi() {

    }

    public static RedstoneApi getInstance() {

        return INSTANCE;
    }

    private List<IRedstoneProvider> providers = new ArrayList<IRedstoneProvider>();

    @Override
    public byte[] getBundledOutput(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        byte[] output = new byte[16];

        for (IRedstoneProvider provider : providers) {
            byte[] out = provider.getBundledOutput(world, x, y, z, side, face);
            if (out == null)
                continue;
            for (int i = 0; i < output.length; i++)
                output[i] = (byte) Math.max(output[i], out[i]);
        }

        return output;
    }

    @Override
    public IRedstoneDevice getRedstoneDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        for (IRedstoneProvider provider : providers) {
            IRedstoneDevice device = provider.getRedstoneDevice(world, x, y, z, face, side);
            if (device != null) {
                if (device == returnDevice)
                    return null;
                return device;
            }
        }

        return null;
    }

    @Override
    public IBundledDevice getBundledDevice(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        for (IRedstoneProvider provider : providers) {
            IBundledDevice device = provider.getBundledDevice(world, x, y, z, face, side);
            if (device != null) {
                if (device == returnDevice)
                    return null;
                return device;
            }
        }

        return null;
    }

    @Override
    public void registerRedstoneProvider(IRedstoneProvider provider) {

        if (provider == null)
            return;
        if (providers.contains(provider))
            return;

        providers.add(provider);
    }

    @Override
    public IPropagator getPropagator() {

        return WirePropagator.INSTANCE;
    }

    @Override
    public Object getReturnDevice() {

        return returnDevice;
    }

}
