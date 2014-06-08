package net.quetzi.bluepower.api.part;

import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.vec.Vector3;

public abstract class BPPartFace extends BPPart implements IBPFacePart, IBPRedstonePart {
    
    @Override
    public int getFace() {
    
        return 0;// TODO: Determine what face the BPPartFace is on
    }
    
    @Override
    public boolean canStay() {
    
        Vector3 v = new Vector3(this.x, this.y, this.z, this.world).getRelative(ForgeDirection.getOrientation(getFace()));
        return !v.isBlock(null, true);
    }
    
    @Override
    public final boolean canConnect(ForgeDirection side) {
    
        return false;
    }
    
    @Override
    public final int getStrongOutput(ForgeDirection side) {
    
        return 0;
    }
    
    @Override
    public final int getWeakOutput(ForgeDirection side) {
    
        return 0;
    }
    
}
