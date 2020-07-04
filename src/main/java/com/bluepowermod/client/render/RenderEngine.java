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
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.EmptyModelData;
import org.lwjgl.opengl.GL11;

import com.bluepowermod.tile.tier3.TileEngine;

import java.util.Random;

/**
 *
 * @author TheFjong, MoreThanHidden
 * TODO: Create Rotation Helper and re-implement as Fast TESR
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderEngine extends TileEntityRenderer<TileEngine> {

    float rotateAmount  = 0F;

    RenderEngine(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }


    @Override
    public void render(TileEngine engine, float f, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int i, int i1) {

        World world = engine.getWorld();
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        BlockPos pos = engine.getPos();

        TileEngine tile = (TileEngine) engine.getWorld().getTileEntity(engine.getPos());
        BlockState state = BPBlocks.engine.getDefaultState().with(BlockEngine.FACING, tile.getOrientation());


        matrixStack.push();

        float f2 = 0;
        if(tile.isActive) {
            f += tile.pumpTick;
            if (tile.pumpSpeed > 0) {
                f /= tile.pumpSpeed;
            }
            f2 = ((float) (.5 - .5 * Math.cos(3.1415926535897931D * (double) f)) / 4);
        }
        GL11.glTranslatef(0, f2, 0);
        IBakedModel glider = dispatcher.getModelForState(state.with(BlockEngine.GLIDER, true));
        //Render the glider
        dispatcher.getBlockModelRenderer().renderModel(world, glider, state.with(BlockEngine.GLIDER, true), pos, matrixStack, iRenderTypeBuffer.getBuffer(RenderType.getCutout()), false, new Random(), 0, 0, EmptyModelData.INSTANCE);

        matrixStack.pop();
        matrixStack.push();

        long angle = tile.isActive ? (System.currentTimeMillis() / 10) % 360 : 0;
        RenderSystem.rotatef(angle, 0, 1, 0);
        IBakedModel gear = dispatcher.getModelForState(state.with(BlockEngine.GEAR, true));
        // Render the rotating cog
        dispatcher.getBlockModelRenderer().renderModel(world, gear, state.with(BlockEngine.GEAR, true), pos, matrixStack, iRenderTypeBuffer.getBuffer(RenderType.getCutout()), false, new Random(), 0, 0, EmptyModelData.INSTANCE);

        matrixStack.pop();
    }

}