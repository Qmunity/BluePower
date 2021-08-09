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
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

public class BPMenuType {

    @ObjectHolder(ContainerNames.ALLOY_FURNACE)
    public static MenuType<ContainerAlloyFurnace> ALLOY_FURNACE;

    @ObjectHolder(ContainerNames.BUFFER)
    public static MenuType<ContainerBuffer> BUFFER;

    @ObjectHolder(ContainerNames.SORTING_MACHINE)
    public static MenuType<ContainerSortingMachine> SORTING_MACHINE;

    @ObjectHolder(ContainerNames.SEEDBAG)
    public static MenuType<ContainerSeedBag> SEEDBAG;

    @ObjectHolder(ContainerNames.CANVAS_BAG)
    public static MenuType<ContainerCanvasBag> CANVAS_BAG;

    @ObjectHolder(ContainerNames.CPU)
    public static MenuType<ContainerCPU> CPU;

    @ObjectHolder(ContainerNames.MONITOR)
    public static MenuType<ContainerMonitor> MONITOR;

    @ObjectHolder(ContainerNames.DISK_DRIVE)
    public static MenuType<ContainerDiskDrive> DISK_DRIVE;

    @ObjectHolder(ContainerNames.IO_EXPANDER)
    public static MenuType<ContainerIOExpander> IO_EXPANDER;

    @ObjectHolder(ContainerNames.REDBUS)
    public static MenuType<ContainerRedbusID> REDBUS;

    @ObjectHolder(ContainerNames.KINETIC_GENERATOR)
    public static MenuType<ContainerKinect> KINETIC_GENERATOR;

    @ObjectHolder(ContainerNames.DEPLOYER)
    public static MenuType<ContainerDeployer> DEPLOYER;

    @ObjectHolder(ContainerNames.RELAY)
    public static MenuType<ContainerRelay> RELAY;

    @ObjectHolder(ContainerNames.EJECTOR)
    public static MenuType<ContainerEjector> EJECTOR;

    @ObjectHolder(ContainerNames.FILTER)
    public static MenuType<ContainerFilter> FILTER;

    @ObjectHolder(ContainerNames.RETRIEVER)
    public static MenuType<ContainerRetriever> RETRIEVER;

    @ObjectHolder(ContainerNames.REGULATOR)
    public static MenuType<ContainerRegulator> REGULATOR;

    @ObjectHolder(ContainerNames.ITEM_DETECTOR)
    public static MenuType<ContainerItemDetector> ITEM_DETECTOR;

    @ObjectHolder(ContainerNames.PROJECT_TABLE)
    public static MenuType<ContainerProjectTable> PROJECT_TABLE;

    @ObjectHolder(ContainerNames.CIRCUIT_TABLE)
    public static MenuType<ContainerCircuitTable> CIRCUIT_TABLE;

    @ObjectHolder(ContainerNames.MANAGER)
    public static MenuType<ContainerManager> MANAGER;

    @ObjectHolder(ContainerNames.CIRCUITDATABASE_MAIN)
    public static MenuType<ContainerCircuitDatabaseMain> CIRCUITDATABASE_MAIN;

    @ObjectHolder(ContainerNames.CIRCUITDATABASE_SHARING)
    public static MenuType<ContainerCircuitDatabaseSharing> CIRCUITDATABASE_SHARING;

    @ObjectHolder(ContainerNames.BLULECTRIC_ALLOY_FURNACE)
    public static MenuType<ContainerBlulectricAlloyFurnace> BLULECTRIC_ALLOY_FURNACE;

    @ObjectHolder(ContainerNames.BLULECTRIC_FURNACE)
    public static MenuType<ContainerBlulectricFurnace> BLULECTRIC_FURNACE;


