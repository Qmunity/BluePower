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
import net.minecraft.item.ItemDye;

import com.bluepowermod.blocks.BPBlockMultipart;
import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.blocks.BlockItemOre;
import com.bluepowermod.blocks.machines.BlockAlloyFurnace;
import com.bluepowermod.blocks.machines.BlockContainerFrontRender;
import com.bluepowermod.blocks.machines.BlockContainerTwoSideRender;
import com.bluepowermod.blocks.machines.BlockIgniter;
import com.bluepowermod.blocks.machines.BlockLamp;
import com.bluepowermod.blocks.machines.BlockProjectTable;
import com.bluepowermod.blocks.machines.BlockSortron;
import com.bluepowermod.blocks.worldgen.BlockCrackedBasalt;
import com.bluepowermod.blocks.worldgen.BlockCrop;
import com.bluepowermod.blocks.worldgen.BlockCustomFlower;
import com.bluepowermod.blocks.worldgen.BlockStoneOre;
import com.bluepowermod.blocks.worldgen.BlockStoneOreConnected;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.tileentities.tier1.TileBlockBreaker;
import com.bluepowermod.tileentities.tier1.TileBuffer;
import com.bluepowermod.tileentities.tier1.TileDeployer;
import com.bluepowermod.tileentities.tier1.TileEjector;
import com.bluepowermod.tileentities.tier1.TileFilter;
import com.bluepowermod.tileentities.tier1.TileItemDetector;
import com.bluepowermod.tileentities.tier1.TileRelay;
import com.bluepowermod.tileentities.tier1.TileTransposer;
import com.bluepowermod.tileentities.tier2.TileRegulator;
import com.bluepowermod.tileentities.tier2.TileRetriever;
import com.bluepowermod.tileentities.tier2.TileSortingMachine;
import com.bluepowermod.util.Dependencies;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

@GameRegistry.ObjectHolder(Refs.MODID)
public class BPBlocks {

    public static Block   basalt;
    public static Block   marble;
    public static Block   basalt_cobble;
    public static Block   basalt_brick;
    public static Block   marble_brick;
    public static Block   cracked_basalt;

    public static Block   basaltbrick_cracked;
    public static Block   basalt_brick_small;
    public static Block   marble_brick_small;
    public static Block   fancy_basalt;
    public static Block   fancy_marble;
    public static Block   marble_tile;
    public static Block   basalt_tile;
    public static Block   marble_paver;
    public static Block   basalt_paver;

    public static Block   teslatite_ore;
    public static Block   ruby_ore;
    public static Block   sapphire_ore;
    public static Block   amethyst_ore;

    public static Block   copper_ore;
    public static Block   silver_ore;
    public static Block   zinc_ore;
    public static Block   tungsten_ore;

    public static Block   ruby_block;
    public static Block   sapphire_block;
    public static Block   amethyst_block;
    public static Block   teslatite_block;
    public static Block   copper_block;
    public static Block   silver_block;
    public static Block   zinc_block;
    public static Block   tungsten_block;

    public static Block   flax_crop;
    public static Block   indigo_flower;

    public static Block   alloy_furnace;
    public static Block   block_breaker;
    public static Block   igniter;
    public static Block   buffer;
    public static Block   deployer;
    public static Block   transposer;
    public static Block   sorting_machine;
    public static Block   sortron;
    public static Block   project_table;
    public static Block   ejector;
    public static Block   relay;
    public static Block   filter;
    public static Block   retriever;
    public static Block   regulator;
    public static Block   item_detector;

    public static Block   cpu;
    public static Block   monitor;
    public static Block   disk_drive;
    public static Block   io_expander;
    
    public static Block   multipart;
    
    public static Block   engine;
    public static Block   kinetic_generator;
    public static Block   windmill;
    public static Block[] blockLamp;
    public static Block[] blockLampInverted;

