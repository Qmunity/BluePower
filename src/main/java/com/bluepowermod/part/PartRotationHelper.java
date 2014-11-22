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
