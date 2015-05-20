package com.bluepowermod.power;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.power.IPowerApi;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.api.power.IPoweredDeviceProvider;

public class PowerApi implements IPowerApi {

    private static final PowerApi instance = new PowerApi();

    public static IPowerApi getInstance() {

        return instance;
    }

    private List<IPoweredDeviceProvider> providers = new ArrayList<IPoweredDeviceProvider>();

    @Override
    public IPowered getPoweredDeviceAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

        for (IPoweredDeviceProvider provider : providers) {
            IPowered device = provider.getPoweredDeviceAt(world, x, y, z, face, side);
            if (device != null)
                return device;
        }

        return null;
    }

    @Override
    public void registerPoweredDeviceProvider(IPoweredDeviceProvider provider) {

        if (provider == null)
            return;
        if (providers.contains(provider))
            return;

        providers.add(provider);
    }

    @Override
    public IPowerBase createPowerHandler(IPowered device) {

        return new PowerHandler(device);
    }

}
