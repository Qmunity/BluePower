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

import mcmultipart.api.multipart.IMultipart;
import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.vec.Vec3i;

public class PartPlacementFaceRotate extends PartPlacementFace {

    protected int rotation;

    public PartPlacementFaceRotate(EnumFacing face, int rotation) {

        super(face);
        this.rotation = rotation;
    }

    @Override
    public boolean placePart(IMultipart part, World world, Vec3i location, boolean simulated) {

        if (part instanceof BPPartFaceRotate)
            ((BPPartFaceRotate) part).setRotation(rotation);

        return super.placePart(part, world, location, multipartSystem, simulated);
    }

}
