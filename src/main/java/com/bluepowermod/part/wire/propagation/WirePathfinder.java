package com.bluepowermod.part.wire.propagation;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;

public class WirePathfinder {

    public static List<IRedstoneDevice> pathfind(IRedstoneConductor from, ForgeDirection side) {

        List<IRedstoneDevice> result = new ArrayList<IRedstoneDevice>();

        List<IRedstoneDevice> current = new ArrayList<IRedstoneDevice>();
        current.addAll(from.propagate(side));

        List<IRedstoneDevice> newDevices = new ArrayList<IRedstoneDevice>();

        while (current.size() > 0) {
            for (IRedstoneDevice d : current) {
                if (d instanceof IRedstoneConductor) {

                }
            }
        }

        return result;
    }

}
