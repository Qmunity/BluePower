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
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class RenderLamp extends TileEntityRenderer<TileLamp> {


    RenderLamp(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TileLamp te, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int partialTicks, int destroyStage) {
        if (!(te.getBlockState().getBlock() instanceof BlockLamp)) //|| Loader.isModLoaded("albedo"))
            return;

            BlockState stateLamp = te.getBlockState();
            BlockLamp bLamp = (BlockLamp) stateLamp.getBlock();
            if(te.getWorld() == null) {return;}
            int power = stateLamp.get(BlockLamp.POWER);
            int color = bLamp.getColor(te.getWorld(), te.getPos(), 0);

            int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int r = (color & redMask) >> 16;
            int g = (color & greenMask) >> 8;
            int b = (color & blueMask);

            AxisAlignedBB box = stateLamp.getShape(te.getWorld(), te.getPos()).getBoundingBox();

            //Define our base Glow
            box = box.equals(new AxisAlignedBB(0,0,0,1,1,1)) ? box.grow(0.05) : box.grow(0.03125);
            boolean[] renderFaces = new boolean[] { true, true, true, true, true, true };

            //Remove overlapping Glow
            if(stateLamp.getShape(te.getWorld(), te.getPos()).getBoundingBox().equals(new AxisAlignedBB(0,0,0,1,1,1))) {
                for (Direction face : Direction.values()) {
                    BlockState state = te.getWorld().getBlockState(te.getPos().offset(face.getOpposite()));
                    if (state.getBlock() instanceof BlockLamp && state.getShape(te.getWorld(), te.getPos()).getBoundingBox().equals(new AxisAlignedBB(0,0,0,1,1,1))) {
                        if (state.get(BlockLamp.POWER) > 0) {
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

            matrixStack.push();
            double powerDivision = power / 18D;
            com.bluepowermod.client.render.RenderHelper.drawColoredCube(box, iRenderTypeBuffer.getBuffer(RenderType.getLightning()), matrixStack, r, g, b, (int)(powerDivision * 200), 200,
                    renderFaces);
            matrixStack.pop();
    }
}
