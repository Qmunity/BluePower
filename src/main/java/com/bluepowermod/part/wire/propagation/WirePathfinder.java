package com.bluepowermod.part.wire.propagation;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.part.wire.WireHelper;

public class WirePathfinder {

    public static List<Pair<IRedstoneDevice, ForgeDirection>> pathfind(IRedstoneConductor from, ForgeDirection side) {

        List<Pair<IRedstoneDevice, ForgeDirection>> result = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();
        result.add(new Pair<IRedstoneDevice, ForgeDirection>(from, side));

        List<Pair<IRedstoneDevice, ForgeDirection>> current = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();
        current.addAll(from.propagate(side));
        correctDirections(current, from);

        List<Pair<IRedstoneDevice, ForgeDirection>> newDevices = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();

        while (current.size() > 0) {
            List<Pair<IRedstoneDevice, ForgeDirection>> tmp = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();
            for (Pair<IRedstoneDevice, ForgeDirection> pair : current) {
                IRedstoneDevice device = pair.getKey();
                ForgeDirection fromDirection = pair.getValue();

                if (device instanceof IRedstoneConductor) {
                    tmp.addAll(((IRedstoneConductor) device).propagate(fromDirection));
                    for (Pair<IRedstoneDevice, ForgeDirection> p : tmp) {
                        Pair<IRedstoneDevice, ForgeDirection> p2 = new Pair<IRedstoneDevice, ForgeDirection>(device, p.getValue());
                        if (!result.contains(p2))
                            result.add(p2);
                    }
                    correctDirections(tmp, device);
                    newDevices.addAll(tmp);
                }

                tmp.clear();
            }

            result.addAll(current);
            current.clear();

            for (Pair<IRedstoneDevice, ForgeDirection> pair : newDevices)
                if (!result.contains(pair))
                    current.add(pair);

            newDevices.clear();
        }

        return result;
    }

    private static void correctDirections(List<Pair<IRedstoneDevice, ForgeDirection>> list, IRedstoneDevice parent) {

        for (Pair<IRedstoneDevice, ForgeDirection> pair : list)
            pair.setValue(WireHelper.getConnectionSide(pair.getKey(), parent));
    }
}
