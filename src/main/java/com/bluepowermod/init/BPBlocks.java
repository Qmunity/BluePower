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

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.blocks.machines.BlockAlloyFurnace;
import com.bluepowermod.blocks.machines.BlockCircuitDatabase;
import com.bluepowermod.blocks.machines.BlockContainerFrontRender;
import com.bluepowermod.blocks.machines.BlockContainerTwoSideRender;
import com.bluepowermod.blocks.machines.BlockIgniter;
import com.bluepowermod.blocks.machines.BlockLamp;
import com.bluepowermod.blocks.machines.BlockProjectTable;
import com.bluepowermod.blocks.machines.BlockRejecting;
import com.bluepowermod.blocks.machines.BlockSortron;
import com.bluepowermod.blocks.worldgen.BlockAmethystOre;
import com.bluepowermod.blocks.worldgen.BlockBasalt;
import com.bluepowermod.blocks.worldgen.BlockCrackedBasalt;
import com.bluepowermod.blocks.worldgen.BlockCrop;
import com.bluepowermod.blocks.worldgen.BlockCustomFlower;
import com.bluepowermod.blocks.worldgen.BlockRubyOre;
import com.bluepowermod.blocks.worldgen.BlockSapphireOre;
import com.bluepowermod.blocks.worldgen.BlockStoneOre;
import com.bluepowermod.blocks.worldgen.BlockStoneOreConnected;
import com.bluepowermod.blocks.worldgen.BlockTeslatiteOre;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier1.TileBlockBreaker;
import com.bluepowermod.tileentities.tier1.TileBuffer;
import com.bluepowermod.tileentities.tier1.TileDeployer;
import com.bluepowermod.tileentities.tier1.TileEjector;
import com.bluepowermod.tileentities.tier1.TileFilter;
import com.bluepowermod.tileentities.tier1.TileItemDetector;
import com.bluepowermod.tileentities.tier1.TileRelay;
import com.bluepowermod.tileentities.tier1.TileTransposer;
import com.bluepowermod.tileentities.tier2.TileCircuitTable;
import com.bluepowermod.tileentities.tier2.TileRegulator;
import com.bluepowermod.tileentities.tier2.TileRetriever;
import com.bluepowermod.tileentities.tier2.TileSortingMachine;
import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;
import com.bluepowermod.tileentities.tier3.TileManager;
import com.bluepowermod.util.Dependencies;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Refs.MODID)
public class BPBlocks {

    public static final Block basalt = new BlockBasalt(Refs.BASALT_NAME).setResistance(25.0F);
    public static final Block marble = new BlockStoneOre(Refs.MARBLE_NAME).setResistance(1.0F).setHardness(1.5F);
    public static final Block basalt_cobble = new BlockStoneOre(Refs.BASALTCOBBLE_NAME);
    public static final Block basalt_brick = new BlockStoneOre(Refs.BASALTBRICK_NAME);
    public static final Block marble_brick = new BlockStoneOre(Refs.MARBLEBRICK_NAME);
    public static final Block cracked_basalt_lava = new BlockCrackedBasalt(Refs.CRACKED_BASALT);

    public static final Block basaltbrick_cracked = new BlockStoneOre(Refs.CRACKEDBASALTBRICK_NAME);
    public static final Block basalt_brick_small = new BlockStoneOre(Refs.SMALLBASALTBRICK_NAME);
    public static final Block marble_brick_small = new BlockStoneOre(Refs.SMALLMARBLEBRICK_NAME);
    public static final Block fancy_basalt = new BlockStoneOre(Refs.CHISELEDBASALTBRICK_NAME);
    public static final Block fancy_marble = new BlockStoneOre(Refs.CHISELEDMARBLEBRICK_NAME);
    public static final Block marble_tile = new BlockStoneOreConnected(Refs.MARBLETILE_NAME);
    public static final Block basalt_tile = new BlockStoneOreConnected(Refs.BASALTTILE_NAME);
    public static final Block marble_paver = new BlockStoneOre(Refs.MARBLEPAVER_NAME);
    public static final Block basalt_paver = new BlockStoneOre(Refs.BASALTPAVER_NAME);
    public static final Block tiles = new BlockStoneOre(Refs.TILES);

