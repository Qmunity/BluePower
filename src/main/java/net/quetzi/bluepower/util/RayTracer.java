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
