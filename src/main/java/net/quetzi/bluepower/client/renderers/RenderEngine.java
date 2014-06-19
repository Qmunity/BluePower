package net.quetzi.bluepower.client.renderers;

import org.lwjgl.opengl.GL11;


import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier1.TileEngine;
/**
 * 
 * @author TheFjong
 *
 */
public class RenderEngine extends TileEntitySpecialRenderer{

	private ResourceLocation modelLocation = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_LOCATION + "engine.obj");
	private ResourceLocation textureLocation = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_TEXTURE_LOCATION + "engine.png");
	IModelCustom engineModel;
	float rotateAmount = 0F;
	double glidingAmount = 0F;
	public RenderEngine(){
		
		engineModel = AdvancedModelLoader.loadModel(modelLocation);	
	}
	
	
	@Override
	public void renderTileEntityAt(TileEntity engine, double x, double y, double z, float dunnoWhatThisDo) {

		if(engine instanceof TileEngine){
			
			TileEngine tile = (TileEngine) engine.getWorldObj().getTileEntity(engine.xCoord, engine.yCoord, engine.zCoord);
			
			GL11.glPushMatrix();
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glTranslated(x + .5, y + .12, z + .5);
			GL11.glScaled(.0315, .0315, .0315);
			
			
			bindTexture(textureLocation);
			engineModel.renderAllExcept("gear", "glider");
			
			if(tile.isActive){
				rotateAmount += .01F;
				
				GL11.glRotated(360*rotateAmount, 0, 1, 0);
				engineModel.renderPart("gear");
				
				GL11.glRotated(-360*rotateAmount, 0, 1, 0);
				GL11.glTranslated(x + .5, y +glidingAmount + .5, z + .5);
				engineModel.renderPart("glider");
			}
			
			GL11.glPopMatrix();
			GL11.glEnable(GL11.GL_LIGHTING);
		}
	}
	
}
