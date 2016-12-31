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

package com.bluepowermod.helper;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3dHelper;

import java.util.List;

;

public class VectorHelper {

    public static final void rotateBoxes(List<Vec3dCube> boxes, EnumFacing face, int rotation) {

        for (Vec3dCube box : boxes)
            rotateBox(box, face, rotation);
    }

    public static final void rotateBox(Vec3dCube box, EnumFacing face, int rotation) {

        box.rotate(0, rotation * 90, 0, Vec3dHelper.CENTER);
        box.rotate(face, Vec3dHelper.CENTER);
    }

    public  static final EnumFacing toEnumFacing(Vec3d vec3) {

        if (vec3.zCoord == 1)
            return EnumFacing.SOUTH;
        if (vec3.zCoord == -1)
            return EnumFacing.NORTH;

        if (vec3.xCoord == 1)
            return EnumFacing.EAST;
        if (vec3.xCoord == -1)
            return EnumFacing.WEST;

        if (vec3.yCoord == 1)
            return EnumFacing.UP;
        if (vec3.yCoord == -1)
            return EnumFacing.DOWN;

        return null;
    }

}
