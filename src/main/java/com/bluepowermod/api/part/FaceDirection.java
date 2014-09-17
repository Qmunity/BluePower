/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.part;

import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.util.ForgeDirection;

public enum FaceDirection {

    FRONT("front"), BACK("back"), LEFT("left"), RIGHT("right");

    private FaceDirection(String name) {

        this.name = name;
    }

    private String name; // used in texture paths

    public FaceDirection getOpposite() {

        return FaceDirection.getDirection((getDirection() + 2) % 4);
    }

    public int getDirection() {

        switch (this) {
        case FRONT:
            return 0;
        case LEFT:
            return 1;
        case BACK:
            return 2;
        case RIGHT:
            return 3;
        }
        return -1;
    }

    public String getName() {

        return name;
    }

    public String getLocalizedName() {

        return I18n.format("direction." + getName());
    }

    public static FaceDirection getDirection(int id) {

        for (FaceDirection d : values())
            if (d.getDirection() == id)
                return d;

        return null;
    }

    public static FaceDirection getDirection(ForgeDirection face, ForgeDirection direction, int rotation) {

        FaceDirection dir = null;

        int rot = rotation;

        switch (face) {
        case UP:
        case DOWN:
            switch (direction) {
            case NORTH:
                dir = BACK;
                break;
            case EAST:
                dir = LEFT;
                break;
            case SOUTH:
                dir = FRONT;
                break;
            case WEST:
                dir = RIGHT;
                break;
            default:
                break;
            }

            if (face == ForgeDirection.UP && dir != null)
                dir = dir.getOpposite();
            break;
        case WEST:
        case EAST:
            rot++;
            switch (direction) {
            case UP:
                dir = FRONT;
                break;
            case NORTH:
                dir = LEFT;
                break;
            case DOWN:
                dir = BACK;
                break;
            case SOUTH:
                dir = RIGHT;
                break;
            default:
                break;
            }

            if (face == ForgeDirection.WEST && dir != null)
                dir = dir.getOpposite();
            break;
        case NORTH:
        case SOUTH:
            switch (direction) {
            case UP:
                dir = FRONT;
                break;
            case EAST:
                dir = LEFT;
                break;
            case DOWN:
                dir = BACK;
                break;
            case WEST:
                dir = RIGHT;
                break;
            default:
                break;
            }

            if (face == ForgeDirection.NORTH && dir != null)
                dir = dir.getOpposite();
            break;
        default:
            break;
        }

        if (dir != null)
            dir = getDirection((dir.getDirection() + rot) % 4);

        return dir;
    }

}
