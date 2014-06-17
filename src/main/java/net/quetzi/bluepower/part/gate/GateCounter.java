package net.quetzi.bluepower.part.gate;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.client.gui.gate.GuiGateCounter;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.part.IGuiButtonSensitive;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateCounter extends GateBase implements IGuiButtonSensitive {
    
    private int count = 0, max = 10, increment = 1, decrement = 1;
    
    private boolean wasOnLeft = false, wasOnRight = false;
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        // Init front (+ out)
        front.enable();
        front.setOutput();
        
        // Init left (- in)
        left.enable();
        left.setInput();
        
        // Init back (- out)
        back.enable();
        back.setOutput();
        
        // Init right (+ in)
        right.enable();
        right.setInput();
    }
    
    @Override
    public String getGateID() {
    
        return "counter";
    }
    
    @Override
    public void renderTop(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right, float frame) {
    
        renderTopTexture(FaceDirection.LEFT, left.getPower() > 0);
        renderTopTexture(FaceDirection.RIGHT, right.getPower() > 0);
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/centerleft_" + (left.getPower() > 0 ? "on" : "off") +  ".png");
        renderTopTexture(Refs.MODID + ":textures/blocks/gates/" + getType() + "/centerright_" + (left.getPower() > 0 ? "on" : "off") +  ".png");
        RenderHelper.renderRedstoneTorch(2/16D, 1D / 8D, 0, 13D / 16D, true);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, 5D/16D, 8D / 16D, count == 0);
        RenderHelper.renderRedstoneTorch(0, 1D / 8D, -5D/16D, 8D / 16D, count == max);
        GL11.glPushMatrix();
        {
            GL11.glTranslated(2/16D, 0, 0);
            double min = 0.555;
            double max = 0.385;
            
            double angle = min + (max * (count / ((double)this.max)));
            
            RenderHelper.renderPointer(0, 7/16D, 0, -angle);
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        super.addOcclusionBoxes(boxes);
        
        boxes.add(AxisAlignedBB.getBoundingBox(7D / 16D, 2D / 16D, 7D / 16D, 9D / 16D, 8D / 16D, 9D / 16D));
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        if(left.getPower() > 0 && !wasOnLeft){
            wasOnLeft = true;
            count -= decrement;
        }
        if(left.getPower() == 0)
            wasOnLeft = false;
    
        if(right.getPower() > 0 && !wasOnRight){
            wasOnRight = true;
            count += increment;
        }
        if(right.getPower() == 0)
            wasOnRight = false;

        count = Math.max(Math.min(count, max), 0);
        increment = Math.max(Math.min(increment, max), 0);
        decrement = Math.max(Math.min(decrement, max), 0);
        
        front.setPower(count == max ? 15 : 0);
        back.setPower(count == 0 ? 15 : 0);
    }
    
    @Override
    public void save(NBTTagCompound tag) {
    
        super.save(tag);
        tag.setInteger("count", count);
        tag.setInteger("max", max);
        tag.setInteger("increment", increment);
        tag.setInteger("decrement", decrement);
        
        tag.setBoolean("left", wasOnLeft);
        tag.setBoolean("right", wasOnRight);
    }
    
    @Override
    public void load(NBTTagCompound tag) {
    
        super.load(tag);
        count = tag.getInteger("count");
        max = tag.getInteger("max");
        increment = tag.getInteger("increment");
        decrement = tag.getInteger("decrement");

        wasOnLeft = tag.getBoolean("left");
        wasOnRight = tag.getBoolean("right");
    }
    
    @Override
    public void onButtonPress(int messageId, int value) {
    
        switch (messageId) {
            case 0:
                max = value;
                break;
            case 1:
                increment = value;
                break;
            case 2:
                decrement = value;
                break;
        }
        sendUpdatePacket();
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui() {
        
        System.out.println(count + " - " + max);
    
        return new GuiGateCounter(this) {
            
            @Override
            protected int getCurrentMax() {
            
                return max;
            }
            
            @Override
            protected int getCurrentIncrement() {
            
                return increment;
            }
            
            @Override
            protected int getCurrentDecrement() {
            
                return decrement;
            }
            
        };
    }
    
    @Override
    protected boolean hasGUI() {
    
        return true;
    }
    
}
