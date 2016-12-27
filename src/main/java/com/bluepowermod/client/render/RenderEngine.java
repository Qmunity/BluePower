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
import net.minecraft.util.EnumFacing;;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileEngine;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author TheFjong
 * 
 */
@SideOnly(Side.CLIENT)
public class RenderEngine extends TileEntitySpecialRenderer {
    
    private ResourceLocation modelLocation      = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_LOCATION + "engine.obj");
    private ResourceLocation textureLocationOff = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_TEXTURE_LOCATION + "engineoff.png");
    private ResourceLocation textureLocationOn  = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_TEXTURE_LOCATION + "engineon.png");
    
    IModelCustom             engineModel;
    float                    rotateAmount       = 0F;
    
    public RenderEngine() {
    
        engineModel = AdvancedModelLoader.loadModel(modelLocation);
    }
    
    @SuppressWarnings("cast")
    @Override
    public void renderTileEntityAt(TileEntity engine, double x, double y, double z, float f) {
    
        if (engine instanceof TileEngine) {
            
            GL11.glPushMatrix();
            GL11.glDisable(GL11.GL_LIGHTING);
            TileEngine tile = (TileEngine) engine.getWorldObj().getTileEntity(engine.xCoord, engine.yCoord, engine.zCoord);
            
            EnumFacing direction = tile.getOrientation();
            
            GL11.glTranslated(x, y, z);
            GL11.glScaled(.0315, .0315, .0315);
            
            if (direction == EnumFacing.UP) {
                GL11.glTranslated(16, 28, 16);
                GL11.glRotatef(180, 1, 0, 0);
            }
            if (direction == EnumFacing.DOWN) {
                GL11.glTranslated(16, 4, 16);
                GL11.glRotatef(0, 0, 0, 0);
            }
            if (direction == EnumFacing.EAST) {
                GL11.glTranslated(28, 16, 16);
                GL11.glRotatef(90, 0, 0, 1);
            }
            if (direction == EnumFacing.WEST) {
                GL11.glTranslated(4, 16, 16);
                GL11.glRotatef(90, 0, 0, -1);
            }
            if (direction == EnumFacing.NORTH) {
                GL11.glTranslated(16, 16, 4);
                GL11.glRotatef(90, 1, 0, 0);
            }
            if (direction == EnumFacing.SOUTH) {
                GL11.glTranslated(16, 16, 28);
                GL11.glRotatef(90, -1, 0, 0);
            }
            if (tile.isActive) {
                bindTexture(textureLocationOn);
            } else {
                bindTexture(textureLocationOff);
            }
            engineModel.renderAllExcept("gear", "glider");
            
            if (tile.isActive) {
                f += tile.pumpTick;
                if (tile.pumpSpeed > 0) {
                    f /= tile.pumpSpeed;
                }
            } else {
                f = 0;
            }
            f = (float) ((float) 6 * (.5 - .5 * Math.cos(3.1415926535897931D * (double) f)));
            GL11.glTranslatef(0, f, 0);
            
            engineModel.renderPart("glider");
            
            GL11.glTranslatef(0, -f, 0);
            
            if (tile.isActive) {
                if (tile.getWorldObj().isRemote) {
                    rotateAmount++;
                    
                    GL11.glRotated(tile.gearTick * 19, 0, 1.5707963267948966D * (double) f, 0);
                }
            }
            engineModel.renderPart("gear");
            
            GL11.glEnable(GL11.GL_LIGHTING);
            
            GL11.glPopMatrix();
        }
    }
    
}
