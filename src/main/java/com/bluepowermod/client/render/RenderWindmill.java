/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.render;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier2.TileWindmill;

public class RenderWindmill extends TileEntitySpecialRenderer {

	private ResourceLocation modelLocation = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_LOCATION + "windmill.obj");
	private ResourceLocation textureLocation = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_TEXTURE_LOCATION + "windmill.png");
	IModelCustom model;
	
	public RenderWindmill(){
		
		model = AdvancedModelLoader.loadModel(modelLocation);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float var8) {
		TileWindmill mill = (TileWindmill)tile.getWorldObj().getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord);
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glTranslated(x + .5, y, z + .5);
		GL11.glScaled(.15, .15, .15);
		this.bindTexture(textureLocation);
		
		GL11.glRotated(mill.turbineTick, 0, 1, 0);
		model.renderAll();
		GL11.glEnable(GL11.GL_LIGHTING);
		GL11.glPopMatrix();
	}

}
