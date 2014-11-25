package com.bluepowermod.part;

import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.part.PartPlacementFace;
import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.vec.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class PartPlacementFaceRotate extends PartPlacementFace {

    protected int rotation;

    public PartPlacementFaceRotate(ForgeDirection face, int rotation) {

        super(face);
        this.rotation = rotation;
    }

    @Override
    public boolean placePart(IPart part, World world, Vec3i location, IMultipartCompat multipartSystem, boolean simulated) {

        if (part instanceof IPartFace)
            ((IPartFace) part).setFace(face);
        if (part instanceof BPPartFaceRotate)
            ((BPPartFaceRotate) part).setRotation(rotation);

        return multipartSystem.addPartToWorld(part, world, location, simulated);
    }

}
