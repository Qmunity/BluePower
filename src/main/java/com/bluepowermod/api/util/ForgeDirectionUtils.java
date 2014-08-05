package com.bluepowermod.api.util;

import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.common.util.ForgeDirection;

public class ForgeDirectionUtils {
    
    public static int getSide(ForgeDirection dir) {
    
        for (int i = 0; i < ForgeDirection.VALID_DIRECTIONS.length; i++) {
            if (ForgeDirection.VALID_DIRECTIONS[i] == dir) return i;
        }
        return -1;
    }
    
    /**
     * Returns the ForgeDirection of the facing of the entity given.
     * @param entity
     * @param includeUpAndDown false when UP/DOWN should not be included.
     * @return
     */
    public static ForgeDirection getDirectionFacing(EntityLivingBase entity, boolean includeUpAndDown) {
    
        double yaw = entity.rotationYaw;
        while (yaw < 0)
            yaw += 360;
        yaw = yaw % 360;
        if (includeUpAndDown) {
            if (entity.rotationPitch > 45) return ForgeDirection.DOWN;
            else if (entity.rotationPitch < -45) return ForgeDirection.UP;
        }
        if (yaw < 45) return ForgeDirection.SOUTH;
        else if (yaw < 135) return ForgeDirection.WEST;
        else if (yaw < 225) return ForgeDirection.NORTH;
        else if (yaw < 315) return ForgeDirection.EAST;
        
        else return ForgeDirection.SOUTH;
    }
}
