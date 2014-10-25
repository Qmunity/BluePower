/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.bluestone;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;

public class BluestoneHelper {

    public static int rotateConductionMap(int map, int times) {

        int down = (map & 0xF00000) >> 20;
        int up = (map & 0x0F0000) >> 16;
        int north = (map & 0x00F000) >> 12;
        int south = (map & 0x000F00) >> 8;
        int west = (map & 0x0000F0) >> 4;
        int east = (map & 0x00000F);

        for (int i = 0; i < times; i++) {
            int a = north;
            north = west;
            west = south;
            south = east;
            east = a;
        }

        return (down << 20) + (up << 16) + (north << 12) + (south << 8) + (west << 4) + east;
    }

    public static int rotateConductionMap(int map, ForgeDirection face) {

        int down = (map & 0xF00000) >> 20;
        int up = (map & 0x0F0000) >> 16;
        int north = (map & 0x00F000) >> 12;
        int south = (map & 0x000F00) >> 8;
        int west = (map & 0x0000F0) >> 4;
        int east = (map & 0x00000F);

        int a;

        switch (face) {
        case DOWN:
            break;
        case UP:
            a = up;
            up = down;
            down = a;
            break;
        case NORTH:
            a = north;
            north = down;
            down = south;
            south = up;
            up = a;
            break;
        case SOUTH:
            a = south;
            south = down;
            down = north;
            north = up;
            up = a;
            break;
        case WEST:
            a = west;
            west = down;
            down = east;
            east = up;
            up = a;
            break;
        case EAST:
            a = east;
            east = down;
            down = west;
            west = up;
            up = a;
            break;
        default:
            break;
        }

        return (down << 20) + (up << 16) + (north << 12) + (south << 8) + (west << 4) + east;
    }

    public static boolean doesShareNetwork(int map, ForgeDirection side1, ForgeDirection side2) {

        return getNetwork(map, side1) == getNetwork(map, side2);
    }

    public static int getNetwork(int map, ForgeDirection side) {

        return (map & (0xF << (4 * (5 - side.ordinal())))) >> (4 * (5 - side.ordinal()));
    }

    public static List<IBluestoneHandler> getConnectedHandlers(IBluestoneHandler handler, int network) {

        List<IBluestoneHandler> handlers = new ArrayList<IBluestoneHandler>();

        int map = handler.getConductionMap();
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (getNetwork(map, d) == network) {
                IBluestoneHandler h = handler.getConnectedHandler(d);
                if (h != null && !handlers.contains(h))
                    handlers.add(h);
            }
        }

        return handlers;
    }

    public static List<Entry<IBluestoneHandler, Integer>> listHandlersInNetwork(IBluestoneHandler handler, int network) {

        List<Entry<IBluestoneHandler, Integer>> handlers = new ArrayList<Entry<IBluestoneHandler, Integer>>();
        listHandlersInNetwork(handlers, handler, network);
        return handlers;
    }

    @SuppressWarnings("rawtypes")
    private static void listHandlersInNetwork(List<Entry<IBluestoneHandler, Integer>> visited, IBluestoneHandler handler, int network) {

        Entry<IBluestoneHandler, Integer> data = new AbstractMap.SimpleEntry(handler, network);

        if (visited.contains(data))
            return;
        visited.add(data);

        int map = handler.getConductionMap();
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            if (getNetwork(map, d) == network) {
                IBluestoneHandler h = handler.getConnectedHandler(d);
                if (h != null)
                    listHandlersInNetwork(visited, h, getNetwork(h.getConductionMap(), d.getOpposite()));
            }
        }
    }

    public static List<IBluestoneDevice> listDevicesInNetwork(IBluestoneHandler handler, int network) {

        List<IBluestoneDevice> devices = new ArrayList<IBluestoneDevice>();

        for (Entry<IBluestoneHandler, Integer> e : listHandlersInNetwork(handler, network)) {
            IBluestoneDevice dev = e.getKey().getDevice();
            if (!devices.contains(dev))
                devices.add(dev);
        }

        return devices;
    }

}
