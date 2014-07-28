package com.bluepowermod.util;

import java.util.Comparator;

import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import com.bluepowermod.api.vec.Vector3;

public class ComparatorMOP implements Comparator<MovingObjectPosition> {
    
    private Vec3 start = null;
    
    public ComparatorMOP(Vector3 start) {
    
        this.start = start.toVec3();
    }
    
    @Override
    public int compare(MovingObjectPosition arg0, MovingObjectPosition arg1) {
    
        return (int)(((double)((arg0.hitVec.distanceTo(start) - arg1.hitVec.distanceTo(start)) * 1000000)));
    }
    
}
