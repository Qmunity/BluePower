/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.helper;

import net.minecraft.util.Direction;

public class DirectionHelper {

    /**
     * Returns an Array of Directions for Rotation on a given facing direction.
     * @author MoreThanHidden
     */
    public static Direction[] ArrayFromDirection(Direction direction){
        Direction[] dirs = {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
        switch (direction){
            case DOWN:
                dirs = new Direction[]{Direction.NORTH, Direction.WEST, Direction.SOUTH, Direction.EAST};
                break;
            case UP:
                dirs = new Direction[]{Direction.SOUTH, Direction.WEST, Direction.NORTH, Direction.EAST};
                break;
            case NORTH:
                dirs = new Direction[]{Direction.UP, Direction.WEST, Direction.DOWN, Direction.EAST};
                break;
            case SOUTH:
                dirs = new Direction[]{Direction.UP, Direction.EAST, Direction.DOWN, Direction.WEST};
                break;
            case WEST:
                dirs = new Direction[]{Direction.UP, Direction.SOUTH , Direction.DOWN, Direction.NORTH};
                break;
            case EAST:
                dirs = new Direction[]{Direction.UP, Direction.NORTH, Direction.DOWN, Direction.SOUTH};
                break;
        }
        return dirs;
    }
}
