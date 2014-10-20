/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.lamp;

import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.client.renderers.RenderHelper;
import com.bluepowermod.part.BPPartFace;
import com.qmunity.lib.helper.RedstoneHelper;
import com.qmunity.lib.part.IPartLightEmitter;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;

/**
 * Base class for the lamps that are multiparts.
 *
 * @author Koen Beckers (K4Unl), Amadornes
 *
 */
public class PartLamp extends BPPartFace implements IPartLightEmitter {

    protected String colorName;
    private int colorVal;
    protected boolean inverted;

    protected int power = 0;

    /**
     * @author amadornes
     * @param colorName
     * @param colorVal
     * @param inverted
     *            TODO
     */
    public PartLamp(String colorName, Integer colorVal, Boolean inverted) {

        this.colorName = colorName;
        this.colorVal = colorVal;
        this.inverted = inverted;
    }

    @Override
    public String getType() {

        return (inverted ? "inverted" : "") + "lamp" + colorName;
    }

    /**
     * @author amadornes
     */
    @Override
    public String getUnlocalizedName() {

        return (inverted ? "inverted" : "") + "lamp." + colorName;
    }

    /**
     * @author amadornes
     */
    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

        boxes.addAll(getSelectionBoxes());
    }

    /**
     * @author amadornes
     */
    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return getSelectionBoxes();
    }

    /**
     * @author amadornes
     */

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        return Arrays.asList(new Vec3dCube(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));
    }

    /**
     * @author Koen Beckers (K4Unl)
     */
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        power = inverted ? 15 : 0;

        GL11.glPushMatrix();
        {
            Tessellator t = Tessellator.instance;
            Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

            GL11.glPushMatrix();
            {
                t.startDrawingQuads();
                renderDynamic(new Vec3d(0, 0, 0), 0, 0);
                t.draw();
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationItemsTexture);
    }

    /**
     * @author Koen Beckers (K4Unl), Amadornes
     */
    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        GL11.glPushMatrix();
        {
            // Rotate and translate
            GL11.glTranslated(translation.getX(), translation.getY(), translation.getZ());
            GL11.glTranslated(0.5, 0.5, 0.5);
            {

                switch (getFace().ordinal()) {
                case 0:
                    break;
                case 1:
                    GL11.glRotated(180, 1, 0, 0);
                    break;
                case 2:
                    GL11.glRotated(90, 1, 0, 0);
                    break;
                case 3:
                    GL11.glRotated(90, -1, 0, 0);
                    break;
                case 4:
                    GL11.glRotated(90, 0, 0, -1);
                    break;
                case 5:
                    GL11.glRotated(90, 0, 0, 1);
                    break;
                }
            }
            GL11.glTranslated(-0.5, -0.5, -0.5);

            // Render

            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();

            t.setColorOpaque_F(1, 1, 1);
            // Render base
            renderBase(pass);

            // Color multiplier
            int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int r = (colorVal & redMask) >> 16;
            int g = (colorVal & greenMask) >> 8;
            int b = (colorVal & blueMask);

            t.setColorOpaque(r, g, b);
            // Render lamp itself here
            renderLamp(pass, r, g, b);

            t.setColorOpaque_F(1, 1, 1);

            t.draw();
        }
        GL11.glPopMatrix();
    }

    /**
     * Code to render the base portion of the lamp. Will not be colored
     *
     * @author Koen Beckers (K4Unl)
     * @param pass
     *            The pass that is rendered now. Pass 1 for solids. Pass 2 for transparents
     */
    public void renderBase(int pass) {

    }

    /**
     * Code to render the actual lamp portion of the lamp. Will be colored
     *
     * @author Koen Beckers (K4Unl)
     * @param pass
     *            The pass that is rendered now. Pass 1 for solids. Pass 2 for transparents
     * @param r
     *            The ammount of red in the lamp
     * @param g
     *            The ammount of green in the lamp
     * @param b
     *            The ammount of blue in the lamp
     */
    public void renderLamp(int pass, int r, int g, int b) {

        Vec3dCube vector = new Vec3dCube(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

        if (pass == 0) {
            Tessellator t = Tessellator.instance;
            IIcon iconToUse;
            if (power == 0) {
                iconToUse = IconSupplier.lampOff;
            } else {
                iconToUse = IconSupplier.lampOn;

                /*
                 * t.setColorRGBA(r, g, b, 20); RenderHelper.drawTesselatedCube(new Vec3dCube(pixel * 4.5, pixel * 2, pixel * 4.5, 1.0 - (pixel*4.5),
                 * 1.0 - (pixel * 4.5), 1.0 - pixel * 4.5)); t.setColorRGBA(r, g, b, 255);
                 */
            }

            double minU = iconToUse.getMinU();
            double maxU = iconToUse.getMaxU();
            double minV = iconToUse.getMinV();
            double maxV = iconToUse.getMaxV();

            // Top side
            t.setNormal(0, 1, 0);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);

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
            t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
            t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
            t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        }

        if (power > 0 && pass == 1) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDepthMask(false);
            GL11.glBegin(GL11.GL_QUADS);
            RenderHelper.drawColoredCube(vector.clone().expand(0.8 / 16D), r / 256D, g / 256D, b / 256D, (power / 15D) * 0.625);
            GL11.glEnd();
            GL11.glDepthMask(true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    @Override
    public int getLightValue() {

        return power;
    }

    /**
     * @author amadornes
     */
    @Override
    public void onUpdate() {

        int old = power;

        power = 0;
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            power = Math.max(power, RedstoneHelper.getInput(getWorld(), getX(), getY(), getZ(), d));

        if (inverted) {
            power = 15 - power;
        }

        if (old != power) {
            notifyUpdate();
            getWorld().updateLightByType(EnumSkyBlock.Block, getX(), getY(), getZ());
        }
    }

    /**
     * @author amadornes
     */
    // @Override
    // public CreativeTabs getCreativeTab() {
    //
    // return CustomTabs.tabBluePowerLighting;
    // }
    //
    // @Override
    // public float getHardness() {
    //
    // return 1.5F;
    // }

}
