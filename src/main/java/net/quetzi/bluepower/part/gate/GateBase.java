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

package net.quetzi.bluepower.part.gate;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.quetzi.bluepower.api.part.BPPartFace;
import net.quetzi.bluepower.api.vec.Vector3;

import org.lwjgl.opengl.GL11;

public class GateBase extends BPPartFace {
    
    @Override
    public String getType() {
    
        return "gatebase";
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return "gate.gatebase";
    }
    
    @Override
    public void renderDynamic(Vector3 loc, int pass, float frame) {
    
        GL11.glPushMatrix();
        {
            GL11.glTranslated(loc.getX(), loc.getY(), loc.getZ());
            
            GL11.glTranslated(0.5, 0.0, 0.5);
            double divider = 8;
            GL11.glRotated((System.currentTimeMillis() % (360 * divider)) / divider, 0, 1, 0);
            GL11.glTranslated(-0.5, 0, 0);
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColor4d(0, 1, 0, 1);
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setBrightness(100);
            t.addVertex(0, 0, 0);
            t.addVertex(1, 0, 0);
            t.addVertex(1, 1, 0);
            t.addVertex(0, 1, 0);
            t.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
        GL11.glPopMatrix();
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
    
        GL11.glPushMatrix();
        {
            if (type != ItemRenderType.ENTITY) {
                GL11.glTranslated(0.5, 0.0, 0.5);
                double divider = 8;
                GL11.glRotated((System.currentTimeMillis() % (360 * divider)) / divider, 0, 1, 0);
                GL11.glTranslated(-0.5, 0, 0);
            } else {
                GL11.glTranslated(0.0, 0.0, 0.5);
            }
            
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glColor4d(0, 1, 0, 1);
            Tessellator t = Tessellator.instance;
            t.startDrawingQuads();
            t.setBrightness(100);
            t.addVertex(0, 0, 0);
            t.addVertex(1, 0, 0);
            t.addVertex(1, 1, 0);
            t.addVertex(0, 1, 0);
            t.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
        }
        GL11.glPopMatrix();
    }
    
}
