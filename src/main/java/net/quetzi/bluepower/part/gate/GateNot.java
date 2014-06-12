package net.quetzi.bluepower.part.gate;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.references.Refs;

public class GateNot extends GateBase {
    
    private boolean power = false;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front
        front.enable();
        front.setOutput();
        
        // Init left
        left.enable();
        left.setOutput();
        
        // Init back
        back.enable();
        back.setInput();
        
        // Init right
        right.enable();
        right.setOutput();
    }
    
    @Override
    public String getGateID() {
    
        return "not";
    }
    
    @Override
    public void renderTop(float frame) {
    
        renderTopItem();
    }
    
    @Override
    public void renderTopItem() {
        
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/top.png");
        if(!power){
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceFrontOn.png");
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceLeftOn.png");
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceRightOn.png");
        }else{
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceFrontOff.png");
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceLeftOff.png");
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceRightOff.png");
        }
        
        if(getConnection(FaceDirection.BACK).getPower() > 0){
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceBackOn.png");
        }else{
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/not/traceBackOff.png");
        }
        
        RenderHelper.renderRedstoneTorch(0, 1D/8D, -1D/16D, 9D/16D, !power);
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        power = back.isEnabled() && back.getPower() > 0;

        left.setPower(!power ? 15 : 0);
        front.setPower(!power ? 15 : 0);
        right.setPower(!power ? 15 : 0);
    }
    
    @Override
    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addCollisionBoxes(boxes);

        boxes.add(AxisAlignedBB.getBoundingBox(7D/16D, 1D/8D, 7D/16D, 9D/16D, 7D/16D, 9D/16D));
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);

        boxes.add(AxisAlignedBB.getBoundingBox(7D/16D, 1D/8D, 7D/16D, 9D/16D, 7D/16D, 9D/16D));
        
    }
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addSelectionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D/16D, 1D/8D, 7D/16D, 9D/16D, 7D/16D, 9D/16D));
    }
    
}
