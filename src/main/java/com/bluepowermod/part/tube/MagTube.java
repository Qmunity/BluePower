package com.bluepowermod.part.tube;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.renderers.IconSupplier;

/**
 * @author MineMaarten
 */
public class MagTube extends PneumaticTube {
    
    @Override
    public String getType() {
    
        return "magTube";
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "magTube";
    }
    
    @Override
    protected IIcon getSideIcon() {
    
        return IconSupplier.magTubeSide;
    }
    
    @Override
    protected IIcon getNodeIcon() {
    
        return IconSupplier.magTubeNode;
    }
    
    @Override
    protected boolean canConnectToInventories() {
    
        return false;
    }
    
    @Override
    public List<AxisAlignedBB> getSelectionBoxes() {
    
        List<AxisAlignedBB> aabbs = super.getSelectionBoxes();
        if (!shouldRenderNode()) {
            if (connections[0]) {
                aabbs.add(AxisAlignedBB.getBoundingBox(2 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 6 / 16D, 14 / 16D));
                aabbs.add(AxisAlignedBB.getBoundingBox(2 / 16D, 10 / 16D, 2 / 16D, 14 / 16D, 14 / 16D, 14 / 16D));
            } else if (connections[2]) {
                aabbs.add(AxisAlignedBB.getBoundingBox(2 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 14 / 16D, 6 / 16D));
                aabbs.add(AxisAlignedBB.getBoundingBox(2 / 16D, 2 / 16D, 10 / 16D, 14 / 16D, 14 / 16D, 14 / 16D));
            } else if (connections[4]) {
                aabbs.add(AxisAlignedBB.getBoundingBox(2 / 16D, 2 / 16D, 2 / 16D, 6 / 16D, 14 / 16D, 14 / 16D));
                aabbs.add(AxisAlignedBB.getBoundingBox(10 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 14 / 16D, 14 / 16D));
            }
        }
        return aabbs;
    }
    
    /**
     * Render method that works, and now should be buried under the ground so no-one looks at it
     */
    @Override
    protected void renderSide() {
    
        Tessellator t = Tessellator.instance;
        t.draw();
        
        GL11.glPushMatrix();
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        if (connections[2]) {
            GL11.glRotated(90, 1, 0, 0);
        } else if (connections[4]) {
            GL11.glRotated(90, 0, 0, 1);
        }
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        
        t.startDrawingQuads();
        
        double min = 2 / 16D;
        double max = 14 / 16D;
        double inMin = 12.001 / 16D;
        double inMax = 3.999 / 16D;
        
        IIcon icon = IconSupplier.magCoilSide;
        
        double minX = icon.getInterpolatedU(min * 16);
        double maxX = icon.getInterpolatedU(max * 16);
        double minY = icon.getInterpolatedV(min * 16);
        double maxY = icon.getInterpolatedV(max * 16);
        
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(min, min, max, maxX, maxY);// maxZ
        t.addVertexWithUV(max, min, max, minX, maxY);
        t.addVertexWithUV(max, max, max, minX, minY);
        t.addVertexWithUV(min, max, max, maxX, minY);
        
        t.addVertexWithUV(min, min, inMax, maxX, maxY);// inside maxZ
        t.addVertexWithUV(max, min, inMax, minX, maxY);
        t.addVertexWithUV(max, max, inMax, minX, minY);
        t.addVertexWithUV(min, max, inMax, maxX, minY);
        
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(min, min, min, minX, maxY);// minZ
        t.addVertexWithUV(min, max, min, minX, minY);
        t.addVertexWithUV(max, max, min, maxX, minY);
        t.addVertexWithUV(max, min, min, maxX, maxY);
        
        t.addVertexWithUV(min, min, inMin, maxX, maxY);// inside minZ
        t.addVertexWithUV(min, max, inMin, minX, minY);
        t.addVertexWithUV(max, max, inMin, minX, minY);
        t.addVertexWithUV(max, min, inMin, maxX, maxY);
        
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(min, min, min, maxX, maxY);// minX
        t.addVertexWithUV(min, min, max, minX, maxY);
        t.addVertexWithUV(min, max, max, minX, minY);
        t.addVertexWithUV(min, max, min, maxX, minY);
        
        t.addVertexWithUV(inMin, min, min, maxX, maxY);// inside minX
        t.addVertexWithUV(inMin, min, max, minX, maxY);
        t.addVertexWithUV(inMin, max, max, minX, minY);
        t.addVertexWithUV(inMin, max, min, maxX, minY);
        
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(max, min, min, minX, minY);// maxX
        t.addVertexWithUV(max, max, min, minX, maxY);
        t.addVertexWithUV(max, max, max, maxX, maxY);
        t.addVertexWithUV(max, min, max, maxX, minY);
        
        t.addVertexWithUV(inMax, min, min, maxX, minY);// maxX
        t.addVertexWithUV(inMax, max, min, minX, maxY);
        t.addVertexWithUV(inMax, max, max, minX, maxY);
        t.addVertexWithUV(inMax, min, max, maxX, minY);
        
        icon = IconSupplier.magCoilFront;
        minX = icon.getInterpolatedU(min * 16);
        maxX = icon.getInterpolatedU(max * 16);
        minY = icon.getInterpolatedV(min * 16);
        maxY = icon.getInterpolatedV(max * 16);
        for (int i = 2; i < 16; i += 8) {
            t.setNormal(0, 1, 0);
            t.addVertexWithUV(min, 1 - i / 16D, min, maxX, maxY);// maxY
            t.addVertexWithUV(min, 1 - i / 16D, max, minX, maxY);
            t.addVertexWithUV(max, 1 - i / 16D, max, minX, minY);
            t.addVertexWithUV(max, 1 - i / 16D, min, maxX, minY);
            
            t.setNormal(0, -1, 0);
            t.addVertexWithUV(min, i / 16D, min, maxX, maxY);// minY
            t.addVertexWithUV(max, i / 16D, min, minX, maxY);
            t.addVertexWithUV(max, i / 16D, max, minX, minY);
            t.addVertexWithUV(min, i / 16D, max, maxX, minY);
        }
        t.draw();
        GL11.glPopMatrix();
        t.startDrawingQuads();
    }
    
