package com.bluepowermod.part.wire;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IPropagator;
import com.bluepowermod.api.redstone.IRedstoneApi;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneProvider;
import com.bluepowermod.part.wire.propagation.WirePropagator;

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
