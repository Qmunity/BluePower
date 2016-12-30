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

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.IMultipartCompat;
import uk.co.qmunity.lib.tile.TileMultipart;

public class PartPlacementFace extends uk.co.qmunity.lib.part.PartPlacementFace {

    public PartPlacementFace(EnumFacing face) {

        super(face);
    }

    @Override
    public boolean placePart(IPart part, World world, BlockPos location, IMultipartCompat multipartSystem, boolean simulated) {

        if (part instanceof BPPartFace) {
            TileMultipart te = new TileMultipart(true);
            te.setPos(location);
            te.setWorld(world);

            part.setParent(te);
            ((BPPartFace) part).setFace(face);
            if (!((BPPartFace) part).canStay()) {
                part.setParent(null);
                return false;
            }
            part.setParent(null);
        }

        return super.placePart(part, world, location, multipartSystem, simulated);
    }

}
