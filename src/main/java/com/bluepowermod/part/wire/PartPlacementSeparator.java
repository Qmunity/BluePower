package com.bluepowermod.part.wire;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.PartPlacementDefault;
import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.BPPartFaceRotate;

public class PartPlacementSeparator extends PartPlacementDefault {

    protected ForgeDirection face;
    protected int rotation;

    public PartPlacementSeparator(ForgeDirection face, int rotation) {

        this.face = face;
        this.rotation = rotation;
    }

    @Override
    public boolean placePart(IPart part, World world, Vec3i location, IMultipartCompat multipartSystem, boolean simulated) {

        ((BPPartFaceRotate) part).setFace(face);
        ((BPPartFaceRotate) part).setRotation(rotation);

        return super.placePart(part, world, location, multipartSystem, simulated);
    }

}
