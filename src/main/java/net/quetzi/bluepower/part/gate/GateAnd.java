package net.quetzi.bluepower.part.gate;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.references.Refs;

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
    public void renderTop(float frame) {
    
        renderTopItem();
    }
    
    @Override
    public void renderTopItem() {
    
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/top.png");
        if (!power) {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceFrontOn.png");
        } else {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceFrontOff.png");
        }
        
        if (getConnection(FaceDirection.LEFT).getPower() > 0) {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceLeftOn.png");
        } else {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceLeftOff.png");
        }
        
        if (getConnection(FaceDirection.BACK).getPower() > 0) {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceBackOn.png");
        } else {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceBackOff.png");
        }
        
        if (getConnection(FaceDirection.RIGHT).getPower() > 0) {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceRightOn.png");
        } else {
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceRightOff.png");
        }
        
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -1D / 16D, 9D / 16D, !power);
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
    
    @Override
    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addCollisionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 1D / 8D, 7D / 16D, 9D / 16D, 7D / 16D, 9D / 16D));
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 1D / 8D, 7D / 16D, 9D / 16D, 7D / 16D, 9D / 16D));
        
    }
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addSelectionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 1D / 8D, 7D / 16D, 9D / 16D, 7D / 16D, 9D / 16D));
    }
    
}