    public static void init() {

        basalt = new BlockStoneOre(Refs.BASALT_NAME);
        marble = new BlockStoneOre(Refs.MARBLE_NAME);
        basalt_cobble = new BlockStoneOre(Refs.BASALTCOBBLE_NAME);
        basalt_brick = new BlockStoneOre(Refs.BASALTBRICK_NAME);
        marble_brick = new BlockStoneOre(Refs.MARBLEBRICK_NAME);
        cracked_basalt = new BlockCrackedBasalt(Refs.CRACKED_BASALT);
        basaltbrick_cracked = new BlockStoneOre(Refs.CRACKEDBASALTBRICK_NAME);
        basalt_brick_small = new BlockStoneOre(Refs.SMALLBASALTBRICK_NAME);
        marble_brick_small = new BlockStoneOre(Refs.SMALLMARBLEBRICK_NAME);
        fancy_basalt = new BlockStoneOre(Refs.CHISELEDBASALTBRICK_NAME);
        fancy_marble = new BlockStoneOre(Refs.CHISELEDMARBLEBRICK_NAME);
        marble_tile = new BlockStoneOreConnected(Refs.MARBLETILE_NAME);
        basalt_tile = new BlockStoneOreConnected(Refs.BASALTTILE_NAME);
        marble_paver = new BlockStoneOre(Refs.MARBLEPAVER_NAME);
        basalt_paver = new BlockStoneOre(Refs.BASALTPAVER_NAME);

        teslatite_ore = new BlockItemOre(Refs.TESLATITEORE_NAME);
        ruby_ore = new BlockItemOre(Refs.RUBYORE_NAME);
        sapphire_ore = new BlockItemOre(Refs.SAPPHIREORE_NAME);
        amethyst_ore = new BlockItemOre(Refs.AMETHYSTORE_NAME);
        copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
        silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME);
        zinc_ore = new BlockStoneOre(Refs.ZINCORE_NAME);
        tungsten_ore = new BlockStoneOre(Refs.TUNGSTENORE_NAME);

        ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME);
        sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME);
        amethyst_block = new BlockStoneOre(Refs.AMETHYSTBLOCK_NAME);
        teslatite_block = new BlockStoneOre(Refs.TESLATITEBLOCK_NAME);
        copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
        silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME);
        zinc_block = new BlockStoneOre(Refs.ZINCBLOCK_NAME);
        tungsten_block = new BlockStoneOre(Refs.TUNGSTENBLOCK_NAME);

        flax_crop = new BlockCrop().setBlockName(Refs.FLAXCROP_NAME);
        indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME);

        alloy_furnace = new BlockAlloyFurnace();
        sorting_machine = new BlockContainerBase(Material.rock, TileSortingMachine.class).setGuiId(GuiIDs.SORTING_MACHINE).setBlockName(Refs.SORTING_MACHINE_NAME);
        block_breaker = new BlockContainerFrontRender(Material.rock, TileBlockBreaker.class).setBlockName(Refs.BLOCKBREAKER_NAME);
        igniter = new BlockIgniter();
        buffer = new BlockContainerBase(Material.rock, TileBuffer.class).setGuiId(GuiIDs.BUFFER).setBlockName(Refs.BLOCKBUFFER_NAME);
        deployer = new BlockContainerFrontRender(Material.rock, TileDeployer.class).setGuiId(GuiIDs.DEPLOYER_ID).setBlockName(Refs.BLOCKDEPLOYER_NAME);
        project_table = new BlockProjectTable().setGuiId(GuiIDs.PROJECTTABLE_ID);

        transposer = new BlockContainerBase(Material.rock, TileTransposer.class).setBlockName(Refs.TRANSPOSER_NAME);
        ejector = new BlockContainerTwoSideRender(Material.rock, TileEjector.class).setGuiId(GuiIDs.EJECTOR_ID).setBlockName(Refs.EJECTOR_NAME);
        relay = new BlockContainerTwoSideRender(Material.rock, TileRelay.class).setGuiId(GuiIDs.RELAY_ID).setBlockName(Refs.RELAY_NAME);
        filter = new BlockContainerBase(Material.rock, TileFilter.class).setGuiId(GuiIDs.FILTER_ID).setBlockName(Refs.FILTER_NAME);
        retriever = new BlockContainerBase(Material.rock, TileRetriever.class).setGuiId(GuiIDs.RETRIEVER_ID).setBlockName(Refs.RETRIEVER_NAME);
        regulator = new BlockContainerTwoSideRender(Material.rock, TileRegulator.class).setGuiId(GuiIDs.REGULATOR_ID).emitsRedstone().setBlockName(Refs.REGULATOR_NAME);
        item_detector = new BlockContainerTwoSideRender(Material.rock, TileItemDetector.class).setGuiId(GuiIDs.ITEMDETECTOR_ID).emitsRedstone().setBlockName(Refs.ITEMDETECTOR_NAME);
        
        /*cpu = new BlockCPU();
        monitor = new BlockMonitor();
        disk_drive = new BlockDiskDrive();
        io_expander = new BlockIOExpander();

        engine = new BlockEngine();
        kinetic_generator = new BlockKineticGenerator();
        windmill = new BlockWindmill();*/

        blockLamp = new Block[ItemDye.field_150922_c.length];
        blockLampInverted = new Block[ItemDye.field_150922_c.length];

        for (int i = 0; i < ItemDye.field_150922_c.length; i++) {
            blockLamp[i] = new BlockLamp(false, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i]);
        }
        for (int i = 0; i < ItemDye.field_150922_c.length; i++) {
            blockLampInverted[i] = new BlockLamp(true, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i]);
        }

        if (!Loader.isModLoaded(Dependencies.FMP)) {
            multipart = new BPBlockMultipart();
        }

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
        GameRegistry.registerBlock(cracked_basalt, Refs.CRACKED_BASALT);
        GameRegistry.registerBlock(basalt_tile, Refs.BASALTTILE_NAME);
        GameRegistry.registerBlock(basalt_paver, Refs.BASALTPAVER_NAME);

        GameRegistry.registerBlock(marble, Refs.MARBLE_NAME);
        GameRegistry.registerBlock(marble_brick, Refs.MARBLEBRICK_NAME);
        GameRegistry.registerBlock(fancy_marble, Refs.CHISELEDMARBLEBRICK_NAME);
        GameRegistry.registerBlock(marble_brick_small, Refs.SMALLMARBLEBRICK_NAME);
        GameRegistry.registerBlock(marble_tile, Refs.MARBLETILE_NAME);
        GameRegistry.registerBlock(marble_paver, Refs.MARBLEPAVER_NAME);

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

        GameRegistry.registerBlock(alloy_furnace, Refs.ALLOYFURNACE_NAME);
        GameRegistry.registerBlock(sorting_machine, Refs.SORTING_MACHINE_NAME);
        GameRegistry.registerBlock(block_breaker, Refs.BLOCKBREAKER_NAME);
        GameRegistry.registerBlock(igniter, Refs.BLOCKIGNITER_NAME);
        GameRegistry.registerBlock(buffer, Refs.BLOCKBUFFER_NAME);
        GameRegistry.registerBlock(deployer, Refs.BLOCKDEPLOYER_NAME);
        GameRegistry.registerBlock(project_table, Refs.PROJECTTABLE_NAME);
        GameRegistry.registerBlock(transposer, Refs.TRANSPOSER_NAME);
        GameRegistry.registerBlock(ejector, Refs.EJECTOR_NAME);
        GameRegistry.registerBlock(relay, Refs.RELAY_NAME);
        GameRegistry.registerBlock(filter, Refs.FILTER_NAME);
        GameRegistry.registerBlock(retriever, Refs.RETRIEVER_NAME);
        GameRegistry.registerBlock(regulator, Refs.REGULATOR_NAME);
        GameRegistry.registerBlock(item_detector, Refs.ITEMDETECTOR_NAME);
        
        /*GameRegistry.registerBlock(cpu, Refs.BLOCKCPU_NAME);
        GameRegistry.registerBlock(monitor, Refs.BLOCKMONITOR_NAME);
        GameRegistry.registerBlock(disk_drive, Refs.BLOCKDISKDRIVE_NAME);
        GameRegistry.registerBlock(io_expander, Refs.BLOCKIOEXPANDER_NAME);

        GameRegistry.registerBlock(engine, Refs.ENGINE_NAME);
        GameRegistry.registerBlock(kinetic_generator, Refs.KINETICGENERATOR_NAME);
        GameRegistry.registerBlock(windmill, Refs.WINDMILL_NAME);*/

        for (int i = 0; i < ItemDye.field_150922_c.length; i++) {
            GameRegistry.registerBlock(blockLamp[i], blockLamp[i].getUnlocalizedName().substring(blockLamp[i].getUnlocalizedName().indexOf(":") + 1));
        }
        for (int i = 0; i < ItemDye.field_150922_c.length; i++) {
            GameRegistry.registerBlock(blockLampInverted[i], blockLampInverted[i].getUnlocalizedName().substring(blockLampInverted[i].getUnlocalizedName().indexOf(":") + 1));
        }

        if (!Loader.isModLoaded(Dependencies.FMP)) {
            GameRegistry.registerBlock(multipart, Refs.MULTIPART_BLOCK_NAME);
        }
    }

    private static void initModDependantBlocks() {

        if (Loader.isModLoaded(Dependencies.COMPUTER_CRAFT) || Loader.isModLoaded(Dependencies.OPEN_COMPUTERS)) {
            sortron = new BlockSortron();
            GameRegistry.registerBlock(sortron, Refs.BLOCKSORTRON_NAME);
        }
    }
}
