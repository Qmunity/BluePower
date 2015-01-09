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

package com.bluepowermod.part.wire.redstone;

import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IConductor;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;

public class WireCommons {

    public static boolean canConnect(IRedstoneConductor a, IRedstoneDevice b) {

        if (b instanceof IConductor && (((IConductor) b).isAnalog() != a.isAnalog() || ((IConductor) b).hasLoss() != a.hasLoss()))
            return false;

        MinecraftColor c1 = a.getInsulationColor();
        MinecraftColor c2 = b.getInsulationColor();

        if (c1 == null || c2 == null)
            return false;

        return c1.matches(c2) || c1 == MinecraftColor.NONE || c2 == MinecraftColor.NONE;
    }

    public static boolean canConnect(IBundledConductor a, IBundledDevice b) {

        if (b instanceof IConductor && (((IConductor) b).isAnalog() != a.isAnalog() || ((IConductor) b).hasLoss() != a.hasLoss()))
            return false;

        MinecraftColor c1 = a.getBundledColor();
        MinecraftColor c2 = b.getBundledColor();

        if (c1 == null || c2 == null)
            return false;

        return (c1.matches(c2) || c1 == MinecraftColor.NONE || c2 == MinecraftColor.NONE);
    }

    public static void refreshConnections(IRedstoneConductor conductor, IBundledConductor conductor1) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            IRedstoneDevice oldDevice = conductor.getDeviceOnSide(d);
            IBundledDevice oldBundledDevice = conductor1.getBundledDeviceOnSide(d);
            boolean wasConnected = oldDevice != null || oldBundledDevice != null;

            Entry<IBundledDevice, ForgeDirection> bundledDevice = WireHelper.getBundledNeighbor(conductor1, d);
            if (bundledDevice != null && bundledDevice.getKey() != conductor1.getBundledDeviceOnSide(d)) {
                conductor1.onConnect(d, bundledDevice.getKey());
                bundledDevice.getKey().onConnect(bundledDevice.getValue(), conductor1);
            }

            Entry<IRedstoneDevice, ForgeDirection> redstoneDevice = WireHelper.getNeighbor(conductor, d);
            if (redstoneDevice != null && redstoneDevice.getKey() != conductor.getDeviceOnSide(d)) {
                conductor.onConnect(d, redstoneDevice.getKey());
                redstoneDevice.getKey().onConnect(redstoneDevice.getValue(), conductor);
            }

            if (wasConnected && bundledDevice == null && redstoneDevice == null) {
                conductor.onDisconnect(d);
                if (oldDevice != null)
                    for (ForgeDirection s : ForgeDirection.VALID_DIRECTIONS)
                        if (oldDevice.getDeviceOnSide(s) == conductor)
                            oldDevice.onDisconnect(s);
                if (oldBundledDevice != null)
                    for (ForgeDirection s : ForgeDirection.VALID_DIRECTIONS)
                        if (oldBundledDevice.getBundledDeviceOnSide(s) == conductor1)
                            oldBundledDevice.onDisconnect(s);
            }
        }
    }

    public static void refreshConnectionsRedstone(IRedstoneDevice device) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            boolean wasConnected = device.getDeviceOnSide(d) != null;
            Entry<IRedstoneDevice, ForgeDirection> redstoneDevice = WireHelper.getNeighbor(device, d);
            if (redstoneDevice != null && redstoneDevice.getKey() != device.getDeviceOnSide(d)) {
                device.onConnect(d, redstoneDevice.getKey());
                redstoneDevice.getKey().onConnect(redstoneDevice.getValue(), device);
            }
            if (wasConnected && redstoneDevice == null)
                device.onDisconnect(d);
        }
    }

    public static void refreshConnectionsBundled(IBundledDevice device) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            Entry<IBundledDevice, ForgeDirection> bundledDevice = WireHelper.getBundledNeighbor(device, d);
            boolean wasConnected = device.getBundledDeviceOnSide(d) != null;
            if (bundledDevice != null && bundledDevice.getKey() != device.getBundledDeviceOnSide(d)) {
                device.onConnect(d, bundledDevice.getKey());
                bundledDevice.getKey().onConnect(bundledDevice.getValue(), device);
            }
            if (wasConnected && bundledDevice == null)
                device.onDisconnect(d);
        }
    }

    public static void disconnect(IRedstoneDevice device, IBundledDevice device1) {

        disconnectRedstone(device);
        disconnectBundled(device1);
    }

    public static void disconnectRedstone(IRedstoneDevice device) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            IRedstoneDevice redstoneDevice = device.getDeviceOnSide(d);
            if (redstoneDevice != null) {
                device.onDisconnect(d);
                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    IRedstoneDevice t = redstoneDevice.getDeviceOnSide(dir);
                    if (t == device) {
                        redstoneDevice.onDisconnect(dir);
                        break;
                    }
                }
            }
        }
    }

    public static void disconnectBundled(IBundledDevice device) {

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            IBundledDevice bundledDevice = device.getBundledDeviceOnSide(d);
            if (bundledDevice != null) {
                device.onDisconnect(d);
                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    IBundledDevice t = bundledDevice.getBundledDeviceOnSide(dir);
                    if (t == device) {
                        bundledDevice.onDisconnect(dir);
                        break;
                    }
                }
            }
        }
    }

    public static int getColorForPowerLevel(int color, byte power) {

        double mul = (0.3 + (0.7 * ((power & 0xFF) / 255D)));
        return ((int) ((color & 0xFF0000) * mul) & 0xFF0000) + ((int) ((color & 0x00FF00) * mul) & 0x00FF00)
                + ((int) ((color & 0x0000FF) * mul) & 0x0000FF);
    }
}
