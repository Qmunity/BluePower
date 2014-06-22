package net.quetzi.bluepower.tileentities.tier1;

import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.helper.RedstoneHelper;
import net.quetzi.bluepower.tileentities.TileBase;

/**
 * @author Koen Beckers (K4Unl)
 * 
 */
public class TileLamp extends TileBase {
    
    public int getPower() {
    
        int power = 0;
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            power = Math.max(power, RedstoneHelper.getInput(getWorldObj(), xCoord, yCoord, zCoord, d));
        }
        return power;
    }
    
    public int getLightLevel() {
    
        return 0;
    }
    
}
