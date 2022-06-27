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
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BPMenuType {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Refs.MODID);

    public static final RegistryObject<MenuType<ContainerAlloyFurnace>> ALLOY_FURNACE = CONTAINERS.register(ContainerNames.ALLOY_FURNACE, () -> new MenuType<>(ContainerAlloyFurnace::new));
    public static final RegistryObject<MenuType<ContainerBuffer>> BUFFER = CONTAINERS.register(ContainerNames.BUFFER,() -> new MenuType<>(ContainerBuffer::new));
    public static final RegistryObject<MenuType<ContainerSortingMachine>> SORTING_MACHINE = CONTAINERS.register(ContainerNames.SORTING_MACHINE,() -> new MenuType<>(ContainerSortingMachine::new));
    public static final RegistryObject<MenuType<ContainerSeedBag>> SEEDBAG = CONTAINERS.register(ContainerNames.SEEDBAG,() -> new MenuType<>(ContainerSeedBag::new));
    public static final RegistryObject<MenuType<ContainerCanvasBag>> CANVAS_BAG = CONTAINERS.register(ContainerNames.CANVAS_BAG,() -> new MenuType<>(ContainerCanvasBag::new));
    public static final RegistryObject<MenuType<ContainerCPU>> CPU = CONTAINERS.register(ContainerNames.CPU,() -> new MenuType<>(ContainerCPU::new));
    public static final RegistryObject<MenuType<ContainerMonitor>> MONITOR = CONTAINERS.register(ContainerNames.MONITOR,() -> new MenuType<>(ContainerMonitor::new));
    public static final RegistryObject<MenuType<ContainerDiskDrive>> DISK_DRIVE = CONTAINERS.register(ContainerNames.DISK_DRIVE,() -> new MenuType<>(ContainerDiskDrive::new));
    public static final RegistryObject<MenuType<ContainerIOExpander>> IO_EXPANDER = CONTAINERS.register(ContainerNames.IO_EXPANDER,() -> new MenuType<>(ContainerIOExpander::new));
    public static final RegistryObject<MenuType<ContainerRedbusID>> REDBUS = CONTAINERS.register(ContainerNames.REDBUS,() -> new MenuType<>(ContainerRedbusID::new));
    public static final RegistryObject<MenuType<ContainerKinect>> KINETIC_GENERATOR = CONTAINERS.register(ContainerNames.KINETIC_GENERATOR,() -> new MenuType<>(ContainerKinect::new));
    public static final RegistryObject<MenuType<ContainerDeployer>> DEPLOYER = CONTAINERS.register(ContainerNames.DEPLOYER,() -> new MenuType<>(ContainerDeployer::new));
    public static final RegistryObject<MenuType<ContainerRelay>> RELAY = CONTAINERS.register(ContainerNames.RELAY,() -> new MenuType<>(ContainerRelay::new));
    public static final RegistryObject<MenuType<ContainerEjector>> EJECTOR = CONTAINERS.register(ContainerNames.EJECTOR,() -> new MenuType<>(ContainerEjector::new));
    public static final RegistryObject<MenuType<ContainerFilter>> FILTER = CONTAINERS.register(ContainerNames.FILTER,() -> new MenuType<>(ContainerFilter::new));
    public static final RegistryObject<MenuType<ContainerRetriever>> RETRIEVER = CONTAINERS.register(ContainerNames.RETRIEVER,() -> new MenuType<>(ContainerRetriever::new));
    public static final RegistryObject<MenuType<ContainerRegulator>> REGULATOR = CONTAINERS.register(ContainerNames.REGULATOR,() -> new MenuType<>(ContainerRegulator::new));
    public static final RegistryObject<MenuType<ContainerItemDetector>> ITEM_DETECTOR = CONTAINERS.register(ContainerNames.ITEM_DETECTOR,() -> new MenuType<>(ContainerItemDetector::new));
    public static final RegistryObject<MenuType<ContainerProjectTable>> PROJECT_TABLE = CONTAINERS.register(ContainerNames.PROJECT_TABLE,() -> new MenuType<>(ContainerProjectTable::new));
    public static final RegistryObject<MenuType<ContainerCircuitTable>> CIRCUIT_TABLE = CONTAINERS.register(ContainerNames.CIRCUIT_TABLE,() -> new MenuType<>(ContainerCircuitTable::new));
    public static final RegistryObject<MenuType<ContainerManager>> MANAGER = CONTAINERS.register(ContainerNames.MANAGER,() -> new MenuType<>(ContainerManager::new));
    public static final RegistryObject<MenuType<ContainerCircuitDatabaseMain>> CIRCUITDATABASE_MAIN = CONTAINERS.register(ContainerNames.CIRCUITDATABASE_MAIN,() -> new MenuType<>(ContainerCircuitDatabaseMain::new));
    public static final RegistryObject<MenuType<ContainerCircuitDatabaseSharing>> CIRCUITDATABASE_SHARING = CONTAINERS.register(ContainerNames.CIRCUITDATABASE_SHARING,() -> new MenuType<>(ContainerCircuitDatabaseSharing::new));
    public static final RegistryObject<MenuType<ContainerBlulectricAlloyFurnace>> BLULECTRIC_ALLOY_FURNACE = CONTAINERS.register(ContainerNames.BLULECTRIC_ALLOY_FURNACE,() -> new MenuType<>(ContainerBlulectricAlloyFurnace::new));
    public static final RegistryObject<MenuType<ContainerBlulectricFurnace>> BLULECTRIC_FURNACE = CONTAINERS.register(ContainerNames.BLULECTRIC_FURNACE,() -> new MenuType<>(ContainerBlulectricFurnace::new));

    @OnlyIn(Dist.CLIENT)
    public static void registerScreenFactories(){
        MenuScreens.register(ALLOY_FURNACE.get(), GuiAlloyFurnace::new);
        MenuScreens.register(BLULECTRIC_FURNACE.get(), GuiBlulectricFurnace::new);
        MenuScreens.register(BLULECTRIC_ALLOY_FURNACE.get(), GuiBlulectricAlloyFurnace::new);
        MenuScreens.register(BUFFER.get(), GuiBuffer::new);
        MenuScreens.register(SORTING_MACHINE.get(), GuiSortingMachine::new);
        MenuScreens.register(SEEDBAG.get(), GuiSeedBag::new);
        MenuScreens.register(CANVAS_BAG.get(), GuiCanvasBag::new);
        MenuScreens.register(CPU.get(), GuiCPU::new);
        MenuScreens.register(MONITOR.get(), GuiMonitor::new);
        MenuScreens.register(DISK_DRIVE.get(), GuiDiskDrive::new);
        MenuScreens.register(IO_EXPANDER.get(), GuiIOExpander::new);
        MenuScreens.register(REDBUS.get(), GuiRedbusID::new);
        MenuScreens.register(KINETIC_GENERATOR.get(), GuiKinect::new);
        MenuScreens.register(DEPLOYER.get(), GuiDeployer::new);
        MenuScreens.register(RELAY.get(), GuiRelay::new);
        MenuScreens.register(EJECTOR.get(), GuiEjector::new);
        MenuScreens.register(FILTER.get(), GuiFilter::new);
        MenuScreens.register(RETRIEVER.get(), GuiRetriever::new);
        MenuScreens.register(REGULATOR.get(), GuiRegulator::new);
        MenuScreens.register(ITEM_DETECTOR.get(), GuiItemDetector::new);
        MenuScreens.register(PROJECT_TABLE.get(), GuiProjectTable::new);
        MenuScreens.register(CIRCUIT_TABLE.get(), GuiCircuitTable::new);
        MenuScreens.register(MANAGER.get(), GuiManager::new);
        MenuScreens.register(CIRCUITDATABASE_MAIN.get(), GuiCircuitDatabaseMain::new);
        MenuScreens.register(CIRCUITDATABASE_SHARING.get(), GuiCircuitDatabaseSharing::new);
    }

}
