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
import com.bluepowermod.tileentities.tier1.TileAlloyFurnace;
import com.bluepowermod.tileentities.tier1.TileBlockBreaker;
import com.bluepowermod.tileentities.tier1.TileBuffer;
import com.bluepowermod.tileentities.tier1.TileDeployer;
import com.bluepowermod.tileentities.tier1.TileEjector;
import com.bluepowermod.tileentities.tier1.TileFilter;
import com.bluepowermod.tileentities.tier1.TileIgniter;
import com.bluepowermod.tileentities.tier1.TileItemDetector;
import com.bluepowermod.tileentities.tier1.TileLamp;
import com.bluepowermod.tileentities.tier1.TileProjectTable;
import com.bluepowermod.tileentities.tier1.TileRelay;
import com.bluepowermod.tileentities.tier1.TileTransposer;
import com.bluepowermod.tileentities.tier2.TileRegulator;
import com.bluepowermod.tileentities.tier2.TileRetriever;
import com.bluepowermod.tileentities.tier2.TileSortingMachine;
import com.bluepowermod.tileentities.tier3.TileEngine;
import com.bluepowermod.tileentities.tier3.TileSortron;

import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {

    /**
     * Method to register the TE's to the game. If a TE is not registered, it _will_ cause issues!
     */
    public static void init() {

        GameRegistry.registerTileEntity(TileAlloyFurnace.class, "alloyFurnace");
        GameRegistry.registerTileEntity(TileBlockBreaker.class, "blockBreaker");
        GameRegistry.registerTileEntity(TileIgniter.class, "igniter");
        GameRegistry.registerTileEntity(TileBuffer.class, "buffer");
        GameRegistry.registerTileEntity(TileTransposer.class, "transposer");
        GameRegistry.registerTileEntity(TileSortingMachine.class, "sortingMachine");
        GameRegistry.registerTileEntity(TileSortron.class, "sortron");
        // GameRegistry.registerTileEntity(TileCPU.class, "CPU");
        // GameRegistry.registerTileEntity(TileMonitor.class, "monitor");
        // GameRegistry.registerTileEntity(TileDiskDrive.class, "diskDrive");
        // GameRegistry.registerTileEntity(TileIOExpander.class, "IOExpander");
        GameRegistry.registerTileEntity(TileEngine.class, "engine");
        // GameRegistry.registerTileEntity(TileWindmill.class, "windmill");
        // GameRegistry.registerTileEntity(TileKinectGenerator.class, "kinectgenerator");
        GameRegistry.registerTileEntity(TileDeployer.class, "deployer");
        GameRegistry.registerTileEntity(TileEjector.class, "ejector");
        GameRegistry.registerTileEntity(TileRelay.class, "relay");
        GameRegistry.registerTileEntity(TileFilter.class, "filter");
        GameRegistry.registerTileEntity(TileRetriever.class, "retriever");
        GameRegistry.registerTileEntity(TileRegulator.class, "regulator");
        GameRegistry.registerTileEntity(TileItemDetector.class, "itemDetector");
        GameRegistry.registerTileEntity(TileProjectTable.class, "projectTable");
        GameRegistry.registerTileEntity(TileLamp.class, "lamp");
        GameRegistry.registerTileEntity(BPTileMultipart.class, "multipart");
    }
}
