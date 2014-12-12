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

import uk.co.qmunity.lib.vec.Vec3d;
import net.minecraft.util.IIcon;

import com.bluepowermod.client.renderers.IconSupplier;

public class PneumaticTubeOpaque extends PneumaticTube {

    @Override
    public String getType() {

        return "pneumaticTubeOpaque";
    }

    @Override
    public String getUnlocalizedName() {

        return "pneumaticTubeOpaque";
    }

    @Override
    protected IIcon getSideIcon() {

        return IconSupplier.pneumaticTubeOpaqueSide;
    }

    @Override
    protected IIcon getNodeIcon() {

        return IconSupplier.pneumaticTubeOpaqueNode;
    }

    @Override
    public void renderDynamic(Vec3d loc, double delta, int pass) {

    }
}
