/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.bluestone;

import net.minecraftforge.common.util.ForgeDirection;

public class ConductionMapHelper {

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
            east = a;
            break;
        case EAST:
            a = east;
            east = down;
            down = west;
            west = a;
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

        switch (side) {
        case DOWN:
            return (map & 0xF00000) >> 20;
        case UP:
            return (map & 0x0F0000) >> 16;
        case NORTH:
            return (map & 0x00F000) >> 12;
        case SOUTH:
            return (map & 0x000F00) >> 8;
        case WEST:
            return (map & 0x0000F0) >> 4;
        case EAST:
            return (map & 0x00000F);
        default:
            break;
        }

        return -1;
    }

}
