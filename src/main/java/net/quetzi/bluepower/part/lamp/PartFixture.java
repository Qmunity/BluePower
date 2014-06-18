package net.quetzi.bluepower.part.lamp;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.client.renderers.IconSupplier;
import net.quetzi.bluepower.client.renderers.RenderHelper;

import org.lwjgl.opengl.GL11;

/**
 * 
 * @author Koen Beckers (K4Unl)
 * 
 */
public class PartFixture extends PartLamp {
    
    public PartFixture(String colorName, Integer colorVal) {
    
        super(colorName, colorVal);
    }
    
    /**
     * @author Koen Beckers (K4Unl)
     */
    
    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {
    
        boxes.add(AxisAlignedBB.getBoundingBox(pixel * 2, 0.0, pixel * 2, 1.0 - (pixel * 2), pixel * 2, 1.0 - pixel * 2));
        boxes.add(AxisAlignedBB.getBoundingBox(pixel * 3, pixel * 2, pixel * 3, 1.0 - (pixel * 3), pixel * 8, 1.0 - pixel * 3));
    }
    
    @Override
    public String getType() {
    
        return "fixture" + colorName;
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "fixture." + colorName;
    }
    
    @Override
    public void renderBase(int pass) {
    
        Tessellator t = Tessellator.instance;
        Vector3Cube vector = new Vector3Cube(pixel * 2, 0.0, pixel * 2, 1.0 - (pixel * 2), pixel * 2, 1.0 - pixel * 2);
        IIcon topIcon = IconSupplier.fixtureFootTop;
        // IIcon sideIcon = IconSupplier.fixtureFootSide;
        
        double minU = topIcon.getInterpolatedU(vector.getMinX() * 16);
        double maxU = topIcon.getInterpolatedU(vector.getMaxX() * 16);
        double minV = topIcon.getInterpolatedV(vector.getMinZ() * 16);
        double maxV = topIcon.getInterpolatedV(vector.getMaxZ() * 16);
        
        // Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
        
        // FIXME: Icons for the side aren't rendered..
        
        /*
         * minU = sideIcon.getInterpolatedU(vector.getMinX() * 16); maxU = sideIcon.getInterpolatedU(vector.getMaxX() * 16); minV =
         * sideIcon.getInterpolatedV((vector.getMinY() + (3*pixel) )* 16); maxV = sideIcon.getInterpolatedV((vector.getMaxY() + (3*pixel) )* 16);
         */
        // Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        // Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        
        // Draw north side
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        // Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, maxV);
    }
    
    @Override
    public void renderLamp(int pass, int r, int g, int b) {
    
        Vector3Cube vector = new Vector3Cube(pixel * 3, pixel * 2, pixel * 3, 1.0 - (pixel * 3), pixel * 8, 1.0 - pixel * 3);
        
        if (pass == 0) {
            Tessellator t = Tessellator.instance;
            IIcon iconToUseTop;
            IIcon iconToUseSide;
            if (power == 0) {
                iconToUseSide = IconSupplier.fixtureLampSideOff;
                iconToUseTop = IconSupplier.fixtureLampTopOff;
            } else {
                iconToUseSide = IconSupplier.fixtureLampSideOn;
                iconToUseTop = IconSupplier.fixtureLampTopOn;
                
                t.setColorRGBA(r, g, b, 20);
                RenderHelper.drawTesselatedCube(new Vector3Cube(pixel * 4.5, pixel * 2, pixel * 4.5, 1.0 - (pixel * 4.5), 1.0 - (pixel * 4.5),
                        1.0 - pixel * 4.5));
                t.setColorRGBA(r, g, b, 255);
            }
            
            double minU = iconToUseTop.getInterpolatedU(vector.getMinX() * 16);
            double maxU = iconToUseTop.getInterpolatedU(vector.getMaxX() * 16);
            double minV = iconToUseTop.getInterpolatedV(vector.getMinZ() * 16);
            double maxV = iconToUseTop.getInterpolatedV(vector.getMaxZ() * 16);
            
            // Top side
            t.setNormal(0, 1, 0);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
            
            // I think there might be something wrong with the textures here..
            minU = iconToUseSide.getInterpolatedU(vector.getMinZ() * 16);
            maxU = iconToUseSide.getInterpolatedU(vector.getMaxZ() * 16);
            minV = iconToUseSide.getInterpolatedV((vector.getMinY() + (3 * pixel)) * 16);
            maxV = iconToUseSide.getInterpolatedV((vector.getMaxY() + (3 * pixel)) * 16);
            
            // Draw west side:
            t.setNormal(-1, 0, 0);
            t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
            t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
            
            // Draw east side:
            t.setNormal(1, 0, 0);
            t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
            
            // Draw north side
            t.setNormal(0, 0, -1);
            t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
            
            // Draw south side
            t.setNormal(0, 0, 1);
            t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        }
        
        if (power > 0 && pass == 1) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            // GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDepthMask(false);
            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.drawColoredCube(vector.clone().expand(0.8 / 16D), r / 256D, g / 256D, b / 256D, (power / 15D) * 0.625);
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }
}
