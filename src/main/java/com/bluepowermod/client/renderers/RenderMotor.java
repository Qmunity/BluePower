package com.bluepowermod.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.model.ModelMotor;
import com.bluepowermod.tileentities.TileMotor;

public class RenderMotor extends TileEntitySpecialRenderer {

    private static final ModelMotor model = new ModelMotor();

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float frame) {

        TileMotor te = (TileMotor) tile;

        bindTexture(new ResourceLocation("bluepower:textures/blocks/motor.png"));

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);

            GL11.glTranslated(0.5, 0, 0.5);
            GL11.glRotated(90 * te.getRotation(), 0, 1, 0);
            GL11.glTranslated(-0.5, 0, -0.5);

            GL11.glDisable(GL11.GL_LIGHTING);
            model.render(te.isRotating());
            GL11.glEnable(GL11.GL_LIGHTING);
        }
        GL11.glPopMatrix();
    }

}
