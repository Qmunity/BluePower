/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import com.bluepowermod.block.lighting.BlockLamp;
import com.bluepowermod.tile.tier1.TileLamp;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class RenderLamp implements BlockEntityRenderer<TileLamp> {

    @Override
    public void render(TileLamp te, float v, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int partialTicks, int destroyStage) {
        if (!(te.getBlockState().getBlock() instanceof BlockLamp)) //|| Loader.isModLoaded("albedo"))
            return;

            BlockState stateLamp = te.getBlockState();
            BlockLamp bLamp = (BlockLamp) stateLamp.getBlock();
            if(te.getLevel() == null) {return;}
            int power = stateLamp.getValue(BlockLamp.POWER);
            int color = bLamp.getColor(stateLamp, te.getLevel(), te.getBlockPos(), 0);

            int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int r = (color & redMask) >> 16;
            int g = (color & greenMask) >> 8;
            int b = (color & blueMask);

            AABB box = stateLamp.getShape(te.getLevel(), te.getBlockPos()).bounds();

            //Define our base Glow
            box = box.equals(new AABB(0,0,0,1,1,1)) ? box.inflate(0.05) : box.inflate(0.03125);
            boolean[] renderFaces = new boolean[] { true, true, true, true, true, true };

            //Remove overlapping Glow
            if(stateLamp.getShape(te.getLevel(), te.getBlockPos()).bounds().equals(new AABB(0,0,0,1,1,1))) {
                for (Direction face : Direction.values()) {
                    BlockState state = te.getLevel().getBlockState(te.getBlockPos().relative(face.getOpposite()));
                    if (state.getBlock() instanceof BlockLamp && state.getShape(te.getLevel(), te.getBlockPos()).bounds().equals(new AABB(0,0,0,1,1,1))) {
                        if (state.getValue(BlockLamp.POWER) > 0) {
                            renderFaces[face.get3DDataValue()] = false;
                            double offsetx = (face.getStepX() * 0.05) > 0 ? (face.getStepX() * 0.05) : 0;
                            double offsety = (face.getStepY() * 0.05) > 0 ? (face.getStepY() * 0.05) : 0;
                            double offsetz = (face.getStepZ() * 0.05) > 0 ? (face.getStepZ() * 0.05) : 0;
                            double toffsetx = (face.getStepX() * 0.05) < 0 ? (face.getStepX() * 0.05) : 0;
                            double toffsety = (face.getStepY() * 0.05) < 0 ? (face.getStepY() * 0.05) : 0;
                            double toffsetz = (face.getStepZ() * 0.05) < 0 ? (face.getStepZ() * 0.05) : 0;
                            box = new AABB(box.minX + offsetx, box.minY + offsety, box.minZ + offsetz, box.maxX + toffsetx, box.maxY + toffsety, box.maxZ + toffsetz);
                        }
                    }
                }
            }

            matrixStack.pushPose();
            double powerDivision = power / 18D;
            com.bluepowermod.client.render.RenderHelper.drawColoredCube(box, iRenderTypeBuffer.getBuffer(BPRenderTypes.LAMP_GLOW), matrixStack, r, g, b, (int)(powerDivision * 200), 200,
                    renderFaces);
            matrixStack.popPose();
    }
}
