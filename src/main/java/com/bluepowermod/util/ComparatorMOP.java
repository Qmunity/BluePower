/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.util;

import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Comparator;


public class ComparatorMOP implements Comparator<RayTraceResult> {

    private Vec3d start = null;

    public ComparatorMOP(Vec3d start) {

        this.start = start;
    }

    @Override
    public int compare(RayTraceResult arg0, RayTraceResult arg1) {

        return (int) (((arg0.hitVec.distanceTo(start) - arg1.hitVec.distanceTo(start)) * 1000000));
    }

}
