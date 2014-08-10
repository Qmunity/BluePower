package com.bluepowermod.part.gate;

import java.util.List;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.util.Refs;

public class GateWire extends GateBase {
    
    public static final String ID = "icWire";
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        front.enable();
        left.enable();
        back.enable();
        right.enable();
        
        front.setOutput();
        left.setOutput();
        back.setOutput();
        right.setOutput();
    }
    
    @Override
    public String getGateID() {
    
        return ID;
    }
    
    @Override
    protected void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        boolean isOn = false;
        for (FaceDirection dir : FaceDirection.values()) {
            if (getConnection(dir).isEnabled()) {
                isOn = getConnection(dir).getPower() > 0;
                break;
            }
        }
        this.renderTopTexture(FaceDirection.FRONT, front);
        renderTopTexture(FaceDirection.BACK, back);
        renderTopTexture(FaceDirection.LEFT, left);
        renderTopTexture(FaceDirection.RIGHT, right);
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getTextureName() + "/center_" + (isOn ? "on" : "off") + ".png");
    }
    
    @Override
    public boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (front.isEnabled() && left.isEnabled() && back.isEnabled() && right.isEnabled()) {
            right.disable();
        } else if (front.isEnabled() && left.isEnabled() && back.isEnabled()) {
            left.disable();
        } else if (front.isEnabled() && back.isEnabled()) {
            left.enable();
            front.disable();
        } else if (left.isEnabled() && back.isEnabled()) {
            front.enable();
            right.enable();
        }
        return true;
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        // TODO Auto-generated method stub
        
    }
    
}
