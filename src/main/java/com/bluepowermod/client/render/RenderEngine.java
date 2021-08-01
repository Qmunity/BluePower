/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import com.bluepowermod.block.power.BlockEngine;
import com.bluepowermod.init.BPBlocks;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;

import net.minecraft.client.renderer.vertex.VertexBuffer;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;

import com.bluepowermod.tile.tier3.TileEngine;

import java.util.Random;

/**
 *
 * @author TheFjong, MoreThanHidden
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderEngine extends BlockEntityRenderer<TileEngine> {

    float rotateAmount  = 0F;

    RenderEngine(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }


    @Override
    public void render(TileEngine engine, float f, PoseStack matrixStack, MultiBufferSource iRenderTypeBuffer, int i, int i1) {

        Level world = engine.getLevel();
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRenderer();
        BlockPos pos = engine.getBlockPos();
        BlockState state = BPBlocks.engine.defaultBlockState().setValue(BlockEngine.FACING, engine.getOrientation());
        Direction facing = engine.getOrientation();

        matrixStack.pushPose();

        float rotationX = 90.0F;
        float rotation = 0.0F;
        if (facing == Direction.NORTH) {
            matrixStack.translate(0,1,0);
            rotation = 0F;
        } else if (facing == Direction.SOUTH) {
            matrixStack.translate(1,1,1);
            rotation = 180F;
        } else if (facing == Direction.EAST) {
            matrixStack.translate(1,1,0);
            rotation = 90F;
        } else if (facing == Direction.WEST) {
            matrixStack.translate(0,1,1);
            rotation = -90F;
        } else if (facing == Direction.UP) {
            matrixStack.translate(0,1,1);
            rotationX = 180F;
        } else if (facing == Direction.DOWN) {
            rotationX = 0F;
        }

        matrixStack.mulPose(Vector3f.XP.rotationDegrees(rotationX));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(rotation));

        matrixStack.pushPose();

        float f2 = 0;
        if(engine.isActive) {
            f += engine.pumpTick;
            if (engine.pumpSpeed > 0) {
                f /= engine.pumpSpeed;
            }
            f2 = ((float) (.5 - .5 * Math.cos(3.1415926535897931D * (double) f)) / 4);
        }

        matrixStack.translate(0, f2, 0);

        IBakedModel glider = dispatcher.getBlockModel(state.setValue(BlockEngine.GLIDER, true));
        //Render the glider
        VertexConsumer builder = iRenderTypeBuffer.getBuffer(RenderType.cutout());
        dispatcher.getModelRenderer().renderModel(world, glider, state.setValue(BlockEngine.GLIDER, true), pos, matrixStack, builder, false, new Random(), 0, 0, EmptyModelData.INSTANCE);

        matrixStack.popPose();
        matrixStack.pushPose();
        matrixStack.translate(0.5, 0, 0.5);
        long angle = engine.isActive ? (System.currentTimeMillis() / 10) % 360 : 0;
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(angle));
        matrixStack.translate(-0.5, 0, -0.5);
        IBakedModel gear = dispatcher.getBlockModel(state.setValue(BlockEngine.GEAR, true));
        // Render the rotating cog
        dispatcher.getModelRenderer().renderModel(world, gear, state.setValue(BlockEngine.GEAR, true), pos, matrixStack, iRenderTypeBuffer.getBuffer(RenderType.cutout()), false, new Random(), 0, 0, EmptyModelData.INSTANCE);

        matrixStack.popPose();
        matrixStack.popPose();
    }

}