/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;



@SideOnly(Side.CLIENT)
public class RenderLamp extends TileEntitySpecialRenderer {

    public static int pass;


    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        if (!(te.getBlockType() instanceof BlockLamp))
            return;

        if (pass != 0) {
            BlockLamp bLamp = (BlockLamp) te.getBlockType();
            int power = ((TileLamp) te).getPower();

            int color = bLamp.getColor(te.getWorld(), te.getPos());

            int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int r = (color & redMask) >> 16;
            int g = (color & greenMask) >> 8;
            int b = (color & blueMask);

            if (bLamp.isInverted()) {
                power = 15 - power;
            }
            // power = 15;
            BlockPos vector = te.getPos();
            AxisAlignedBB box = new AxisAlignedBB(-0.05, -0.05, -0.05, 1.05, 1.05, 1.05);

            boolean[] renderFaces = new boolean[] { true, true, true, true, true, true };

            for (EnumFacing d : EnumFacing.VALUES) {
                BlockPos v = vector.offset(d);
                IBlockState vs = te.getWorld().getBlockState(v);
                Block bl = vs.getBlock();
                if (bl instanceof BlockLamp && ((BlockLamp) bl).getPower(te.getWorld(), v) > 0) {
                    if (d.getFrontOffsetX() < 0) {
                        box = new AxisAlignedBB(new Vec3d(-0.5, box.getMinY(), box.getMinZ()), box.getMax());
                        renderFaces[2] = false;
                    } else if (d.getFrontOffsetY() < 0) {
                        box = new AxisAlignedBB(new Vec3d(box.getMinX(), -0.5, box.getMinZ()), box.getMax());
                        renderFaces[1] = false;
                    } else if (d.getFrontOffsetZ() < 0) {
                        box = new AxisAlignedBB(new Vec3d(box.getMinX(), box.getMinY(), -0.5), box.getMax());
                        renderFaces[4] = false;
                    } else if (d.getFrontOffsetX() > 0) {
                        box = new AxisAlignedBB(box.getMin(), new Vec3d(0.5, box.getMaxY(), box.getMaxZ()));
                        renderFaces[3] = false;
                    } else if (d.getFrontOffsetY() > 0) {
                        box = new AxisAlignedBB(box.getMin(), new Vec3d(box.getMaxX(), 0.5, box.getMaxZ()));
                        renderFaces[0] = false;
                    } else if (d.getFrontOffsetZ() > 0) {
                        box = new AxisAlignedBB(box.getMin(), new Vec3d(box.getMaxX(), box.getMaxY(), 0.5));
                        renderFaces[5] = false;
                    }
                }
            }

            box.getMin().add(new Vec3d(0.5, 0.5, 0.5));
            box.getMax().add(new Vec3d(0.5, 0.5, 0.5));

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