    public static final Block teslatite_ore = new BlockTeslatiteOre(Refs.TESLATITEORE_NAME);
    public static final Block ruby_ore = new BlockRubyOre(Refs.RUBYORE_NAME);
    public static final Block sapphire_ore = new BlockSapphireOre(Refs.SAPPHIREORE_NAME);
    public static final Block amethyst_ore = new BlockAmethystOre(Refs.AMETHYSTORE_NAME);

    public static final Block copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
    public static final Block silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME).setToolLevel(2);
    public static final Block zinc_ore = new BlockStoneOre(Refs.ZINCORE_NAME);
    public static final Block tungsten_ore = new BlockStoneOre(Refs.TUNGSTENORE_NAME).setToolLevel(3).setResistance(6.0F)
            .setHardness(15.0F);

    public static final Block ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME).setToolLevel(2);
    public static final Block sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME).setToolLevel(2);
    public static final Block amethyst_block = new BlockStoneOre(Refs.AMETHYSTBLOCK_NAME).setToolLevel(2);
    public static final Block teslatite_block = new BlockStoneOre(Refs.TESLATITEBLOCK_NAME).setToolLevel(2);
    public static final Block copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
    public static final Block silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME).setToolLevel(2);
    public static final Block zinc_block = new BlockStoneOre(Refs.ZINCBLOCK_NAME);
    public static final Block tungsten_block = new BlockStoneOre(Refs.TUNGSTENBLOCK_NAME).setToolLevel(3).setResistance(25.0F)
            .setHardness(5.0F);

    public static final Block flax_crop = new BlockCrop().setBlockName(Refs.FLAXCROP_NAME);
    public static final Block indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME);

    public static final Block alloyfurnace = new BlockAlloyFurnace();
    public static final Block block_breaker = new BlockContainerFrontRender(Material.rock, TileBlockBreaker.class)
            .setBlockName(Refs.BLOCKBREAKER_NAME);
    public static final Block igniter = new BlockIgniter();
    public static final Block buffer = new BlockContainerBase(Material.rock, TileBuffer.class).setGuiId(GuiIDs.BUFFER).setBlockName(
            Refs.BLOCKBUFFER_NAME);
    public static final Block deployer = new BlockContainerFrontRender(Material.rock, TileDeployer.class).setGuiId(GuiIDs.DEPLOYER_ID)
            .setBlockName(Refs.BLOCKDEPLOYER_NAME);
    public static final Block transposer = new BlockContainerBase(Material.rock, TileTransposer.class).setBlockName(Refs.TRANSPOSER_NAME);
    public static final Block sorting_machine = new BlockContainerBase(Material.rock, TileSortingMachine.class).setGuiId(
            GuiIDs.SORTING_MACHINE).setBlockName(Refs.SORTING_MACHINE_NAME);
    public static final Block project_table = new BlockProjectTable().setGuiId(GuiIDs.PROJECTTABLE_ID);
    public static final Block circuit_table = new BlockProjectTable(TileCircuitTable.class).setGuiId(GuiIDs.CIRCUITTABLE_ID).setBlockName(
            Refs.CIRCUITTABLE_NAME);
    public static final Block circuit_database = new BlockCircuitDatabase(TileCircuitDatabase.class).setGuiId(
            GuiIDs.CIRCUITDATABASE_MAIN_ID).setBlockName(Refs.CIRCUITDATABASE_NAME);
    public static final Block ejector = new BlockContainerTwoSideRender(Material.rock, TileEjector.class).setGuiId(GuiIDs.EJECTOR_ID)
            .setBlockName(Refs.EJECTOR_NAME);
    public static final Block relay = new BlockContainerTwoSideRender(Material.rock, TileRelay.class).setGuiId(GuiIDs.RELAY_ID)
            .setBlockName(Refs.RELAY_NAME);
    public static final Block filter = new BlockContainerBase(Material.rock, TileFilter.class).setGuiId(GuiIDs.FILTER_ID).setBlockName(
            Refs.FILTER_NAME);
    public static final Block retriever = new BlockContainerBase(Material.rock, TileRetriever.class).setGuiId(GuiIDs.RETRIEVER_ID)
            .setBlockName(Refs.RETRIEVER_NAME);
    public static final Block regulator = new BlockContainerTwoSideRender(Material.rock, TileRegulator.class).setGuiId(GuiIDs.REGULATOR_ID)
            .emitsRedstone().setBlockName(Refs.REGULATOR_NAME);
    public static final Block item_detector = new BlockContainerTwoSideRender(Material.rock, TileItemDetector.class)
            .setGuiId(GuiIDs.ITEMDETECTOR_ID).emitsRedstone().setBlockName(Refs.ITEMDETECTOR_NAME);
    public static final Block manager = new BlockRejecting(Material.rock, TileManager.class).setGuiId(GuiIDs.MANAGER_ID).emitsRedstone()
            .setBlockName(Refs.MANAGER_NAME);
    // public static final Block engine = new BlockEngine();
    // public static final Block kinetic_generator = new BlockKineticGenerator();
    // public static final Block windmill = new BlockWindmill();
    public static Block[] blockLamp = new Block[MinecraftColor.VALID_COLORS.length];

    // public static final Block cpu = new BlockCPU();
    // public static final Block monitor = new BlockMonitor();
    // public static final Block disk_drive = new BlockDiskDrive();
    // public static final Block io_expander = new BlockIOExpander();
    public static Block[] blockLampInverted = new Block[MinecraftColor.VALID_COLORS.length];
    public static Block sortron;

    public static void init() {

        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLamp[i] = new BlockLamp(false, MinecraftColor.VALID_COLORS[i]);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLampInverted[i] = new BlockLamp(true, MinecraftColor.VALID_COLORS[i]);

        registerBlocks();
        initModDependantBlocks();
    }

    private static void registerBlocks() {

        GameRegistry.registerBlock(basalt, Refs.BASALT_NAME);
        GameRegistry.registerBlock(basalt_cobble, Refs.BASALTCOBBLE_NAME);
        GameRegistry.registerBlock(basalt_brick, Refs.BASALTBRICK_NAME);
        GameRegistry.registerBlock(basaltbrick_cracked, Refs.CRACKEDBASALTBRICK_NAME);
        GameRegistry.registerBlock(fancy_basalt, Refs.CHISELEDBASALTBRICK_NAME);
        GameRegistry.registerBlock(basalt_brick_small, Refs.SMALLBASALTBRICK_NAME);
        GameRegistry.registerBlock(cracked_basalt_lava, Refs.CRACKED_BASALT);
        GameRegistry.registerBlock(basalt_tile, Refs.BASALTTILE_NAME);
        GameRegistry.registerBlock(basalt_paver, Refs.BASALTPAVER_NAME);

        GameRegistry.registerBlock(marble, Refs.MARBLE_NAME);
        GameRegistry.registerBlock(marble_brick, Refs.MARBLEBRICK_NAME);
        GameRegistry.registerBlock(fancy_marble, Refs.CHISELEDMARBLEBRICK_NAME);
        GameRegistry.registerBlock(marble_brick_small, Refs.SMALLMARBLEBRICK_NAME);
        GameRegistry.registerBlock(marble_tile, Refs.MARBLETILE_NAME);
        GameRegistry.registerBlock(marble_paver, Refs.MARBLEPAVER_NAME);
        GameRegistry.registerBlock(tiles, Refs.TILES_NAME);

        GameRegistry.registerBlock(teslatite_ore, Refs.TESLATITEORE_NAME);
        GameRegistry.registerBlock(copper_ore, Refs.COPPERORE_NAME);
        GameRegistry.registerBlock(silver_ore, Refs.SILVERORE_NAME);
        GameRegistry.registerBlock(zinc_ore, Refs.ZINCORE_NAME);
        GameRegistry.registerBlock(tungsten_ore, Refs.TUNGSTENORE_NAME);
        GameRegistry.registerBlock(ruby_ore, Refs.RUBYORE_NAME);
        GameRegistry.registerBlock(sapphire_ore, Refs.SAPPHIREORE_NAME);
        GameRegistry.registerBlock(amethyst_ore, Refs.AMETHYSTORE_NAME);

        GameRegistry.registerBlock(ruby_block, Refs.RUBYBLOCK_NAME);
        GameRegistry.registerBlock(sapphire_block, Refs.SAPPHIREBLOCK_NAME);
        GameRegistry.registerBlock(amethyst_block, Refs.AMETHYSTBLOCK_NAME);
        GameRegistry.registerBlock(teslatite_block, Refs.TESLATITEBLOCK_NAME);
        GameRegistry.registerBlock(copper_block, Refs.COPPERBLOCK_NAME);
        GameRegistry.registerBlock(silver_block, Refs.SILVERBLOCK_NAME);
        GameRegistry.registerBlock(zinc_block, Refs.ZINCBLOCK_NAME);
        GameRegistry.registerBlock(tungsten_block, Refs.TUNGSTENBLOCK_NAME);

        GameRegistry.registerBlock(flax_crop, Refs.FLAXCROP_NAME);
        GameRegistry.registerBlock(indigo_flower, Refs.INDIGOFLOWER_NAME);

        GameRegistry.registerBlock(alloyfurnace, Refs.ALLOYFURNACE_NAME);
        GameRegistry.registerBlock(sorting_machine, Refs.SORTING_MACHINE_NAME);
        GameRegistry.registerBlock(block_breaker, Refs.BLOCKBREAKER_NAME);
        GameRegistry.registerBlock(igniter, Refs.BLOCKIGNITER_NAME);
        GameRegistry.registerBlock(buffer, Refs.BLOCKBUFFER_NAME);
        GameRegistry.registerBlock(deployer, Refs.BLOCKDEPLOYER_NAME);
        GameRegistry.registerBlock(project_table, Refs.PROJECTTABLE_NAME);
        GameRegistry.registerBlock(circuit_table, Refs.CIRCUITTABLE_NAME);
        GameRegistry.registerBlock(circuit_database, Refs.CIRCUITDATABASE_NAME);
        GameRegistry.registerBlock(transposer, Refs.TRANSPOSER_NAME);
        GameRegistry.registerBlock(ejector, Refs.EJECTOR_NAME);
        GameRegistry.registerBlock(relay, Refs.RELAY_NAME);
        GameRegistry.registerBlock(filter, Refs.FILTER_NAME);
        GameRegistry.registerBlock(retriever, Refs.RETRIEVER_NAME);
        GameRegistry.registerBlock(regulator, Refs.REGULATOR_NAME);
        GameRegistry.registerBlock(item_detector, Refs.ITEMDETECTOR_NAME);
        GameRegistry.registerBlock(manager, Refs.MANAGER_NAME);

        /*
         * GameRegistry.registerBlock(cpu, Refs.BLOCKCPU_NAME); GameRegistry.registerBlock(monitor, Refs.BLOCKMONITOR_NAME);
         * GameRegistry.registerBlock(disk_drive, Refs.BLOCKDISKDRIVE_NAME); GameRegistry.registerBlock(io_expander, Refs.BLOCKIOEXPANDER_NAME);
         * 
         * GameRegistry.registerBlock(engine, Refs.ENGINE_NAME); GameRegistry.registerBlock(kinetic_generator, Refs.KINETICGENERATOR_NAME);
         * GameRegistry.registerBlock(windmill, Refs.WINDMILL_NAME);
         */

        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++) {
            MinecraftColor color = MinecraftColor.VALID_COLORS[i];
            GameRegistry.registerBlock(blockLamp[i], Refs.LAMP_NAME + color.name().toLowerCase());
        }
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++) {
            MinecraftColor color = MinecraftColor.VALID_COLORS[i];
            GameRegistry.registerBlock(blockLampInverted[i], Refs.LAMP_NAME + "inverted" + color.name().toLowerCase());
        }

    }

    private static void initModDependantBlocks() {

        if (Loader.isModLoaded(Dependencies.COMPUTER_CRAFT) || Loader.isModLoaded(Dependencies.OPEN_COMPUTERS)) {
            sortron = new BlockSortron();
            GameRegistry.registerBlock(sortron, Refs.BLOCKSORTRON_NAME);
        }
    }
}
