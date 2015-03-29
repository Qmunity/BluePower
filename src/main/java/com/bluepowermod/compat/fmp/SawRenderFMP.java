/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.fmp;

import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import codechicken.lib.render.CCModel;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.uv.UVTranslation;
import codechicken.lib.vec.Rotation;
import codechicken.lib.vec.Scale;
import codechicken.lib.vec.SwapYZ;
import codechicken.lib.vec.Transformation;
import codechicken.lib.vec.TransformationList;
import codechicken.lib.vec.Translation;

import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;

public class SawRenderFMP implements IItemRenderer {

    Map<String, CCModel> models = CCModel.parseObjModels(new ResourceLocation("microblock", "models/saw.obj"), 7, new SwapYZ());
    CCModel handle = models.get("Handle");
    CCModel holder = models.get("BladeSupport");
    CCModel blade = models.get("Blade");

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType renderType) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType renderType, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType renderType, ItemStack item, Object... data) {

        double pi = Math.PI;

        Transformation t;
        switch (renderType) {
        case INVENTORY:
            t = new TransformationList(new Scale(1.8), new Translation(0, 0, -0.6), new Rotation(-pi / 4, 1, 0, 0), new Rotation(pi * 3 / 4, 0, 1, 0));
            break;
        case ENTITY:
            t = new TransformationList(new Scale(1), new Translation(0, 0, -0.25), new Rotation(-pi / 4, 1, 0, 0));
            break;
        case EQUIPPED_FIRST_PERSON:
            t = new TransformationList(new Scale(1.5), new Rotation(-pi / 3, 1, 0, 0), new Rotation(pi * 3 / 4, 0, 1, 0), new Translation(0.5, 0.5,
                    0.5));
            break;
        case EQUIPPED:
            t = new TransformationList(new Scale(1.5), new Rotation(-pi / 5, 1, 0, 0), new Rotation(-pi * 3 / 4, 0, 1, 0), new Translation(0.75, 0.5,
                    0.75));
            break;
        default:
            t = null;
            break;
        }

        CCRenderState.reset();
        CCRenderState.useNormals = true;
        CCRenderState.pullLightmap();
        CCRenderState.changeTexture("microblock:textures/items/saw.png");
        CCRenderState.startDrawing();
        handle.render(t);
        holder.render(t);
        CCRenderState.draw();
        GL11.glDisable(GL11.GL_CULL_FACE);
        CCRenderState.changeTexture(Refs.MODID + ":textures/items/fmpsaw.png");
        CCRenderState.startDrawing();
        int tex = 0;
        if (item.getItem() == BPItems.ruby_saw) {
            tex = 0;
        } else if (item.getItem() == BPItems.amethyst_saw) {
            tex = 1;
        } else if (item.getItem() == BPItems.sapphire_saw) {
            tex = 2;
        }
        blade.render(t, new UVTranslation(0, tex * 4 / 64D));
        CCRenderState.draw();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }
}
