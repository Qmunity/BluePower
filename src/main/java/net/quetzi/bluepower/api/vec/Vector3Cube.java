package net.quetzi.bluepower.api.vec;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.util.ForgeDirectionUtils;

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
    
    public AxisAlignedBB toAABB() {
    
        return AxisAlignedBB.getBoundingBox(getMinX(), getMinY(), getMinZ(), getMaxX(), getMaxY(), getMaxZ());
    }
    
    @Override
    public Vector3Cube clone() {
    
        return new Vector3Cube(min.clone(), max.clone());
    }
    
    private ForgeDirection last = ForgeDirection.UNKNOWN;
    
    public Vector3Cube rotate90Degrees(ForgeDirection dir) {
    
        if (last == ForgeDirection.UNKNOWN) {
            last = dir;
        } else {
            dir = ForgeDirection.getOrientation(ForgeDirection.ROTATION_MATRIX[ForgeDirectionUtils.getSide(last)][ForgeDirectionUtils.getSide(dir)]);
        }
        
        Vector3 min = this.min.clone();
        Vector3 max = this.max.clone();
        
        rotate(min, dir);
        rotate(max, dir);
        
        return new Vector3Cube(min, max);
    }
    
    private void rotate(Vector3 vec, ForgeDirection dir) {
    
    }
    
}
