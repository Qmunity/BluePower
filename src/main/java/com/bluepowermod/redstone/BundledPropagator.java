package com.bluepowermod.redstone;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IPropagator;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire.IInsulatedRedwire;
import net.minecraft.core.Direction;

public class BundledPropagator implements IPropagator<IBundledDevice> {

    private IBundledDevice device;
    private Direction side;

    public BundledPropagator(IBundledDevice device, Direction side) {

        this.device = device;
        this.side = side;
    }

    @Override
    public void propagate() {

        if ((device instanceof IBPPartBlock) || device.getLevel() == null)
            return;

        for (MinecraftColor c : MinecraftColor.VALID_COLORS) {
            IRedstoneDevice dev;
            if (device instanceof IInsulatedRedwire) {
                dev = (IRedstoneDevice) device;
            } else {
                dev = BundledDeviceWrapper.wrap(device, c);
            }
            if (dev == null)
                continue;
            RedstoneApi.getInstance().getRedstonePropagator(dev, side).propagate();
        }
    }

}