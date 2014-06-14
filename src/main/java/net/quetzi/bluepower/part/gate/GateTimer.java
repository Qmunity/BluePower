package net.quetzi.bluepower.part.gate;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.client.gui.gate.GuiGateSingleTime;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.part.IGuiButtonSensitive;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateTimer extends GateBase implements IGuiButtonSensitive {
    
    private boolean power = false;
    private int     start = -1;
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
    
        //renderTopTexture(FaceDirection.FRONT, power);
        renderTopTexture(FaceDirection.LEFT, power);
        renderTopTexture(FaceDirection.RIGHT, power);
        renderTopTexture(FaceDirection.BACK, back.getPower() > 0);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 0, 13D / 16D, true);
        RenderHelper.renderPointer(0, 7D / 16D, 0, world != null ? start >= 0 ? 1 - (double) (ticks - start + frame) / (double) time : 0 : 0);
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if (power) power = false;
        
        power = false;
        
        if (back.getPower() > 0) {
            start = -1;
        }
        
        if (start == -1 && back.getPower() == 0) {
            start = ticks;
        }
        
        if (start >= 0) {
            if (ticks >= start + time) {
                if (back.getPower() > 0) {
                    start = -1;
                } else {
                    start = ticks;
                }
                power = true;
            }
        }
        
        left.setPower(power ? 15 : 0);
        front.setPower(power ? 15 : 0);
        right.setPower(power ? 15 : 0);
        
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
    
}
