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

package net.quetzi.bluepower.init;

import net.minecraft.block.Block;
import net.minecraft.item.ItemDye;
import net.quetzi.bluepower.blocks.machines.BlockBuffer;
import net.quetzi.bluepower.blocks.BlockItemOre;
import net.quetzi.bluepower.blocks.computer.BlockCPU;
import net.quetzi.bluepower.blocks.computer.BlockDiskDrive;
import net.quetzi.bluepower.blocks.computer.BlockIOExpander;
import net.quetzi.bluepower.blocks.computer.BlockMonitor;
import net.quetzi.bluepower.blocks.machines.*;
import net.quetzi.bluepower.blocks.worldgen.BlockCrackedBasalt;
import net.quetzi.bluepower.blocks.worldgen.BlockCrop;
import net.quetzi.bluepower.blocks.worldgen.BlockCustomFlower;
import net.quetzi.bluepower.blocks.worldgen.BlockStoneOre;
import net.quetzi.bluepower.blocks.worldgen.BlockStoneOreConnected;
import net.quetzi.bluepower.part.lamp.PartLamp;
import net.quetzi.bluepower.references.Dependencies;
import net.quetzi.bluepower.references.Refs;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public class BPBlocks {

    public static Block basalt;
    public static Block marble;
    public static Block basalt_cobble;
    public static Block basalt_brick;
    public static Block marble_brick;
    public static Block cracked_basalt;

    public static Block basaltbrick_cracked;
    public static Block basalt_brick_small;
    public static Block marble_brick_small;
    public static Block fancy_basalt;
    public static Block fancy_marble;
    public static Block marble_tile;
    public static Block basalt_tile;
    public static Block marble_paver;
    public static Block basalt_paver;

    public static Block nikolite_ore;
    public static Block ruby_ore;
    public static Block sapphire_ore;
    public static Block amethyst_ore;

    public static Block copper_ore;
    public static Block silver_ore;
    public static Block tin_ore;
    public static Block tungsten_ore;

    public static Block ruby_block;
    public static Block sapphire_block;
    public static Block amethyst_block;
    public static Block nikolite_block;
    public static Block copper_block;
    public static Block silver_block;
    public static Block tin_block;
    public static Block tungsten_block;

    public static Block flax_crop;
    public static Block indigo_flower;

    public static Block alloy_furnace;
    public static Block block_breaker;
    public static Block igniter;
    public static Block buffer;
    public static Block Deployer;
    public static Block transposer;
    public static Block sorting_machine;
    public static Block sortron;
    public static Block project_table;
    public static Block ejector;
    public static Block relay;

    public static Block cpu;
    public static Block monitor;
    public static Block disk_drive;
    public static Block io_expander;

    public static Block multipart;          // DO NOT GENERATE OR REMOVE THIS BLOCK!

    public static Block engine;
    public static Block kinetic_generator;
    public static Block windmill;
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

        nikolite_ore = new BlockItemOre(Refs.NIKOLITEORE_NAME);
        ruby_ore = new BlockItemOre(Refs.RUBYORE_NAME);
        sapphire_ore = new BlockItemOre(Refs.SAPPHIREORE_NAME);
        amethyst_ore = new BlockItemOre(Refs.AMETHYSTORE_NAME);
        copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
        silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME);
        tin_ore = new BlockStoneOre(Refs.TINORE_NAME);
        tungsten_ore = new BlockStoneOre(Refs.TUNGSTENORE_NAME);

        ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME);
        sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME);
        amethyst_block = new BlockStoneOre(Refs.AMETHYSTBLOCK_NAME);
        nikolite_block = new BlockStoneOre(Refs.NIKOLITEBLOCK_NAME);
        copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
        silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME);
        tin_block = new BlockStoneOre(Refs.TINBLOCK_NAME);
        tungsten_block = new BlockStoneOre(Refs.TUNGSTENBLOCK_NAME);

        flax_crop = new BlockCrop().setBlockName(Refs.FLAXCROP_NAME);
        indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME);

        alloy_furnace = new BlockAlloyFurnace();
        sorting_machine = new BlockSortingMachine();
        block_breaker = new BlockBlockBreaker();
        igniter = new BlockIgniter();
        buffer = new BlockBuffer();
        Deployer = new BlockDeployer();
        project_table = new BlockProjectTable();
        transposer = new BlockTransposer();
        ejector = new BlockEjector();
        relay = new BlockRelay();

        cpu = new BlockCPU();
        monitor = new BlockMonitor();
        disk_drive = new BlockDiskDrive();
        io_expander = new BlockIOExpander();

        engine = new BlockEngine();
        kinetic_generator = new BlockKinectGenerator();
        windmill = new BlockWindmill();
        
        blockLamp = new Block[ItemDye.field_150922_c.length];
        blockLampInverted = new Block[ItemDye.field_150922_c.length];
        
        for (int i = 0; i < ItemDye.field_150922_c.length; i++){
            blockLamp[i] = new BlockLamp(false, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i]);
            //registerPart(PartLamp.class, ItemDye.field_150921_b[i].toLowerCase(), ItemDye.field_150922_c[i], false);
            
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

        GameRegistry.registerBlock(nikolite_ore, Refs.NIKOLITEORE_NAME);
        GameRegistry.registerBlock(copper_ore, Refs.COPPERORE_NAME);
        GameRegistry.registerBlock(silver_ore, Refs.SILVERORE_NAME);
        GameRegistry.registerBlock(tin_ore, Refs.TINORE_NAME);
        GameRegistry.registerBlock(tungsten_ore, Refs.TUNGSTENORE_NAME);
        GameRegistry.registerBlock(ruby_ore, Refs.RUBYORE_NAME);
        GameRegistry.registerBlock(sapphire_ore, Refs.SAPPHIREORE_NAME);
        GameRegistry.registerBlock(amethyst_ore, Refs.AMETHYSTORE_NAME);

        GameRegistry.registerBlock(ruby_block, Refs.RUBYBLOCK_NAME);
        GameRegistry.registerBlock(sapphire_block, Refs.SAPPHIREBLOCK_NAME);
        GameRegistry.registerBlock(amethyst_block, Refs.AMETHYSTBLOCK_NAME);
        GameRegistry.registerBlock(nikolite_block, Refs.NIKOLITEBLOCK_NAME);
        GameRegistry.registerBlock(copper_block, Refs.COPPERBLOCK_NAME);
        GameRegistry.registerBlock(silver_block, Refs.SILVERBLOCK_NAME);
        GameRegistry.registerBlock(tin_block, Refs.TINBLOCK_NAME);
        GameRegistry.registerBlock(tungsten_block, Refs.TUNGSTENBLOCK_NAME);

        GameRegistry.registerBlock(flax_crop, Refs.FLAXCROP_NAME);
        GameRegistry.registerBlock(indigo_flower, Refs.INDIGOFLOWER_NAME);

        GameRegistry.registerBlock(alloy_furnace, Refs.ALLOYFURNACE_NAME);
        GameRegistry.registerBlock(sorting_machine, Refs.SORTING_MACHINE_NAME);
        GameRegistry.registerBlock(block_breaker, Refs.BLOCKBREAKER_NAME);
        GameRegistry.registerBlock(igniter, Refs.BLOCKIGNITER_NAME);
        GameRegistry.registerBlock(buffer, Refs.BLOCKBUFFER_NAME);
        GameRegistry.registerBlock(Deployer, Refs.BLOCKDEPLOYER_NAME);
        GameRegistry.registerBlock(project_table, Refs.PROJECTTABLE_NAME);
        GameRegistry.registerBlock(transposer, Refs.TRANSPOSER_NAME);
        GameRegistry.registerBlock(ejector, Refs.EJECTOR_NAME);
        GameRegistry.registerBlock(relay, Refs.RELAY_NAME);

        GameRegistry.registerBlock(cpu, Refs.BLOCKCPU_NAME);
        GameRegistry.registerBlock(monitor, Refs.BLOCKMONITOR_NAME);
        GameRegistry.registerBlock(disk_drive, Refs.BLOCKDISKDRIVE_NAME);
        GameRegistry.registerBlock(io_expander, Refs.BLOCKIOEXPANDER_NAME);

        GameRegistry.registerBlock(engine, Refs.ENGINE_NAME);
        GameRegistry.registerBlock(kinetic_generator, Refs.KINECT_NAME);
        GameRegistry.registerBlock(windmill, Refs.WINDMILL_NAME);
        
        for (int i = 0; i < ItemDye.field_150922_c.length; i++){
            GameRegistry.registerBlock(blockLamp[i], blockLamp[i].getUnlocalizedName());
        }
    }

    private static void initModDependantBlocks() {

        if (Loader.isModLoaded(Dependencies.COMPUTER_CRAFT) || Loader.isModLoaded(Dependencies.OPEN_COMPUTERS)) {
            sortron = new BlockSortron();
            GameRegistry.registerBlock(sortron, Refs.BLOCKSORTRON_NAME);
        }
    }
}
