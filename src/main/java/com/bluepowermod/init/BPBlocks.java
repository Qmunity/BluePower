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
import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.*;
import com.bluepowermod.block.gates.BlockGateBase;
import com.bluepowermod.block.lighting.BlockLampRGBSurface;
import com.bluepowermod.block.lighting.BlockLampSurface;
import com.bluepowermod.block.machine.*;
import com.bluepowermod.block.lighting.BlockLamp;
import com.bluepowermod.block.lighting.BlockLampRGB;
import com.bluepowermod.block.power.*;
import com.bluepowermod.block.worldgen.*;
import com.bluepowermod.item.ItemBPPart;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.*;
import com.bluepowermod.tile.tier2.*;
import com.bluepowermod.tile.tier3.TileCircuitDatabase;
import com.bluepowermod.tile.tier3.TileManager;
import com.bluepowermod.util.Dependencies;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.trees.OakTree;
import net.minecraft.item.Item;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = Refs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BPBlocks {

    public static List<Block> blockList = new ArrayList<>();
    public static List<Block> microblocks = new ArrayList<>();
    public static Block basalt;
    public static Block marble;
    public static Block basalt_cobble;
    public static Block basalt_brick;
    public static Block marble_brick;
    public static Block cracked_basalt_lava;
    public static Block cracked_basalt_decorative;

    public static Block half_block;
    public static Block panel;
    public static Block cover;

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
    public static Block green_sapphire_ore;

    public static Block ruby_block;
    public static Block sapphire_block;
    public static Block amethyst_block;
    public static Block teslatite_block;
    public static Block copper_block;
    public static Block silver_block;
    public static Block zinc_block;
    public static Block tungsten_block;
    public static Block green_sapphire_block;

    public static Block rubber_log;
    public static Block rubber_leaves;
    public static Block rubber_sapling;

    public static Block sapphire_glass;
    public static Block reinforced_sapphire_glass;

    public static Block flax_crop;
    public static BushBlock indigo_flower;

    public static Block alloyfurnace;
    public static Block block_breaker;
    public static Block igniter;
    public static Block buffer;
    public static Block deployer;
    public static Block transposer;
    public static Block tube;
    public static Block sorting_machine;
    public static Block project_table;
    public static Block auto_project_table;
    public static Block[] project_tables = new Block[2];
    public static Block circuit_table;
    public static Block circuit_database;
    public static Block ejector;
    public static Block relay;
    public static Block filter;
    public static Block retriever;
    public static Block regulator;
    public static Block item_detector;
    public static Block manager;
    public static Block battery;
    public static Block blulectric_cable;
    public static Block blulectric_alloyfurnace;
    public static Block blulectric_furnace;
    public static Block engine;
    public static Block kinetic_generator;
    public static Block windmill;
    public static Block solarpanel;
    public static Block thermopile;
    public static Block multipart;

    // public static Block cpu;
    // public static Block monitor;
    // public static Block disk_drive;
    // public static Block io_expander;

    public static List<Block> allLamps;

    public static Block[] blockLamp;
    public static Block[] blockLampInverted;
    public static Block[] cagedLamp;
    public static Block[] cagedLampInverted;
    public static Block[] fixedLamp;
    public static Block[] fixedLampInverted;

    public static Block blockLampRGB;
    public static Block blockLampRGBInverted;
    public static Block cagedLampRGB;
    public static Block cagedLampRGBInverted;
    public static Block fixedLampRGB;
    public static Block fixedLampRGBInverted;



    public static Block blockGateAND;
    public static Block blockGateNAND;

    public static Block blockRedAlloyWire;
    public static Block blockBlueAlloyWire;

    public static Block sortron;

    public static void init() {
        instantiateBlocks();
        initModDependantBlocks();
    }

    private static void instantiateBlocks() {

        basalt = new BlockBasalt(Refs.BASALT_NAME);
        marble = new BlockStoneOre(Refs.MARBLE_NAME);
        basalt_cobble = new BlockStoneOre(Refs.BASALTCOBBLE_NAME);
        basalt_brick = new BlockStoneOre(Refs.BASALTBRICK_NAME);
        marble_brick = new BlockStoneOre(Refs.MARBLEBRICK_NAME);
        cracked_basalt_lava = new BlockCrackedBasalt(Refs.CRACKED_BASALT_NAME);
        cracked_basalt_decorative = new BlockStoneOre(Refs.CRACKED_BASALT_DECOR_NAME);

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

        teslatite_ore = new BlockItemOre(Refs.TESLATITEORE_NAME);
        ruby_ore = new BlockItemOre(Refs.RUBYORE_NAME);
        sapphire_ore = new BlockItemOre(Refs.SAPPHIREORE_NAME);
        amethyst_ore = new BlockItemOre(Refs.AMETHYSTORE_NAME);
        green_sapphire_ore = new BlockItemOre(Refs.GREENSAPPHIREORE_NAME);

        copper_ore = new BlockStoneOre(Refs.COPPERORE_NAME);
        silver_ore = new BlockStoneOre(Refs.SILVERORE_NAME);
        zinc_ore = new BlockStoneOre(Refs.ZINCORE_NAME);
        tungsten_ore = new BlockStoneOre(Refs.TUNGSTENORE_NAME);

        ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME);
        sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME);
        amethyst_block = new BlockStoneOre(Refs.AMETHYSTBLOCK_NAME);
        teslatite_block = new BlockStoneOre(Refs.TESLATITEBLOCK_NAME);
        green_sapphire_block = new BlockStoneOre(Refs.GREENSAPPHIREBLOCK_NAME);
        copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
        silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME);
        zinc_block = new BlockStoneOre(Refs.ZINCBLOCK_NAME);
        tungsten_block = new BlockStoneOre(Refs.TUNGSTENBLOCK_NAME);

        multipart = new BlockBPMultipart();

        rubber_leaves = new BlockRubberLeaves(Block.Properties.of(Material.PLANT).noOcclusion());
        rubber_log = new BlockRubberLog(Block.Properties.of(Material.WOOD));
        rubber_sapling = new BlockRubberSapling(new OakTree(), Block.Properties.of(Material.PLANT));

        sapphire_glass = new BlockBPGlass(Refs.SAPPHIREGLASS_NAME);
        reinforced_sapphire_glass = new BlockBPGlass(Refs.REINFORCEDSAPPHIREGLASS_NAME, true);

        flax_crop = new BlockCrop(Block.Properties.of(Material.PLANT));
        indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME, Block.Properties.of(Material.PLANT));

        alloyfurnace = new BlockAlloyFurnace();
        block_breaker = new BlockContainerFacingBase(Material.STONE, TileBlockBreaker.class).setRegistryName(Refs.MODID, Refs.BLOCKBREAKER_NAME);
        igniter = new BlockIgniter();

        buffer = new BlockContainerHorizontalFacingBase(Material.STONE, TileBuffer.class){
            @Override
            protected boolean canRotateVertical(){return false;}
        }.setRegistryName(Refs.MODID, Refs.BLOCKBUFFER_NAME);

        deployer = new BlockContainerFacingBase(Material.STONE, TileDeployer.class)
                .setRegistryName(Refs.MODID, Refs.BLOCKDEPLOYER_NAME);
        transposer = new BlockContainerFacingBase(Material.STONE, TileTransposer.class).setRegistryName(Refs.MODID, Refs.TRANSPOSER_NAME);
        tube = new BlockTube().setRegistryName(Refs.MODID, Refs.TUBE_NAME);
        sorting_machine = new BlockContainerFacingBase(Material.STONE, TileSortingMachine.class).setWIP(true)
                .setRegistryName(Refs.MODID, Refs.SORTING_MACHINE_NAME);
        project_table = new BlockProjectTable();
        project_tables[0] = project_table;
        auto_project_table = new BlockProjectTable(TileAutoProjectTable.class).setRegistryName(Refs.MODID, Refs.AUTOPROJECTTABLE_NAME);
        project_tables[1] = auto_project_table;

        circuit_table = new BlockProjectTable(TileCircuitTable.class).setWIP(true).setRegistryName(Refs.MODID, Refs.CIRCUITTABLE_NAME);
        circuit_database = new BlockCircuitDatabase(TileCircuitDatabase.class).setWIP(true)
                .setRegistryName(Refs.MODID, Refs.CIRCUITDATABASE_NAME);
        ejector = new BlockContainerFacingBase(Material.STONE, TileEjector.class).setRegistryName(Refs.MODID, Refs.EJECTOR_NAME);
        relay = new BlockContainerFacingBase(Material.STONE, TileRelay.class).setWIP(true).setRegistryName(Refs.MODID, Refs.RELAY_NAME);
        filter = new BlockContainerFacingBase(Material.STONE, TileFilter.class).setWIP(true).setRegistryName(Refs.MODID, Refs.FILTER_NAME);
        retriever = new BlockContainerFacingBase(Material.STONE, TileRetriever.class).setWIP(true).setRegistryName(Refs.MODID, Refs.RETRIEVER_NAME);
        regulator = new BlockContainerFacingBase(Material.STONE, TileRegulator.class).emitsRedstone().setWIP(true)
                .setRegistryName(Refs.MODID, Refs.REGULATOR_NAME);
        item_detector = new BlockContainerFacingBase(Material.STONE, TileItemDetector.class).emitsRedstone().setWIP(true)
                .setRegistryName(Refs.MODID, Refs.ITEMDETECTOR_NAME);
        manager = new BlockRejecting(Material.STONE, TileManager.class).emitsRedstone().setWIP(true).setRegistryName(Refs.MODID, Refs.MANAGER_NAME);

        battery = new BlockBattery();
        blulectric_cable = new BlockBlulectricCable();
        blulectric_furnace = new BlockBlulectricFurnace();
        blulectric_alloyfurnace = new BlockBlulectricAlloyFurnace();
        engine = new BlockEngine();
        kinetic_generator = new BlockKineticGenerator().setWIP(true);
        //windmill = new BlockWindmill();
        solarpanel = new BlockSolarPanel();
        thermopile = new BlockThermopile().setWIP(true);

        half_block = new BlockBPMicroblock(Block.box(0,0,0,16,8,16)).setRegistryName(Refs.MODID + ":half_block");
        panel = new BlockBPMicroblock(Block.box(0,0,0,16,4,16)).setRegistryName(Refs.MODID + ":panel");
        cover = new BlockBPMicroblock(Block.box(0,0,0,16,2,16)).setRegistryName(Refs.MODID + ":cover");
        microblocks.add(half_block);
        microblocks.add(panel);
        microblocks.add(cover);

        // cpu = new BlockCPU();
        // monitor = new BlockMonitor();
        // disk_drive = new BlockDiskDrive();
        // io_expander = new BlockIOExpander();

        blockLamp = new Block[MinecraftColor.VALID_COLORS.length];
        blockLampInverted = new Block[MinecraftColor.VALID_COLORS.length];
        cagedLamp = new Block[MinecraftColor.VALID_COLORS.length];
        cagedLampInverted = new Block[MinecraftColor.VALID_COLORS.length];
        fixedLamp = new Block[MinecraftColor.VALID_COLORS.length];
        fixedLampInverted = new Block[MinecraftColor.VALID_COLORS.length];

        //Regular Lamp
        blockLampRGB = new BlockLampRGB(Refs.LAMP_NAME,false).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLamp[i] = new BlockLamp(Refs.LAMP_NAME, false, MinecraftColor.VALID_COLORS[i]);
        blockLampRGBInverted = new BlockLampRGB(Refs.LAMP_NAME,true).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            blockLampInverted[i] = new BlockLamp(Refs.LAMP_NAME, true, MinecraftColor.VALID_COLORS[i]);

        allLamps = new ArrayList<>();
        allLamps.addAll(Arrays.asList(blockLamp));
        allLamps.addAll(Arrays.asList(blockLampInverted));
        allLamps.add(blockLampRGB);
        allLamps.add(blockLampRGBInverted);

        //Cage Lamp
        cagedLampRGB = new BlockLampRGBSurface(Refs.CAGELAMP_NAME,false, Refs.CAGELAMP_AABB).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            cagedLamp[i] = new BlockLampSurface(Refs.CAGELAMP_NAME, false, MinecraftColor.VALID_COLORS[i], Refs.CAGELAMP_AABB);
        cagedLampRGBInverted = new BlockLampRGBSurface(Refs.CAGELAMP_NAME,true, Refs.CAGELAMP_AABB).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            cagedLampInverted[i] = new BlockLampSurface(Refs.CAGELAMP_NAME, true, MinecraftColor.VALID_COLORS[i], Refs.CAGELAMP_AABB);

        allLamps.addAll(Arrays.asList(cagedLamp));
        allLamps.addAll(Arrays.asList(cagedLampInverted));
        allLamps.add(cagedLampRGB);
        allLamps.add(cagedLampRGBInverted);

        //Fixture Lamp
        fixedLampRGB = new BlockLampRGBSurface(Refs.FIXTURELAMP_NAME,false, Refs.FIXTURELAMP_AABB).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            fixedLamp[i] = new BlockLampSurface(Refs.FIXTURELAMP_NAME, false, MinecraftColor.VALID_COLORS[i], Refs.FIXTURELAMP_AABB);
        fixedLampRGBInverted = new BlockLampRGBSurface(Refs.FIXTURELAMP_NAME,true, Refs.FIXTURELAMP_AABB).setWIP(true);
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++)
            fixedLampInverted[i] = new BlockLampSurface(Refs.FIXTURELAMP_NAME, true, MinecraftColor.VALID_COLORS[i], Refs.FIXTURELAMP_AABB);

        allLamps.addAll(Arrays.asList(fixedLamp));
        allLamps.addAll(Arrays.asList(fixedLampInverted));
        allLamps.add(fixedLampRGB);
        allLamps.add(fixedLampRGBInverted);

        //Gates
        blockGateAND = new BlockGateBase("gate_and").setRegistryName("bluepower:gate_and");
        blockGateNAND = new BlockGateBase("gate_nand"){
            @Override
            public byte computeRedstone(byte back, byte left, byte right){
                return (byte)((left == 0 || right == 0 || back == 0 ) ?  16 : 0);
            }
        }.setRegistryName("bluepower:gate_nand");

        //Wires
        blockBlueAlloyWire = new BlockAlloyWire(RedwireType.BLUESTONE.getName()).setWIP(true);
        blockRedAlloyWire =  new BlockAlloyWire(RedwireType.RED_ALLOY.getName()).setWIP(true);
        //blockInsulatedBlueAlloyWire = new BlockInsulatedAlloyWire(RedwireType.BLUESTONE.getName());
        //blockInsulatedRedAlloyWire = new BlockInsulatedAlloyWire(RedwireType.RED_ALLOY.getName());
    }

    private static void initModDependantBlocks() {
        if (ModList.get().isLoaded(Dependencies.COMPUTER_CRAFT) || ModList.get().isLoaded(Dependencies.OPEN_COMPUTERS)) {
            sortron = new BlockSortron();
        }
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        for(Block block : blockList) {
            event.getRegistry().register(block);

        }
    }

    @SubscribeEvent
    public static void registerBlockItems(RegistryEvent.Register<Item> event) {
        for (Block block : blockList) {
            if (block.getRegistryName() != null && !(block instanceof BlockCrop)) { // Crops have seeds rather than blocks
                if((block instanceof BlockBase && ((BlockBase)block).getWIP()) || block instanceof BlockBPMultipart || block instanceof BlockBPMicroblock || block instanceof BlockTube ){
                    if(block instanceof IBPPartBlock) {
                        event.getRegistry().register(new ItemBPPart(block, new Item.Properties()).setRegistryName(block.getRegistryName()));
                    }else{
                        event.getRegistry().register(new BlockItem(block, new Item.Properties()).setRegistryName(block.getRegistryName()));
                    }
                }else{
                    ItemGroup group = BPCreativeTabs.blocks;
                    if(block instanceof BlockContainerBase){group = BPCreativeTabs.machines;}
                    if(block instanceof BlockLamp){group = BPCreativeTabs.lighting;}
                    if(block instanceof BlockAlloyWire){group = BPCreativeTabs.wiring;}
                    if(block instanceof BlockBlulectricCable){group = BPCreativeTabs.wiring;}
                    if(block instanceof BlockGateBase){group = BPCreativeTabs.circuits;}
                    if(block instanceof IBPPartBlock){
                        event.getRegistry().register(new ItemBPPart(block, new Item.Properties().tab(group)).setRegistryName(block.getRegistryName()));
                    }else {
                        event.getRegistry().register(new BlockItem(block, new Item.Properties().tab(group)).setRegistryName(block.getRegistryName()));
                    }
                }
            }
        }
    }

}
