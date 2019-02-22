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

package com.bluepowermod.init;

import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.*;
import com.bluepowermod.tile.tier2.*;
import com.bluepowermod.tile.tier3.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileEntities {

    /**
     * Method to register the TE's to the game. If a TE is not registered, it _will_ cause issues!
     */
    public static void init() {

        GameRegistry.registerTileEntity(TileAlloyFurnace.class, new ResourceLocation(Refs.MODID, "alloyFurnace"));
        GameRegistry.registerTileEntity(TileBlockBreaker.class, new ResourceLocation(Refs.MODID, "blockBreaker"));
        GameRegistry.registerTileEntity(TileIgniter.class, new ResourceLocation(Refs.MODID, "igniter"));
        GameRegistry.registerTileEntity(TileBuffer.class, new ResourceLocation(Refs.MODID, "buffer"));
        GameRegistry.registerTileEntity(TileTransposer.class, new ResourceLocation(Refs.MODID, "transposer"));
        GameRegistry.registerTileEntity(TileSortingMachine.class, new ResourceLocation(Refs.MODID, "sortingMachine"));
        GameRegistry.registerTileEntity(TileSortron.class, new ResourceLocation(Refs.MODID, "sortron"));
        // GameRegistry.registerTileEntity(TileCPU.class, "CPU");
        // GameRegistry.registerTileEntity(TileMonitor.class, "monitor");
        // GameRegistry.registerTileEntity(TileDiskDrive.class, "diskDrive");
        // GameRegistry.registerTileEntity(TileIOExpander.class, "IOExpander");
        GameRegistry.registerTileEntity(TileEngine.class, new ResourceLocation(Refs.MODID, "engine"));
        GameRegistry.registerTileEntity(TileBattery.class, new ResourceLocation(Refs.MODID, "battery"));
        GameRegistry.registerTileEntity(TileWindmill.class, new ResourceLocation(Refs.MODID, "windmill"));
        GameRegistry.registerTileEntity(TileKinectGenerator.class, new ResourceLocation(Refs.MODID, "kinectgenerator"));
        GameRegistry.registerTileEntity(TileSolarPanel.class, new ResourceLocation(Refs.MODID, "solarpanel"));


        GameRegistry.registerTileEntity(TileDeployer.class, new ResourceLocation(Refs.MODID, "deployer"));
        GameRegistry.registerTileEntity(TileEjector.class, new ResourceLocation(Refs.MODID, "ejector"));
        GameRegistry.registerTileEntity(TileRelay.class, new ResourceLocation(Refs.MODID, "relay"));
        GameRegistry.registerTileEntity(TileFilter.class, new ResourceLocation(Refs.MODID, "filter"));
        GameRegistry.registerTileEntity(TileRetriever.class, new ResourceLocation(Refs.MODID, "retriever"));
        GameRegistry.registerTileEntity(TileRegulator.class, new ResourceLocation(Refs.MODID, "regulator"));
        GameRegistry.registerTileEntity(TileItemDetector.class, new ResourceLocation(Refs.MODID, "itemDetector"));
        GameRegistry.registerTileEntity(TileManager.class, new ResourceLocation(Refs.MODID, "manager"));
        GameRegistry.registerTileEntity(TileProjectTable.class, new ResourceLocation(Refs.MODID, "projectTable"));
        GameRegistry.registerTileEntity(TileAutoProjectTable.class, new ResourceLocation(Refs.MODID, "autoProjectTable"));
        GameRegistry.registerTileEntity(TileCircuitTable.class, new ResourceLocation(Refs.MODID, "circuitTable"));
        GameRegistry.registerTileEntity(TileCircuitDatabase.class, new ResourceLocation(Refs.MODID, "circuitDatabase"));
        GameRegistry.registerTileEntity(TileLamp.class, new ResourceLocation(Refs.MODID, "lamp"));
        GameRegistry.registerTileEntity(TileWire.class, new ResourceLocation(Refs.MODID, "wire"));

    }
}
