package net.quetzi.bluepower.client.renderers;

import org.lwjgl.opengl.GL11;

public class RenderHelper {
    
    public static void addVertex(double x, double y, double z) {
    
        GL11.glVertex3d(x, y, z);
    }
    
    public static void addVertexWithTexture(double x, double y, double z, double tx, double ty) {
    
        GL11.glTexCoord2d(tx, ty);
        GL11.glVertex3d(x, y, z);
    }
    
}
