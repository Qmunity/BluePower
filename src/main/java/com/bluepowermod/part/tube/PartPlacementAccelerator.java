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

package com.bluepowermod.part.tube;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.co.qmunity.lib.misc.EnumFacingUtils;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.PartPlacementDefault;
import uk.co.qmunity.lib.part.compat.IMultipartCompat;

;


public class PartPlacementAccelerator extends PartPlacementDefault {

    private EnumFacing rotation;

    public PartPlacementAccelerator(EntityPlayer player) {

        rotation = EnumFacingUtils.getDirectionFacing(player, true);
    }

    @Override
    public boolean placePart(IPart part, World world, BlockPos location, IMultipartCompat multipartSystem, boolean simulated) {

        if (part instanceof Accelerator)
            ((Accelerator) part).setRotation(rotation);

        return super.placePart(part, world, location, multipartSystem, simulated);
    }

}
