package com.bluepowermod.tileentities;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Implemented by BluePower block that can be rotated.
 * @author MineMaarten
 */
public interface IRotatable {
    
    public void setFacingDirection(ForgeDirection dir);
    
    public ForgeDirection getFacingDirection();
}
