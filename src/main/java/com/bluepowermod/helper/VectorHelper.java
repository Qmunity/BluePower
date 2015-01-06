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

import java.util.List;

import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import net.minecraftforge.common.util.ForgeDirection;

public class VectorHelper {

    public static final void rotateBoxes(List<Vec3dCube> boxes, ForgeDirection face, int rotation) {

        for (Vec3dCube box : boxes)
            rotateBox(box, face, rotation);
    }

    public static final void rotateBox(Vec3dCube box, ForgeDirection face, int rotation) {

        box.rotate(0, rotation * 90, 0, Vec3d.center);
        box.rotate(face, Vec3d.center);
    }

}
