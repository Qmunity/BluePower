package net.quetzi.bluepower.api.part.redstone;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

public interface IBPRedstoneConductor {
    
    public List<IBPRedstonePart> getConnections(ForgeDirection side);
    
    public RedstoneNetwork getNetwork();
    
    public void setNetwork(RedstoneNetwork network);
    
    public void update();
    
}
