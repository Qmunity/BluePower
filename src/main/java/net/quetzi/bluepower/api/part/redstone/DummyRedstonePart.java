package net.quetzi.bluepower.api.part.redstone;

import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;


public class DummyRedstonePart implements IBPRedstonePart {
    
    
    public DummyRedstonePart() {
    
    }

    @Override
    public boolean canConnect(ForgeDirection side) {
    
        return false;
    }

    @Override
    public int getStrongOutput(ForgeDirection side) {
    
        return 0;
    }

    @Override
    public int getWeakOutput(ForgeDirection side) {
    
        return 0;
    }

    @Override
    public List<IBPRedstonePart> getConnections(ForgeDirection side) {
    
        return null;
    }
    
}
