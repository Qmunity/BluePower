package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;

public class GateLightCell extends GateBase {
    
    private final boolean power = false;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front
        front.enable();
        front.setOutput();
    }
    
    @Override
    public String getGateID() {
    
        return "lightCell";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.FRONT, front);
        // renderTopTexture(FaceDirection.LEFT, !power);
        // renderTopTexture(FaceDirection.RIGHT, !power);
        // renderTopTexture(FaceDirection.BACK, back.getPower() > 0);
        // RenderHelper.renderRedstoneTorch(0, 1D / 8D, 0, 9D / 16D, !power);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 8D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        int light = getWorld().getBlockLightValue(getX(), getY(), getZ());
        front.setPower(light);
        /*
         * power = back.getPower() > 0;
         * 
         * left.setPower(!power ? 15 : 0); front.setPower(!power ? 15 : 0); right.setPower(!power ? 15 : 0);
         */
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
    
}
