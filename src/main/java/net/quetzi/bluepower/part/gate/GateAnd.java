package net.quetzi.bluepower.part.gate;

import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.client.renderers.RenderHelper;

public class GateAnd extends GateBase {
    
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
    
        return "and";
    }
    
    @Override
    public void renderTopItem(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        renderTopTexture(FaceDirection.FRONT, power);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -3D / 16D, 9D / 16D, power);
        
        renderTopTexture(FaceDirection.LEFT, left);
        renderTopTexture(FaceDirection.BACK, back);
        renderTopTexture(FaceDirection.RIGHT, right);
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        power = true;
        
        if (left.isEnabled()) power &= left.getPower() > 0;
        if (back.isEnabled()) power &= back.getPower() > 0;
        if (right.isEnabled()) power &= right.getPower() > 0;
        
        front.setPower(power ? 15 : 0);
    }
    
    @Override
    protected boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (left.isEnabled() && back.isEnabled() && right.isEnabled()) {
            right.disable();
        } else if (left.isEnabled() && back.isEnabled()) {
            back.disable();
            right.enable();
        } else if (left.isEnabled() && right.isEnabled()) {
            left.disable();
            back.enable();
        } else if (back.isEnabled() && right.isEnabled()) {
            left.enable();
            back.disable();
            right.disable();
        } else if (left.isEnabled()) {
            left.disable();
            back.enable();
        } else if (back.isEnabled()) {
            back.disable();
            right.enable();
        } else {//right enabled
            left.enable();
            back.enable();
        }
        return true;
    }
    
}
