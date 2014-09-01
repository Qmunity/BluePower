/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.client.renderers;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.tileentities.BPTileMultipart;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderMultipart extends TileEntitySpecialRenderer {

    public static int renderId;

    public static Map<Vector3, Integer> lists = new HashMap<Vector3, Integer>();

    public static void init() {

        RenderMultipart inst = new RenderMultipart();
        renderId = RenderingRegistry.getNextAvailableRenderId();
        ClientRegistry.bindTileEntitySpecialRenderer(BPTileMultipart.class, inst);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float frame) {

        if (tile == null || !(tile instanceof BPTileMultipart))
            return;
        BPTileMultipart te = (BPTileMultipart) tile;

        te.renderDynamic(new Vector3(x, y, z), MinecraftForgeClient.getRenderPass(), frame);

        int list = getRenderList(tile.xCoord, tile.yCoord, tile.zCoord);
        if (te.shouldReRender()) {
            for (int i = 0; i < 2; i++) {
                int l = list + i;
                GL11.glNewList(l, GL11.GL_COMPILE);
                GL11.glPushMatrix();
                {
                    Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);
                    Tessellator.instance.startDrawingQuads();
                    te.renderStatic(new Vector3(0, 0, 0), MinecraftForgeClient.getRenderPass());
                    Tessellator.instance.draw();
                }
                GL11.glPopMatrix();
                GL11.glEndList();
            }
        }

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);
            GL11.glCallList(list + MinecraftForgeClient.getRenderPass());
        }
        GL11.glPopMatrix();
    }

    private int getRenderList(int x, int y, int z) {

        for (Vector3 v : lists.keySet()) {
            if (v.getBlockX() == x && v.getBlockY() == y && v.getBlockZ() == z) {
                return lists.get(v).intValue();
            }
        }
        Vector3 v = new Vector3(x, y, z);
        int i = GL11.glGenLists(2);
        lists.put(v, i);
        return i;
    }

}
