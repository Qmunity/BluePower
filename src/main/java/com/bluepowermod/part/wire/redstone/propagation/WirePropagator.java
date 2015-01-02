/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.wire.redstone.propagation;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

import com.bluepowermod.api.redstone.IPropagator;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.part.wire.redstone.RedstoneApi;
import com.bluepowermod.part.wire.redstone.WireHelper;

public class WirePropagator implements IPropagator {

    public static final WirePropagator INSTANCE = new WirePropagator();

    private WirePropagator() {

    }

    public void onPowerLevelChange(IRedstoneDevice device, ForgeDirection side, byte from, byte to) {

        if (device instanceof IRedstoneConductor) {
            if (((IRedstoneConductor) device).hasLoss()) {
                new LossyPropagatorLogic().beginPropagation((IRedstoneConductor) device, side, from, to);// s, from, to);
            } else {
                new PropagatorLogic().beginPropagation((IRedstoneConductor) device, side, from, to);// s, from, to);
            }
        } else {
            device.setRedstonePower(side, to);// s, to);
            device.onRedstoneUpdate();
        }

        BundledDeviceWrapper.clearCache();
    }

    private static interface IPropagatorLogic {

        public void beginPropagation(IRedstoneConductor device, ForgeDirection fromSide, byte from, byte to);

    }

    private static final class LossyPropagatorLogic implements IPropagatorLogic {

        @Override
        public void beginPropagation(IRedstoneConductor device, ForgeDirection fromSide, byte from, byte to) {

            List<Pair<IRedstoneDevice, ForgeDirection>> visited = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();
            visited.addAll(WirePathfinder.pathfind(device, fromSide));

            List<IRedstoneDevice> devices = new ArrayList<IRedstoneDevice>();

            new PropagatorLogic().propagate((byte) 0, devices, visited);

            for (Pair<IRedstoneDevice, ForgeDirection> pair : visited) {
                RedstoneApi.getInstance().setWiresOutputPower(false);
                byte power = pair.getKey().getRedstonePower(pair.getValue());
                RedstoneApi.getInstance().setWiresOutputPower(true);
                if ((power & 0xFF) > 0) {
                    if (pair.getKey() instanceof IRedstoneConductor) {
                        propagate((IRedstoneConductor) pair.getKey(), pair.getValue(), power);
                    } else {
                        IRedstoneDevice d = pair.getKey().getDeviceOnSide(pair.getValue());
                        if (d != null) {
                            if (d instanceof IRedstoneConductor) {
                                propagate((IRedstoneConductor) d, WireHelper.getConnectionSide(d, pair.getKey()), power);
                            }
                        }
                    }
                }
            }

            for (IRedstoneDevice d : devices)
                d.onRedstoneUpdate();
        }

        private void propagate(IRedstoneConductor cond, ForgeDirection from, byte power) {

            if ((power & 0xFF) < 0)
                return;
            if ((cond.getRedstonePower(from) & 0xFF) >= (power & 0xFF))
                return;

            for (Pair<IRedstoneDevice, ForgeDirection> p : cond.propagate(from)) {
                // cond.setRedstonePower(p.getValue(), power);
                ForgeDirection d = WireHelper.getConnectionSide(p.getKey(), cond);
                cond.setRedstonePower(p.getValue(), power);
                if (p.getKey() instanceof IRedstoneConductor) {
                    propagate((IRedstoneConductor) p.getKey(), d, (byte) ((power & 0xFF) - 1));
                }
            }
        }
    }

    private static final class PropagatorLogic implements IPropagatorLogic {

        @Override
        public void beginPropagation(IRedstoneConductor device, ForgeDirection fromSide, byte from, byte to) {

            List<Pair<IRedstoneDevice, ForgeDirection>> visited = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();
            visited.addAll(WirePathfinder.pathfind(device, fromSide));

            RedstoneApi.getInstance().setWiresOutputPower(false);
            int power = 0;
            for (Pair<IRedstoneDevice, ForgeDirection> pair : visited)
                power = Math.max(power, pair.getKey().getRedstonePower(pair.getValue()) & 0xFF);
            RedstoneApi.getInstance().setWiresOutputPower(true);

            List<IRedstoneDevice> devices = new ArrayList<IRedstoneDevice>();

            propagate((byte) power, devices, visited);

            for (IRedstoneDevice d : devices)
                d.onRedstoneUpdate();

            visited.clear();
        }

        private void propagate(byte power, List<IRedstoneDevice> devices, List<Pair<IRedstoneDevice, ForgeDirection>> visited) {

            for (Pair<IRedstoneDevice, ForgeDirection> pair : visited) {
                pair.getKey().setRedstonePower(pair.getValue(), power);
                if (!devices.contains(pair.getKey()))
                    devices.add(pair.getKey());
            }
        }
    }

}
