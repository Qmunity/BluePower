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

package com.bluepowermod.part;

import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.common.util.ForgeDirection;

public class PartRotationHelper {

    public static int getPlacementRotation(MovingObjectPosition mop) {

        ForgeDirection faceHit = ForgeDirection.getOrientation(mop.sideHit);

        double x = 0;
        double z = 0;

        switch (faceHit) {
        case UP:
        case DOWN:
            x = mop.hitVec.xCoord % 1;
            z = mop.hitVec.zCoord % 1;
            break;
        case EAST:
        case WEST:
            x = mop.hitVec.yCoord % 1;
            z = mop.hitVec.zCoord % 1;
            break;
        case NORTH:
        case SOUTH:
            x = mop.hitVec.xCoord % 1;
            z = mop.hitVec.yCoord % 1;
            break;
        default:
            break;
        }

        if (x < 0)
            x = 1 + x;
        if (z < 0)
            z = 1 + z;

        return getPlacementRotation(x, z);
    }

    public static int getPlacementRotation(double x, double z) {

        x -= 0.5;
        z -= 0.5;

        if (z > 0 && z > Math.abs(x)) {
            return 0;
        } else if (x > 0 && x > Math.abs(z)) {
            return 3;
        } else if (z < 0 && Math.abs(z) > Math.abs(x)) {
            return 2;
        }
        return 1;
    }

}
