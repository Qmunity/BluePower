package com.bluepowermod.part.wire.propagation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

import com.bluepowermod.api.redstone.IPropagator;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;

public class WirePropagator implements IPropagator {

    public static final WirePropagator INSTANCE = new WirePropagator();

    private WirePropagator() {

    }

    public void onPowerLevelChange(IRedstoneDevice device, ForgeDirection side, byte from, byte to) {

        IRedstoneDevice dev = device;// device.getDeviceOnSide(side);
        // System.out.println(dev);
        // if (dev == null)
        // return;

        // ForgeDirection s = WireHelper.getConnectionSide(dev, device);

        if (dev instanceof IRedstoneConductor) {
            if (((IRedstoneConductor) dev).hasLoss()) {
                new LossyPropagatorLogic().beginPropagation((IRedstoneConductor) dev, side, from, to);// s, from, to);
            } else {
                new PropagatorLogic().beginPropagation((IRedstoneConductor) dev, side, from, to);// s, from, to);
            }
        } else {
            dev.setRedstonePower(side, to);// s, to);
        }
    }

    private static interface IPropagatorLogic {

        public void beginPropagation(IRedstoneConductor device, ForgeDirection fromSide, byte from, byte to);

    }

    private static final class LossyPropagatorLogic implements IPropagatorLogic {

        List<Entry<IRedstoneDevice, ForgeDirection>> visited = new ArrayList<Entry<IRedstoneDevice, ForgeDirection>>();

        @Override
        public void beginPropagation(IRedstoneConductor device, ForgeDirection fromSide, byte from, byte to) {

            byte original = device.getRedstonePower(fromSide);

            if (to == original)
                return;

            if ((to & 0xFF) > (original & 0xFF)) {
                propagateRising(device, fromSide, to);
            } else {

            }

            propagateFalling(device, fromSide);

            List<IRedstoneDevice> devices = new ArrayList<IRedstoneDevice>();
            for (Entry<IRedstoneDevice, ForgeDirection> e : visited) {
                if (devices.contains(e.getKey()))
                    continue;
                devices.add(e.getKey());
                e.getKey().onRedstoneUpdate();
            }

            visited.clear();
            devices.clear();
        }

        private void propagateRising(IRedstoneConductor device, ForgeDirection fromSide, byte power) {

            // if (!hasVisited(device, fromSide))
            // visited.add(new AbstractMap.SimpleEntry(device, fromSide));
            // else
            // return;
            //
            // Collection<Pair<IRedstoneDevice, ForgeDirection>> neighbors = device.propagate(fromSide);
            // for (Pair<IRedstoneDevice, ForgeDirection> pair : neighbors) {
            // ForgeDirection d = WireHelper.getConnectionSide(device, dev);
            // if (!hasVisited(device, d))
            // visited.add(new AbstractMap.SimpleEntry(device, d));
            //
            // ForgeDirection s = WireHelper.getConnectionSide(dev, device);
            // dev.setRedstonePower(s, power);
            // if (dev instanceof IRedstoneConductor && ((power & 0xFF) - 1) > 0) {
            // propagateRising((IRedstoneConductor) dev, s, (byte) ((power & 0xFF) - 1));
            // }
            // }
        }

        private void propagateFalling(IRedstoneConductor device, ForgeDirection fromSide) {

        }

        private boolean hasVisited(IRedstoneDevice conductor, ForgeDirection side) {

            for (Entry<IRedstoneDevice, ForgeDirection> e : visited)
                if (e.getKey() == conductor && e.getValue() == side)
                    return true;
            return false;
        }
    }

    private static final class PropagatorLogic implements IPropagatorLogic {

        List<Pair<IRedstoneDevice, ForgeDirection>> visited = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();

        @Override
        public void beginPropagation(IRedstoneConductor device, ForgeDirection fromSide, byte from, byte to) {

            visited.addAll(WirePathfinder.pathfind(device, fromSide));

            int power = 0;
            for (Pair<IRedstoneDevice, ForgeDirection> pair : visited)
                pair.getKey().setRedstonePower(pair.getValue(), (byte) 0);
            for (Pair<IRedstoneDevice, ForgeDirection> pair : visited)
                power = Math.max(power, pair.getKey().getRedstonePower(pair.getValue()) & 0xFF);

            List<IRedstoneDevice> devices = new ArrayList<IRedstoneDevice>();
            for (Pair<IRedstoneDevice, ForgeDirection> pair : visited) {
                pair.getKey().setRedstonePower(pair.getValue(), (byte) power);
                if (!devices.contains(pair.getKey()))
                    devices.add(pair.getKey());
            }

            for (IRedstoneDevice d : devices)
                d.onRedstoneUpdate();

            visited.clear();
        }
    }

}
