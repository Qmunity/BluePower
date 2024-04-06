/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.init;

import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMicroblock;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.tier1.*;
import com.bluepowermod.tile.tier2.*;
import com.bluepowermod.tile.tier3.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * @author MoreThanHidden
 */
public class BPBlockEntityType {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPE = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Refs.MODID);

    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileAlloyFurnace>> ALLOY_FURNACE = BLOCK_ENTITY_TYPE.register(Refs.ALLOYFURNACE_NAME, () -> BlockEntityType.Builder.of(TileAlloyFurnace::new, BPBlocks.alloyfurnace.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBuffer>> BUFFER = BLOCK_ENTITY_TYPE.register(Refs.BLOCKBUFFER_NAME, () -> BlockEntityType.Builder.of(TileBuffer::new, BPBlocks.buffer.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSortingMachine>> SORTING_MACHINE = BLOCK_ENTITY_TYPE.register(Refs.SORTING_MACHINE_NAME, () -> BlockEntityType.Builder.of(TileSortingMachine::new, BPBlocks.sorting_machine.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCPU>> CPU;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileMonitor>> MONITOR;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDiskDrive>> DISK_DRIVE;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileIOExpander>> IO_EXPANDER;
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileKineticGenerator>> KINETIC_GENERATOR = BLOCK_ENTITY_TYPE.register(Refs.KINETICGENERATOR_NAME, () -> BlockEntityType.Builder.of(TileKineticGenerator::new, BPBlocks.kinetic_generator.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileDeployer>> DEPLOYER = BLOCK_ENTITY_TYPE.register(Refs.BLOCKDEPLOYER_NAME, () -> BlockEntityType.Builder.of(TileDeployer::new, BPBlocks.deployer.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileRelay>> RELAY = BLOCK_ENTITY_TYPE.register(Refs.RELAY_NAME, () -> BlockEntityType.Builder.of(TileRelay::new, BPBlocks.relay.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEjector>> EJECTOR = BLOCK_ENTITY_TYPE.register(Refs.EJECTOR_NAME, () -> BlockEntityType.Builder.of(TileEjector::new, BPBlocks.ejector.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileFilter>> FILTER = BLOCK_ENTITY_TYPE.register(Refs.FILTER_NAME, () -> BlockEntityType.Builder.of(TileFilter::new, BPBlocks.filter.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileRetriever>> RETRIEVER = BLOCK_ENTITY_TYPE.register(Refs.RETRIEVER_NAME, () -> BlockEntityType.Builder.of(TileRetriever::new, BPBlocks.retriever.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileRegulator>> REGULATOR = BLOCK_ENTITY_TYPE.register(Refs.REGULATOR_NAME, () -> BlockEntityType.Builder.of(TileRegulator::new, BPBlocks.regulator.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileItemDetector>> ITEM_DETECTOR = BLOCK_ENTITY_TYPE.register(Refs.ITEMDETECTOR_NAME, () -> BlockEntityType.Builder.of(TileItemDetector::new, BPBlocks.item_detector.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileProjectTable>> PROJECT_TABLE = BLOCK_ENTITY_TYPE.register(Refs.PROJECTTABLE_NAME, () -> BlockEntityType.Builder.of(TileProjectTable::new, BPBlocks.project_table.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileAutoProjectTable>> AUTO_PROJECT_TABLE = BLOCK_ENTITY_TYPE.register(Refs.AUTOPROJECTTABLE_NAME, () -> BlockEntityType.Builder.of(TileAutoProjectTable::new, BPBlocks.auto_project_table.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCircuitTable>> CIRCUIT_TABLE = BLOCK_ENTITY_TYPE.register(Refs.CIRCUITTABLE_NAME, () -> BlockEntityType.Builder.of(TileCircuitTable::new, BPBlocks.circuit_table.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileManager>> MANAGER = BLOCK_ENTITY_TYPE.register(Refs.MANAGER_NAME, () -> BlockEntityType.Builder.of(TileManager::new, BPBlocks.manager.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCircuitDatabase>> CIRCUITDATABASE = BLOCK_ENTITY_TYPE.register(Refs.CIRCUITDATABASE_NAME, () -> BlockEntityType.Builder.of(TileCircuitDatabase::new, BPBlocks.circuit_database.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileCircuitDatabase>> WINDMILL;//BLOCK_ENTITY_TYPE.register(Refs.WINDMILL_NAME, () -> BlockEntityType.Builder.of(TileWindmill::new, BPBlocks.windmill.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileEngine>> ENGINE = BLOCK_ENTITY_TYPE.register(Refs.ENGINE_NAME, () -> BlockEntityType.Builder.of(TileEngine::new, BPBlocks.engine.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileTube>> TUBE = BLOCK_ENTITY_TYPE.register(Refs.TUBE_NAME, () -> BlockEntityType.Builder.of(TileTube::new, BPBlocks.tube.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSortron>> SORTRON; //BLOCK_ENTITY_TYPE.register(Refs.SORTRON_NAME, () -> BlockEntityType.Builder.of(TileSortron::new, BPBlocks.sortron).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBlockBreaker>> BLOCKBREAKER = BLOCK_ENTITY_TYPE.register(Refs.BLOCKBREAKER_NAME, () -> BlockEntityType.Builder.of(TileBlockBreaker::new, BPBlocks.block_breaker.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileIgniter>> IGNITER = BLOCK_ENTITY_TYPE.register(Refs.BLOCKIGNITER_NAME, () -> BlockEntityType.Builder.of(TileIgniter::new, BPBlocks.igniter.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileLamp>> LAMP = BLOCK_ENTITY_TYPE.register(Refs.LAMP_NAME, () -> BlockEntityType.Builder.of(TileLamp::new, BPBlocks.allLamps.stream().map(DeferredHolder::get).toList().toArray(new Block[0])).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBPMultipart>> MULTIPART = BLOCK_ENTITY_TYPE.register(Refs.MULTIPART_NAME, () -> BlockEntityType.Builder.of(TileBPMultipart::new, BPBlocks.multipart.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileTransposer>> TRANSPOSER = BLOCK_ENTITY_TYPE.register(Refs.TRANSPOSER_NAME, () -> BlockEntityType.Builder.of(TileTransposer::new, BPBlocks.transposer.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileWire>> WIRE = BLOCK_ENTITY_TYPE.register("alloywire", () -> BlockEntityType.Builder.of(TileWire::new, BPBlocks.blockRedAlloyWire.get(), BPBlocks.blockBlueAlloyWire.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileInsulatedWire>> INSULATEDWIRE; //BLOCK_ENTITY_TYPE.register("insulated_alloywire", () -> BlockEntityType.Builder.of(TileInsulatedWire::new, BPBlocks.blockInsulatedBlueAlloyWire, BPBlocks.blockInsulatedRedAlloyWire).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBattery>> BATTERY = BLOCK_ENTITY_TYPE.register(Refs.BATTERY_NAME, () -> BlockEntityType.Builder.of(TileBattery::new, BPBlocks.battery.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBlulectricCable>> BLULECTRIC_CABLE = BLOCK_ENTITY_TYPE.register(Refs.BLULECTRICCABLE_NAME, () -> BlockEntityType.Builder.of(TileBlulectricCable::new, BPBlocks.blulectric_cable.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBlulectricFurnace>> BLULECTRIC_FURNACE = BLOCK_ENTITY_TYPE.register(Refs.BLULECTRICFURNACE_NAME, () -> BlockEntityType.Builder.of(TileBlulectricFurnace::new, BPBlocks.blulectric_furnace.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBlulectricAlloyFurnace>> BLULECTRIC_ALLOY_FURNACE = BLOCK_ENTITY_TYPE.register(Refs.BLULECTRICALLOYFURNACE_NAME, () -> BlockEntityType.Builder.of(TileBlulectricAlloyFurnace::new, BPBlocks.blulectric_alloyfurnace.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileSolarPanel>> SOLAR_PANEL = BLOCK_ENTITY_TYPE.register(Refs.SOLARPANEL_NAME, () -> BlockEntityType.Builder.of(TileSolarPanel::new, BPBlocks.solarpanel.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileThermopile>> THERMOPILE = BLOCK_ENTITY_TYPE.register(Refs.THERMOPILE_NAME, () -> BlockEntityType.Builder.of(TileThermopile::new, BPBlocks.thermopile.get()).build(null));
    public static DeferredHolder<BlockEntityType<?>, BlockEntityType<TileBPMicroblock>> MICROBLOCK = BLOCK_ENTITY_TYPE.register("microblock", () -> BlockEntityType.Builder.of(TileBPMicroblock::new, BPBlocks.microblocks.stream().map(DeferredHolder::get).toList().toArray(new Block[0])).build(null));

}
