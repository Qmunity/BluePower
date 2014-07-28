package com.bluepowermod.tileentities.tier1;

import net.minecraftforge.common.util.ForgeDirection;
import com.bluepowermod.helper.RedstoneHelper;
import com.bluepowermod.tileentities.TileBase;

/**
 * @author Koen Beckers (K4Unl)
 * Yes. I only need this class to do the getPower() function.. damn :(
 */
public class TileLamp extends TileBase {
    
    public int getPower() {
    
        int power = 0;
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            power = Math.max(power, RedstoneHelper.getInput(getWorldObj(), xCoord, yCoord, zCoord, d));
        }
        return power;
    }
}
