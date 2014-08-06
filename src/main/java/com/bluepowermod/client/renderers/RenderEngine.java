package com.bluepowermod.client.renderers;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.tileentities.tier3.TileEngine;
import com.bluepowermod.util.Refs;

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
            
            ForgeDirection direction = tile.getOrientation();
            
            GL11.glTranslated(x, y, z);
            GL11.glScaled(.0315, .0315, .0315);
            
            if (direction == ForgeDirection.UP) {
                GL11.glTranslated(16, 28, 16);
                GL11.glRotatef(180, 1, 0, 0);
            }
            if (direction == ForgeDirection.DOWN) {
                GL11.glTranslated(16, 4, 16);
                GL11.glRotatef(0, 0, 0, 0);
            }
            if (direction == ForgeDirection.EAST) {
                GL11.glTranslated(28, 16, 16);
                GL11.glRotatef(90, 0, 0, 1);
            }
            if (direction == ForgeDirection.WEST) {
                GL11.glTranslated(4, 16, 16);
                GL11.glRotatef(90, 0, 0, -1);
            }
            if (direction == ForgeDirection.NORTH) {
                GL11.glTranslated(16, 16, 4);
                GL11.glRotatef(90, 1, 0, 0);
            }
            if (direction == ForgeDirection.SOUTH) {
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
