package com.bluepowermod.part.gate;

import com.bluepowermod.api.part.FaceDirection;
import com.bluepowermod.api.part.RedstoneConnection;
import com.bluepowermod.client.renderers.RenderHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;

import java.util.List;

/**
 * @Author Koen Beckers (K4Unl)
 */
public class GateRepeater extends GateBase {
    
    private boolean power = false;
    private boolean powerBack = false;
    private int ticksRemaining = 0;
    private int location = 0;
    private int[] ticks = {1, 2, 3, 4, 8, 16, 32, 64, 128, 256, 1024};
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front
        front.enable();
        front.setOutput();

        // Init back
        back.enable();
        back.setInput();
    }
    
    @Override
    public String getGateID() {
    
        return "repeater";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.FRONT, power);
        RenderHelper.renderRedstoneTorch(pixel*-3, pixel*2, pixel*-6, pixel * 8, !power);

        renderTopTexture(FaceDirection.BACK, powerBack);
        RenderHelper.renderRedstoneTorch(pixel*4, pixel*2, pixel * (5 - location), pixel * 8, (back.getPower() > 0));
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 9D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {

        if(powerBack != back.getPower() > 0 && ticksRemaining == 0){
            ticksRemaining = ticks[location];
        }
        powerBack = back.getPower() > 0;
        if(ticksRemaining == 0){
            power = powerBack;
        }else{
            ticksRemaining --;
        }

        front.setPower(power ? 15 : 0);
    }
    
    @Override
    protected boolean changeMode(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
        location++;
        if(location == 11){
            location = 0;
        }
        return true;
    }
    
    @Override
    public void addWailaInfo(List<String> info) {
    /*
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
                        */
    }

    public void save(NBTTagCompound tag) {
        tag.setInteger("location", location);
    }

    public void load(NBTTagCompound tag) {
        location = tag.getInteger("location");
    }
    
}
