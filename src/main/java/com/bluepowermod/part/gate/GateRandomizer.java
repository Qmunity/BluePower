package com.bluepowermod.part.gate;

import java.util.List;
import java.util.Random;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.util.Refs;

public class GateRandomizer extends GateBase {
    
    private static final Random random = new Random();
    
    private int                 ticks  = 0;
    
    private boolean             out[]  = new boolean[3];
    
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
    
        return "randomizer";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.LEFT, out[0]);
        renderTopTexture(FaceDirection.FRONT, out[1]);
        renderTopTexture(FaceDirection.RIGHT, out[2]);
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/center_" + (back.getPower() > 0 ? "on" : "off") + ".png");
        
        RenderHelper.renderRandomizerButton(this, -4 / 16D, 0, 6 / 16D, out[0]);
        RenderHelper.renderRandomizerButton(this, 0, 0, 0, out[1]);
        RenderHelper.renderRandomizerButton(this, 4 / 16D, 0, 6 / 16D, out[2]);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 8D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (!getWorld().isRemote) {
            if (back.getPower() > 0) {
                if (ticks % 5 == 0) {
                    out[0] = random.nextBoolean();
                    out[1] = random.nextBoolean();
                    out[2] = random.nextBoolean();
                    left.setPower(out[0] ? 15 : 0);
                    front.setPower(out[1] ? 15 : 0);
                    right.setPower(out[2] ? 15 : 0);
                    sendUpdatePacket();
                }
                ticks++;
            } else {
                ticks = 0;
            }
        }
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setBoolean("out_0", out[0]);
        tag.setBoolean("out_1", out[1]);
        tag.setBoolean("out_2", out[2]);
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        out[0] = tag.getBoolean("out_0");
        out[1] = tag.getBoolean("out_1");
        out[2] = tag.getBoolean("out_2");
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
    }
}
