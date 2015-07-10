/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.BluePower;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.part.BPPart;
import com.bluepowermod.part.PartManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPartItem implements IItemRenderer {

    public static final RenderPartItem instance = new RenderPartItem();

    private RenderPartItem() {

    }

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

        boolean blend = GL11.glGetBoolean(GL11.GL_BLEND);
        boolean alpha = GL11.glGetBoolean(GL11.GL_ALPHA_TEST);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        GL11.glTranslatef(x, y, z);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
        try {
            BPPart part = PartManager.getExample(item);
            part.renderItem(type, item, data);
        } catch (Exception ex) {
            BluePower.log.error(ex.getMessage());
        }
        GL11.glPopMatrix();

        if (!blend)
            GL11.glDisable(GL11.GL_BLEND);
        if (!alpha)
            GL11.glDisable(GL11.GL_ALPHA_TEST);
    }
}
