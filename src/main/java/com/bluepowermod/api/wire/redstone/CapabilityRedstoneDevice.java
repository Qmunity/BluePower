package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

/**
 * @author MoreThanHidden
 */
public class CapabilityRedstoneDevice {

    @CapabilityInject(IInsulatedRedstoneDevice.class)
    public static Capability<IInsulatedRedstoneDevice> INSULATED_CAPABILITY = null;

    @CapabilityInject(IRedstoneDevice.class)
    public static Capability<IRedstoneDevice> UNINSULATED_CAPABILITY = null;

    public static void register() {
        CapabilityManager.INSTANCE.register(IInsulatedRedstoneDevice.class);
        CapabilityManager.INSTANCE.register(IRedstoneDevice.class);
    }

}