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

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.wire.redstone.*;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

;

public class RedstoneApi implements IRedstoneApi {

    private static RedstoneApi INSTANCE = new RedstoneApi();

    private RedstoneApi() {

    }

    public static RedstoneApi getInstance() {

        return INSTANCE;
    }

    private List<IRedstoneProvider> providers = new ArrayList<IRedstoneProvider>();
    private boolean shouldWiresOutputPower = true;
    private boolean shouldLossyWiresOutputPower = true;
    private boolean shouldWiresHandleUpdates = true;
    private DummyRedstoneDevice returnDevice = DummyRedstoneDevice.getDeviceAt(null, null, null);

    @Override
    public IRedstoneDevice getRedstoneDevice(World world, BlockPos pos, EnumFacing face, EnumFacing side) {

        boolean returned = false;
        for (IRedstoneProvider provider : providers) {
            if (returned && provider instanceof RedstoneProviderVanilla)
                continue;
            IRedstoneDevice device = provider.getRedstoneDeviceAt(world, pos, face, side);
            if (device == returnDevice) {
                returned = true;
                continue;
            }
            if (device != null)
                return device;
        }

        return null;
    }

    @Override
    public IBundledDevice getBundledDevice(World world, BlockPos pos, EnumFacing face, EnumFacing side) {

        for (IRedstoneProvider provider : providers) {
            IBundledDevice device = provider.getBundledDeviceAt(world, pos, face, side);
            if (device == returnDevice)
                return null;
            if (device != null)
                return device;
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
    public boolean shouldWiresOutputPower(boolean lossy) {

        return lossy ? shouldLossyWiresOutputPower : shouldWiresOutputPower;
    }

    @Override
    public void setWiresOutputPower(boolean shouldWiresOutputPower, boolean lossy) {

        if (lossy)
            shouldLossyWiresOutputPower = shouldWiresOutputPower;
        else
            this.shouldWiresOutputPower = shouldWiresOutputPower;
    }

    @Override
    public boolean shouldWiresHandleUpdates() {

        return shouldWiresHandleUpdates;
    }

    @Override
    public void setWiresHandleUpdates(boolean shouldWiresHandleUpdates) {

        this.shouldWiresHandleUpdates = shouldWiresHandleUpdates;
    }

    public IRedstoneDevice getReturnDevice() {

        return returnDevice;
    }

    @Override
    public RedstoneConnection createConnection(IRedstoneDevice a, IRedstoneDevice b, EnumFacing sideA, EnumFacing sideB,
            ConnectionType type) {

        if (a == null || b == null || sideA == null || sideB == null || type == null || a == b)
            return null;

        return new RedstoneConnection(a, b, sideA, sideB, type);
    }

    @Override
    public BundledConnection createConnection(IBundledDevice a, IBundledDevice b, EnumFacing sideA, EnumFacing sideB,
            ConnectionType type) {

        if (a == null || b == null || sideA == null || sideB == null || type == null || a == b)
            return null;

        return new BundledConnection(a, b, sideA, sideB, type);
    }

    @Override
    public RedstoneConnectionCache createRedstoneConnectionCache(IRedstoneDevice dev) {

        return new RedstoneConnectionCache(dev);
    }

    @Override
    public BundledConnectionCache createBundledConnectionCache(IBundledDevice dev) {

        return new BundledConnectionCache(dev);
    }

    @Override
    public IPropagator<IRedstoneDevice> getRedstonePropagator(IRedstoneDevice device, EnumFacing side) {

        return new RedstonePropagator.RedPropagator(device, side);
    }

    @Override
    public IPropagator<IBundledDevice> getBundledPropagator(IBundledDevice device, EnumFacing side) {

        return new BundledPropagator(device, side);
    }

}
