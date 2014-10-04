/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.lamp;

import java.util.List;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.client.renderers.RenderHelper;
import com.qmunity.lib.vec.Vec3dCube;

/**
 *
 * @author Koen Beckers (K4Unl)
 *
 */
public class PartCageLamp extends PartLamp {

    public PartCageLamp(String colorName, Integer colorVal, Boolean inverted) {

        super(colorName, colorVal, inverted);
    }

    /**
     * @author Koen Beckers (K4Unl)
     */

    @Override
    public void addSelectionBoxes(List<AxisAlignedBB> boxes) {

        boxes.add(AxisAlignedBB.getBoundingBox(pixel * 3, 0.0, pixel * 3, 1.0 - (pixel * 3), pixel * 2, 1.0 - pixel * 3));
        boxes.add(AxisAlignedBB.getBoundingBox(pixel * 4, pixel * 2, pixel * 4, 1.0 - (pixel * 4), 1.0 - (pixel * 4), 1.0 - pixel * 4));
    }

    @Override
    public void renderBase(int pass) {

        if (pass != 0)
            return;
        Tessellator t = Tessellator.instance;
        Vec3dCube vector = new Vec3dCube(pixel * 3, 0.0, pixel * 3, 1.0 - (pixel * 3), pixel * 2, 1.0 - pixel * 3);
        IIcon topIcon = IconSupplier.cagedLampFootTop;
        IIcon sideIcon = IconSupplier.cagedLampFootSide;

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

        /*
         * minU = sideIcon.getInterpolatedU(vector.getMinX() * 16); maxU = sideIcon.getInterpolatedU(vector.getMaxX() * 16); minV =
         * sideIcon.getInterpolatedV(vector.getMinY() * 16); maxV = sideIcon.getInterpolatedV(vector.getMaxY() * 16);
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

        // And now, the cage itself!
        // No. Not Nicholas Cage. The lamp-cage!
        vector = new Vec3dCube(pixel * 4, pixel * 2, pixel * 4, 1.0 - (pixel * 4), 1.0 - (pixel * 4), 1.0 - pixel * 4);
        topIcon = IconSupplier.cagedLampCageTop;
        sideIcon = IconSupplier.cagedLampCageSide;

        minU = topIcon.getInterpolatedU(vector.getMinX() * 16);
        maxU = topIcon.getInterpolatedU(vector.getMaxX() * 16);
        minV = topIcon.getInterpolatedV(vector.getMinZ() * 16);
        maxV = topIcon.getInterpolatedV(vector.getMaxZ() * 16);

        // Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);

        minU = sideIcon.getInterpolatedU(vector.getMinX() * 16);
        maxU = sideIcon.getInterpolatedU(vector.getMaxX() * 16);
        minV = sideIcon.getInterpolatedV(vector.getMinY() * 16);
        maxV = sideIcon.getInterpolatedV(vector.getMaxY() * 16);
        // Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);

        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);

        // Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);

        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);

        // Draw north side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);

        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);

        // Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);

        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
    }

    @Override
    public void renderLamp(int pass, int r, int g, int b) {

        Vec3dCube vector = new Vec3dCube(pixel * 5, pixel * 2, pixel * 5, 1.0 - (pixel * 5), 1.0 - (pixel * 5), 1.0 - pixel * 5);

        Tessellator t = Tessellator.instance;
        IIcon iconToUseTop;
        IIcon iconToUseSide;
        if (power == 0) {
            iconToUseSide = IconSupplier.cagedLampLampInactive;
            iconToUseTop = IconSupplier.cagedLampLampInactiveTop;
        } else {
            iconToUseSide = IconSupplier.cagedLampLampActive;
            iconToUseTop = IconSupplier.cagedLampLampActiveTop;

            t.setColorRGBA(r, g, b, 20);
            RenderHelper.drawTesselatedCube(new Vec3dCube(pixel * 4.5, pixel * 2, pixel * 4.5, 1.0 - (pixel * 4.5), 1.0 - (pixel * 4.5),
                    1.0 - pixel * 4.5));
            t.setColorRGBA(r, g, b, 255);
        }

        if (pass == 0) {
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

            minU = iconToUseSide.getInterpolatedU(vector.getMinX() * 16);
            maxU = iconToUseSide.getInterpolatedU(vector.getMaxX() * 16);
            minV = iconToUseSide.getInterpolatedV(vector.getMinZ() * 16);
            maxV = iconToUseSide.getInterpolatedV(vector.getMaxZ() * 16);

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

        if (power > 0 && pass == 1) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            // GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDepthMask(false);
            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.drawColoredCube(vector.clone().expand(0.5 / 16D), r / 256D, g / 256D, b / 256D, (power / 15D) * 0.625);
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_BLEND);
        }

    }

    @Override
    public String getType() {

        return (inverted ? "inverted" : "") + "cagelamp" + colorName;
    }

    @Override
    public String getUnlocalizedName() {

        return (inverted ? "inverted" : "") + "cagelamp." + colorName;
    }

}
