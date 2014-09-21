/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.renderers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.part.PartRegistry;

public class RenderItemBPPart implements IItemRenderer {

    private final BPPart part;

    public RenderItemBPPart(String partId) {
        part = PartRegistry.getInstance().createPart(partId);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @SuppressWarnings("incomplete-switch")
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        GL11.glPushMatrix();
        {
            switch (type) {
            case ENTITY:
                GL11.glScaled(0.5, 0.5, 0.5);
                GL11.glTranslated(-0.5, 0, -0.5);
                if (item.getItemFrame() != null)
                    GL11.glTranslated(0, -0.25, 0);
                break;
            case EQUIPPED:
                break;
            case EQUIPPED_FIRST_PERSON:
                break;
            case INVENTORY:
                GL11.glTranslated(0, -0.1, 0);
                break;
            }
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            part.renderItem(type, item, data);
            GL11.glDisable(GL11.GL_BLEND);
        }
        GL11.glPopMatrix();
    }

}
