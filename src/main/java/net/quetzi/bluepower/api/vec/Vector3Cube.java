package net.quetzi.bluepower.api.vec;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import codechicken.lib.vec.Transformation;

public class Vector3Cube {
    
    private Vector3 min, max;
    
    public Vector3Cube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
    
        this(minX, minY, minZ, maxX, maxY, maxZ, null);
    }
    
    public Vector3Cube(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, World world) {
    
        this(new Vector3(minX, minY, minZ, world), new Vector3(maxX, maxY, maxZ, world));
    }
    
    public Vector3Cube(Vector3 a, Vector3 b) {
    
        World w = a.getWorld();
        if (w == null) w = b.getWorld();
        
        double minX = Math.min(a.getX(), b.getX());
        double minY = Math.min(a.getY(), b.getY());
        double minZ = Math.min(a.getZ(), b.getZ());
        
        double maxX = Math.max(a.getX(), b.getX());
        double maxY = Math.max(a.getY(), b.getY());
        double maxZ = Math.max(a.getZ(), b.getZ());
        
        this.min = new Vector3(minX, minY, minZ, w);
        this.max = new Vector3(maxX, maxY, maxZ, w);
    }
    
    public Vector3 getMin() {
    
        return min;
    }
    
    public Vector3 getMax() {
    
        return max;
    }
    
    public Vector3 getCenter() {
    
        return new Vector3((getMinX() + getMaxX()) / 2D, (getMinY() + getMaxY()) / 2D, (getMinZ() + getMaxZ()) / 2D, getMin().getWorld());
    }
    
    public double getMinX() {
    
        return min.getX();
    }
    
    public double getMinY() {
    
        return min.getY();
    }
    
    public double getMinZ() {
    
        return min.getZ();
    }
    
    public double getMaxX() {
    
        return max.getX();
    }
    
    public double getMaxY() {
    
        return max.getY();
    }
    
    public double getMaxZ() {
    
        return max.getZ();
    }
    
    public AxisAlignedBB toAABB(){
        return AxisAlignedBB.getBoundingBox(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
    }
    
    @Override
    public Vector3Cube clone() {
    
        return new Vector3Cube(min.clone(), max.clone());
    }
    
    public Vector3Cube rotate(Transformation trans) {
    
        Vector3Cube c = this.clone();
        
        return c;
    }
    
}
