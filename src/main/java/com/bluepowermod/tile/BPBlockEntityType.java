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
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ObjectHolder;

/**
 * @author MoreThanHidden
 */
public class BPBlockEntityType {

    @ObjectHolder(ContainerNames.ALLOY_FURNACE)
    public static BlockEntityType<TileAlloyFurnace> ALLOY_FURNACE;

    @ObjectHolder(ContainerNames.BUFFER)
    public static BlockEntityType<TileBuffer> BUFFER;

    @ObjectHolder(ContainerNames.SORTING_MACHINE)
    public static BlockEntityType<TileSortingMachine> SORTING_MACHINE;

    @ObjectHolder(ContainerNames.CPU)
    public static BlockEntityType<TileCPU> CPU;

    @ObjectHolder(ContainerNames.MONITOR)
    public static BlockEntityType<TileMonitor> MONITOR;

    @ObjectHolder(ContainerNames.DISK_DRIVE)
    public static BlockEntityType<TileDiskDrive> DISK_DRIVE;

    @ObjectHolder(ContainerNames.IO_EXPANDER)
    public static BlockEntityType<TileIOExpander> IO_EXPANDER;

    @ObjectHolder(ContainerNames.KINETIC_GENERATOR)
    public static BlockEntityType<TileKinectGenerator> KINETIC_GENERATOR;

    @ObjectHolder(ContainerNames.DEPLOYER)
    public static BlockEntityType<TileDeployer> DEPLOYER;

    @ObjectHolder(ContainerNames.RELAY)
    public static BlockEntityType<TileRelay> RELAY;

    @ObjectHolder(ContainerNames.EJECTOR)
    public static BlockEntityType<TileEjector> EJECTOR;

    @ObjectHolder(ContainerNames.FILTER)
    public static BlockEntityType<TileFilter> FILTER;

    @ObjectHolder(ContainerNames.RETRIEVER)
    public static BlockEntityType<TileRetriever> RETRIEVER;

    @ObjectHolder(ContainerNames.REGULATOR)
    public static BlockEntityType<TileRegulator> REGULATOR;

    @ObjectHolder(ContainerNames.ITEM_DETECTOR)
    public static BlockEntityType<TileItemDetector> ITEM_DETECTOR;

    @ObjectHolder(ContainerNames.PROJECT_TABLE)
    public static BlockEntityType<TileProjectTable> PROJECT_TABLE;

    @ObjectHolder(ContainerNames.AUTO_PROJECT_TABLE)
    public static BlockEntityType<TileAutoProjectTable> AUTO_PROJECT_TABLE;

    @ObjectHolder(ContainerNames.CIRCUIT_TABLE)
    public static BlockEntityType<TileCircuitTable> CIRCUIT_TABLE;

    @ObjectHolder(ContainerNames.MANAGER)
    public static BlockEntityType<TileManager> MANAGER;

    @ObjectHolder(ContainerNames.CIRCUITDATABASE_MAIN)
    public static BlockEntityType<TileCircuitDatabase> CIRCUITDATABASE;

    @ObjectHolder(ContainerNames.WINDMILL)
    public static BlockEntityType<TileCircuitDatabase> WINDMILL;

    @ObjectHolder(Refs.MODID + ":engine")
    public static BlockEntityType<TileEngine> ENGINE;

    @ObjectHolder(Refs.MODID + ":tube")
    public static BlockEntityType<TileEngine> TUBE;

    @ObjectHolder(Refs.MODID + ":sortron")
    public static BlockEntityType<TileSortron> SORTRON;

    @ObjectHolder(Refs.MODID + ":block_breaker")
    public static BlockEntityType<TileBlockBreaker> BLOCKBREAKER;

    @ObjectHolder(Refs.MODID + ":igniter")
    public static BlockEntityType<TileIgniter> IGNITER;

    @ObjectHolder(Refs.MODID + ":lamp")
    public static BlockEntityType<TileLamp> LAMP;

    @ObjectHolder(Refs.MODID + ":multipart")
    public static BlockEntityType<TileBPMultipart> MULTIPART;

    @ObjectHolder(Refs.MODID + ":transposer")
    public static BlockEntityType<TileTransposer> TRANSPOSER;

    @ObjectHolder(Refs.MODID + ":wire")
    public static BlockEntityType<TileWire> WIRE;

    @ObjectHolder(Refs.MODID + ":insulatedwire")
    public static BlockEntityType<TileInsulatedWire> INSULATEDWIRE;

    @ObjectHolder(Refs.MODID + ":battery")
    public static BlockEntityType<TileBattery> BATTERY;

    @ObjectHolder(Refs.MODID + ":" + Refs.BLULECTRICCABLE_NAME)
    public static BlockEntityType<TileBattery> BLULECTRIC_CABLE;

    @ObjectHolder(Refs.MODID + ":" + Refs.BLULECTRICFURNACE_NAME)
    public static BlockEntityType<TileBattery> BLULECTRIC_FURNACE;

    @ObjectHolder(Refs.MODID + ":" + Refs.BLULECTRICALLOYFURNACE_NAME)
    public static BlockEntityType<TileBattery> BLULECTRIC_ALLOY_FURNACE;

    @ObjectHolder(Refs.MODID + ":solar_panel")
    public static BlockEntityType<TileSolarPanel> SOLAR_PANEL;

    @ObjectHolder(Refs.MODID + ":" + Refs.THERMOPILE_NAME)
    public static BlockEntityType<TileThermopile> THERMOPILE;

    @ObjectHolder(Refs.MODID + ":microblock")
    public static BlockEntityType<TileBPMicroblock> MICROBLOCK;


}
