package net.quetzi.bluepower.compat.fmp;

import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.IBPFacePart;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.TFacePart;
import codechicken.multipart.TileMultipart;

public class MultipartFaceBPPart extends MultipartBPPart implements TFacePart, IFaceRedstonePart {
    
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
    
    @Override
    public int getFace() {
    
        return facePart.getFace();
    }
    
    @Override
    public void onNeighborChanged() {
    
        if (!facePart.canStay()) {
            for (ItemStack is : getPart().getDrops()) {
                TileMultipart.dropItem(is, world(), Vector3.fromTileEntityCenter(tile()));
            }
            tile().remPart(this);
            return;
        }
        
        super.onNeighborChanged();
    }
    
    @Override
    public void update() {
    
        super.update();
    }
    
}
