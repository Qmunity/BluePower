package net.quetzi.bluepower.compat.fmp;

import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.IBPFacePart;
import codechicken.multipart.TFacePart;

public class MultipartFaceBPPart extends MultipartBPPart implements TFacePart {
    
    private IBPFacePart facePart;
    
    public MultipartFaceBPPart(IBPFacePart part) {
        super((BPPart) part);
        facePart = part;
    }
    
    @Override
    public int getSlotMask() {
    
        return 1 << this.facePart.getFace();
    }
    
    @Override
    public int redstoneConductionMap() {
    
        return 0;
    }
    
    @Override
    public boolean solid(int side) {
    
        return false;
    }
    
}
