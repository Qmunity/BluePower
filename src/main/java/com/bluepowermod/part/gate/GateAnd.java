package com.bluepowermod.part.gate;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Color;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

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
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.FRONT, !power);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -4D / 16D, 10D / 16D, power);
        
        renderTopTexture(FaceDirection.LEFT, left);
        RenderHelper.renderRedstoneTorch(-3D / 16D, 1D / 8D, 0, 8D / 16D, left.getPower() == 0 && left.isEnabled());
        renderTopTexture(FaceDirection.BACK, back);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 0, 8D / 16D, back.getPower() == 0 && back.isEnabled());
        renderTopTexture(FaceDirection.RIGHT, right);
        RenderHelper.renderRedstoneTorch(3D / 16D, 1D / 8D, 0, 8D / 16D, right.getPower() == 0 && right.isEnabled());
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 9D / 16D, 9D / 16D));
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
        } else {// right enabled
            left.enable();
            back.enable();
        }
        return true;
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        info.add(Color.YELLOW + I18n.format("gui.connections") + ":");
        info.add("  "
                + FaceDirection.LEFT.getLocalizedName()
                + ": "
                + (getConnection(FaceDirection.LEFT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED
                        + I18n.format("random.disabled")));
        info.add("  "
                + FaceDirection.BACK.getLocalizedName()
                + ": "
                + (getConnection(FaceDirection.BACK).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED
                        + I18n.format("random.disabled")));
        info.add("  "
                + FaceDirection.RIGHT.getLocalizedName()
                + ": "
                + (getConnection(FaceDirection.RIGHT).isEnabled() ? Color.GREEN + I18n.format("random.enabled") : Color.RED
                        + I18n.format("random.disabled")));
    }
    
}
