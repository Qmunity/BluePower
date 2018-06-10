/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import com.bluepowermod.init.BPBlocks;
import com.sun.xml.internal.ws.api.pipe.Engine;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileEngine;

/**
 *
 * @author TheFjong
 *
 */
@SideOnly(Side.CLIENT)
public class RenderEngine extends TileEntitySpecialRenderer<TileEngine> {

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
        this.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        if (Minecraft.isAmbientOcclusionEnabled()) {
            GlStateManager.shadeModel(GL11.GL_SMOOTH);
        } else {
            GlStateManager.shadeModel(GL11.GL_FLAT);
        }

        World world = engine.getWorld();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.BLOCK);
        IBlockState state = BPBlocks.engine.getDefaultState();
        BlockRendererDispatcher dispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
        IBakedModel model = dispatcher.getModelForState(state);
        BlockPos pos = engine.getPos();

        GlStateManager.translate(x, y, z);

        TileEngine tile = (TileEngine) engine.getWorld().getTileEntity(engine.getPos());
        EnumFacing direction = tile.getOrientation();

        if (direction == EnumFacing.UP) {
            GL11.glTranslated(0,1,1);
            GL11.glRotatef(180, 1, 0, 0);
        }
        if (direction == EnumFacing.DOWN) {
            GL11.glRotatef(0, 0, 0, 0);
        }
        if (direction == EnumFacing.EAST) {
            GL11.glTranslated(1,0,0);
            GL11.glRotatef(90, 0, 0, 1);
        }
        if (direction == EnumFacing.WEST) {
            GL11.glTranslated(0,1,0);
            GL11.glRotatef(90, 0, 0, -1);
        }
        if (direction == EnumFacing.NORTH) {
            GL11.glTranslated(0,1,0);
            GL11.glRotatef(90, 1, 0, 0);
        }
        if (direction == EnumFacing.SOUTH) {
            GL11.glTranslated(0,0,1);
            GL11.glRotatef(90, -1, 0, 0);
        }

        tessellator.getBuffer().setTranslation(-pos.getX(), -pos.getY(), -pos.getZ());
        dispatcher.getBlockModelRenderer().renderModel(world, model, state, pos, bufferBuilder, false);

        tessellator.draw();

        RenderHelper.enableStandardItemLighting();
        tessellator.getBuffer().setTranslation(0, 0, 0);

        GlStateManager.popMatrix();


        GlStateManager.popMatrix();
        GlStateManager.popAttrib();
    }



}