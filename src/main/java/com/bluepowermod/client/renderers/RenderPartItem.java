package com.bluepowermod.client.renderers;

import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.part.PartManager;

import cpw.mods.fml.client.FMLClientHandler;

public class RenderPartItem implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
        case ENTITY: {
            render(-0.5F, 0.0F, -0.5F, 0.6F, type, item, data);
            return;
        }
        case EQUIPPED: {
            render(0F, 0F, 0F, 1.0F, type, item, data);
            return;
        }
        case EQUIPPED_FIRST_PERSON: {
            render(0F, 0F, 0F, 1.0F, type, item, data);
            return;
        }
        case INVENTORY: {
            render(0.0F, 0.0F, 0.0F, 1.0F, type, item, data);
            return;
        }
        default:
            return;
        }
    }

    private void render(float x, float y, float z, float scale, ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(x, y, z);
        FMLClientHandler.instance().getClient().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        PartManager.getExample(item).renderItem(type, item, data);
        GL11.glPopMatrix();
    }

}
