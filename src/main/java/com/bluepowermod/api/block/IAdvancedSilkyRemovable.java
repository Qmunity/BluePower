package com.bluepowermod.api.block;

import net.minecraft.world.World;

/**
 * Implemented by blocks/parts which need more control over the Silky removing.
 * Like setting a flag so inventory contents don't get dropped.
 * @author MineMaarten
 */
public interface IAdvancedSilkyRemovable extends ISilkyRemovable {
    
    /**
     * 
     * @param world
     * @param x
     * @param y
     * @param z
     * @return return false to prevent silky removing.
     */
    public boolean preSilkyRemoval(World world, int x, int y, int z);
    
    public void postSilkyRemoval(World world, int x, int y, int z);
}
