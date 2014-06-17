package net.quetzi.bluepower.part.tube;

import net.minecraftforge.common.util.ForgeDirection;

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
     * @return The TubeStack that was unable to enter the ITubeConnector
     */
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from);
}
