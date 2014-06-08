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

package net.quetzi.bluepower.util;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.quetzi.bluepower.api.vec.Vector3;

import java.util.List;

public class RayTracer {
    
    public static final MovingObjectPosition rayTrace(Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs) {
    
        return rayTrace(start, end, aabbs, new Vector3(0, 0, 0));
    }
    
    public static final MovingObjectPosition rayTrace(Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs, int x, int y, int z) {
    
        return rayTrace(start, end, aabbs, new Vector3(x, y, z));
    }
    
    public static final MovingObjectPosition rayTrace(Vector3 start, Vector3 end, List<AxisAlignedBB> aabbs, Vector3 location) {
    
        return null;
    }
    
}
