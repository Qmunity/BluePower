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

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.block.machine.*;
import com.bluepowermod.block.worldgen.*;
import com.bluepowermod.reference.GuiIDs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.*;
import com.bluepowermod.tile.tier2.*;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import com.bluepowermod.tile.tier3.TileManager;
import com.bluepowermod.util.Dependencies;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.registry.GameRegistry;


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
    public static BlockBush indigo_flower;

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

        flax_crop = new BlockCrop().setRegistryName(Refs.FLAXCROP_NAME);
        indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME);

        alloyfurnace = new BlockAlloyFurnace();
        block_breaker = new BlockContainerFrontRender(Material.ROCK, TileBlockBreaker.class).setBlockName(Refs.BLOCKBREAKER_NAME);
        igniter = new BlockIgniter();
        buffer = new BlockContainerBase(Material.ROCK, TileBuffer.class).setGuiId(GuiIDs.BUFFER).setBlockName(Refs.BLOCKBUFFER_NAME);
        deployer = new BlockContainerFrontRender(Material.ROCK, TileDeployer.class).setGuiId(GuiIDs.DEPLOYER_ID)
                .setBlockName(Refs.BLOCKDEPLOYER_NAME);
        transposer = new BlockContainerBase(Material.ROCK, TileTransposer.class).setBlockName(Refs.TRANSPOSER_NAME);
        sorting_machine = new BlockContainerBase(Material.ROCK, TileSortingMachine.class).setGuiId(GuiIDs.SORTING_MACHINE).setBlockName(
                Refs.SORTING_MACHINE_NAME);
        project_table = new BlockProjectTable().setGuiId(GuiIDs.PROJECTTABLE_ID);
        auto_project_table = new BlockProjectTable(TileAutoProjectTable.class).setGuiId(GuiIDs.PROJECTTABLE_ID).setBlockName(
                Refs.AUTOPROJECTTABLE_NAME);
        circuit_table = new BlockProjectTable(TileCircuitTable.class).setGuiId(GuiIDs.CIRCUITTABLE_ID).setBlockName(Refs.CIRCUITTABLE_NAME);
        circuit_database = new BlockCircuitDatabase(TileCircuitDatabase.class).setGuiId(GuiIDs.CIRCUITDATABASE_MAIN_ID).setBlockName(
                Refs.CIRCUITDATABASE_NAME);
        ejector = new BlockContainerTwoSideRender(Material.ROCK, TileEjector.class).setGuiId(GuiIDs.EJECTOR_ID).setBlockName(Refs.EJECTOR_NAME);
        relay = new BlockContainerTwoSideRender(Material.ROCK, TileRelay.class).setGuiId(GuiIDs.RELAY_ID).setBlockName(Refs.RELAY_NAME);
        filter = new BlockContainerBase(Material.ROCK, TileFilter.class).setGuiId(GuiIDs.FILTER_ID).setBlockName(Refs.FILTER_NAME);
        retriever = new BlockContainerBase(Material.ROCK, TileRetriever.class).setGuiId(GuiIDs.RETRIEVER_ID).setBlockName(Refs.RETRIEVER_NAME);
        regulator = new BlockContainerTwoSideRender(Material.ROCK, TileRegulator.class).setGuiId(GuiIDs.REGULATOR_ID).emitsRedstone()
                .setBlockName(Refs.REGULATOR_NAME);
        item_detector = new BlockContainerTwoSideRender(Material.ROCK, TileItemDetector.class).setGuiId(GuiIDs.ITEMDETECTOR_ID).emitsRedstone()
                .setBlockName(Refs.ITEMDETECTOR_NAME);
        manager = new BlockRejecting(Material.ROCK, TileManager.class).setGuiId(GuiIDs.MANAGER_ID).emitsRedstone().setBlockName(Refs.MANAGER_NAME);

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

        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLamp[i] = new BlockLamp(false, MinecraftColor.VALID_COLORS[i]);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLampInverted[i] = new BlockLamp(true, MinecraftColor.VALID_COLORS[i]);
    }

    private static void registerBlocks() {

        GameRegistry.register(basalt.setRegistryName(Refs.BASALT_NAME));
        GameRegistry.register(basalt_cobble.setRegistryName(Refs.BASALTCOBBLE_NAME));
        GameRegistry.register(basalt_brick.setRegistryName(Refs.BASALTBRICK_NAME));
        GameRegistry.register(basaltbrick_cracked.setRegistryName(Refs.CRACKEDBASALTBRICK_NAME));
        GameRegistry.register(fancy_basalt.setRegistryName(Refs.CHISELEDBASALTBRICK_NAME));
        GameRegistry.register(basalt_brick_small.setRegistryName(Refs.SMALLBASALTBRICK_NAME));
        GameRegistry.register(cracked_basalt_lava.setRegistryName(Refs.CRACKED_BASALT));
        GameRegistry.register(basalt_tile.setRegistryName(Refs.BASALTTILE_NAME));
        GameRegistry.register(basalt_paver.setRegistryName(Refs.BASALTPAVER_NAME));

        GameRegistry.register(marble.setRegistryName(Refs.MARBLE_NAME));
        GameRegistry.register(marble_brick.setRegistryName(Refs.MARBLEBRICK_NAME));
        GameRegistry.register(fancy_marble.setRegistryName(Refs.CHISELEDMARBLEBRICK_NAME));
        GameRegistry.register(marble_brick_small.setRegistryName(Refs.SMALLMARBLEBRICK_NAME));
        GameRegistry.register(marble_tile.setRegistryName(Refs.MARBLETILE_NAME));
        GameRegistry.register(marble_paver.setRegistryName(Refs.MARBLEPAVER_NAME));
        GameRegistry.register(tiles.setRegistryName(Refs.TILES_NAME));

        GameRegistry.register(teslatite_ore.setRegistryName(Refs.TESLATITEORE_NAME));
        GameRegistry.register(copper_ore.setRegistryName(Refs.COPPERORE_NAME));
        GameRegistry.register(silver_ore.setRegistryName(Refs.SILVERORE_NAME));
        GameRegistry.register(zinc_ore.setRegistryName(Refs.ZINCORE_NAME));
        GameRegistry.register(tungsten_ore.setRegistryName(Refs.TUNGSTENORE_NAME));
        GameRegistry.register(ruby_ore.setRegistryName(Refs.RUBYORE_NAME));
        GameRegistry.register(sapphire_ore.setRegistryName(Refs.SAPPHIREORE_NAME));
        GameRegistry.register(amethyst_ore.setRegistryName(Refs.AMETHYSTORE_NAME));

        GameRegistry.register(ruby_block.setRegistryName(Refs.RUBYBLOCK_NAME));
        GameRegistry.register(sapphire_block.setRegistryName(Refs.SAPPHIREBLOCK_NAME));
        GameRegistry.register(amethyst_block.setRegistryName(Refs.AMETHYSTBLOCK_NAME));
        GameRegistry.register(teslatite_block.setRegistryName(Refs.TESLATITEBLOCK_NAME));
        GameRegistry.register(copper_block.setRegistryName(Refs.COPPERBLOCK_NAME));
        GameRegistry.register(silver_block.setRegistryName(Refs.SILVERBLOCK_NAME));
        GameRegistry.register(zinc_block.setRegistryName(Refs.ZINCBLOCK_NAME));
        GameRegistry.register(tungsten_block.setRegistryName(Refs.TUNGSTENBLOCK_NAME));

        GameRegistry.register(sapphire_glass.setRegistryName(Refs.SAPPHIREGLASS_NAME));
        GameRegistry.register(reinforced_sapphire_glass.setRegistryName(Refs.REINFORCEDSAPPHIREGLASS_NAME));

        GameRegistry.register(flax_crop.setRegistryName(Refs.FLAXCROP_NAME));
        GameRegistry.register(indigo_flower.setRegistryName(Refs.INDIGOFLOWER_NAME));

        GameRegistry.register(alloyfurnace.setRegistryName(Refs.ALLOYFURNACE_NAME));
        GameRegistry.register(sorting_machine.setRegistryName(Refs.SORTING_MACHINE_NAME));
        GameRegistry.register(block_breaker.setRegistryName(Refs.BLOCKBREAKER_NAME));
        GameRegistry.register(igniter.setRegistryName(Refs.BLOCKIGNITER_NAME));
        GameRegistry.register(buffer.setRegistryName(Refs.BLOCKBUFFER_NAME));
        GameRegistry.register(deployer.setRegistryName(Refs.BLOCKDEPLOYER_NAME));
        GameRegistry.register(project_table.setRegistryName(Refs.PROJECTTABLE_NAME));
        GameRegistry.register(auto_project_table.setRegistryName(Refs.AUTOPROJECTTABLE_NAME));
        GameRegistry.register(circuit_table.setRegistryName(Refs.CIRCUITTABLE_NAME));
        GameRegistry.register(circuit_database.setRegistryName(Refs.CIRCUITDATABASE_NAME));
        GameRegistry.register(transposer.setRegistryName(Refs.TRANSPOSER_NAME));
        GameRegistry.register(ejector.setRegistryName(Refs.EJECTOR_NAME));
        GameRegistry.register(relay.setRegistryName(Refs.RELAY_NAME));
        GameRegistry.register(filter.setRegistryName(Refs.FILTER_NAME));
        GameRegistry.register(retriever.setRegistryName(Refs.RETRIEVER_NAME));
        GameRegistry.register(regulator.setRegistryName(Refs.REGULATOR_NAME));
        GameRegistry.register(item_detector.setRegistryName(Refs.ITEMDETECTOR_NAME));
        GameRegistry.register(manager.setRegistryName(Refs.MANAGER_NAME));

        /*
         * GameRegistry.register(cpu.setRegistryName(Refs.BLOCKCPU_NAME); GameRegistry.register(monitor.setRegistryName(Refs.BLOCKMONITOR_NAME);
         * GameRegistry.register(disk_drive.setRegistryName(Refs.BLOCKDISKDRIVE_NAME); GameRegistry.register(io_expander.setRegistryName(Refs.BLOCKIOEXPANDER_NAME);
         * 
         * GameRegistry.register(engine.setRegistryName(Refs.ENGINE_NAME); GameRegistry.register(kinetic_generator.setRegistryName(Refs.KINETICGENERATOR_NAME);
         * GameRegistry.register(windmill.setRegistryName(Refs.WINDMILL_NAME);
         */

        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++) {
            MinecraftColor color = MinecraftColor.VALID_COLORS[i];
            GameRegistry.register(blockLamp[i].setRegistryName(Refs.LAMP_NAME + color.name().toLowerCase()));
        }
        GameRegistry.register(blockLampRGB.setRegistryName(Refs.LAMP_NAME + "RGB"));
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++) {
            MinecraftColor color = MinecraftColor.VALID_COLORS[i];
            GameRegistry.register(blockLampInverted[i].setRegistryName(Refs.LAMP_NAME + "inverted" + color.name().toLowerCase()));
        }
        GameRegistry.register(blockLampRGBInverted.setRegistryName(Refs.LAMP_NAME + "invertedRGB"));

    }

    private static void initModDependantBlocks() {

        if (Loader.isModLoaded(Dependencies.COMPUTER_CRAFT) || Loader.isModLoaded(Dependencies.OPEN_COMPUTERS)) {
            sortron = new BlockSortron();
            GameRegistry.register(sortron.setRegistryName(Refs.BLOCKSORTRON_NAME));
        }
    }
}
