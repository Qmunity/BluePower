package net.quetzi.bluepower.api.part;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBPRedstonePart {
    
    public boolean canConnect(ForgeDirection side);
    
    public int getStrongOutput(ForgeDirection side);
    
    public int getWeakOutput(ForgeDirection side);
    
}
