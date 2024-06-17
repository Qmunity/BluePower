/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

@OnlyIn(Dist.CLIENT)
public class RenderHelper {

    /**
     * Adds a vertex. Just a wrapper function for openGL
     *
     * @author Koen Beckers (K4Unl)
     * @param x
     * @param y
     * @param z
     */
    public static void addVertex(double x, double y, double z) {

        GL11.glVertex3d(x, y, z);
    }

    /**
     * Adds a vertex with a texture.
     *
     * @author Koen Beckers (K4Unl)
     * @param x
     * @param y
     * @param z
     * @param tx
     * @param ty
     */
    public static void addVertexWithTexture(double x, double y, double z, double tx, double ty) {

        GL11.glTexCoord2d(tx, ty);
        GL11.glVertex3d(x, y, z);
    }

/*    private static RenderBlocks rb = new RenderBlocks();

    public static void renderDigitalRedstoneTorch(double x, double y, double z, double height, boolean state) {

        renderRedstoneTorch(x, y, z, height, state, true);
    }

    public static void renderAnalogRedstoneTorch(double x, double y, double z, double height, boolean state) {

        renderRedstoneTorch(x, y, z, height, state, false);
    }

    public static void renderRedstoneTorch(double x, double y, double z, double height, boolean state, boolean digital) {

        GL11.glPushMatrix();
        {
            Minecraft.getInstance().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

            if (digital) {
            } else {
                rb.overrideBlockTexture = state ? Blocks.redstone_torch.getIcon(0, 0) : Blocks.unlit_redstone_torch.getIcon(0, 0);
            }

            GL11.glEnable(GL11.GL_CLIP_PLANE0);
            GL11.glClipPlane(GL11.GL_CLIP_PLANE0, uk.co.qmunity.lib.client.render.RenderUtils.planeEquation(0, 1, 0));

            GL11.glTranslated(x, y + height - 1, z);

            Tesselator t = Tesselator.instance;

            t.setColorOpaque_F(1.0F, 1.0F, 1.0F);
            t.startDrawingQuads();
            {
                rb.setRenderBounds(7 / 16D, 0, 0, 9 / 16D, 1, 1);
                t.setNormal(-1, 1, 0);
                rb.renderFaceXNeg(Blocks.stone, 0, 0, 0, null);
                t.setNormal(1, 1, 0);
                rb.renderFaceXPos(Blocks.stone, 0, 0, 0, null);

                rb.setRenderBounds(0, 0, 7 / 16D, 1, 1, 9 / 16D);
                t.setNormal(0, 1, -1);
                rb.renderFaceZNeg(Blocks.stone, 0, 0, 0, null);
                t.setNormal(0, 1, 1);
                rb.renderFaceZPos(Blocks.stone, 0, 0, 0, null);

                rb.setRenderBounds(7 / 16D, 0, 6 / 16D, 9 / 16D, 10 / 16D, 8 / 16D);
                t.setNormal(0, 1, 0);
                rb.renderFaceYPos(Blocks.stone, 0, 0, 1 / 16D, null);
            }
            t.draw();

            GL11.glDisable(GL11.GL_CLIP_PLANE0);

            rb.overrideBlockTexture = null;
        }
        GL11.glPopMatrix();
    }

    public static void renderRandomizerButton(double x, double y, double z, boolean tainted) {

        String res = Refs.MODID + ":textures/blocks/gates/components/" + (tainted ? "tainted_" : "") + "silicon_chip_on.png";
        String resSide = Refs.MODID + ":textures/blocks/gates/randomizer/button_side.png";
        renderButton(x, y, z, res, resSide);
    }

    public static void renderQuartzResonator(double x, double y, double z) {

        String res = Refs.MODID + ":textures/blocks/gates/components/resonator.png";
        String resSide = Refs.MODID + ":textures/blocks/gates/randomizer/button_side.png";
        renderButton(x, y, z, res, resSide);
    }

    public static void renderButton(double x, double y, double z, String res, String resSide) {

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);

            GL11.glPushMatrix();
            {
                GL11.glTranslated(6 / 16D, 2 / 16D, 8 / 16D);
                Minecraft.getInstance().renderEngine.bindTexture(new ResourceLocation(resSide));
                for (int i = 0; i < 4; i++) {
                    GL11.glTranslated(2 / 16D, 0, 2 / 16D);
                    GL11.glRotated(90, 0, 1, 0);
                    GL11.glTranslated(-2 / 16D, 0, -2 / 16D);
                    GL11.glBegin(GL11.GL_QUADS);
                    {
                        GL11.glNormal3d(1, 0, 0);
                        addVertexWithTexture(0, 0, 0, 0, 0);
                        addVertexWithTexture(0, 1 / 16D, 0, 0, 1);
                        addVertexWithTexture(4 / 16D, 1 / 16D, 0, 1, 1);
                        addVertexWithTexture(4 / 16D, 0, 0, 1, 0);
                    }
                    GL11.glEnd();
                }
            }
            GL11.glPopMatrix();

            GL11.glTranslated(0, 1 / 16D, 0 / 16D);

            Minecraft.getInstance().renderEngine.bindTexture(new ResourceLocation(res));
            Tesselator t = Tesselator.instance;

            y = 2 / 16D;

            t.startDrawingQuads();
            t.setNormal(0, 1, 0);
            {
                t.addVertexWithUV(0, y, 0, 1, 1);
                t.addVertexWithUV(0, y, 1, 1, 0);
                t.addVertexWithUV(1, y, 1, 0, 0);
                t.addVertexWithUV(1, y, 0, 0, 1);
            }
            t.draw();
        }
        GL11.glPopMatrix();
    }

    */
    /*
     * @author amadornes
     * @param x
     * @param y
     * @param z
     * @param angle
     */
    public static void renderPointer(double x, double y, double z, double angle) {

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);