    @Mod.EventBusSubscriber(modid = Refs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration{
        @SubscribeEvent
        public static void onMenuTypeRegistry(final RegistryEvent.Register<MenuType<?>> e){
            e.getRegistry().registerAll(
                    new MenuType<>(ContainerAlloyFurnace::new).setRegistryName(ContainerNames.ALLOY_FURNACE),
                    new MenuType<>(ContainerBlulectricFurnace::new).setRegistryName(ContainerNames.BLULECTRIC_FURNACE),
                    new MenuType<>(ContainerBlulectricAlloyFurnace::new).setRegistryName(ContainerNames.BLULECTRIC_ALLOY_FURNACE),
                    new MenuType<>(ContainerBuffer::new).setRegistryName(ContainerNames.BUFFER),
                    new MenuType<>(ContainerSortingMachine::new).setRegistryName(ContainerNames.SORTING_MACHINE),
                    new MenuType<>(ContainerSeedBag::new).setRegistryName(ContainerNames.SEEDBAG),
                    new MenuType<>(ContainerCanvasBag::new).setRegistryName(ContainerNames.CANVAS_BAG),
                    new MenuType<>(ContainerCPU::new).setRegistryName(ContainerNames.CPU),
                    new MenuType<>(ContainerMonitor::new).setRegistryName(ContainerNames.MONITOR),
                    new MenuType<>(ContainerDiskDrive::new).setRegistryName(ContainerNames.DISK_DRIVE),
                    new MenuType<>(ContainerIOExpander::new).setRegistryName(ContainerNames.IO_EXPANDER),
                    new MenuType<>(ContainerRedbusID::new).setRegistryName(ContainerNames.REDBUS),
                    new MenuType<>(ContainerKinect::new).setRegistryName(ContainerNames.KINETIC_GENERATOR),
                    new MenuType<>(ContainerDeployer::new).setRegistryName(ContainerNames.DEPLOYER),
                    new MenuType<>(ContainerRelay::new).setRegistryName(ContainerNames.RELAY),
                    new MenuType<>(ContainerEjector::new).setRegistryName(ContainerNames.EJECTOR),
                    new MenuType<>(ContainerFilter::new).setRegistryName(ContainerNames.FILTER),
                    new MenuType<>(ContainerRetriever::new).setRegistryName(ContainerNames.RETRIEVER),
                    new MenuType<>(ContainerRegulator::new).setRegistryName(ContainerNames.REGULATOR),
                    new MenuType<>(ContainerItemDetector::new).setRegistryName(ContainerNames.ITEM_DETECTOR),
                    new MenuType<>(ContainerProjectTable::new).setRegistryName(ContainerNames.PROJECT_TABLE),
                    new MenuType<>(ContainerCircuitTable::new).setRegistryName(ContainerNames.CIRCUIT_TABLE),
                    new MenuType<>(ContainerManager::new).setRegistryName(ContainerNames.MANAGER),
                    new MenuType<>(ContainerCircuitDatabaseMain::new).setRegistryName(ContainerNames.CIRCUITDATABASE_MAIN),
                    new MenuType<>(ContainerCircuitDatabaseSharing::new).setRegistryName(ContainerNames.CIRCUITDATABASE_SHARING)
            );
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerScreenFactories(){
        MenuScreens.register(ALLOY_FURNACE, GuiAlloyFurnace::new);
        ScreenManager.register(BLULECTRIC_FURNACE, GuiBlulectricFurnace::new);
        ScreenManager.register(BLULECTRIC_ALLOY_FURNACE, GuiBlulectricAlloyFurnace::new);
        ScreenManager.register(BUFFER, GuiBuffer::new);
        ScreenManager.register(SORTING_MACHINE, GuiSortingMachine::new);
        ScreenManager.register(SEEDBAG, GuiSeedBag::new);
        ScreenManager.register(CANVAS_BAG, GuiCanvasBag::new);
        ScreenManager.register(CPU, GuiCPU::new);
        ScreenManager.register(MONITOR, GuiMonitor::new);
        ScreenManager.register(DISK_DRIVE, GuiDiskDrive::new);
        ScreenManager.register(IO_EXPANDER, GuiIOExpander::new);
        ScreenManager.register(REDBUS, GuiRedbusID::new);
        ScreenManager.register(KINETIC_GENERATOR, GuiKinect::new);
        ScreenManager.register(DEPLOYER, GuiDeployer::new);
        ScreenManager.register(RELAY, GuiRelay::new);
        ScreenManager.register(EJECTOR, GuiEjector::new);
        ScreenManager.register(FILTER, GuiFilter::new);
        ScreenManager.register(RETRIEVER, GuiRetriever::new);
        ScreenManager.register(REGULATOR, GuiRegulator::new);
        ScreenManager.register(ITEM_DETECTOR, GuiItemDetector::new);
        ScreenManager.register(PROJECT_TABLE, GuiProjectTable::new);
        ScreenManager.register(CIRCUIT_TABLE, GuiCircuitTable::new);
        ScreenManager.register(MANAGER, GuiManager::new);
        ScreenManager.register(CIRCUITDATABASE_MAIN, GuiCircuitDatabaseMain::new);
        ScreenManager.register(CIRCUITDATABASE_SHARING, GuiCircuitDatabaseSharing::new);
    }

}
