/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.tile.tier1.TileLamp;
import com.bluepowermod.tile.tier2.TileChargingBench;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Renderers {

    public static int RenderIdLamps;

    public static void init() {

        RenderingRegistry.registerBlockHandler(new RendererBlockBase());

        RenderSolarPanel sp = new RenderSolarPanel();
        RenderingRegistry.registerBlockHandler(sp);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BPBlocks.solar_panel), sp);

        // ClientRegistry.bindTileEntitySpecialRenderer(TileEngine.class, new RenderEngine());
        // MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BPBlocks.engine), new RenderItemEngine());
        // ClientRegistry.bindTileEntitySpecialRenderer(TileWindmill.class, new RenderWindmill());

        RenderLamp rl = new RenderLamp();
        RenderingRegistry.registerBlockHandler(rl);
        ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, rl);
        for (Block l : BPBlocks.blockLamp)
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(l), rl);
        for (Block l : BPBlocks.blockLampInverted)
            MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(l), rl);

        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BPBlocks.blockLampRGB), rl);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BPBlocks.blockLampRGBInverted), rl);

        RenderCircuitTile rct = new RenderCircuitTile();
        MinecraftForgeClient.registerItemRenderer(BPItems.stone_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.bluestone_anode_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.bluestone_cathode_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.bluestone_pointer_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.bluestone_wire_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.redstone_anode_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.redstone_cathode_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.redstone_pointer_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.redstone_wire_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.silicon_chip_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.tainted_silicon_chip_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.quartz_resonator_tile, rct);
        MinecraftForgeClient.registerItemRenderer(BPItems.stone_bundle, rct);

        RenderChargingBench chargingBench = new RenderChargingBench();
        RenderingRegistry.registerBlockHandler(chargingBench);
        ClientRegistry.bindTileEntitySpecialRenderer(TileChargingBench.class, chargingBench);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(BPBlocks.chargingBench), chargingBench);
    }
}
