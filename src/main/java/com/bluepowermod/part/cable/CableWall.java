package com.bluepowermod.part.cable;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.part.BPPartFace;
import com.bluepowermod.util.ForgeDirectionUtils;

/**
 * @author amadornes
 * 
 */
public abstract class CableWall extends BPPartFace {

    private boolean[] connections = new boolean[6];

    /**
     * @author amadornes
     * 
     */
    public CableWall() {

    }

    /**
     * @author amadornes
     * 
     */
    public boolean canConnectOnSide(ForgeDirection direction) {

        return direction != ForgeDirection.getOrientation(getFace()) && direction != ForgeDirection.getOrientation(getFace()).getOpposite();
    }

    /**
     * @author amadornes
     * 
     */
    public boolean isConnectedOnSide(ForgeDirection direction) {

        return connections[ForgeDirectionUtils.getSide(direction)];
    }

}
