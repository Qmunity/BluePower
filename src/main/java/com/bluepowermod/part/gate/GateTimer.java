package com.bluepowermod.part.gate;

import java.util.List;

import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.gui.gate.GuiGateSingleTime;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.part.IGuiButtonSensitive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateTimer extends GateBase implements IGuiButtonSensitive {
    
    private boolean power = false;
    private int     time  = 40;
    private int     ticks = 0;
    
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
    
        return "timer";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        // renderTopTexture(FaceDirection.FRONT, power);
        renderTopTexture(FaceDirection.LEFT, power);
        renderTopTexture(FaceDirection.RIGHT, power);
        renderTopTexture(FaceDirection.BACK, back.getPower() > 0);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 0, 13D / 16D, true);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -5D / 16D, 1D / 2D, power);
        RenderHelper.renderPointer(0, 7D / 16D, 0, getWorld() != null ? back.getPower() == 0 ? 1 - (double) (ticks + frame) / (double) time : 0 : 0);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 12D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (!getWorld().isRemote && getWorld().getWorldTime() % 400 == 0) sendUpdatePacket();//Prevent slow desyncing of the timer.
        power = false;
        
        if (back.getPower() == 0) {
            if (ticks++ >= time) {
                ticks = 0;
                power = true;
                playTickSound();
            }
        } else {
            ticks = 0;
        }
        
        left.setPower(power ? 15 : 0);
        front.setPower(power ? 15 : 0);
        right.setPower(power ? 15 : 0);
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setInteger("ticks", ticks);
        tag.setInteger("time", time);
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        ticks = tag.getInteger("ticks");
        time = tag.getInteger("time");
    }
    
    @Override
    public void onButtonPress(int messageId, int value) {
    
        time = value;
        sendUpdatePacket();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui() {
    
        return new GuiGateSingleTime(this) {
            
            @Override
            protected int getCurrentIntervalTicks() {
            
                return time;
            }
            
        };
    }
    
    @Override
    protected boolean hasGUI() {
    
        return true;
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    
        String t = "";
        
        int time = this.time * 50;
        if (time >= 1000) {
            t = time / 1000 + "." + time % 1000 + "s";
        } else {
            t = time + "ms";
        }
        
        info.add(I18n.format("gui.timerInterval") + ": " + SpecialChars.WHITE + t);
    }
}
