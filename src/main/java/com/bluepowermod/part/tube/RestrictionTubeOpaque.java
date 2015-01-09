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

import net.minecraft.util.IIcon;

import com.bluepowermod.client.render.IconSupplier;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class RestrictionTubeOpaque extends RestrictionTube {

    @Override
    public String getType() {

        return "restrictionTubeOpaque";
    }

    @Override
    public String getUnlocalizedName() {

        return "restrictionTubeOpaque";
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getSideIcon() {

        return IconSupplier.restrictionTubeSideOpaque;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getNodeIcon() {

        return IconSupplier.restrictionTubeNodeOpaque;
    }
}
