package com.bluepowermod.part.gate;

import java.util.Arrays;
import java.util.List;

import mcp.mobius.waila.api.SpecialChars;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.gui.gate.GuiGateSingleTime;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.part.IGuiButtonSensitive;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateSequencer extends GateBase implements IGuiButtonSensitive {
    
    private final boolean[] power = new boolean[4];
    private int             start = -1;
    private int             time  = 160;
    private int             ticks = 0;
    
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
        back.setOutput();
        
        // Init right
        right.enable();
        right.setOutput();
    }
    
    @Override
    public String getGateID() {
    
        return "sequencer";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -5D / 16D, 8D / 16D, power[1]);
        RenderHelper.renderRedstoneTorch(5D / 16D, 1D / 8D, 0, 8D / 16D, power[2]);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 5D / 16D, 8D / 16D, power[3]);
        RenderHelper.renderRedstoneTorch(-5D / 16D, 1D / 8D, 0, 8D / 16D, power[0]);
        
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 0, 13D / 16D, true);
        RenderHelper.renderPointer(0, 7D / 16D, 0, getWorld() != null ? start >= 0 ? 1 - (double) (ticks - start + frame) / (double) time : 0 : 0);
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 12D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        Arrays.fill(power, false);
        
        if (start >= 0) {
            if (ticks >= start + time) {
                start = ticks;
            }
        } else {
            start = ticks;
        }
        
        double t = (double) (ticks - start) / (double) time;
        
        if (t >= 1D / 8D && t < 3D / 8D) {
            power[2] = true;
            if (right.getPower() == 0) playTickSound();
        } else if (t >= 3D / 8D && t < 5D / 8D) {
            power[3] = true;
            if (back.getPower() == 0) playTickSound();
        } else if (t >= 5D / 8D && t < 7D / 8D) {
            power[0] = true;
            if (left.getPower() == 0) playTickSound();
        } else if (t >= 7D / 8D && t < 1 || t >= 0 && t < 1D / 8D) {
            power[1] = true;
            if (front.getPower() == 0) playTickSound();
        }
        
        left.setPower(power[0] ? 15 : 0);
        front.setPower(power[1] ? 15 : 0);
        right.setPower(power[2] ? 15 : 0);
        back.setPower(power[3] ? 15 : 0);
        
        ticks++;
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setInteger("start", start);
        tag.setInteger("ticks", ticks);
        tag.setInteger("time", time);
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        start = tag.getInteger("start");
        ticks = tag.getInteger("ticks");
        time = tag.getInteger("time");
    }
    
    @Override
    public void onButtonPress(int messageId, int value) {
    
        time = value * 4;
        sendUpdatePacket();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGui() {
    
        return new GuiGateSingleTime(this) {
            
            @Override
            protected int getCurrentIntervalTicks() {
            
                return time / 4;
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
        
        int time = this.time / 4 * 50;
        if (time >= 1000) {
            t = time / 1000 + "." + time % 1000 + "s";
        } else {
            t = time + "ms";
        }
        
        info.add(I18n.format("gui.timerInterval") + ": " + SpecialChars.WHITE + t);
    }
}
