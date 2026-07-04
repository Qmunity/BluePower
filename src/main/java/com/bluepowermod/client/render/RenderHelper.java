/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderHelper {


    /**
     * @author amadornes
     * @param x
     * @param y
     * @param z
     * @param angle
     **/
    public static void renderPointer(VertexConsumer vertexBuilder, PoseStack matrixStack, double x, double y, double z, double angle) {
        PoseStack.Pose entry = matrixStack.last();

        matrixStack.pushPose();
        {
            matrixStack.translate(x, y, z);

            matrixStack.translate(0.5, 0.5, 0.5);
            matrixStack.mulPose(Axis.YP.rotationDegrees((float) (180 + 360 * -angle)));
            matrixStack.translate(-0.5, -0.5, -0.5);

            Matrix4f positionMatrix = entry.pose();
            Matrix3f normalMatrix = entry.normal();

            // Bottom
            vertexBuilder.addVertex(positionMatrix, 0.5f, 0f, 2f / 16f)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.5f, 1f / 16f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(0xf000f0)
                    .setNormal(entry, 0, -1, 0);

            vertexBuilder.addVertex(positionMatrix, 0.5f + 1f / 8f, 0f, 0.5f)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.5f + 1f / 8f, 0.5f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(0xf000f0)
                    .setNormal(entry, 0, -1, 0);

            vertexBuilder.addVertex(positionMatrix, 0.5f, 0f, 0.5f + 1f / 8f)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.5f, 0.5f + 1f / 8f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(0xf000f0)
                    .setNormal(entry, 0, -1, 0);

            vertexBuilder.addVertex(positionMatrix, 0.5f - 1f / 8f, 0f, 0.5f)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.5f - 1f / 8f, 0.5f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(0xf000f0)
                    .setNormal(entry, 0, -1, 0);

            // Top
            vertexBuilder.addVertex(positionMatrix, 0.5f, 1f / 16f, 2f / 16f)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.5f, 1f / 16f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(0xf000f0)
                    .setNormal(entry, 0, 1, 0);

            vertexBuilder.addVertex(positionMatrix, 0.5f - 1f / 8f, 1f / 16f, 0.5f)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.5f - 1f / 8f, 0.5f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(0xf000f0)
                    .setNormal(entry, 0, 1, 0);

            vertexBuilder.addVertex(positionMatrix, 0.5f, 1f / 16f, 0.5f + 1f / 8f)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.5f, 0.5f + 1f / 8f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(0xf000f0)
                    .setNormal(entry, 0, 1, 0);

            vertexBuilder.addVertex(positionMatrix, 0.5f + 1f / 8f, 1f / 16f, 0.5f)
                    .setColor(255, 255, 255, 255)
                    .setUv(0.5f + 1f / 8f, 0.5f)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(0xf000f0)
                    .setNormal(entry, 0, 1, 0);
        }
        matrixStack.popPose();
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

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(ResourceLocation.parse("minecraft:white_concrete"));
        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();

        // Top side
        if (renderFaces.length < 1 || renderFaces[0]) {
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 1.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 1.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 1.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 1.0F, 0.0F);
        }

        // Bottom side
        if (renderFaces.length < 2 || renderFaces[1]) {
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, -1.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, -1.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, -1.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, -1.0F, 0.0F);
        }

        // Draw west side:
        if (renderFaces.length < 3 || renderFaces[5]) {
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, -1.0F, 0.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, -1.0F, 0.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, -1.0F, 0.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, -1.0F, 0.0F, 0.0F);
        }

        // Draw east side:
        if (renderFaces.length < 4 || renderFaces[4]) {
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 1.0F, 0.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 1.0F, 0.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 1.0F, 0.0F, 0.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 1.0F, 0.0F, 0.0F);
        }

        // Draw north side
        if (renderFaces.length < 5 || renderFaces[3]) {
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 0.0F, -1.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 0.0F, -1.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 0.0F, -1.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.minZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 0.0F, -1.0F);
        }

        // Draw south side
        if (renderFaces.length < 6 || renderFaces[2]) {
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.minY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 0.0F, 1.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.minY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(minU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 0.0F, 1.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.maxX, (float) vector.maxY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, minV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 0.0F, 1.0F);
            vertexBuilder.addVertex(positionMatrix, (float) vector.minX, (float) vector.maxY, (float) vector.maxZ)
                    .setColor(r,g,b,a)
                    .setUv(maxU, maxV)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(light)
                    .setNormal(entry, 0.0F, 0.0F, 1.0F);
        }

    }

    /**
     * Draws a cube with the size of vector. It uses the texture that is already bound and maps that completely This uses the Tesselator
     *
     * @author Koen Beckers (K4Unl)
     * @param vector
     */
    public static void drawTesselatedTexturedCube(AABB vector) {

        Tesselator t = Tesselator.getInstance();
        BufferBuilder b = t.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        float minU = 0;
        float maxU = 1;
        float minV = 0;
        float maxV = 1;

        // Top side
        //b.setNormal(0, 1, 0);
        b.addVertex((float) vector.minX, (float) vector.maxY, (float) vector.maxZ).setUv(minU, maxV);
        b.addVertex((float) vector.maxX, (float) vector.maxY, (float) vector.maxZ).setUv(minU, minV);
        b.addVertex((float) vector.maxX, (float) vector.maxY, (float) vector.minZ).setUv(maxU, minV);
        b.addVertex((float) vector.minX, (float) vector.maxY, (float) vector.minZ).setUv(maxU, maxV);

        // Bottom side
        //b.setNormal(0, -1, 0);
        b.addVertex((float) vector.maxX, (float) vector.minY, (float) vector.maxZ).setUv(minU, maxV);
        b.addVertex((float) vector.minX, (float) vector.minY, (float) vector.maxZ).setUv(minU, minV);
        b.addVertex((float) vector.minX, (float) vector.minY, (float) vector.minZ).setUv(maxU, minV);
        b.addVertex((float) vector.maxX, (float) vector.minY, (float) vector.minZ).setUv(maxU, maxV);

        // Draw west side:
        //b.setNormal(-1, 0, 0);
        b.addVertex((float) vector.minX, (float) vector.minY, (float) vector.maxZ).setUv(minU, maxV);
        b.addVertex((float) vector.minX, (float) vector.maxY, (float) vector.maxZ).setUv(minU, minV);
        b.addVertex((float) vector.minX, (float) vector.maxY, (float) vector.minZ).setUv(maxU, minV);
        b.addVertex((float) vector.minX, (float) vector.minY, (float) vector.minZ).setUv(maxU, maxV);

        // Draw east side:
        //b.setNormal(1, 0, 0);
        b.addVertex((float) vector.maxX, (float) vector.minY, (float) vector.minZ).setUv(minU, maxV);
        b.addVertex((float) vector.maxX, (float) vector.maxY, (float) vector.minZ).setUv(minU, minV);
        b.addVertex((float) vector.maxX, (float) vector.maxY, (float) vector.maxZ).setUv(maxU, minV);
        b.addVertex((float) vector.maxX, (float) vector.minY, (float) vector.maxZ).setUv(maxU, maxV);

        // Draw north side
        //b.setNormal(0, 0, -1);
        b.addVertex((float) vector.minX, (float) vector.minY, (float) vector.minZ).setUv(minU, maxV);
        b.addVertex((float) vector.minX, (float) vector.maxY, (float) vector.minZ).setUv(minU, minV);
        b.addVertex((float) vector.maxX, (float) vector.maxY, (float) vector.minZ).setUv(maxU, minV);
        b.addVertex((float) vector.maxX, (float) vector.minY, (float) vector.minZ).setUv(maxU, maxV);

        // Draw south side
        //b.setNormal(0, 0, 1);
        b.addVertex((float) vector.minX, (float) vector.minY, (float) vector.maxZ).setUv(minU, maxV);
        b.addVertex((float) vector.maxX, (float) vector.minY, (float) vector.maxZ).setUv(minU, minV);
        b.addVertex((float) vector.maxX, (float) vector.maxY, (float) vector.maxZ).setUv(maxU, minV);
        b.addVertex((float) vector.minX, (float) vector.maxY, (float) vector.maxZ).setUv(maxU, maxV);

    }

}
