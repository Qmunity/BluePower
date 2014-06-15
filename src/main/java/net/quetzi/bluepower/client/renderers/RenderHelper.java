package net.quetzi.bluepower.client.renderers;

import java.nio.DoubleBuffer;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.vec.Vector3Cube;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class RenderHelper {
    
    public static void addVertex(double x, double y, double z) {
    
        GL11.glVertex3d(x, y, z);
    }
    
    public static void addVertexWithTexture(double x, double y, double z, double tx, double ty) {
    
        GL11.glTexCoord2d(tx, ty);
        GL11.glVertex3d(x, y, z);
    }
    
    private static RenderBlocks rb = new RenderBlocks();
    
    public static void renderRedstoneTorch(double x, double y, double z, double height, boolean state) {
    
        Block b = null;
        if (state) b = Blocks.redstone_torch;
        else b = Blocks.unlit_redstone_torch;
        
        GL11.glTranslated(x, y, z);
        
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        
        GL11.glEnable(GL11.GL_CLIP_PLANE0);
        GL11.glClipPlane(GL11.GL_CLIP_PLANE0, planeEquation(0, 0, 0, 0, 0, 1, 1, 0, 1));
        
        Tessellator t = Tessellator.instance;
        
        t.startDrawingQuads();
        t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
        rb.renderTorchAtAngle(b, 0, y + height - 1, 0, 0, 0, 0);
        t.draw();
        
        GL11.glDisable(GL11.GL_CLIP_PLANE0);
        
        GL11.glTranslated(-x, -y, -z);
    }
    
    public static void renderPointer(double x, double y, double z, double angle) {
    
        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);
            
            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotated(360 * angle, 0, 1, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);
            
            Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation("minecraft:textures/blocks/stone.png"));
            
            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glNormal3d(0, -1, 0);
                // Bottom
                addVertexWithTexture(0.5, 0, 1D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5 + 1D / 8D, 0, 0.5, 0.5 + 1D / 8D, 0.5);
                addVertexWithTexture(0.5, 0, 0.5 + 1D / 8D, 0.5, 0.5 + 1D / 8D);
                addVertexWithTexture(0.5 - 1D / 8D, 0, 0.5, 0.5 - 1D / 8D, 0.5);
                GL11.glNormal3d(0, 1, 0);
                // Top
                addVertexWithTexture(0.5, 1D / 16D, 1D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5 - 1D / 8D, 1D / 16D, 0.5, 0.5 - 1D / 8D, 0.5);
                addVertexWithTexture(0.5, 1D / 16D, 0.5 + 1D / 8D, 0.5, 0.5 + 1D / 8D);
                addVertexWithTexture(0.5 + 1D / 8D, 1D / 16D, 0.5, 0.5 + 1D / 8D, 0.5);
                GL11.glNormal3d(1, 0, 0);
                // Side 1
                addVertexWithTexture(0.5, 1D / 16D, 1D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5, 0, 1D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5 - 1D / 8D, 0, 0.5, 0.5 - 1D / 8D, 0.5);
                addVertexWithTexture(0.5 - 1D / 8D, 1D / 16D, 0.5, 0.5 - 1D / 8D, 0.5);
                // Side 2
                addVertexWithTexture(0.5 - 1D / 8D, 1D / 16D, 0.5, 0.5 - 1D / 8D, 0.5);
                addVertexWithTexture(0.5 - 1D / 8D, 0, 0.5, 0.5 - 1D / 8D, 0.5);
                addVertexWithTexture(0.5, 0, 0.5 + 1D / 8D, 0.5, 0.5 + 1D / 8D);
                addVertexWithTexture(0.5, 1D / 16D, 0.5 + 1D / 8D, 0.5, 0.5 + 1D / 8D);
                GL11.glNormal3d(-1, 0, 0);
                // Side 3
                addVertexWithTexture(0.5, 1D / 16D, 0.5 + 1D / 8D, 0.5, 0.5 + 1D / 8D);
                addVertexWithTexture(0.5, 0, 0.5 + 1D / 8D, 0.5, 0.5 + 1D / 8D);
                addVertexWithTexture(0.5 + 1D / 8D, 0, 0.5, 0.5 + 1D / 8D, 0.5);
                addVertexWithTexture(0.5 + 1D / 8D, 1D / 16D, 0.5, 0.5 + 1D / 8D, 0.5);
                // Side 4
                addVertexWithTexture(0.5 + 1D / 8D, 1D / 16D, 0.5, 0.5 + 1D / 8D, 0.5);
                addVertexWithTexture(0.5 + 1D / 8D, 0, 0.5, 0.5 + 1D / 8D, 0.5);
                addVertexWithTexture(0.5, 0, 1D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5, 1D / 16D, 1D / 16D, 0.5, 1D / 16D);
            }
            GL11.glEnd();
            
        }
        GL11.glPopMatrix();
        
    }
    
    public static DoubleBuffer planeEquation(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3, double z3) {
    
        double[] eq = new double[4];
        eq[0] = y1 * (z2 - z3) + y2 * (z3 - z1) + y3 * (z1 - z2);
        eq[1] = z1 * (x2 - x3) + z2 * (x3 - x1) + z3 * (x1 - x2);
        eq[2] = x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2);
        eq[3] = -(x1 * (y2 * z3 - y3 * z2) + x2 * (y3 * z1 - y1 * z3) + x3 * (y1 * z2 - y2 * z1));
        DoubleBuffer b = BufferUtils.createDoubleBuffer(8).put(eq);
        b.flip();
        return b;
    }
    
    public static void drawColoredCube(Vector3Cube vector) {
    
        //Top side
        GL11.glColor3f(1.0F, 0.0F, 0.0F);
        addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
        addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
        addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
        addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
        
        //Bottom side
        GL11.glColor3f(1.0F, 1.0F, 0.0F);
        addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
        addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
        addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
        addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
        
        //Draw west side:
        GL11.glColor3f(0.0F, 1.0F, 0.0F);
        addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
        addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
        addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
        addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
        
        //Draw east side:
        GL11.glColor3f(0.0F, 1.0F, 1.0F);
        addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
        addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
        addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
        addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
        
        //Draw north side
        GL11.glColor3f(0.0F, 0.0F, 1.0F);
        addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
        addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
        addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
        addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
        
        //Draw south side
        GL11.glColor3f(0.0F, 0.0F, 0.0F);
        addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
        addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
        addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
        addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
    }
    
    public static void drawTesselatedColoredCube(Vector3Cube vector) {
    	
    	Tessellator t = Tessellator.instance;
    	boolean wasTesselating = false;
    	
    	//Check if we were already tesselating
    	try{
    		t.startDrawingQuads();
    	}catch(IllegalStateException e){
    		wasTesselating = true;
    	}
    	
        //Top side
        t.setColorRGBA_F(1.0F, 0.0F, 0.0F, 1.0F);
        t.setNormal(0, 1, 0);
        t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
        t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
        t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
        t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
        
        //Bottom side
        t.setColorRGBA_F(1.0F, 1.0F, 0.0F, 1.0F);
        t.setNormal(0, -1, 0);
        t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
        t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
        t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
        t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
        
        //Draw west side:
        t.setColorRGBA_F(0.0F, 1.0F, 0.0F, 1.0F);
        t.setNormal(-1, 0, 0);
        t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
        t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
        t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
        t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
        
        //Draw east side:
        t.setColorRGBA_F(0.0F, 1.0F, 1.0F, 1.0F);
        t.setNormal(1, 0, 0);
        t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
        t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
        t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
        t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
        
        //Draw north side
        t.setColorRGBA_F(0.0F, 0.0F, 1.0F, 1.0F);
        t.setNormal(0, 0, -1);
        t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
        t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
        t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
        t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
        
        //Draw south side
        t.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
        t.setNormal(0, 0, 1);
        t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
        t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
        t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
        t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
        
        if(!wasTesselating){
        	t.draw();
        }
    }
    
    public static void drawTesselatedTexturedCube(Vector3Cube vector, IIcon iconToUse){
    	
    	Tessellator t = Tessellator.instance;
    	boolean wasTesselating = false;
    	
    	//Check if we were already tesselating
    	try{
    		t.startDrawingQuads();
    	}catch(IllegalStateException e){
    		wasTesselating = true;
    	}
    	
    	double minU = iconToUse.getMinU();
        double maxU = iconToUse.getMaxU();
        double minV = iconToUse.getMinV();
        double maxV = iconToUse.getMaxV();
    	
        //Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
        
        //Bottom side
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        
        //Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        //Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        
        //Draw north side
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        
        //Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, maxV);
        
        if(!wasTesselating){
        	t.draw();
        }
    }
    
    public static void drawTesselatedCube(Vector3Cube vector){
    	
		Tessellator t = Tessellator.instance;
		boolean wasTesselating = false;
		
		//Check if we were already tesselating
		try{
			t.startDrawingQuads();
		}catch(IllegalStateException e){
			wasTesselating = true;
		}
		
	    //Top side
	    t.setNormal(0, 1, 0);
	    t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
	    t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
	    t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
	    t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
	    
	    //Bottom side
	    t.setNormal(0, -1, 0);
	    t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
	    t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
	    t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
	    t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
	    
	    //Draw west side:
	    t.setNormal(-1, 0, 0);
	    t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
	    t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
	    t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
	    t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
	    
	    //Draw east side:
	    t.setNormal(1, 0, 0);
	    t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
	    t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
	    t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
	    t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
	    
	    //Draw north side
	    t.setNormal(0, 0, -1);
	    t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMinZ());
	    t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMinZ());
	    t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMinZ());
	    t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMinZ());
	    
	    //Draw south side
	    t.setNormal(0, 0, 1);
	    t.addVertex(vector.getMinX(), vector.getMinY(), vector.getMaxZ());
	    t.addVertex(vector.getMaxX(), vector.getMinY(), vector.getMaxZ());
	    t.addVertex(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ());
	    t.addVertex(vector.getMinX(), vector.getMaxY(), vector.getMaxZ());
	    
	    if(!wasTesselating){
	    	t.draw();
	    }
    }
    
    public static void rotateRenderMatrix(ForgeDirection d) {
    
        switch (d) {
            case UP:
                GL11.glRotatef(1, 0, 0, -90);
                break;
            case DOWN:
                GL11.glRotatef(1, 0, 0, 90);
                break;
            case NORTH:
                GL11.glRotatef(1, 0, -90, 0);
                break;
            case SOUTH:
                GL11.glRotatef(1, 0, 90, 0);
                break;
            case WEST:
                GL11.glRotatef(1, 0, 0, 180);
                break;
            case EAST:
                GL11.glRotatef(1, 0, 0, 0);
                break;
		default:
			break;
        }
    }
}
