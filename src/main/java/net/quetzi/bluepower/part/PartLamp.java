package net.quetzi.bluepower.part;

import java.util.List;

import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.part.BPPartFace;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.api.vec.Vector3;
import net.quetzi.bluepower.helper.RedstoneHelper;

import org.lwjgl.opengl.GL11;

public class PartLamp extends BPPartFace {
    
    private String colorName;
    private int    colorVal;
    
    private int    power = 0;
    
    public PartLamp(String colorName, Integer colorVal) {
    
        this.colorName = colorName;
        this.colorVal = colorVal.intValue();
        
        for (int i = 0; i < 4; i++)
            connections[i] = new RedstoneConnection(this, i + "", true, false);
        
        for (RedstoneConnection c : connections) {
            c.enable();
            c.setInput();
        }
    }
    
    @Override
    public String getType() {
    
        return "lamp" + colorName;
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "lamp." + colorName;
    }
    
    @Override
    public void addCollisionBoxes(List<AxisAlignedBB> boxes) {
    
        addSelectionBoxes(boxes);
    }
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(AxisAlignedBB.getBoundingBox(1D / 4D, 0, 1D / 4D, 3D / 4D, 3D / 8D, 3D / 4D));
    }
    
    @Override
    public void addOcclusionBoxes(List<AxisAlignedBB> boxes) {
    
        addSelectionBoxes(boxes);
    }
    
    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {
    
        GL11.glPushMatrix();
        {
            super.renderDynamic(loc, pass, frame);
            
            // Render base here
            
            // Color multiplier
            int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int r = (colorVal & redMask) >> 16;
            int g = (colorVal & greenMask) >> 8;
            int b = (colorVal & blueMask);
            GL11.glColor4d(r / 256D, g / 256D, b / 256D, 1);
            
            // Render lamp itself here
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public int getLightValue() {
    
        return power;
    }
    
    @Override
    public void update() {
    
        super.update();
        
        int old = power;
        
        power = 0;
        for(ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            power = Math.max(power, RedstoneHelper.getInput(world, x, y, z, d));
        
        if(old != power){
            notifyUpdate();
            world.updateLightByType(EnumSkyBlock.Block, x, y, z);
        }
    }
    
    
    
}
