/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.lamp;

import com.bluepowermod.client.renderers.IconSupplier;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.vec.Vec3dCube;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Koen Beckers (K4Unl), Amadornes
 *
 */
public class PartFixture extends PartLamp {

    public PartFixture(String colorName, Integer colorVal, Boolean inverted) {

        super(colorName, colorVal, inverted);
    }

    /**
     * @author Koen Beckers (K4Unl), Amadornes
     */
    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        boxes.add(new Vec3dCube(2 / 16D, 0.0, 2 / 16D, 14 / 16D, 2 / 16D, 14 / 16D));
        boxes.add(new Vec3dCube(3 / 16D, 2 / 16D, 3 / 16D, 13 / 16D, 8 / 16D, 13 / 16D));

        return boxes;
    }

    @Override
    public String getType() {

        return (inverted ? "inverted" : "") + "fixture" + colorName;
    }

    @Override
    public String getUnlocalizedName() {

        return (inverted ? "inverted" : "") + "fixture." + colorName;
    }

    /**
     * @author Koen Beckers (K4Unl), Amadornes
     */
    @Override
    public void renderBase(uk.co.qmunity.lib.client.render.RenderHelper renderer, int pass) {

        if (pass != 0)
            return;
        Tessellator t = Tessellator.instance;
        Vec3dCube vector = new Vec3dCube(2 / 16D, 0.0, 2 / 16D, 1.0 - (2 / 16D), 2 / 16D, 1.0 - 2 / 16D);
        IIcon topIcon = IconSupplier.fixtureFootTop;
        IIcon sideIcon = IconSupplier.fixtureFootSide;

        renderer.setRenderSides(false, true, false, false, false, false);
        renderer.renderBox(vector, topIcon);
        renderer.setRenderSides(false, false, true, true, true, true);
        renderer.renderBox(vector, sideIcon);
    }

    @Override
    public void renderLamp(uk.co.qmunity.lib.client.render.RenderHelper renderer, int pass, int r, int g, int b) {

        Vec3dCube vector = new Vec3dCube(3 / 16D, 2 / 16D, 3 / 16D, 1.0 - (3 / 16D), 8 / 16D, 13 / 16D);

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
                //RenderHelper.drawTesselatedCube(new Vec3dCube(4.5 / 16D, 2 / 16D, 4.5 / 16D, 11.5 / 16D, 11.5 / 16D, 11.5 / 16D));
                t.setColorRGBA(r, g, b, 255);
            }

            renderer.setTextureRotations(0, 0, 3, 3, 3, 3);
            renderer.setRenderSides(true, true, false, false, false, false);
            renderer.renderBox(vector, iconToUseTop);
            renderer.setRenderSides(false, false, true, true, true, true);
            renderer.renderBox(vector, iconToUseSide);
            renderer.resetTextureRotations();
        }

        if (power > 0 && pass == 1) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            // GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDepthMask(false);
            GL11.glBegin(GL11.GL_QUADS);
            //renderer.setColor(renderer.);
            //, r / 256D, g / 256D, b / 256D, (power / 15D) * 0.625);
            renderer.renderBox(vector.clone().expand(0.8 / 16D));
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
