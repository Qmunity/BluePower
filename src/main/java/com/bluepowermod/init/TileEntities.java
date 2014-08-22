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

import com.bluepowermod.tileentities.BPTileMultipart;
import com.bluepowermod.tileentities.tier1.*;
import com.bluepowermod.tileentities.tier2.*;
import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;
import com.bluepowermod.tileentities.tier3.TileEngine;
import com.bluepowermod.tileentities.tier3.TileManager;
import com.bluepowermod.tileentities.tier3.TileSortron;
import com.bluepowermod.util.Refs;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {
    
    /**
     * Method to register the TE's to the game. If a TE is not registered, it _will_ cause issues!
     */
    public static void init() {
    
        GameRegistry.registerTileEntity(TileAlloyFurnace.class, Refs.MODID + ".alloyFurnace");
        GameRegistry.registerTileEntity(TileBlockBreaker.class, Refs.MODID + ".blockBreaker");
        GameRegistry.registerTileEntity(TileIgniter.class, Refs.MODID + ".igniter");
        GameRegistry.registerTileEntity(TileBuffer.class, Refs.MODID + ".buffer");
        GameRegistry.registerTileEntity(TileTransposer.class, Refs.MODID + ".transposer");
        GameRegistry.registerTileEntity(TileSortingMachine.class, Refs.MODID + ".sortingMachine");
        GameRegistry.registerTileEntity(TileSortron.class, Refs.MODID + ".sortron");
        // GameRegistry.registerTileEntity(TileCPU.class, "CPU");
        // GameRegistry.registerTileEntity(TileMonitor.class, "monitor");
        // GameRegistry.registerTileEntity(TileDiskDrive.class, "diskDrive");
        // GameRegistry.registerTileEntity(TileIOExpander.class, "IOExpander");
        GameRegistry.registerTileEntity(TileEngine.class, Refs.MODID + ".engine");
        // GameRegistry.registerTileEntity(TileWindmill.class, "windmill");
        // GameRegistry.registerTileEntity(TileKinectGenerator.class, "kinectgenerator");
        GameRegistry.registerTileEntity(TileDeployer.class, Refs.MODID + ".deployer");
        GameRegistry.registerTileEntity(TileEjector.class, Refs.MODID + ".ejector");
        GameRegistry.registerTileEntity(TileRelay.class, Refs.MODID + ".relay");
        GameRegistry.registerTileEntity(TileFilter.class, Refs.MODID + ".filter");
        GameRegistry.registerTileEntity(TileRetriever.class, Refs.MODID + ".retriever");
        GameRegistry.registerTileEntity(TileRegulator.class, Refs.MODID + ".regulator");
        GameRegistry.registerTileEntity(TileItemDetector.class, Refs.MODID + ".itemDetector");
        GameRegistry.registerTileEntity(TileManager.class, Refs.MODID + ".manager");
        GameRegistry.registerTileEntity(TileProjectTable.class, Refs.MODID + ".projectTable");
        GameRegistry.registerTileEntity(TileCircuitTable.class, Refs.MODID + ".circuitTable");
        GameRegistry.registerTileEntity(TileCircuitDatabase.class, Refs.MODID + ".circuitDatabase");
        GameRegistry.registerTileEntity(TileLamp.class, Refs.MODID + ".lamp");
        GameRegistry.registerTileEntity(BPTileMultipart.class, Refs.MODID + ".multipart");

        GameRegistry.registerTileEntity(TileSolarPanel.class, Refs.MODID + ".solarPanel");
    }
}