    @Override
    public void update() {
    
        super.update();
        
        TubeLogic logic = getLogic();
        for (TubeStack stack : logic.tubeStacks)
            stack.setSpeed(Math.max(stack.getSpeed() - 1 / 32D, 1 / 16D));
    }
    
    @Override
    public float getHardness() {
    
        return 1.5F;
    }
    
}

/*
 * 
 * Code for when a real static render is applied, and no changes of render states are allowed. This is not finished
 * 
 * @Override protected void renderSide() {
 * 
 * double min = 2 / 16D; double max = 14 / 16D;
 * 
 * Tessellator t = Tessellator.instance; IIcon icon = IconSupplier.magCoilSide;
 * 
 * double minX = icon.getInterpolatedU(min * 16); double maxX = icon.getInterpolatedU(max * 16); double minY = icon.getInterpolatedV(min * 16); double
 * maxY = icon.getInterpolatedV(max * 16);
 * 
 * if (connections[0]) { t.setNormal(0, 0, 1); t.addVertexWithUV(min, min, max, maxX, maxY);// maxZ t.addVertexWithUV(max, min, max, minX, maxY);
 * t.addVertexWithUV(max, max, max, minX, minY); t.addVertexWithUV(min, max, max, maxX, minY);
 * 
 * t.setNormal(0, 0, -1); t.addVertexWithUV(min, min, min, maxX, maxY);// minZ t.addVertexWithUV(min, max, min, minX, minY); t.addVertexWithUV(max,
 * max, min, minX, minY); t.addVertexWithUV(max, min, min, maxX, maxY);
 * 
 * t.setNormal(-1, 0, 0); t.addVertexWithUV(min, min, min, maxX, maxY);// minX t.addVertexWithUV(min, min, max, minX, maxY); t.addVertexWithUV(min,
 * max, max, minX, minY); t.addVertexWithUV(min, max, min, maxX, minY);
 * 
 * t.setNormal(1, 0, 0); t.addVertexWithUV(max, min, min, maxX, minY);// maxX t.addVertexWithUV(max, max, min, minX, maxY); t.addVertexWithUV(max,
 * max, max, minX, maxY); t.addVertexWithUV(max, min, max, maxX, minY); } else if (connections[2]) { t.setNormal(0, -1, 0); t.addVertexWithUV(min,
 * minY, min, maxX, maxY);// minY t.addVertexWithUV(max, minY, min, minX, maxY); t.addVertexWithUV(max, minY, max, minX, minY); t.addVertexWithUV(min,
 * minY, max, maxX, minY);
 * 
 * t.setNormal(0, 1, 0); t.addVertexWithUV(min, max, min, maxX, maxY);// maxY t.addVertexWithUV(min, max, max, minX, minY); t.addVertexWithUV(max,
 * max, max, minX, minY); t.addVertexWithUV(max, max, min, maxX, maxY);
 * 
 * t.setNormal(-1, 0, 0); t.addVertexWithUV(min, min, min, maxX, maxY);// minX t.addVertexWithUV(min, min, max, minX, minY); t.addVertexWithUV(min,
 * max, max, minX, minY); t.addVertexWithUV(min, max, min, maxX, maxY);
 * 
 * t.setNormal(1, 0, 0); t.addVertexWithUV(max, min, min, maxX, minY);// maxX t.addVertexWithUV(max, max, min, minX, minY); t.addVertexWithUV(max,
 * max, max, minX, maxY); t.addVertexWithUV(max, min, max, maxX, maxY);
 * 
 * } else if (connections[4]) { t.setNormal(0, 0, 1); t.addVertexWithUV(min, min, max, maxX, maxY);// maxZ t.addVertexWithUV(max, min, max, minX,
 * minY); t.addVertexWithUV(max, max, max, minX, minY); t.addVertexWithUV(min, max, max, maxX, maxY);
 * 
 * t.setNormal(0, 0, -1); t.addVertexWithUV(min, min, min, maxX, maxY);// minZ t.addVertexWithUV(min, max, min, minX, maxY); t.addVertexWithUV(max,
 * max, min, minX, minY); t.addVertexWithUV(max, min, min, maxX, minY);
 * 
 * t.setNormal(0, 1, 0); t.addVertexWithUV(min, max, min, maxX, maxY);// maxY t.addVertexWithUV(min, max, max, minX, maxY); t.addVertexWithUV(max,
 * max, max, minX, minY); t.addVertexWithUV(max, max, min, maxX, minY);
 * 
 * t.setNormal(0, -1, 0); t.addVertexWithUV(min, min, min, maxX, minY);// minY t.addVertexWithUV(max, min, min, minX, maxY); t.addVertexWithUV(max,
 * min, max, minX, maxY); t.addVertexWithUV(min, min, max, maxX, minY); }
 * 
 * icon = IconSupplier.magCoilFront; minX = icon.getInterpolatedU(min * 16); maxX = icon.getInterpolatedU(max * 16); minY = icon.getInterpolatedV(min
 * * 16); maxY = icon.getInterpolatedV(max * 16); for (int i = 2; i < 16; i += 8) { if (connections[0]) { t.setNormal(0, 1, 0); t.addVertexWithUV(min,
 * 1 - i / 16D, min, maxX, maxY);// maxY t.addVertexWithUV(min, 1 - i / 16D, max, minX, maxY); t.addVertexWithUV(max, 1 - i / 16D, max, minX, minY);
 * t.addVertexWithUV(max, 1 - i / 16D, min, maxX, minY);
 * 
 * t.setNormal(0, -1, 0); t.addVertexWithUV(min, i / 16D, min, maxX, maxY);// minY t.addVertexWithUV(max, i / 16D, min, minX, maxY);
 * t.addVertexWithUV(max, i / 16D, max, minX, minY); t.addVertexWithUV(min, i / 16D, max, maxX, minY); } else if (connections[2]) { t.setNormal(0, 0,
 * 1); t.addVertexWithUV(min, min, 1 - i / 16D, maxX, maxY);// maxZ t.addVertexWithUV(max, min, 1 - i / 16D, minX, maxY); t.addVertexWithUV(max, max,
 * 1 - i / 16D, minX, minY); t.addVertexWithUV(min, max, 1 - i / 16D, maxX, minY);
 * 
 * t.setNormal(0, 0, -1); t.addVertexWithUV(min, min, i / 16D, maxX, maxY);//minZ t.addVertexWithUV(min, max, i / 16D, minX, maxY);
 * t.addVertexWithUV(max, max, i / 16D, minX, minY); t.addVertexWithUV(max, min, i / 16D, maxX, minY); } else if (connections[4]) { t.setNormal(-1, 0,
 * 0); t.addVertexWithUV(i / 16D, min, min, maxX, maxY);// minX t.addVertexWithUV(i / 16D, min, max, minX, maxY); t.addVertexWithUV(i / 16D, max, max,
 * minX, minY); t.addVertexWithUV(i / 16D, max, min, maxX, minY);
 * 
 * t.setNormal(1, 0, 0); t.addVertexWithUV(1 - i / 16D, min, min, maxX, minY);// maxX t.addVertexWithUV(1 - i / 16D, max, min, minX, minY);
 * t.addVertexWithUV(1 - i / 16D, max, max, minX, maxY); t.addVertexWithUV(1 - i / 16D, min, max, maxX, maxY); } } }
 */

