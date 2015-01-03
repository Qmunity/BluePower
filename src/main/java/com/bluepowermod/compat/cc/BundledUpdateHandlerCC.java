package com.bluepowermod.compat.cc;

import java.util.Arrays;
import java.util.EnumSet;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IBundledUpdateHandler;

public class BundledUpdateHandlerCC implements IBundledUpdateHandler {

    @Override
    public boolean shouldPropagateOnBlockUpdate(IBundledDevice device) {

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            IBundledDevice d = device.getBundledDeviceOnSide(side);
            if (d != null && d instanceof BundledDeviceCCComputer)
                return true;
        }

        return false;
    }

    @Override
    public EnumSet<MinecraftColor> getColorsToUpdate(IBundledDevice device) {

        return EnumSet.copyOf(Arrays.asList(MinecraftColor.VALID_COLORS));
    }

}
