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

package com.bluepowermod.client.gui;

import com.bluepowermod.container.*;
import com.bluepowermod.reference.ContainerNames;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ObjectHolder;

public class BPContainerType {

    @ObjectHolder(ContainerNames.ALLOY_FURNACE)
    public static ContainerType<ContainerAlloyFurnace> ALLOY_FURNACE;

    @ObjectHolder(ContainerNames.BUFFER)
    public static ContainerType<ContainerBuffer> BUFFER;

    @ObjectHolder(ContainerNames.SORTING_MACHINE)
    public static ContainerType<ContainerSortingMachine> SORTING_MACHINE;

    @ObjectHolder(ContainerNames.SEEDBAG)
    public static ContainerType<ContainerSeedBag> SEEDBAG;

    @ObjectHolder(ContainerNames.CANVAS_BAG)
    public static ContainerType<ContainerCanvasBag> CANVAS_BAG;

    @ObjectHolder(ContainerNames.CPU)
    public static ContainerType<ContainerCPU> CPU;

    @ObjectHolder(ContainerNames.MONITOR)
    public static ContainerType<ContainerMonitor> MONITOR;

    @ObjectHolder(ContainerNames.DISK_DRIVE)
    public static ContainerType<ContainerDiskDrive> DISK_DRIVE;

    @ObjectHolder(ContainerNames.IO_EXPANDER)
    public static ContainerType<ContainerIOExpander> IO_EXPANDER;

    @ObjectHolder(ContainerNames.REDBUS)
    public static ContainerType<ContainerRedbusID> REDBUS;

    @ObjectHolder(ContainerNames.KINETIC_GENERATOR)
    public static ContainerType<ContainerKinect> KINETIC_GENERATOR;

    @ObjectHolder(ContainerNames.DEPLOYER)
    public static ContainerType<ContainerDeployer> DEPLOYER;

    @ObjectHolder(ContainerNames.RELAY)
    public static ContainerType<ContainerRelay> RELAY;

    @ObjectHolder(ContainerNames.EJECTOR)
    public static ContainerType<ContainerEjector> EJECTOR;

    @ObjectHolder(ContainerNames.FILTER)
    public static ContainerType<ContainerFilter> FILTER;

    @ObjectHolder(ContainerNames.RETRIEVER)
    public static ContainerType<ContainerRetriever> RETRIEVER;

    @ObjectHolder(ContainerNames.REGULATOR)
    public static ContainerType<ContainerRegulator> REGULATOR;

    @ObjectHolder(ContainerNames.ITEM_DETECTOR)
    public static ContainerType<ContainerItemDetector> ITEM_DETECTOR;

    @ObjectHolder(ContainerNames.PROJECT_TABLE)
    public static ContainerType<ContainerProjectTable> PROJECT_TABLE;

    @ObjectHolder(ContainerNames.CIRCUIT_TABLE)
    public static ContainerType<ContainerCircuitTable> CIRCUIT_TABLE;

    @ObjectHolder(ContainerNames.MANAGER)
    public static ContainerType<ContainerManager> MANAGER;

    @ObjectHolder(ContainerNames.CIRCUITDATABASE_MAIN)
    public static ContainerType<ContainerCircuitDatabaseMain> CIRCUITDATABASE_MAIN;

    @ObjectHolder(ContainerNames.CIRCUITDATABASE_SHARING)
    public static ContainerType<ContainerCircuitDatabaseSharing> CIRCUITDATABASE_SHARING;


    @OnlyIn(Dist.CLIENT)
    public static void registerScreenFactories(){
        ScreenManager.registerFactory(ALLOY_FURNACE, GuiAlloyFurnace::new);
    }

}
