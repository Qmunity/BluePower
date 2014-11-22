package com.bluepowermod.part;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.IPartFace;
import com.qmunity.lib.part.PartPlacementFace;
import com.qmunity.lib.part.compat.IMultipartCompat;
import com.qmunity.lib.vec.Vec3i;

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
