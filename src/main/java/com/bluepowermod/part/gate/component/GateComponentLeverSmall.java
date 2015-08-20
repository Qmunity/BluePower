package com.bluepowermod.part.gate.component;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.part.gate.GateBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateComponentLeverSmall extends GateComponentLever {

    public GateComponentLeverSmall(GateBase gate, int color) {

        super(gate, color);
    }

    public GateComponentLeverSmall(GateBase gate, double x, double z) {

        super(gate, x, z);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        renderer.addTransformation(new Translation(x, 0, z));
        renderer.renderBox(new Vec3dCube(0, 2 / 16D, 0, 4 / 16D, 4 / 16D, 7 / 16D), Blocks.cobblestone.getIcon(0, 0));
        renderer.removeTransformation();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            Tessellator t = Tessellator.instance;

            IIcon icon = Blocks.lever.getIcon(0, 0);
            double minU = icon.getMinU();
            double minV = icon.getMinV();
            double maxU = icon.getMaxU();
            double maxV = icon.getMaxV();

            GL11.glTranslated(-x + 6 / 16D, 2 / 16D, -z + 11.5 / 16D);

            GL11.glTranslated(0, 0, 1 / 16D);
            GL11.glRotated(state ? 40 : -40, 1, 0, 0);
            GL11.glTranslated(0, 0, -1 / 16D);

            t.startDrawingQuads();
            {
                t.setNormal(0, 1, 0);

                t.addVertexWithUV(0, 0, 0, minU, maxV);
                t.addVertexWithUV(0, 6F / 8F, 0, minU, minV);
                t.addVertexWithUV(1, 6F / 8F, 0, maxU, minV);
                t.addVertexWithUV(1, 0, 0, maxU, maxV);

                t.addTranslation(0, 0, 2 / 16F);
                t.addVertexWithUV(0, 0, 0, maxU, maxV);
                t.addVertexWithUV(1, 0, 0, minU, maxV);
                t.addVertexWithUV(1, 6F / 8F, 0, minU, minV);
                t.addVertexWithUV(0, 6F / 8F, 0, maxU, minV);
                t.addTranslation(0, 0, -2 / 16F);

                t.addTranslation(7 / 16F, 0, -7 / 16F);
                t.addVertexWithUV(0, 0, 0, maxU, maxV);
                t.addVertexWithUV(0, 0, 1, minU, maxV);
                t.addVertexWithUV(0, 6F / 8F, 1, minU, minV);
                t.addVertexWithUV(0, 6F / 8F, 0, maxU, minV);
                t.addTranslation(-7 / 16F, 0, 7 / 16F);

                t.addTranslation(9 / 16F, 0, -7 / 16F);
                t.addVertexWithUV(0, 0, 0, minU, maxV);
                t.addVertexWithUV(0, 6F / 8F, 0, minU, minV);
                t.addVertexWithUV(0, 6F / 8F, 1, maxU, minV);
                t.addVertexWithUV(0, 0, 1, maxU, maxV);
                t.addTranslation(-9 / 16F, 0, 7 / 16F);

                t.addTranslation(7 / 16F, 0, 0);
                minU = icon.getInterpolatedU(7);
                maxU = icon.getInterpolatedU(9);
                minV = icon.getInterpolatedV(6);
                maxV = icon.getInterpolatedV(8);
                t.addVertexWithUV(0, 15 / 32D, 0, minU, maxV);
                t.addVertexWithUV(0, 15 / 32D, 1 / 8D, minU, minV);
                t.addVertexWithUV(1 / 8D, 15 / 32D, 1 / 8D, maxU, minV);
                t.addVertexWithUV(1 / 8D, 15 / 32D, 0, maxU, maxV);
                t.addTranslation(-7 / 16F, 0, 0);
            }
            t.draw();
        }
        GL11.glPopMatrix();
    }

}
