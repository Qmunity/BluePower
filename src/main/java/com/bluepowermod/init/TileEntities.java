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

import com.bluepowermod.reference.ContainerNames;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.*;
import com.bluepowermod.tile.tier2.*;
import com.bluepowermod.tile.tier3.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


public class TileEntities {

    /**
     * Method to register the TE's to the game. If a TE is not registered, it _will_ cause issues!
     */
    @Mod.EventBusSubscriber(modid = Refs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
        @SubscribeEvent
        public static void onTileEntityTypeRegistry(final RegistryEvent.Register<TileEntityType<?>> event) {
            event.getRegistry().registerAll(
                    TileEntityType.Builder.create(TileAlloyFurnace::new, BPBlocks.alloyfurnace).build(null).setRegistryName(ContainerNames.ALLOY_FURNACE),
                    TileEntityType.Builder.create(TileBlockBreaker::new, BPBlocks.block_breaker).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "block_breaker")),
                    TileEntityType.Builder.create(TileIgniter::new, BPBlocks.igniter).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "igniter")),
                    TileEntityType.Builder.create(TileBuffer::new, BPBlocks.buffer).build(null).setRegistryName(ContainerNames.BUFFER),
                    TileEntityType.Builder.create(TileTransposer::new, BPBlocks.transposer).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "transposer")),
                    TileEntityType.Builder.create(TileSortingMachine::new, BPBlocks.sorting_machine).build(null).setRegistryName(ContainerNames.SORTING_MACHINE),
                    //TileEntityType.Builder.create(TileSortron::new, BPBlocks.sortron).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "sortron")),
                    TileEntityType.Builder.create(TileEngine::new, BPBlocks.engine).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "engine")),
                    TileEntityType.Builder.create(TileBattery::new, BPBlocks.battery).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "battery")),
                    //TileEntityType.Builder.create(TileWindmill::new, BPBlocks.windmill).build(null).setRegistryName(ContainerNames.WINDMILL),
                    TileEntityType.Builder.create(TileKinectGenerator::new, BPBlocks.kinetic_generator).build(null).setRegistryName(ContainerNames.KINETIC_GENERATOR),
                    TileEntityType.Builder.create(TileSolarPanel::new, BPBlocks.solarpanel).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "solar_panel")),
                    TileEntityType.Builder.create(TileDeployer::new, BPBlocks.deployer).build(null).setRegistryName(ContainerNames.DEPLOYER),
                    TileEntityType.Builder.create(TileEjector::new, BPBlocks.ejector).build(null).setRegistryName(ContainerNames.EJECTOR),
                    TileEntityType.Builder.create(TileRelay::new, BPBlocks.relay).build(null).setRegistryName(ContainerNames.RELAY),
                    TileEntityType.Builder.create(TileFilter::new, BPBlocks.filter).build(null).setRegistryName(ContainerNames.FILTER),
                    TileEntityType.Builder.create(TileRetriever::new, BPBlocks.retriever).build(null).setRegistryName(ContainerNames.RETRIEVER),
                    TileEntityType.Builder.create(TileRegulator::new, BPBlocks.regulator).build(null).setRegistryName(ContainerNames.REGULATOR),
                    TileEntityType.Builder.create(TileItemDetector::new, BPBlocks.item_detector).build(null).setRegistryName(ContainerNames.ITEM_DETECTOR),
                    TileEntityType.Builder.create(TileManager::new, BPBlocks.manager).build(null).setRegistryName(ContainerNames.MANAGER),
                    TileEntityType.Builder.create(TileProjectTable::new, BPBlocks.project_tables).build(null).setRegistryName(ContainerNames.PROJECT_TABLE),
                    TileEntityType.Builder.create(TileCircuitTable::new, BPBlocks.circuit_table).build(null).setRegistryName(ContainerNames.CIRCUIT_TABLE),
                    TileEntityType.Builder.create(TileCircuitDatabase::new, BPBlocks.circuit_database).build(null).setRegistryName(ContainerNames.CIRCUITDATABASE_MAIN),
                    TileEntityType.Builder.create(TileLamp::new, BPBlocks.allLamps.toArray(new Block[0])).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "lamp")),
                    TileEntityType.Builder.create(TileWire::new, BPBlocks.blockAlloyWire).build(null).setRegistryName(new ResourceLocation(Refs.MODID, "wire"))
            );

        }
    }
}
