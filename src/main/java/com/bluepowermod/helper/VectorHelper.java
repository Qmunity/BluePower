package com.bluepowermod.helper;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;

public class VectorHelper {

    public static final void rotateBoxes(List<Vec3dCube> boxes, ForgeDirection face, int rotation) {

        for (Vec3dCube box : boxes) {
            rotateBox(box, face, rotation);
        }
    }

    public static final void rotateBox(Vec3dCube box, ForgeDirection face, int rotation) {

        box.rotate(0, rotation * 90, 0, Vec3d.center);
        box.rotate(face, Vec3d.center);
    }

}