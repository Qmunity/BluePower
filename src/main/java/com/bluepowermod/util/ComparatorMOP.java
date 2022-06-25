/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.util;

import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;


public class ComparatorMOP implements Comparator<HitResult> {

    private Vec3 start = null;

    public ComparatorMOP(Vec3 start) {

        this.start = start;
    }

    @Override
    public int compare(HitResult arg0, HitResult arg1) {

        return (int) (((arg0.getLocation().distanceTo(start) - arg1.getLocation().distanceTo(start)) * 1000000));
    }

}
