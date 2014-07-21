package net.quetzi.bluepower.client.renderers;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import net.quetzi.bluepower.references.Refs;
import net.quetzi.bluepower.tileentities.tier2.TileWindmill;

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
