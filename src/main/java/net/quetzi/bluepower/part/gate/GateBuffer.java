package net.quetzi.bluepower.part.gate;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.references.Refs;

public class GateBuffer extends GateBase {
    
    private boolean[] power = new boolean[] { false, false, false };
    
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
    
        return "buffer";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/center_" + (!power[1] ? "on" : "off") + ".png");
        renderTopTexture(FaceDirection.LEFT, power[0]);
        renderTopTexture(FaceDirection.RIGHT, power[0]);
        renderTopTexture(FaceDirection.BACK, back.getPower() > 0);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 0, 8D / 16D, !power[1]);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -4D / 16D, 10D / 16D, power[0]);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 8D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        power[2] = back.getPower() > 0;
        
        left.setPower(power[0] ? 15 : 0);
        front.setPower(power[0] ? 15 : 0);
        right.setPower(power[0] ? 15 : 0);
        
        power[0] = power[1];
        power[1] = power[2];
        power[2] = false;
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
    
}
