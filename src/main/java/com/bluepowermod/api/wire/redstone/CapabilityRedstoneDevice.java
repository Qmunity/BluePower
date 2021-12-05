package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

/**
 * @author MoreThanHidden
 */
public class CapabilityRedstoneDevice {

    public static Capability<IInsulatedRedstoneDevice> INSULATED_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static Capability<IRedstoneDevice> UNINSULATED_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IInsulatedRedstoneDevice.class);
        event.register(IRedstoneDevice.class);
    }

}