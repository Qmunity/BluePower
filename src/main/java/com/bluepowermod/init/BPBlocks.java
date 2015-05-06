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

import com.bluepowermod.block.machine.*;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.block.worldgen.BlockAmethystOre;
import com.bluepowermod.block.worldgen.BlockBasalt;
import com.bluepowermod.block.worldgen.BlockCrackedBasalt;
import com.bluepowermod.block.worldgen.BlockCrop;
import com.bluepowermod.block.worldgen.BlockCustomFlower;
import com.bluepowermod.block.worldgen.BlockRubyOre;
import com.bluepowermod.block.worldgen.BlockSapphireOre;
import com.bluepowermod.block.worldgen.BlockStoneOre;
import com.bluepowermod.block.worldgen.BlockStoneOreConnected;
import com.bluepowermod.block.worldgen.BlockTeslatiteOre;
import com.bluepowermod.item.ItemBlockTooltip;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileBlockBreaker;
import com.bluepowermod.tile.tier1.TileBuffer;
import com.bluepowermod.tile.tier1.TileDeployer;
import com.bluepowermod.tile.tier1.TileEjector;
import com.bluepowermod.tile.tier1.TileFilter;
import com.bluepowermod.tile.tier1.TileItemDetector;
import com.bluepowermod.tile.tier1.TileRelay;
import com.bluepowermod.tile.tier1.TileTransposer;
import com.bluepowermod.tile.tier2.TileAutoProjectTable;
import com.bluepowermod.tile.tier2.TileCircuitTable;
import com.bluepowermod.tile.tier2.TileRegulator;
import com.bluepowermod.tile.tier2.TileRetriever;
import com.bluepowermod.tile.tier2.TileSortingMachine;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import com.bluepowermod.tile.tier3.TileManager;
import com.bluepowermod.util.Dependencies;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Refs.MODID)
public class BPBlocks {

    public static Block basalt;
    public static Block marble;
    public static Block basalt_cobble;
    public static Block basalt_brick;
    public static Block marble_brick;
    public static Block cracked_basalt_lava;

    public static Block basaltbrick_cracked;
    public static Block basalt_brick_small;
    public static Block marble_brick_small;
    public static Block fancy_basalt;
    public static Block fancy_marble;
    public static Block marble_tile;
    public static Block basalt_tile;
    public static Block marble_paver;
    public static Block basalt_paver;
    public static Block tiles;

    public static Block teslatite_ore;
    public static Block ruby_ore;
    public static Block sapphire_ore;
    public static Block amethyst_ore;

    public static Block copper_ore;
    public static Block silver_ore;
    public static Block zinc_ore;
    public static Block tungsten_ore;

    public static Block ruby_block;
    public static Block sapphire_block;
    public static Block amethyst_block;
    public static Block teslatite_block;
    public static Block copper_block;
    public static Block silver_block;
    public static Block zinc_block;
    public static Block tungsten_block;

    public static Block sapphire_glass;
    public static Block reinforced_sapphire_glass;

    public static Block flax_crop;
    public static Block indigo_flower;

    public static Block alloyfurnace;
    public static Block block_breaker;
    public static Block igniter;
    public static Block buffer;
    public static Block deployer;
    public static Block transposer;
    public static Block sorting_machine;
    public static Block project_table;
    public static Block auto_project_table;
    public static Block circuit_table;
    public static Block circuit_database;
    public static Block ejector;
    public static Block relay;
    public static Block filter;
    public static Block retriever;
    public static Block regulator;
    public static Block item_detector;
    public static Block manager;

    // public static Block engine;
    // public static Block kinetic_generator;
    // public static Block windmill;

    // public static Block cpu;
    // public static Block monitor;
    // public static Block disk_drive;
    // public static Block io_expander;

    public static Block[] blockLamp;
    public static Block blockLampRGB;

    public static Block[] blockLampInverted;
    public static Block blockLampRGBInverted;

    public static Block sortron;

    public static Block solar_panel;
    public static Block battery;

    public static void init() {

        instantiateBlocks();
        registerBlocks();
        initModDependantBlocks();
    }

