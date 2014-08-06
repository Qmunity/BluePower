package com.bluepowermod.api.tube;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.part.tube.TubeStack;

/**
 * 
 * @author MineMaarten
 */

public interface ITubeConnection {
    
    public boolean isConnectedTo(ForgeDirection from);
    
    /**
     * 
     * @param stack TubeStack, as it needs to save the color if it bounced into the buffer.
     * @param from
     * @param simulate when true, only return what would have been accepted, but don't actually accept.
     * @return The TubeStack that was unable to enter this ITubeConnection
     */
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from, boolean simulate);
}
