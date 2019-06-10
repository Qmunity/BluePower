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
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import com.bluepowermod.tile.tier3.TileEngine;

/**
 *
 * @author TheFjong, MoreThanHidden
 * TODO: Create Rotation Helper and re-implement as Fast TESR
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderEngine extends TileEntityRenderer<TileEngine> {

    float rotateAmount  = 0F;

    @Override
    public void render(TileEngine engine, double x, double y, double z, float f, int destroyStage, float alpha) {
        GlStateManager.pushAttrib();
        GlStateManager.pushMatrix();
        // Translate to the location of our tile entity
        GlStateManager.disableRescaleNormal();

        // Render the rotating cog
        GlStateManager.pushMatrix();
        RenderHelper.disableStandardItemLighting();

        this.bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = engine.getWorld();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        BlockState state = BPBlocks.engine.getDefaultState();
        BlockRendererDispatcher dispatcher = Minecraft.getInstance().getBlockRendererDispatcher();
        BlockPos pos = engine.getPos();

        GlStateManager.translate(x, y, z);

        TileEngine tile = (TileEngine) engine.getWorld().getTileEntity(engine.getPos());
        Direction direction = tile.getOrientation();

        if (direction == Direction.UP) {
            GL11.glTranslated(0,1,1);
            GL11.glRotatef(180, 1, 0, 0);
        }
        if (direction == Direction.DOWN) {
            GL11.glRotatef(0, 0, 0, 0);
        }
        if (direction == Direction.EAST) {
            GL11.glTranslated(1,0,0);
            GL11.glRotatef(90, 0, 0, 1);
        }
        if (direction == Direction.WEST) {
            GL11.glTranslated(0,1,0);
            GL11.glRotatef(90, 0, 0, -1);
        }
        if (direction == Direction.NORTH) {
            GL11.glTranslated(0,1,0);
            GL11.glRotatef(90, 1, 0, 0);
        }
        if (direction == Direction.SOUTH) {
            GL11.glTranslated(0,0,1);
            GL11.glRotatef(90, -1, 0, 0);
        }

        tessellator.getBuffer().setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());

        //Render Glider
        GlStateManager.pushMatrix();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        float f2 = 0;
        if(tile.isActive) {
            f += tile.pumpTick;
            if (tile.pumpSpeed > 0) {
                f /= tile.pumpSpeed;
            }
            f2 = ((float) (.5 - .5 * Math.cos(3.1415926535897931D * (double) f)) / 4);
        }
        GL11.glTranslatef(0, f2, 0);
        IBakedModel glider = dispatcher.getModelForState(state.withProperty(BlockEngine.GLIDER, true));
        dispatcher.getBlockModelRenderer().renderModel(world, glider, state, pos, bufferBuilder, false);

        tessellator.draw();
        GlStateManager.popMatrix();

        //Render Gear
        GlStateManager.pushMatrix();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);

        GlStateManager.translate(0.5, 0, 0.5);
        long angle = tile.isActive ? (System.currentTimeMillis() / 10) % 360 : 0;
        GlStateManager.rotate(angle, 0, 1, 0);
        GlStateManager.translate(-0.5, 0, -0.5);
        IBakedModel gear = dispatcher.getModelForState(state.withProperty(BlockEngine.GEAR, true));
        dispatcher.getBlockModelRenderer().renderModel(world, gear, state, pos, bufferBuilder, false);

        tessellator.draw();
        GlStateManager.popMatrix();


        RenderHelper.enableStandardItemLighting();
        tessellator.getBuffer().setTranslation(0, 0, 0);

        GlStateManager.popMatrix();

        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }



}