            GL11.glTranslated(0.5, 0.5, 0.5);
            GL11.glRotated(180 + 360 * -angle, 0, 1, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);

            Minecraft.getInstance().getTextureManager().bindForSetup(new ResourceLocation("minecraft:textures/blocks/stone.png"));

            GL11.glBegin(GL11.GL_QUADS);
            {
                GL11.glNormal3d(0, -1, 0);
                // Bottom
                addVertexWithTexture(0.5, 0, 2D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5 + 1D / 8D, 0, 0.5, 0.5 + 1D / 8D, 0.5);
                addVertexWithTexture(0.5, 0, 0.5 + 1D / 8D, 0.5, 0.5 + 1D / 8D);
                addVertexWithTexture(0.5 - 1D / 8D, 0, 0.5, 0.5 - 1D / 8D, 0.5);
                GL11.glNormal3d(0, 1, 0);
                // Top
                addVertexWithTexture(0.5, 1D / 16D, 2D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5 - 1D / 8D, 1D / 16D, 0.5, 0.5 - 1D / 8D, 0.5);
                addVertexWithTexture(0.5, 1D / 16D, 0.5 + 1D / 8D, 0.5, 0.5 + 1D / 8D);
                addVertexWithTexture(0.5 + 1D / 8D, 1D / 16D, 0.5, 0.5 + 1D / 8D, 0.5);
                GL11.glNormal3d(1, 0, 0);
                // Side 1
                addVertexWithTexture(0.5, 1D / 16D, 2D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5, 0, 2D / 16D, 0.5, 1D / 16D);
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
                addVertexWithTexture(0.5, 0, 2D / 16D, 0.5, 1D / 16D);
                addVertexWithTexture(0.5, 1D / 16D, 2D / 16D, 0.5, 1D / 16D);
            }
            GL11.glEnd();

        }
        GL11.glPopMatrix();

    }
    /*
    *//**
     * @author amadornes
     * @param x1
     * @param y1
     * @param z1
     * @param x2
     * @param y2
     * @param z2
     * @param x3
     * @param y3
     * @param z3
     * @return TODO: Maybe move this function?
     *//*
    public static DoubleBuffer planeEquation(double x1, double y1, double z1, double x2, double y2, double z2, double x3, double y3,
            double z3) {

        double[] eq = new double[4];
        eq[0] = y1 * (z2 - z3) + y2 * (z3 - z1) + y3 * (z1 - z2);
        eq[1] = z1 * (x2 - x3) + z2 * (x3 - x1) + z3 * (x1 - x2);
        eq[2] = x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2);
        eq[3] = -(x1 * (y2 * z3 - y3 * z2) + x2 * (y3 * z1 - y1 * z3) + x3 * (y1 * z2 - y2 * z1));
        DoubleBuffer b = BufferUtils.createDoubleBuffer(8).put(eq);
        b.flip();
        return b;
    }*/

    /**
     * Draws a colored cube with the size of vector. Every face has a different color This uses OpenGL
     *
     * @author Koen Beckers (K4Unl)
     * @param vector
     */
    public static void drawColoredCube(AABB vector) {

        // Top side
        GL11.glColor3f(1.0F, 0.0F, 0.0F);
        GL11.glNormal3d(0, 1, 0);
        addVertex(vector.minX, vector.maxY, vector.maxZ);
        addVertex(vector.maxX, vector.maxY, vector.maxZ);
        addVertex(vector.maxX, vector.maxY, vector.minZ);
        addVertex(vector.minX, vector.maxY, vector.minZ);

        // Bottom side
        GL11.glColor3f(1.0F, 1.0F, 0.0F);
        GL11.glNormal3d(0, -1, 0);
        addVertex(vector.maxX, vector.minY, vector.maxZ);
        addVertex(vector.minX, vector.minY, vector.maxZ);
        addVertex(vector.minX, vector.minY, vector.minZ);
        addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw west side:
        GL11.glColor3f(0.0F, 1.0F, 0.0F);
        GL11.glNormal3d(-1, 0, 0);
        addVertex(vector.minX, vector.minY, vector.maxZ);
        addVertex(vector.minX, vector.maxY, vector.maxZ);
        addVertex(vector.minX, vector.maxY, vector.minZ);
        addVertex(vector.minX, vector.minY, vector.minZ);

        // Draw east side:
        GL11.glColor3f(0.0F, 1.0F, 1.0F);
        GL11.glNormal3d(1, 0, 0);
        addVertex(vector.maxX, vector.minY, vector.minZ);
        addVertex(vector.maxX, vector.maxY, vector.minZ);
        addVertex(vector.maxX, vector.maxY, vector.maxZ);
        addVertex(vector.maxX, vector.minY, vector.maxZ);

        // Draw north side
        GL11.glColor3f(0.0F, 0.0F, 1.0F);
        GL11.glNormal3d(0, 0, -1);
        addVertex(vector.minX, vector.minY, vector.minZ);
        addVertex(vector.minX, vector.maxY, vector.minZ);
        addVertex(vector.maxX, vector.maxY, vector.minZ);
        addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw south side
        GL11.glColor3f(0.0F, 0.0F, 0.0F);
        GL11.glNormal3d(0, 0, 1);
        addVertex(vector.minX, vector.minY, vector.maxZ);
        addVertex(vector.maxX, vector.minY, vector.maxZ);
        addVertex(vector.maxX, vector.maxY, vector.maxZ);
        addVertex(vector.minX, vector.maxY, vector.maxZ);
    }

    /**
     * Draws a colored cube with the size of vector. All faces have the specified color. This uses OpenGL
     *
     * @author Koen Beckers (K4Unl) and Amadornes
     * @param vector
     */
    public static void drawColoredCube(AABB vector, double r, double g, double b, double a, boolean... renderFaces) {

        GL11.glColor4d(r, g, b, a);

        // Top side
        if (renderFaces.length < 1 || renderFaces[0]) {
            GL11.glNormal3d(0, 1, 0);
            addVertex(vector.minX, vector.maxY, vector.maxZ);
            addVertex(vector.maxX, vector.maxY, vector.maxZ);
            addVertex(vector.maxX, vector.maxY, vector.minZ);
            addVertex(vector.minX, vector.maxY, vector.minZ);
        }

        // Bottom side
        if (renderFaces.length < 2 || renderFaces[1]) {
            GL11.glNormal3d(0, -1, 0);
            addVertex(vector.maxX, vector.minY, vector.maxZ);
            addVertex(vector.minX, vector.minY, vector.maxZ);
            addVertex(vector.minX, vector.minY, vector.minZ);
            addVertex(vector.maxX, vector.minY, vector.minZ);
        }

        // Draw west side:
        if (renderFaces.length < 3 || renderFaces[5]) {
            GL11.glNormal3d(-1, 0, 0);
            addVertex(vector.minX, vector.minY, vector.maxZ);
            addVertex(vector.minX, vector.maxY, vector.maxZ);
            addVertex(vector.minX, vector.maxY, vector.minZ);
            addVertex(vector.minX, vector.minY, vector.minZ);
        }

        // Draw east side:
        if (renderFaces.length < 4 || renderFaces[4]) {
            GL11.glNormal3d(1, 0, 0);
            addVertex(vector.maxX, vector.minY, vector.minZ);
            addVertex(vector.maxX, vector.maxY, vector.minZ);
            addVertex(vector.maxX, vector.maxY, vector.maxZ);
            addVertex(vector.maxX, vector.minY, vector.maxZ);
        }

        // Draw north side
        if (renderFaces.length < 5 || renderFaces[3]) {
            GL11.glNormal3d(0, 0, -1);
            addVertex(vector.minX, vector.minY, vector.minZ);
            addVertex(vector.minX, vector.maxY, vector.minZ);
            addVertex(vector.maxX, vector.maxY, vector.minZ);
            addVertex(vector.maxX, vector.minY, vector.minZ);
        }

        // Draw south side
        if (renderFaces.length < 6 || renderFaces[2]) {
            GL11.glNormal3d(0, 0, 1);
            addVertex(vector.minX, vector.minY, vector.maxZ);
            addVertex(vector.maxX, vector.minY, vector.maxZ);
            addVertex(vector.maxX, vector.maxY, vector.maxZ);
            addVertex(vector.minX, vector.maxY, vector.maxZ);
        }

        GL11.glColor4d(1, 1, 1, 1);
    }

    /**
     * Draws a colored cube with the size of vector. All faces have the specified color. This uses OpenGL
     *
     * @author Koen Beckers (K4Unl) and Amadornes
     * @param vector
     */
    public static void drawColoredCube(AABB vector, VertexConsumer vertexBuilder, PoseStack matrixStack, int r, int g, int b, int a, int light, boolean... renderFaces) {
        PoseStack.Pose entry = matrixStack.last();
        Matrix4f positionMatrix = entry.pose();
        Matrix3f normalMatrix = entry.normal();

        TextureAtlasSprite sprite =  Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation("minecraft:white_concrete"), "")).getParticleIcon();
        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();

        // Top side
        if (renderFaces.length < 1 || renderFaces[0]) {
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(minU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 1.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(minU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 1.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(maxU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 1.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(maxU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 1.0F, 0.0F)
                    .endVertex();
        }

        // Bottom side
        if (renderFaces.length < 2 || renderFaces[1]) {
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(minU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, -1.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(minU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, -1.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(maxU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, -1.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(maxU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, -1.0F, 0.0F)
                    .endVertex();
        }

        // Draw west side:
        if (renderFaces.length < 3 || renderFaces[5]) {
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(minU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, -1.0F, 0.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(minU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, -1.0F, 0.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(maxU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, -1.0F, 0.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(maxU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, -1.0F, 0.0F, 0.0F)
                    .endVertex();
        }

        // Draw east side:
        if (renderFaces.length < 4 || renderFaces[4]) {
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(minU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 1.0F, 0.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(minU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 1.0F, 0.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(maxU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 1.0F, 0.0F, 0.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(maxU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 1.0F, 0.0F, 0.0F)
                    .endVertex();
        }

        // Draw north side
        if (renderFaces.length < 5 || renderFaces[3]) {
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(minU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 0.0F, -1.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(minU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 0.0F, -1.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(maxU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 0.0F, -1.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.minZ)
                    .color(r,g,b,a)
                    .uv(maxU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 0.0F, -1.0F)
                    .endVertex();
        }

        // Draw south side
        if (renderFaces.length < 6 || renderFaces[2]) {
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(minU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 0.0F, 1.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(minU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 0.0F, 1.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(maxU, minV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 0.0F, 1.0F)
                    .endVertex();
            vertexBuilder.vertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.maxZ)
                    .color(r,g,b,a)
                    .uv(maxU, maxV)
                    .overlayCoords(OverlayTexture.NO_OVERLAY)
                    .uv2(light)
                    .normal(entry, 0.0F, 0.0F, 1.0F)
                    .endVertex();
        }

    }

  /*  *//**
     * Draws a colored cube with the size of vector. All faces have the specified color. This uses Tesselator
     *
     * @author Koen Beckers (K4Unl) and Amadornes
     * @param vector
     * @param color
     *//*
    public static void drawTesselatedColoredCube(AABB vector, int r, int g, int b, int a) {

        Tesselator t = Tesselator.instance;
        boolean wasTesselating = false;

        // Check if we were already tesselating
        try {
            t.startDrawingQuads();
        } catch (IllegalStateException e) {
            wasTesselating = true;

        }

        t.setColorRGBA(r, g, b, a);

        t.setNormal(0, 1, 0);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);

        // Bottom side
        t.setNormal(0, -1, 0);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.minY, vector.minZ);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);
        t.addVertex(vector.minX, vector.minY, vector.minZ);

        // Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);

        // Draw north side
        t.setNormal(0, 0, -1);
        t.addVertex(vector.minX, vector.minY, vector.minZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw south side
        t.setNormal(0, 0, 1);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);

        GL11.glColor4d(1, 1, 1, 1);

        if (!wasTesselating) {
            t.draw();
        }
    }

    *//**
     * Draws a colored cube with the size of vector. Every face has a different color This uses the Tesselator
     *
     * @author Koen Beckers (K4Unl)
     * @param vector
     *//*
    public static void drawTesselatedColoredCube(AABB vector) {

        Tesselator t = Tesselator.instance;
        boolean wasTesselating = false;

        // Check if we were already tesselating
        try {
            t.startDrawingQuads();
        } catch (IllegalStateException e) {
            wasTesselating = true;
        }

        // Top side
        t.setColorRGBA_F(1.0F, 0.0F, 0.0F, 1.0F);
        t.setNormal(0, 1, 0);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);

        // Bottom side
        t.setColorRGBA_F(1.0F, 1.0F, 0.0F, 1.0F);
        t.setNormal(0, -1, 0);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.minY, vector.minZ);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw west side:
        t.setColorRGBA_F(0.0F, 1.0F, 0.0F, 1.0F);
        t.setNormal(-1, 0, 0);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);
        t.addVertex(vector.minX, vector.minY, vector.minZ);

        // Draw east side:
        t.setColorRGBA_F(0.0F, 1.0F, 1.0F, 1.0F);
        t.setNormal(1, 0, 0);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);

        // Draw north side
        t.setColorRGBA_F(0.0F, 0.0F, 1.0F, 1.0F);
        t.setNormal(0, 0, -1);
        t.addVertex(vector.minX, vector.minY, vector.minZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw south side
        t.setColorRGBA_F(0.0F, 0.0F, 0.0F, 1.0F);
        t.setNormal(0, 0, 1);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);

        if (!wasTesselating) {
            t.draw();
        }
    }

    *//**
     * Draws a cube with the size of vector. It uses the texture that is already bound and maps that completely This uses the Tesselator
     *
     * @author Koen Beckers (K4Unl)
     * @param vector
     */
    public static void drawTesselatedTexturedCube(AABB vector) {

        Tesselator t = Tesselator.getInstance();
        BufferBuilder b = t.getBuilder();
        boolean wasTesselating = false;

        // Check if we were already tesselating
        try {
            b.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        } catch (IllegalStateException e) {
            wasTesselating = true;
        }

        float minU = 0;
        float maxU = 1;
        float minV = 0;
        float maxV = 1;

        // Top side
        //b.normal(0, 1, 0);
        b.vertex(vector.minX, vector.maxY, vector.maxZ).uv(minU, maxV).endVertex();
        b.vertex(vector.maxX, vector.maxY, vector.maxZ).uv(minU, minV).endVertex();
        b.vertex(vector.maxX, vector.maxY, vector.minZ).uv(maxU, minV).endVertex();
        b.vertex(vector.minX, vector.maxY, vector.minZ).uv(maxU, maxV).endVertex();

        // Bottom side
        //b.normal(0, -1, 0);
        b.vertex(vector.maxX, vector.minY, vector.maxZ).uv(minU, maxV).endVertex();
        b.vertex(vector.minX, vector.minY, vector.maxZ).uv(minU, minV).endVertex();
        b.vertex(vector.minX, vector.minY, vector.minZ).uv(maxU, minV).endVertex();
        b.vertex(vector.maxX, vector.minY, vector.minZ).uv(maxU, maxV).endVertex();

        // Draw west side:
        //b.normal(-1, 0, 0);
        b.vertex(vector.minX, vector.minY, vector.maxZ).uv(minU, maxV).endVertex();
        b.vertex(vector.minX, vector.maxY, vector.maxZ).uv(minU, minV).endVertex();
        b.vertex(vector.minX, vector.maxY, vector.minZ).uv(maxU, minV).endVertex();
        b.vertex(vector.minX, vector.minY, vector.minZ).uv(maxU, maxV).endVertex();

        // Draw east side:
        //b.normal(1, 0, 0);
        b.vertex(vector.maxX, vector.minY, vector.minZ).uv(minU, maxV).endVertex();
        b.vertex(vector.maxX, vector.maxY, vector.minZ).uv(minU, minV).endVertex();
        b.vertex(vector.maxX, vector.maxY, vector.maxZ).uv(maxU, minV).endVertex();
        b.vertex(vector.maxX, vector.minY, vector.maxZ).uv(maxU, maxV).endVertex();

        // Draw north side
        //b.normal(0, 0, -1);
        b.vertex(vector.minX, vector.minY, vector.minZ).uv(minU, maxV).endVertex();
        b.vertex(vector.minX, vector.maxY, vector.minZ).uv(minU, minV).endVertex();
        b.vertex(vector.maxX, vector.maxY, vector.minZ).uv(maxU, minV).endVertex();
        b.vertex(vector.maxX, vector.minY, vector.minZ).uv(maxU, maxV).endVertex();

        // Draw south side
        //b.normal(0, 0, 1);
        b.vertex(vector.minX, vector.minY, vector.maxZ).uv(minU, maxV).endVertex();
        b.vertex(vector.maxX, vector.minY, vector.maxZ).uv(minU, minV).endVertex();
        b.vertex(vector.maxX, vector.maxY, vector.maxZ).uv(maxU, minV).endVertex();
        b.vertex(vector.minX, vector.maxY, vector.maxZ).uv(maxU, maxV).endVertex();

        if (!wasTesselating) {
            t.end();
        }
    }
    /*
    *//**
     * Draws a cube with the size of vector. Every face has the same color This uses the Tesselator
     *
     * @author Koen Beckers (K4Unl)
     * @param vector
     *//*
    public static void drawTesselatedCube(AABB vector) {

        Tesselator t = Tesselator.instance;
        boolean wasTesselating = false;

        // Check if we were already tesselating
        try {
            t.startDrawingQuads();
        } catch (IllegalStateException e) {
            wasTesselating = true;
        }

        // Top side
        t.setNormal(0, 1, 0);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);

        // Bottom side
        t.setNormal(0, -1, 0);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.minY, vector.minZ);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);
        t.addVertex(vector.minX, vector.minY, vector.minZ);

        // Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);

        // Draw north side
        t.setNormal(0, 0, -1);
        t.addVertex(vector.minX, vector.minY, vector.minZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw south side
        t.setNormal(0, 0, 1);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);

        if (!wasTesselating) {
            t.draw();
        }
    }

    *//**
     * ???
     *
     * @author ???
     * @param vector
     *//*
    public static void drawTesselatedCubeWithoutNormals(AABB vector) {

        Tesselator t = Tesselator.instance;
        boolean wasTesselating = false;

        // Check if we were already tesselating
        try {
            t.startDrawingQuads();
        } catch (IllegalStateException e) {
            wasTesselating = true;
        }

        // Top side
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);

        // Bottom side
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.minY, vector.minZ);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw west side:
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);
        t.addVertex(vector.minX, vector.minY, vector.minZ);

        // Draw east side:
        t.addVertex(vector.maxX, vector.minY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);

        // Draw north side
        t.addVertex(vector.minX, vector.minY, vector.minZ);
        t.addVertex(vector.minX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.maxY, vector.minZ);
        t.addVertex(vector.maxX, vector.minY, vector.minZ);

        // Draw south side
        t.addVertex(vector.minX, vector.minY, vector.maxZ);
        t.addVertex(vector.maxX, vector.minY, vector.maxZ);
        t.addVertex(vector.maxX, vector.maxY, vector.maxZ);
        t.addVertex(vector.minX, vector.maxY, vector.maxZ);

        if (!wasTesselating) {
            t.draw();
        }
    }*/

    /**
     * ???
     *
     * @author amadornes
     * @param d
     */
    public static void rotateRenderMatrix(Direction d) {

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
