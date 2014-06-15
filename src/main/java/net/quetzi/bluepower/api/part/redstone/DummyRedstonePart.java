package net.quetzi.bluepower.api.part.redstone;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.util.ForgeDirectionUtils;

public class DummyRedstonePart implements IBPRedstonePart {
    
    private IBlockAccess w;
    private int          x, y, z;
    private Block        b;
    
    public DummyRedstonePart(IBlockAccess w, int x, int y, int z) {
    
        this.w = w;
        this.x = x;
        this.y = y;
        this.z = z;
        
        b = w.getBlock(x, y, z);
    }
    
    @Override
    public boolean canConnect(ForgeDirection side) {
    
        return b.canConnectRedstone(w, x, y, z, ForgeDirectionUtils.getSide(side));
    }
    
    @Override
    public int getStrongOutput(ForgeDirection side) {
    
        return b.isProvidingStrongPower(w, x, y, z, ForgeDirectionUtils.getSide(side));
    }
    
    @Override
    public int getWeakOutput(ForgeDirection side) {
    
        return b.isProvidingWeakPower(w, x, y, z, ForgeDirectionUtils.getSide(side));
    }
    
    @Override
    public List<IBPRedstonePart> getConnections(ForgeDirection side) {
    
        return null;
    }
    
    private RedstoneNetwork net;

    @Override
    public RedstoneNetwork getNetwork() {
    
        return net;
    }

    @Override
    public void setNetwork(RedstoneNetwork network) {
        this.net = network;
    }
    
}
