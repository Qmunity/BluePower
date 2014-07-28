package com.bluepowermod.client.renderers;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;
import com.bluepowermod.references.Refs;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
/**
 * 
 * @author TheFjonG
 *
 */
@SideOnly(Side.CLIENT)
public class RenderItemEngine implements IItemRenderer{

	private ResourceLocation modelLocation = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_LOCATION + "engine.obj");
	private ResourceLocation textureLocation = new ResourceLocation(Refs.MODID + ":" + Refs.MODEL_TEXTURE_LOCATION + "engineoff.png");
	
	private IModelCustom model;
	
	public RenderItemEngine(){
		
		model = AdvancedModelLoader.loadModel(modelLocation);
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item,ItemRendererHelper helper) {
		
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		switch (type){
            case ENTITY:
            {
                renderEngine(0.0F, 0F, 0.0F, 0, 0, 0, 0);
                return;
            }
            case EQUIPPED:
            {                            
                renderEngine( 8F, 8F, 8F, 10, 1, 0, 0-1F);
                return;
            }
            case EQUIPPED_FIRST_PERSON:
            {
                renderEngine(14F, 2F, 16F, 4, -.8F, 0, -.8F);
                return;
            }
            case INVENTORY:
            {
                renderEngine(0.0F, -10F, 0.0F, 0, 0, 0, 0);
                return;
            }
            default:
        }
		
	}

	private void renderEngine(float x, float y, float z, float rotateAmount, float rotatex, float rotatey, float rotatez) {
		
		GL11.glPushMatrix();
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glScalef(.034F, .034F, .034F);
        GL11.glTranslated(x ,y, z);
        GL11.glRotatef(rotateAmount, rotatex, rotatey, rotatez);
        FMLClientHandler.instance().getClient().renderEngine.bindTexture(textureLocation);
        
        model.renderAll();
        
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