    private static void instantiateBlocks() {

        basalt = new BlockBasalt(Refs.BASALT_NAME).setResistance(25.0F);
        marble = new BlockStoneOre(Refs.MARBLE_NAME).setResistance(1.0F).setHardness(1.5F);
        basalt_cobble = new BlockStoneOre(Refs.BASALTCOBBLE_NAME);
        basalt_brick = new BlockStoneOre(Refs.BASALTBRICK_NAME);
        marble_brick = new BlockStoneOre(Refs.MARBLEBRICK_NAME);
        cracked_basalt_lava = new BlockCrackedBasalt(Refs.CRACKED_BASALT);

        basaltbrick_cracked = new BlockStoneOre(Refs.CRACKEDBASALTBRICK_NAME);
        basalt_brick_small = new BlockStoneOre(Refs.SMALLBASALTBRICK_NAME);
        marble_brick_small = new BlockStoneOre(Refs.SMALLMARBLEBRICK_NAME);
        fancy_basalt = new BlockStoneOre(Refs.CHISELEDBASALTBRICK_NAME);
        fancy_marble = new BlockStoneOre(Refs.CHISELEDMARBLEBRICK_NAME);
        marble_tile = new BlockStoneOreConnected(Refs.MARBLETILE_NAME);
        basalt_tile = new BlockStoneOreConnected(Refs.BASALTTILE_NAME);
        marble_paver = new BlockStoneOre(Refs.MARBLEPAVER_NAME);
        basalt_paver = new BlockStoneOre(Refs.BASALTPAVER_NAME);
        tiles = new BlockStoneOre(Refs.TILES);

        teslatite_ore = new BlockTeslatiteOre(Refs.TESLATITEORE_NAME);
        ruby_ore = new BlockRubyOre(Refs.RUBYORE_NAME);
        sapphire_ore = new BlockSapphireOre(Refs.SAPPHIREORE_NAME);
        amethyst_ore = new BlockAmethystOre(Refs.AMETHYSTORE_NAME);

        copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
        silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME).setToolLevel(2);
        zinc_ore = new BlockStoneOre(Refs.ZINCORE_NAME);
        tungsten_ore = new BlockStoneOre(Refs.TUNGSTENORE_NAME).setToolLevel(3).setResistance(6.0F).setHardness(15.0F);

        ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME).setToolLevel(2);
        sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME).setToolLevel(2);
        amethyst_block = new BlockStoneOre(Refs.AMETHYSTBLOCK_NAME).setToolLevel(2);
        teslatite_block = new BlockStoneOre(Refs.TESLATITEBLOCK_NAME).setToolLevel(2);
        copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
        silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME).setToolLevel(2);
        zinc_block = new BlockStoneOre(Refs.ZINCBLOCK_NAME);
        tungsten_block = new BlockStoneOre(Refs.TUNGSTENBLOCK_NAME).setToolLevel(3).setResistance(25.0F).setHardness(5.0F);

        sapphire_glass = new BlockStoneOreConnected(Refs.SAPPHIREGLASS_NAME).setTransparent(true).setHardness(10).setResistance(10000);
        reinforced_sapphire_glass = new BlockStoneOreConnected(Refs.REINFORCEDSAPPHIREGLASS_NAME).setTransparent(true).setWitherproof(true)
                .setTooltip(MinecraftColor.RED.getChatColor() + "Witherproof").setHardness(30).setResistance(Integer.MAX_VALUE);

        flax_crop = new BlockCrop().setBlockName(Refs.FLAXCROP_NAME);
        indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME);

        alloyfurnace = new BlockAlloyFurnace();
        block_breaker = new BlockContainerFrontRender(Material.rock, TileBlockBreaker.class).setBlockName(Refs.BLOCKBREAKER_NAME);
        igniter = new BlockIgniter();
        buffer = new BlockContainerBase(Material.rock, TileBuffer.class).setGuiId(GuiIDs.BUFFER).setBlockName(Refs.BLOCKBUFFER_NAME);
        deployer = new BlockContainerFrontRender(Material.rock, TileDeployer.class).setGuiId(GuiIDs.DEPLOYER_ID)
                .setBlockName(Refs.BLOCKDEPLOYER_NAME);
        transposer = new BlockContainerBase(Material.rock, TileTransposer.class).setBlockName(Refs.TRANSPOSER_NAME);
        sorting_machine = new BlockContainerBase(Material.rock, TileSortingMachine.class).setGuiId(GuiIDs.SORTING_MACHINE).setBlockName(
                Refs.SORTING_MACHINE_NAME);
        project_table = new BlockProjectTable().setGuiId(GuiIDs.PROJECTTABLE_ID);
        auto_project_table = new BlockProjectTable(TileAutoProjectTable.class).setGuiId(GuiIDs.PROJECTTABLE_ID).setBlockName(
                Refs.AUTOPROJECTTABLE_NAME);
        circuit_table = new BlockProjectTable(TileCircuitTable.class).setGuiId(GuiIDs.CIRCUITTABLE_ID).setBlockName(Refs.CIRCUITTABLE_NAME);
        circuit_database = new BlockCircuitDatabase(TileCircuitDatabase.class).setGuiId(GuiIDs.CIRCUITDATABASE_MAIN_ID).setBlockName(
                Refs.CIRCUITDATABASE_NAME);
        ejector = new BlockContainerTwoSideRender(Material.rock, TileEjector.class).setGuiId(GuiIDs.EJECTOR_ID).setBlockName(Refs.EJECTOR_NAME);
        relay = new BlockContainerTwoSideRender(Material.rock, TileRelay.class).setGuiId(GuiIDs.RELAY_ID).setBlockName(Refs.RELAY_NAME);
        filter = new BlockContainerBase(Material.rock, TileFilter.class).setGuiId(GuiIDs.FILTER_ID).setBlockName(Refs.FILTER_NAME);
        retriever = new BlockContainerBase(Material.rock, TileRetriever.class).setGuiId(GuiIDs.RETRIEVER_ID).setBlockName(Refs.RETRIEVER_NAME);
        regulator = new BlockContainerTwoSideRender(Material.rock, TileRegulator.class).setGuiId(GuiIDs.REGULATOR_ID).emitsRedstone()
                .setBlockName(Refs.REGULATOR_NAME);
        item_detector = new BlockContainerTwoSideRender(Material.rock, TileItemDetector.class).setGuiId(GuiIDs.ITEMDETECTOR_ID).emitsRedstone()
                .setBlockName(Refs.ITEMDETECTOR_NAME);
        manager = new BlockRejecting(Material.rock, TileManager.class).setGuiId(GuiIDs.MANAGER_ID).emitsRedstone().setBlockName(Refs.MANAGER_NAME);

        // engine = new BlockEngine();
        // kinetic_generator = new BlockKineticGenerator();
        // windmill = new BlockWindmill();

        // cpu = new BlockCPU();
        // monitor = new BlockMonitor();
        // disk_drive = new BlockDiskDrive();
        // io_expander = new BlockIOExpander();

        blockLamp = new Block[MinecraftColor.VALID_COLORS.length];
        blockLampRGB = new BlockLampRGB(false);

        blockLampInverted = new Block[MinecraftColor.VALID_COLORS.length];
        blockLampRGBInverted = new BlockLampRGB(true);

        solar_panel = new BlockSolarPanel();
        battery = new BlockBattery();

        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLamp[i] = new BlockLamp(false, MinecraftColor.VALID_COLORS[i]);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLampInverted[i] = new BlockLamp(true, MinecraftColor.VALID_COLORS[i]);
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

        GameRegistry.registerBlock(sapphire_glass, Refs.SAPPHIREGLASS_NAME);
        GameRegistry.registerBlock(reinforced_sapphire_glass, ItemBlockTooltip.class, Refs.REINFORCEDSAPPHIREGLASS_NAME);

        GameRegistry.registerBlock(flax_crop, Refs.FLAXCROP_NAME);
        GameRegistry.registerBlock(indigo_flower, Refs.INDIGOFLOWER_NAME);

        GameRegistry.registerBlock(alloyfurnace, Refs.ALLOYFURNACE_NAME);
        GameRegistry.registerBlock(sorting_machine, Refs.SORTING_MACHINE_NAME);
        GameRegistry.registerBlock(block_breaker, Refs.BLOCKBREAKER_NAME);
        GameRegistry.registerBlock(igniter, Refs.BLOCKIGNITER_NAME);
        GameRegistry.registerBlock(buffer, Refs.BLOCKBUFFER_NAME);
        GameRegistry.registerBlock(deployer, Refs.BLOCKDEPLOYER_NAME);
        GameRegistry.registerBlock(project_table, Refs.PROJECTTABLE_NAME);
        GameRegistry.registerBlock(auto_project_table, Refs.AUTOPROJECTTABLE_NAME);
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

        GameRegistry.registerBlock(solar_panel, Refs.SOLAR_PANEL_NAME);
        GameRegistry.registerBlock(battery, Refs.BATTERY_NAME);

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
        GameRegistry.registerBlock(blockLampRGB, Refs.LAMP_NAME + "RGB");
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++) {
            MinecraftColor color = MinecraftColor.VALID_COLORS[i];
            GameRegistry.registerBlock(blockLampInverted[i], Refs.LAMP_NAME + "inverted" + color.name().toLowerCase());
        }
        GameRegistry.registerBlock(blockLampRGBInverted, Refs.LAMP_NAME + "invertedRGB");

    }

    private static void initModDependantBlocks() {

        if (Loader.isModLoaded(Dependencies.COMPUTER_CRAFT) || Loader.isModLoaded(Dependencies.OPEN_COMPUTERS)) {
            sortron = new BlockSortron();
            GameRegistry.registerBlock(sortron, Refs.BLOCKSORTRON_NAME);
        }
    }
}
