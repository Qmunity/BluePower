/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.tile.tier1.TileLamp;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderLamp extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    public static int pass;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return true;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

    /******* TESR ***********/
    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float f) {

        if (!(te.getBlockType() instanceof BlockLamp))
            return;

        if (pass != 0) {
            BlockLamp bLamp = (BlockLamp) te.getBlockType();
            int power = ((TileLamp) te).getPower();

            int color = bLamp.getColor(te.getWorldObj(), te.xCoord, te.yCoord, te.zCoord);

            int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int r = (color & redMask) >> 16;
            int g = (color & greenMask) >> 8;
            int b = (color & blueMask);

            if (bLamp.isInverted()) {
                power = 15 - power;
            }
            // power = 15;
            Vec3i vector = new Vec3i(te);
            Vec3dCube box = new Vec3dCube(-0.5, -0.5, -0.5, 0.5, 0.5, 0.5).expand(0.8 / 16D);

            boolean[] renderFaces = new boolean[] { true, true, true, true, true, true };

            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                Vec3i v = vector.getRelative(d);
                Block bl = v.getBlock();
                if (bl instanceof BlockLamp && ((BlockLamp) bl).getPower(v.getWorld(), v.getX(), v.getY(), v.getZ()) > 0) {
                    if (d.offsetX < 0) {
                        box.getMin().setX(-0.5);
                        renderFaces[2] = false;
                    } else if (d.offsetY < 0) {
                        box.getMin().setY(-0.5);
                        renderFaces[1] = false;
                    } else if (d.offsetZ < 0) {
                        box.getMin().setZ(-0.5);
                        renderFaces[4] = false;
                    } else if (d.offsetX > 0) {
                        box.getMax().setX(0.5);
                        renderFaces[3] = false;
                    } else if (d.offsetY > 0) {
                        box.getMax().setY(0.5);
                        renderFaces[0] = false;
                    } else if (d.offsetZ > 0) {
                        box.getMax().setZ(0.5);
                        renderFaces[5] = false;
                    }
                }
            }

            box.add(0.5, 0.5, 0.5);

            GL11.glPushMatrix();
            {
                GL11.glTranslated(x, y, z);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);

                float powerDivision = power / 15F;

                float lastX = OpenGlHelper.lastBrightnessX, lastY = OpenGlHelper.lastBrightnessY;
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, powerDivision * 240, powerDivision * 240);

                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glBegin(GL11.GL_QUADS);
                com.bluepowermod.client.render.RenderHelper.drawColoredCube(box, r / 256D, g / 256D, b / 256D, powerDivision * 0.375, renderFaces);
                GL11.glEnd();

                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                GL11.glBegin(GL11.GL_QUADS);
                com.bluepowermod.client.render.RenderHelper.drawColoredCube(box, r / 256D, g / 256D, b / 256D, powerDivision * 0.375, renderFaces);
                GL11.glEnd();

                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastX, lastY);

                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDisable(GL11.GL_BLEND);
            }
            GL11.glPopMatrix();
        }
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        GL11.glPushMatrix();
        {
            switch (type) {
            case ENTITY:
                GL11.glTranslated(-0.5, -0.5, -0.5);
                break;
            case EQUIPPED:
                break;
            case EQUIPPED_FIRST_PERSON:
                GL11.glTranslated(0, -0.1, 0);
                break;
            case INVENTORY:
                GL11.glTranslated(0, -0.1, 0);
                break;
            default:
                break;
            }

            BlockLamp block = (BlockLamp) Block.getBlockFromItem(item.getItem());
            int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int r = (block.getColor() & redMask) >> 16;
            int g = (block.getColor() & greenMask) >> 8;
            int b = (block.getColor() & blueMask);

            Vec3dCube cube = new Vec3dCube(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setColorOpaque(r, g, b);

            RenderHelper h = RenderHelper.instance;
            h.reset();
            h.setColor(block.getColor());
            h.renderBox(cube, block.isInverted() ? BlockLamp.on : BlockLamp.off);
            h.reset();

            t.draw();
            if (block.isInverted()) {
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);

                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glBegin(GL11.GL_QUADS);
                com.bluepowermod.client.render.RenderHelper.drawColoredCube(cube.clone().expand(0.8 / 16D), r / 256D, g / 256D, b / 256D, 0.375);
                GL11.glEnd();

                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
                GL11.glBegin(GL11.GL_QUADS);
                com.bluepowermod.client.render.RenderHelper.drawColoredCube(cube.clone().expand(0.8 / 16D), r / 256D, g / 256D, b / 256D, 0.375);
                GL11.glEnd();

                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                // GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
                GL11.glDisable(GL11.GL_BLEND);
            }
        }
        GL11.glPopMatrix();
    }

}
