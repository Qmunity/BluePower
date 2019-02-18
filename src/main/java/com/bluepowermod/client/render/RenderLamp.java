/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.block.machine.BlockLampSurface;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;


@SideOnly(Side.CLIENT)
public class RenderLamp extends TileEntitySpecialRenderer {

    public static int pass;

    @Override
    public void render(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
        if (!(te.getBlockType() instanceof BlockLamp))
            return;

        if (pass != 0) {
            BlockLamp bLamp = (BlockLamp) te.getBlockType();
            int power = te.getWorld().getBlockState(te.getPos()).getValue(BlockLamp.POWER);

            int color = bLamp.getColor(te.getWorld(), te.getPos(), 0);

            int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int r = (color & redMask) >> 16;
            int g = (color & greenMask) >> 8;
            int b = (color & blueMask);

            if (bLamp.isInverted()) {
                power = 15 - power;
            }
            AxisAlignedBB box = bLamp.getSize();
            //Define our base Glow
            if (bLamp instanceof  BlockLampSurface)
                box = AABBUtils.rotate(box, te.getWorld().getBlockState(te.getPos()).getValue(BlockLampSurface.FACING));
            box = box.equals(Block.FULL_BLOCK_AABB) ? box.grow(0.05) : box.grow(0.03125);
            boolean[] renderFaces = new boolean[] { true, true, true, true, true, true };

            //Remove overlapping Glow
            if(bLamp.getSize().equals(Block.FULL_BLOCK_AABB)) {
                for (EnumFacing face : EnumFacing.VALUES) {
                    IBlockState state = te.getWorld().getBlockState(te.getPos().offset(face.getOpposite()));
                    if (state.getBlock() instanceof BlockLamp && ((BlockLamp)state.getBlock()).getSize().equals(Block.FULL_BLOCK_AABB)) {
                        if (((BlockLamp) state.getBlock()).isInverted() ? state.getValue(BlockLamp.POWER) < 15 : state.getValue(BlockLamp.POWER) > 0) {
                            renderFaces[face.getIndex()] = false;
                            double offsetx = (face.getXOffset() * 0.05) > 0 ? (face.getXOffset() * 0.05) : 0;
                            double offsety = (face.getYOffset() * 0.05) > 0 ? (face.getYOffset() * 0.05) : 0;
                            double offsetz = (face.getZOffset() * 0.05) > 0 ? (face.getZOffset() * 0.05) : 0;
                            double toffsetx = (face.getXOffset() * 0.05) < 0 ? (face.getXOffset() * 0.05) : 0;
                            double toffsety = (face.getYOffset() * 0.05) < 0 ? (face.getYOffset() * 0.05) : 0;
                            double toffsetz = (face.getZOffset() * 0.05) < 0 ? (face.getZOffset() * 0.05) : 0;
                            box = new AxisAlignedBB(box.minX + offsetx, box.minY + offsety, box.minZ + offsetz, box.maxX + toffsetx, box.maxY + toffsety, box.maxZ + toffsetz);
                        }
                    }
                }
            }


            GL11.glTranslated(x, y, z);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            // GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glBegin(GL11.GL_QUADS);
            double powerDivision = power / 18D;
            com.bluepowermod.client.render.RenderHelper.drawColoredCube(box, r / 256D, g / 256D, b / 256D, powerDivision * 0.625D,
                    renderFaces);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_CULL_FACE);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            // GL11.glAlphaFunc(GL11.GL_GREATER, 0.1F);
            GL11.glDisable(GL11.GL_BLEND);

            GL11.glTranslated(-x, -y, -z);
        }
    }

}
