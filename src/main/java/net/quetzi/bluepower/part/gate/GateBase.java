package net.quetzi.bluepower.part.gate;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.IBPFacePart;
import net.quetzi.bluepower.api.vec.Vector3;

import org.lwjgl.opengl.GL11;

public class GateBase extends BPPart implements IBPFacePart {
    
    @Override
    public String getType() {
    
        return "gatebase";
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "gate.gatebase";
    }
    
    @Override
    public int getFace() {
    
        return 0;
    }
    
    @Override
    public void onNeigbourUpdate() {
    
        System.out.println("Neighbour update!");
        super.onNeigbourUpdate();
    }
    
    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {
    
        GL11.glPushMatrix();
        {
            GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColor4d(0, 1, 0, 1);
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setBrightness(100);
            t.addVertex(0, 0, 0);
            t.addVertex(1, 0, 0);
            t.addVertex(1, 1, 0);
            t.addVertex(0, 1, 0);
            t.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    
        GL11.glPushMatrix();
        {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColor4d(0, 1, 0, 1);
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setBrightness(100);
            t.addVertex(0, 0, 0);
            t.addVertex(1, 0, 0);
            t.addVertex(1, 1, 0);
            t.addVertex(0, 1, 0);
            t.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
        GL11.glPopMatrix();
    }
    
}
