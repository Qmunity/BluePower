package net.quetzi.bluepower.part.gate;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.client.renderers.RenderHelper;

public class GateOr extends GateBase {
    
    private boolean power = false;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front
        front.enable();
        front.setOutput();
        
        // Init left
        left.enable();
        left.setInput();
        
        // Init back
        back.enable();
        back.setInput();
        
        // Init right
        right.enable();
        right.setInput();
    }
    
    @Override
    public String getGateID() {
    
        return "or";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.FRONT, !power);
        renderTopTexture(FaceDirection.LEFT, left.getPower() > 0);
        renderTopTexture(FaceDirection.RIGHT, right.getPower() > 0);
        renderTopTexture(FaceDirection.BACK, back.getPower() > 0);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 0, 9D / 16D, !power);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -6D / 16D, 9D / 16D, power);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 8D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        power = back.getPower() > 0 || left.getPower() > 0 || right.getPower() > 0;
        
        front.setPower(power ? 15 : 0);
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
    
}
