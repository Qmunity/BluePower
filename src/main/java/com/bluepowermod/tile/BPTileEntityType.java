/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.tile;

import com.bluepowermod.reference.ContainerNames;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.*;
import com.bluepowermod.tile.tier2.*;
import com.bluepowermod.tile.tier3.*;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * @author MoreThanHidden
 */
public class BPTileEntityType {

    @ObjectHolder(ContainerNames.ALLOY_FURNACE)
    public static TileEntityType<TileAlloyFurnace> ALLOY_FURNACE;

    @ObjectHolder(ContainerNames.BUFFER)
    public static TileEntityType<TileBuffer> BUFFER;

    @ObjectHolder(ContainerNames.SORTING_MACHINE)
    public static TileEntityType<TileSortingMachine> SORTING_MACHINE;

    @ObjectHolder(ContainerNames.CPU)
    public static TileEntityType<TileCPU> CPU;

    @ObjectHolder(ContainerNames.MONITOR)
    public static TileEntityType<TileMonitor> MONITOR;

    @ObjectHolder(ContainerNames.DISK_DRIVE)
    public static TileEntityType<TileDiskDrive> DISK_DRIVE;

    @ObjectHolder(ContainerNames.IO_EXPANDER)
    public static TileEntityType<TileIOExpander> IO_EXPANDER;

    @ObjectHolder(ContainerNames.KINETIC_GENERATOR)
    public static TileEntityType<TileKinectGenerator> KINETIC_GENERATOR;

    @ObjectHolder(ContainerNames.DEPLOYER)
    public static TileEntityType<TileDeployer> DEPLOYER;

    @ObjectHolder(ContainerNames.RELAY)
    public static TileEntityType<TileRelay> RELAY;

    @ObjectHolder(ContainerNames.EJECTOR)
    public static TileEntityType<TileEjector> EJECTOR;

    @ObjectHolder(ContainerNames.FILTER)
    public static TileEntityType<TileFilter> FILTER;

    @ObjectHolder(ContainerNames.RETRIEVER)
    public static TileEntityType<TileRetriever> RETRIEVER;

    @ObjectHolder(ContainerNames.REGULATOR)
    public static TileEntityType<TileRegulator> REGULATOR;

    @ObjectHolder(ContainerNames.ITEM_DETECTOR)
    public static TileEntityType<TileItemDetector> ITEM_DETECTOR;

    @ObjectHolder(ContainerNames.PROJECT_TABLE)
    public static TileEntityType<TileProjectTable> PROJECT_TABLE;

    @ObjectHolder(ContainerNames.AUTO_PROJECT_TABLE)
    public static TileEntityType<TileAutoProjectTable> AUTO_PROJECT_TABLE;

    @ObjectHolder(ContainerNames.CIRCUIT_TABLE)
    public static TileEntityType<TileCircuitTable> CIRCUIT_TABLE;

    @ObjectHolder(ContainerNames.MANAGER)
    public static TileEntityType<TileManager> MANAGER;

    @ObjectHolder(ContainerNames.CIRCUITDATABASE_MAIN)
    public static TileEntityType<TileCircuitDatabase> CIRCUITDATABASE;

    @ObjectHolder(ContainerNames.WINDMILL)
    public static TileEntityType<TileCircuitDatabase> WINDMILL;

    @ObjectHolder(Refs.MODID + ":engine")
    public static TileEntityType<TileEngine> ENGINE;

    @ObjectHolder(Refs.MODID + ":tube")
    public static TileEntityType<TileEngine> TUBE;

    @ObjectHolder(Refs.MODID + ":sortron")
    public static TileEntityType<TileSortron> SORTRON;

    @ObjectHolder(Refs.MODID + ":block_breaker")
    public static TileEntityType<TileBlockBreaker> BLOCKBREAKER;

    @ObjectHolder(Refs.MODID + ":igniter")
    public static TileEntityType<TileIgniter> IGNITER;

    @ObjectHolder(Refs.MODID + ":lamp")
    public static TileEntityType<TileLamp> LAMP;

    @ObjectHolder(Refs.MODID + ":multipart")
    public static TileEntityType<TileBPMultipart> MULTIPART;

    @ObjectHolder(Refs.MODID + ":transposer")
    public static TileEntityType<TileTransposer> TRANSPOSER;

    @ObjectHolder(Refs.MODID + ":wire")
    public static TileEntityType<TileWire> WIRE;

    @ObjectHolder(Refs.MODID + ":insulatedwire")
    public static TileEntityType<TileInsulatedWire> INSULATEDWIRE;

    @ObjectHolder(Refs.MODID + ":battery")
    public static TileEntityType<TileBattery> BATTERY;

    @ObjectHolder(Refs.MODID + ":" + Refs.BLULECTRICCABLE_NAME)
    public static TileEntityType<TileBattery> BLULECTRIC_CABLE;

    @ObjectHolder(Refs.MODID + ":" + Refs.BLULECTRICFURNACE_NAME)
    public static TileEntityType<TileBattery> BLULECTRIC_FURNACE;

    @ObjectHolder(Refs.MODID + ":" + Refs.BLULECTRICALLOYFURNACE_NAME)
    public static TileEntityType<TileBattery> BLULECTRIC_ALLOY_FURNACE;

    @ObjectHolder(Refs.MODID + ":solar_panel")
    public static TileEntityType<TileSolarPanel> SOLAR_PANEL;

    @ObjectHolder(Refs.MODID + ":" + Refs.THERMOPILE_NAME)
    public static TileEntityType<TileThermopile> THERMOPILE;

    @ObjectHolder(Refs.MODID + ":microblock")
    public static TileEntityType<TileBPMicroblock> MICROBLOCK;


}
