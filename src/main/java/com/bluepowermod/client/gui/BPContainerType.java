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
import com.bluepowermod.reference.Refs;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
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


    @Mod.EventBusSubscriber(modid = Refs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration{
        @SubscribeEvent
        public static void onContainerTypeRegistry(final RegistryEvent.Register<ContainerType<?>> e){
            e.getRegistry().registerAll(
                    new ContainerType<>(ContainerAlloyFurnace::new).setRegistryName(ContainerNames.ALLOY_FURNACE),
                    new ContainerType<>(ContainerBuffer::new).setRegistryName(ContainerNames.BUFFER),
                    new ContainerType<>(ContainerSortingMachine::new).setRegistryName(ContainerNames.SORTING_MACHINE),
                    new ContainerType<>(ContainerSeedBag::new).setRegistryName(ContainerNames.SEEDBAG),
                    new ContainerType<>(ContainerCanvasBag::new).setRegistryName(ContainerNames.CANVAS_BAG),
                    new ContainerType<>(ContainerCPU::new).setRegistryName(ContainerNames.CPU),
                    new ContainerType<>(ContainerMonitor::new).setRegistryName(ContainerNames.MONITOR),
                    new ContainerType<>(ContainerDiskDrive::new).setRegistryName(ContainerNames.DISK_DRIVE),
                    new ContainerType<>(ContainerIOExpander::new).setRegistryName(ContainerNames.IO_EXPANDER),
                    new ContainerType<>(ContainerRedbusID::new).setRegistryName(ContainerNames.REDBUS),
                    new ContainerType<>(ContainerKinect::new).setRegistryName(ContainerNames.KINETIC_GENERATOR),
                    new ContainerType<>(ContainerDeployer::new).setRegistryName(ContainerNames.DEPLOYER),
                    new ContainerType<>(ContainerRelay::new).setRegistryName(ContainerNames.RELAY),
                    new ContainerType<>(ContainerEjector::new).setRegistryName(ContainerNames.EJECTOR),
                    new ContainerType<>(ContainerFilter::new).setRegistryName(ContainerNames.FILTER),
                    new ContainerType<>(ContainerRetriever::new).setRegistryName(ContainerNames.RETRIEVER),
                    new ContainerType<>(ContainerRegulator::new).setRegistryName(ContainerNames.REGULATOR),
                    new ContainerType<>(ContainerItemDetector::new).setRegistryName(ContainerNames.ITEM_DETECTOR),
                    new ContainerType<>(ContainerProjectTable::new).setRegistryName(ContainerNames.PROJECT_TABLE),
                    new ContainerType<>(ContainerCircuitTable::new).setRegistryName(ContainerNames.CIRCUIT_TABLE),
                    new ContainerType<>(ContainerManager::new).setRegistryName(ContainerNames.MANAGER),
                    new ContainerType<>(ContainerCircuitDatabaseMain::new).setRegistryName(ContainerNames.CIRCUITDATABASE_MAIN),
                    new ContainerType<>(ContainerCircuitDatabaseSharing::new).setRegistryName(ContainerNames.CIRCUITDATABASE_SHARING)
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreenFactories(){
        ScreenManager.registerFactory(ALLOY_FURNACE, GuiAlloyFurnace::new);
        ScreenManager.registerFactory(BUFFER, GuiBuffer::new);
        ScreenManager.registerFactory(SORTING_MACHINE, GuiSortingMachine::new);
        ScreenManager.registerFactory(SEEDBAG, GuiSeedBag::new);
        ScreenManager.registerFactory(CANVAS_BAG, GuiCanvasBag::new);
        ScreenManager.registerFactory(CPU, GuiCPU::new);
        ScreenManager.registerFactory(MONITOR, GuiMonitor::new);
        ScreenManager.registerFactory(DISK_DRIVE, GuiDiskDrive::new);
        ScreenManager.registerFactory(IO_EXPANDER, GuiIOExpander::new);
        ScreenManager.registerFactory(REDBUS, GuiRedbusID::new);
        ScreenManager.registerFactory(KINETIC_GENERATOR, GuiKinect::new);
        ScreenManager.registerFactory(DEPLOYER, GuiDeployer::new);
        ScreenManager.registerFactory(RELAY, GuiRelay::new);
        ScreenManager.registerFactory(EJECTOR, GuiEjector::new);
        ScreenManager.registerFactory(FILTER, GuiFilter::new);
        ScreenManager.registerFactory(RETRIEVER, GuiRetriever::new);
        ScreenManager.registerFactory(REGULATOR, GuiRegulator::new);
        ScreenManager.registerFactory(ITEM_DETECTOR, GuiItemDetector::new);
        ScreenManager.registerFactory(PROJECT_TABLE, GuiProjectTable::new);
        ScreenManager.registerFactory(CIRCUIT_TABLE, GuiCircuitTable::new);
        ScreenManager.registerFactory(MANAGER, GuiManager::new);
        ScreenManager.registerFactory(CIRCUITDATABASE_MAIN, GuiCircuitDatabaseMain::new);
        ScreenManager.registerFactory(CIRCUITDATABASE_SHARING, GuiCircuitDatabaseSharing::new);
    }

}
