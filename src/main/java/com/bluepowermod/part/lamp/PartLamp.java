/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.part.lamp;

import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.part.BPPartFace;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.EnumSkyBlock;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.part.IPartLightEmitter;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import java.util.Arrays;
import java.util.List;

/**
 * Base class for the lamps that are multiparts.
 *
 * @author Koen Beckers (K4Unl), Amadornes
 *
 */
public class PartLamp extends BPPartFace implements IPartLightEmitter {

    protected String colorName;
    private final int colorVal;
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
                //RenderHelper renderer = new RenderHelper();
                //renderStatic(new Vec3i(0, 0, 0), renderer, null, 0);
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

    }

    /**
     * This render method gets called whenever there's a block update in the chunk. You should use this to remove load from the renderer if a part of
     * the rendering code doesn't need to get called too often or just doesn't change at all. To call a render update to re-render this just call
     * {@link com.bluepowermod.part.BPPart#markPartForRenderUpdate()}
     *
     * @param loc Distance from the player's position
     * @param pass Render pass (0 or 1)
     * @return Whether or not it rendered something
     */

    @Override
    public boolean renderStatic(Vec3i loc, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        //renderer.setTextureRotations(0, 0, 3, 3, 3, 3);
        switch (getFace().ordinal()) {
        case 0:
            break;
        case 1:
            renderer.setRotation(180, 0, 0, Vec3d.center);
            break;
        case 2:
            renderer.setRotation(90, 0, 0, Vec3d.center);
            break;
        case 3:
            renderer.setRotation(-90, 0, 0, Vec3d.center);
            break;
        case 4:
            renderer.setRotation(0, 0, -90, Vec3d.center);
            break;
        case 5:
            renderer.setRotation(0, 0, 90, Vec3d.center);
            break;
        }

        // Render base
        renderBase(renderer, pass);

        // Color multiplier
        int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
        int r = (colorVal & redMask) >> 16;
        int g = (colorVal & greenMask) >> 8;
        int b = colorVal & blueMask;

        renderLamp(renderer, pass, r, g, b);

        return true;
    }

    @Override
    public boolean shouldRenderOnPass(int pass) {

        return true;
    }

    /**
     * Code to render the base portion of the lamp. Will not be colored
     *
     * @author Koen Beckers (K4Unl)
     * @param renderer
     * @param pass
     */
    public void renderBase(RenderHelper renderer, int pass) {

    }

    /**
     * Code to render the actual lamp portion of the lamp. Will be colored
     *
     * @author Koen Beckers (K4Unl)
     * @param renderer The RenderHelper
     * @param pass
     *            The pass that is rendered now. Pass 1 for solids. Pass 2 for transparents
     * @param r
     *            The ammount of red in the lamp
     * @param g
     *            The ammount of green in the lamp
     * @param b
     *            The ammount of blue in the lamp
     */
    public void renderLamp(RenderHelper renderer, int pass, int r, int g, int b) {

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
