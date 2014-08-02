package com.bluepowermod.raytrace;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;

public class BPMop extends MovingObjectPosition {

    private BPPart partHit = null;
    private AxisAlignedBB cubeHit = null;

    public BPMop(int x, int y, int z, int side, Vector3 subHit, BPPart partHit, AxisAlignedBB cubeHit) {

        super(x, y, z, side, subHit.toVec3());

        this.partHit = partHit;
        this.cubeHit = cubeHit;
    }

    public BPMop(int x, int y, int z, int side, Vector3 subHit, AxisAlignedBB cubeHit) {

        this(x, y, z, side, subHit, null, cubeHit);
    }

    public BPMop(int x, int y, int z, int side, Vector3 subHit) {

        this(x, y, z, side, subHit, null);
    }

    public BPMop(MovingObjectPosition mop, BPPart partHit, AxisAlignedBB cubeHit) {

        this(mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, new Vector3(mop.hitVec), partHit, cubeHit);
    }

    public BPMop(MovingObjectPosition mop, AxisAlignedBB cubeHit) {

        this(mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, new Vector3(mop.hitVec), cubeHit);
    }

    public BPMop(MovingObjectPosition mop) {

        this(mop.blockX, mop.blockY, mop.blockZ, mop.sideHit, new Vector3(mop.hitVec));
    }

    public AxisAlignedBB getCubeHit() {

        return cubeHit;
    }

    public BPPart getPartHit() {

        return partHit;
    }

    public void setPartHit(BPPart partHit) {

        this.partHit = partHit;
    }

}
