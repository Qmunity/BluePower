package net.quetzi.bluepower.part.gate;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.quetzi.bluepower.api.part.FaceDirection;
import net.quetzi.bluepower.api.part.RedstoneConnection;
import net.quetzi.bluepower.client.renderers.RenderHelper;
import net.quetzi.bluepower.references.Refs;

import org.lwjgl.opengl.GL11;

public class GateTest extends GateBase {
    
    @Override
    public void initializeConnections(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
    }
    
    @Override
    public String getGateID() {
    
        return "test";
    }
    
    @Override
    public void renderTop(float frame) {
    
        renderTopItem();
    }
    
    @Override
    public void renderTopItem() {

        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Refs.MODID + ":textures/blocks/gates/bottom.png"));
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3d(0, 1, 0);
        RenderHelper.addVertexWithTexture(0, 1D / 8D, 0, 0, 0);
        RenderHelper.addVertexWithTexture(0, 1D / 8D, 1, 0, 1);
        RenderHelper.addVertexWithTexture(1, 1D / 8D, 1, 1, 1);
        RenderHelper.addVertexWithTexture(1, 1D / 8D, 0, 1, 0);
        GL11.glEnd();
    }
    
    @Override
    public void doLogic(RedstoneConnection front, RedstoneConnection left, RedstoneConnection back, RedstoneConnection right) {
    
        int power = 0;
        
        power = Math.max(power, getConnection(FaceDirection.LEFT).getPower() - 1);
        power = Math.max(power, getConnection(FaceDirection.BACK).getPower() - 1);
        power = Math.max(power, getConnection(FaceDirection.RIGHT).getPower() - 1);
        
        getConnection(FaceDirection.FRONT).setPower(power);
    }
    
}
