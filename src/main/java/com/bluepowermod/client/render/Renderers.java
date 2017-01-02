/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Renderers {

    public static int RenderIdLamps;

    public static void init() {

        // ClientRegistry.bindTileEntitySpecialRenderer(TileEngine.class, new RenderEngine());
        // MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BPBlocks.engine), new RenderItemEngine());
        // ClientRegistry.bindTileEntitySpecialRenderer(TileWindmill.class, new RenderWindmill());

        ModelLoader.setCustomModelResourceLocation(BPItems.amethyst_axe, 0, new ModelResourceLocation(Refs.MODID + ":axe", Refs.AMETHYSTAXE_NAME.split("_")[0]));
        ModelLoader.setCustomModelResourceLocation(BPItems.ruby_axe, 0, new ModelResourceLocation(Refs.MODID + ":axe", Refs.RUBYAXE_NAME.split("_")[0]));
        ModelLoader.setCustomModelResourceLocation(BPItems.sapphire_axe, 0, new ModelResourceLocation(Refs.MODID + ":axe", Refs.SAPPHIREAXE_NAME.split("_")[0]));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(BPBlocks.amethyst_ore), 0, new ModelResourceLocation(Refs.MODID + ":ore", Refs.AMETHYSTORE_NAME.split("_")[0]));

        ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, new RenderLamp());
    }
}
