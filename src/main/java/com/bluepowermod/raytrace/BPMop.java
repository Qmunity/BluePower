/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
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
