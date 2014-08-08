package com.bluepowermod.part.gate;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Refs;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

/**
 * @Author Koen Beckers (K4Unl)
 */
public class GateXnor extends GateBase {
    
    private boolean q = false;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front
        front.enable();
        front.setOutput();
        
        // Init left
        left.enable();
        left.setInput();
        
        // Init right
        right.enable();
        right.setInput();
    }
    
    @Override
    public String getGateID() {
    
        return "xnor";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {


        renderTopTexture(FaceDirection.LEFT, left.getPower() > 0);
        renderTopTexture(FaceDirection.RIGHT, right.getPower() > 0);
        boolean lp = left.getPower() > 0;
        boolean rp = right.getPower() > 0;

        //Right
        if(!rp && lp){
            RenderHelper.renderRedstoneTorch(pixel * 4, pixel*2, 0, 9D/16D, true);
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/frontright_on.png");
        }else{
            RenderHelper.renderRedstoneTorch(pixel * 4, pixel*2, 0, 9D/16D, false);
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/frontright_off.png");
        }

        //Left
        if(!lp && rp){
            RenderHelper.renderRedstoneTorch(pixel * -4, pixel*2, 0, 9D/16D, true);
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/frontleft_on.png");
        }else{
            RenderHelper.renderRedstoneTorch(pixel * -4, pixel*2, 0, 9D/16D, false);
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/frontleft_off.png");
        }



        if(!lp && !rp){
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/center_on.png");
            RenderHelper.renderRedstoneTorch(0, pixel*2, pixel * 4, 9D/16D, true);
        }else{
            renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/center_off.png");
            RenderHelper.renderRedstoneTorch(0, pixel*2, pixel * 4, 9D/16D, false);
        }
        RenderHelper.renderRedstoneTorch(0, pixel*2, pixel * -4, pixel*10, q);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 8D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {

        boolean p1 = right.getPower() > 0;
        boolean p2 = left.getPower() > 0;
        q = (p1 && p2) || (!p1 && !p2);
        
        front.setPower(q ? 15 : 0);
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
    
}
