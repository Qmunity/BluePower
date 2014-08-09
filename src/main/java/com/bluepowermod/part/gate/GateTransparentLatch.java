package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Refs;

public class GateTransparentLatch extends GateBase {
    
    private boolean mirrored;
    
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
        right.setOutput();
    }
    
    @Override
    public String getGateID() {
    
        return "transparent";
    }
    
    @Override
    public void renderTop(float frame) {
    
        if (mirrored) {
            GL11.glPushMatrix();
            GL11.glTranslated(0.5, 0, 0.5);
            GL11.glScaled(-1, 1, 1);
            GL11.glTranslated(-0.5, 0, -0.5);
            
            GL11.glDisable(GL11.GL_CULL_FACE);
        }
        super.renderTop(frame);
        if (mirrored) {
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glPopMatrix();
        }
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.FRONT, front);
        renderTopTexture(FaceDirection.LEFT, mirrored ? right : left);
        renderTopTexture(FaceDirection.BACK, back);
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/leftcenter_" + (back.getPower() == 0 ? "on" : "off") + ".png");
        RenderHelper.renderRedstoneTorch(-4 / 16D, 1D / 8D, 4 / 16D, 8D / 16D, back.getPower() == 0);
        RenderHelper.renderRedstoneTorch(-4 / 16D, 1D / 8D, -1 / 16D, 8D / 16D, back.getPower() > 0 && front.getPower() == 0);
        RenderHelper.renderRedstoneTorch(-1 / 16D, 1D / 8D, -1 / 16D, 8D / 16D, back.getPower() == 0 && front.getPower() == 0);
        RenderHelper.renderRedstoneTorch(4 / 16D, 2D / 16D, -1 / 16D, 10D / 16D, front.getPower() > 0);
        RenderHelper.renderRedstoneTorch(-2 / 16D, 2D / 16D, -6 / 16D, 10D / 16D, front.getPower() > 0);
        renderTopTexture(FaceDirection.RIGHT, mirrored ? left : right);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 9D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (back.getPower() > 0) {
            front.setPower(mirrored ? right.getPower() : left.getPower());
            if (mirrored) {
                left.setPower(right.getPower());
            } else {
                right.setPower(left.getPower());
            }
        }
    }
    
    @Override
    protected boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        mirrored = !mirrored;
        if (mirrored) {
            left.setOutput();
            right.setInput();
        } else {
            left.setInput();
            right.setOutput();
        }
        return true;
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setBoolean("mirrored", mirrored);
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        mirrored = tag.getBoolean("mirrored");
        if (mirrored) {
            getConnection(FaceDirection.LEFT).setOutput();
            getConnection(FaceDirection.RIGHT).setInput();
        } else {
            getConnection(FaceDirection.LEFT).setInput();
            getConnection(FaceDirection.RIGHT).setOutput();
        }
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
    
}
