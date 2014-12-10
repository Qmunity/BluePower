package com.bluepowermod.part.wire.redstone.propagation;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.part.wire.redstone.WireHelper;

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

    public static List<Pair<IBundledDevice, ForgeDirection>> pathfind(IBundledConductor from, ForgeDirection side) {

        List<Pair<IBundledDevice, ForgeDirection>> result = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();
        result.add(new Pair<IBundledDevice, ForgeDirection>(from, side));

        List<Pair<IBundledDevice, ForgeDirection>> current = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();
        current.addAll(from.propagateBundled(side));
        correctDirections(current, from);

        List<Pair<IBundledDevice, ForgeDirection>> newDevices = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();

        while (current.size() > 0) {
            List<Pair<IBundledDevice, ForgeDirection>> tmp = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();
            for (Pair<IBundledDevice, ForgeDirection> pair : current) {
                IBundledDevice device = pair.getKey();
                ForgeDirection fromDirection = pair.getValue();

                if (device instanceof IBundledConductor) {
                    tmp.addAll(((IBundledConductor) device).propagateBundled(fromDirection));
                    for (Pair<IBundledDevice, ForgeDirection> p : tmp) {
                        Pair<IBundledDevice, ForgeDirection> p2 = new Pair<IBundledDevice, ForgeDirection>(device, p.getValue());
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

            for (Pair<IBundledDevice, ForgeDirection> pair : newDevices)
                if (!result.contains(pair))
                    current.add(pair);

            newDevices.clear();
        }

        return result;
    }

    private static void correctDirections(List<Pair<IBundledDevice, ForgeDirection>> list, IBundledDevice parent) {

        for (Pair<IBundledDevice, ForgeDirection> pair : list)
            pair.setValue(WireHelper.getConnectionSide(pair.getKey(), parent));
    }
}
