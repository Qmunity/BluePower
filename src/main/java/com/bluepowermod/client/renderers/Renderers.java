/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tileentities.tier1.TileLamp;
import com.bluepowermod.tileentities.tier2.TileWindmill;
import com.bluepowermod.tileentities.tier3.TileEngine;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class Renderers {

    public static int RenderIdLamps;

    public static void init() {

        RenderingRegistry.registerBlockHandler(new RendererBlockBase());

        RenderingRegistry.registerBlockHandler(new RenderLamp());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEngine.class, new RenderEngine());
        // MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BPBlocks.engine), new RenderItemEngine());
        ClientRegistry.bindTileEntitySpecialRenderer(TileWindmill.class, new RenderWindmill());

        ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, new RenderLamp());

        RenderLamp rl = new RenderLamp();
        for (Block l : BPBlocks.blockLamp) {
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(l), rl);
        }
        for (Block l : BPBlocks.blockLampInverted) {
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(l), rl);
        }
    }
}
