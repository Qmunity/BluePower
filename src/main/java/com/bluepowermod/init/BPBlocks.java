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
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.*;
import com.bluepowermod.block.gates.BlockGateBase;
import com.bluepowermod.block.gates.BlockNullCell;
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
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.fml.ModList;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class BPBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, Refs.MODID);

    public static List<Block> blockList = new ArrayList<>();

    public static List<DeferredHolder<Block, Block>> regularBlocks = new ArrayList<>();

    //Register Regular Blocks
    public static final DeferredHolder<Block, Block> basalt = BLOCKS.register(Refs.BASALT_NAME, BlockBasalt::new);
    public static final DeferredHolder<Block, Block> marble = BLOCKS.register(Refs.MARBLE_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> basalt_cobble = BLOCKS.register(Refs.BASALTCOBBLE_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> basalt_brick = BLOCKS.register(Refs.BASALTBRICK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> marble_brick = BLOCKS.register(Refs.MARBLEBRICK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> cracked_basalt_lava = BLOCKS.register(Refs.CRACKED_BASALT_NAME, BlockCrackedBasalt::new);
    public static final DeferredHolder<Block, Block> cracked_basalt_decorative = BLOCKS.register(Refs.CRACKED_BASALT_DECOR_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> basaltbrick_cracked = BLOCKS.register(Refs.CRACKEDBASALTBRICK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> basalt_brick_small = BLOCKS.register(Refs.SMALLBASALTBRICK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> marble_brick_small = BLOCKS.register(Refs.SMALLMARBLEBRICK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> fancy_basalt = BLOCKS.register(Refs.CHISELEDBASALTBRICK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> fancy_marble = BLOCKS.register(Refs.CHISELEDMARBLEBRICK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> marble_paver = BLOCKS.register(Refs.MARBLEPAVER_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> basalt_paver = BLOCKS.register(Refs.BASALTPAVER_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> tiles = BLOCKS.register(Refs.TILES_NAME, () -> new BlockStoneOre());

    public static final DeferredHolder<Block, Block> marble_tile = BLOCKS.register(Refs.MARBLETILE_NAME, BlockStoneOreConnected::new);
    public static final DeferredHolder<Block, Block> basalt_tile = BLOCKS.register(Refs.BASALTTILE_NAME, BlockStoneOreConnected::new);

    public static final DeferredHolder<Block, Block> teslatite_ore = BLOCKS.register(Refs.TESLATITEORE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> ruby_ore = BLOCKS.register(Refs.RUBYORE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> sapphire_ore = BLOCKS.register(Refs.SAPPHIREORE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> amethyst_ore = BLOCKS.register(Refs.AMETHYSTORE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> green_sapphire_ore = BLOCKS.register(Refs.GREENSAPPHIREORE_NAME, BlockItemOre::new);

    public static final DeferredHolder<Block, Block> silver_ore = BLOCKS.register(Refs.SILVERORE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> zinc_ore = BLOCKS.register(Refs.ZINCORE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> tungsten_ore = BLOCKS.register(Refs.TUNGSTENORE_NAME, BlockItemOre::new);

    public static final DeferredHolder<Block, Block> teslatite_deepslate = BLOCKS.register(Refs.TESLATITEDEEPSLATE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> ruby_deepslate = BLOCKS.register(Refs.RUBYDEEPSLATE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> sapphire_deepslate = BLOCKS.register(Refs.SAPPHIREDEEPSLATE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> amethyst_deepslate = BLOCKS.register(Refs.AMETHYSTDEEPSLATE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> green_sapphire_deepslate = BLOCKS.register(Refs.GREENSAPPHIREDEEPSLATE_NAME, BlockItemOre::new);

    public static final DeferredHolder<Block, Block> silver_deepslate = BLOCKS.register(Refs.SILVERDEEPSLATE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> zinc_deepslate = BLOCKS.register(Refs.ZINCDEEPSLATE_NAME, BlockItemOre::new);
    public static final DeferredHolder<Block, Block> tungsten_deepslate = BLOCKS.register(Refs.TUNGSTENDEEPSLATE_NAME, BlockItemOre::new);

    public static final DeferredHolder<Block, Block> ruby_block = BLOCKS.register(Refs.RUBYBLOCK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> sapphire_block = BLOCKS.register(Refs.SAPPHIREBLOCK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> amethyst_block = BLOCKS.register(Refs.AMETHYSTBLOCK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> teslatite_block = BLOCKS.register(Refs.TESLATITEBLOCK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> silver_block = BLOCKS.register(Refs.SILVERBLOCK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> zinc_block = BLOCKS.register(Refs.ZINCBLOCK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> tungsten_block = BLOCKS.register(Refs.TUNGSTENBLOCK_NAME, () -> new BlockStoneOre());
    public static final DeferredHolder<Block, Block> green_sapphire_block = BLOCKS.register(Refs.GREENSAPPHIREBLOCK_NAME, () -> new BlockStoneOre());

    public static final DeferredHolder<Block, Block> rubber_log = BLOCKS.register(Refs.RUBBERLOG_NAME, () -> new BlockRubberLog(Block.Properties.of()));
    public static final DeferredHolder<Block, Block> rubber_leaves = BLOCKS.register(Refs.RUBBERLEAVES_NAME, () ->  new BlockRubberLeaves(Block.Properties.of().strength(0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion()));
    public static final DeferredHolder<Block, Block> rubber_sapling = BLOCKS.register(Refs.RUBBERSAPLING_NAME, () -> new BlockRubberSapling(Block.Properties.of().sound(SoundType.GRASS)));

    public static final DeferredHolder<Block, Block> sapphire_glass = BLOCKS.register(Refs.SAPPHIREGLASS_NAME, () -> new BlockBPGlass());
    public static final DeferredHolder<Block, Block> reinforced_sapphire_glass = BLOCKS.register(Refs.REINFORCEDSAPPHIREGLASS_NAME, () -> new BlockBPGlass(true));

    //Register Items for Regular Blocks
    static {
        regularBlocks.add(basalt);
        regularBlocks.add(marble);
        regularBlocks.add(basalt_cobble);
        regularBlocks.add(basalt_brick);
        regularBlocks.add(marble_brick);
        regularBlocks.add(cracked_basalt_lava);
        regularBlocks.add(cracked_basalt_decorative);
        regularBlocks.add(basaltbrick_cracked);
        regularBlocks.add(basalt_brick_small);
        regularBlocks.add(marble_brick_small);
        regularBlocks.add(fancy_basalt);
        regularBlocks.add(fancy_marble);
        regularBlocks.add(marble_paver);
        regularBlocks.add(basalt_paver);
        regularBlocks.add(tiles);
        regularBlocks.add(marble_tile);
        regularBlocks.add(basalt_tile);

        regularBlocks.add(teslatite_ore);
        regularBlocks.add(ruby_ore);
        regularBlocks.add(sapphire_ore);
        regularBlocks.add(amethyst_ore);
        regularBlocks.add(green_sapphire_ore);
        regularBlocks.add(silver_ore);
        regularBlocks.add(zinc_ore);
        regularBlocks.add(tungsten_ore);

        regularBlocks.add(teslatite_deepslate);
        regularBlocks.add(ruby_deepslate);
        regularBlocks.add(sapphire_deepslate);
        regularBlocks.add(amethyst_deepslate);
        regularBlocks.add(green_sapphire_deepslate);
        regularBlocks.add(silver_deepslate);
        regularBlocks.add(zinc_deepslate);
        regularBlocks.add(tungsten_deepslate);

        regularBlocks.add(ruby_block);
        regularBlocks.add(sapphire_block);
        regularBlocks.add(amethyst_block);
        regularBlocks.add(teslatite_block);
        regularBlocks.add(silver_block);
        regularBlocks.add(zinc_block);
        regularBlocks.add(tungsten_block);
        regularBlocks.add(green_sapphire_block);

        regularBlocks.add(rubber_sapling);
        regularBlocks.add(rubber_leaves);
        regularBlocks.add(rubber_log);

        regularBlocks.add(sapphire_glass);
        regularBlocks.add(reinforced_sapphire_glass);

        for(DeferredHolder<Block, Block> block : regularBlocks){
            BPItems.ITEMS.register(block.getKey().location().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        }

    }

    public static final DeferredHolder<Block, Block> flax_crop = BLOCKS.register(Refs.FLAXCROP_NAME, () -> new BlockCrop(Block.Properties.of()));
    public static final DeferredHolder<Block, BushBlock> indigo_flower = BLOCKS.register(Refs.INDIGOFLOWER_NAME, () -> new BlockCustomFlower(Block.Properties.of()));
    static {BPItems.ITEMS.register(Refs.INDIGOFLOWER_NAME, () -> new BlockItem(indigo_flower.get(), new Item.Properties()));}

    public static List<DeferredHolder<Block, Block>> machines = new ArrayList<>();

    public static final DeferredHolder<Block, Block> alloyfurnace = BLOCKS.register(Refs.ALLOYFURNACE_NAME, BlockAlloyFurnace::new);
    public static final DeferredHolder<Block, Block> block_breaker = BLOCKS.register(Refs.BLOCKBREAKER_NAME, () -> new BlockContainerFacingBase(TileBlockBreaker.class));
    public static final DeferredHolder<Block, Block> igniter = BLOCKS.register(Refs.BLOCKIGNITER_NAME, BlockIgniter::new);
    public static final DeferredHolder<Block, Block> buffer = BLOCKS.register(Refs.BLOCKBUFFER_NAME, () -> new BlockContainerHorizontalFacingBase(TileBuffer.class){
        @Override
        protected boolean canRotateVertical(){return false;}
    });

    public static final DeferredHolder<Block, Block> deployer = BLOCKS.register(Refs.BLOCKDEPLOYER_NAME, () -> new BlockContainerFacingBase(TileDeployer.class));
    public static final DeferredHolder<Block, Block> transposer = BLOCKS.register(Refs.TRANSPOSER_NAME, () -> new BlockContainerFacingBase(TileTransposer.class));
    public static final DeferredHolder<Block, Block> tube = BLOCKS.register(Refs.TUBE_NAME, () -> new BlockTube(BlockBehaviour.Properties.of().noOcclusion()));
    public static final DeferredHolder<Block, Block> sorting_machine = BLOCKS.register(Refs.SORTING_MACHINE_NAME, () -> new BlockContainerFacingBase(TileSortingMachine.class).setWIP(true));
    public static final DeferredHolder<Block, Block> project_table = BLOCKS.register(Refs.PROJECTTABLE_NAME, () -> new BlockProjectTable());
    public static final DeferredHolder<Block, Block> auto_project_table = BLOCKS.register(Refs.AUTOPROJECTTABLE_NAME, () -> new BlockProjectTable(TileAutoProjectTable.class));
    public static final DeferredHolder<Block, Block> circuit_table = BLOCKS.register(Refs.CIRCUITTABLE_NAME, () -> new BlockProjectTable(TileCircuitTable.class).setWIP(true));
    public static final DeferredHolder<Block, Block> circuit_database = BLOCKS.register(Refs.CIRCUITDATABASE_NAME, () -> new BlockCircuitDatabase(TileCircuitDatabase.class).setWIP(true));
    public static final DeferredHolder<Block, Block> ejector = BLOCKS.register(Refs.EJECTOR_NAME, () -> new BlockContainerFacingBase(TileEjector.class));
    public static final DeferredHolder<Block, Block> relay = BLOCKS.register(Refs.RELAY_NAME, () -> new BlockContainerFacingBase(TileRelay.class).setWIP(true));
    public static final DeferredHolder<Block, Block> filter = BLOCKS.register(Refs.FILTER_NAME, () -> new BlockContainerFacingBase(TileFilter.class).setWIP(true));
    public static final DeferredHolder<Block, Block> retriever = BLOCKS.register(Refs.RETRIEVER_NAME, () -> new BlockContainerFacingBase(TileRetriever.class).setWIP(true));
    public static final DeferredHolder<Block, Block> regulator = BLOCKS.register(Refs.REGULATOR_NAME, () -> new BlockContainerFacingBase(TileRegulator.class).emitsRedstone().setWIP(true));
    public static final DeferredHolder<Block, Block> item_detector = BLOCKS.register(Refs.ITEMDETECTOR_NAME, () -> new BlockContainerFacingBase(TileItemDetector.class).emitsRedstone().setWIP(true));
    public static final DeferredHolder<Block, Block> manager = BLOCKS.register(Refs.MANAGER_NAME,() -> new BlockRejecting(TileManager.class).emitsRedstone().setWIP(true));
    public static final DeferredHolder<Block, Block> battery = BLOCKS.register(Refs.BATTERYBLOCK_NAME, BlockBattery::new);
    public static final DeferredHolder<Block, Block> blulectric_alloyfurnace = BLOCKS.register(Refs.BLULECTRICALLOYFURNACE_NAME, BlockBlulectricAlloyFurnace::new);
    public static final DeferredHolder<Block, Block> blulectric_furnace = BLOCKS.register(Refs.BLULECTRICFURNACE_NAME, BlockBlulectricFurnace::new);
    public static final DeferredHolder<Block, Block> engine = BLOCKS.register(Refs.ENGINE_NAME, BlockEngine::new);
    public static final DeferredHolder<Block, Block> kinetic_generator = BLOCKS.register(Refs.KINETICGENERATOR_NAME, () -> new BlockKineticGenerator().setWIP(true));
    //public static final DeferredHolder<Block> windmill = BLOCKS.register(Refs.WINDMILL_NAME, () -> new BlockWindmill().setWIP(true));
    public static final DeferredHolder<Block, Block> solarpanel = BLOCKS.register(Refs.SOLARPANEL_NAME, BlockSolarPanel::new);
    public static final DeferredHolder<Block, Block> thermopile = BLOCKS.register(Refs.THERMOPILE_NAME, () -> new BlockThermopile().setWIP(true));

    //Register Machine Items
    static {
        machines.add(project_table);
        machines.add(auto_project_table);
        machines.add(alloyfurnace);
        machines.add(block_breaker);
        machines.add(igniter);
        machines.add(buffer);
        machines.add(deployer);
        machines.add(transposer);
        machines.add(ejector);
        machines.add(battery);
        machines.add(blulectric_alloyfurnace);
        machines.add(blulectric_furnace);
        machines.add(engine);
        machines.add(solarpanel);
        machines.add(tube);

        for(DeferredHolder<Block, Block> block : machines){
            BPItems.ITEMS.register(block.getKey().location().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        }

    }

    public static final DeferredHolder<Block, Block> blulectric_cable = BLOCKS.register(Refs.BLULECTRICCABLE_NAME, BlockBlulectricCable::new);
    static{BPItems.ITEMS.register(blulectric_cable.getKey().location().getPath(), () -> new ItemBPPart(blulectric_cable.get(), new Item.Properties()));}

    public static final DeferredHolder<Block, Block> multipart = BLOCKS.register(Refs.MULTIPART_NAME, BlockBPMultipart::new);

    public static List<DeferredHolder<Block, Block>> microblocks = new ArrayList<>();

    public static final DeferredHolder<Block, Block> half_block = BLOCKS.register("half_block", () -> new BlockBPMicroblock(Block.box(0,0,0,16,8,16)));
    public static final DeferredHolder<Block, Block> panel = BLOCKS.register("panel", () -> new BlockBPMicroblock(Block.box(0,0,0,16,4,16)));
    public static final DeferredHolder<Block, Block> cover = BLOCKS.register("cover", () -> new BlockBPMicroblock(Block.box(0,0,0,16,2,16)));

    //  public static final DeferredHolder<Block> cpu;
    //  public static final DeferredHolder<Block> monitor;
    //  public static final DeferredHolder<Block> disk_drive;
    //  public static final DeferredHolder<Block> io_expander;

    public static List<DeferredHolder<Block, Block>> allLamps = new ArrayList<>();

    public static List<DeferredHolder<Block, Block>> blockLamp = new ArrayList<>();
    public static List<DeferredHolder<Block, Block>> blockLampInverted = new ArrayList<>();
    public static List<DeferredHolder<Block, Block>> cagedLamp = new ArrayList<>();
    public static List<DeferredHolder<Block, Block>> cagedLampInverted = new ArrayList<>();
    public static List<DeferredHolder<Block, Block>> fixedLamp = new ArrayList<>();
    public static List<DeferredHolder<Block, Block>> fixedLampInverted = new ArrayList<>();

     public static final DeferredHolder<Block, Block> blockLampRGB = BLOCKS.register(Refs.LAMP_NAME + "_rgb", () -> new BlockLampRGB(false).setWIP(true));
     public static final DeferredHolder<Block, Block> blockLampRGBInverted = BLOCKS.register(Refs.LAMP_NAME + "inverted_rgb", () -> new BlockLampRGB(true).setWIP(true));
     public static final DeferredHolder<Block, Block> cagedLampRGB = BLOCKS.register(Refs.CAGELAMP_NAME + "_rgb", () -> new BlockLampRGBSurface(false, Refs.CAGELAMP_AABB).setWIP(true));
     public static final DeferredHolder<Block, Block> cagedLampRGBInverted = BLOCKS.register(Refs.CAGELAMP_NAME + "inverted_rgb", () -> new BlockLampRGBSurface(true, Refs.CAGELAMP_AABB).setWIP(true));
     public static final DeferredHolder<Block, Block> fixedLampRGB = BLOCKS.register(Refs.FIXTURELAMP_NAME + "_rgb", () -> new BlockLampRGBSurface(false, Refs.FIXTURELAMP_AABB).setWIP(true));
     public static final DeferredHolder<Block, Block> fixedLampRGBInverted = BLOCKS.register(Refs.FIXTURELAMP_NAME + "inverted_rgb", () -> new BlockLampRGBSurface(true, Refs.FIXTURELAMP_AABB).setWIP(true));



    public static final DeferredHolder<Block, Block> blockGateAND = BLOCKS.register("gate_and", BlockGateBase::new);
    public static final DeferredHolder<Block, Block> blockNullCell = BLOCKS.register("gate_nullcell", BlockNullCell::new);
     public static final DeferredHolder<Block, Block> blockGateNAND = BLOCKS.register("gate_nand",() -> new BlockGateBase(){
         @Override
         public byte computeRedstone(BlockGateBase.Side side, byte back, byte front, byte left, byte right){
             return (byte)((left == 0 || right == 0 || back == 0 ) ?  16 : 0);
         }
     });

    static{
        BPItems.ITEMS.register(blockGateAND.getKey().location().getPath(), () -> new BlockItem(blockGateAND.get(), new Item.Properties()));
        BPItems.ITEMS.register(blockGateNAND.getKey().location().getPath(), () -> new BlockItem(blockGateNAND.get(), new Item.Properties()));
        BPItems.ITEMS.register(blockNullCell.getKey().location().getPath(), () -> new BlockItem(blockNullCell.get(), new Item.Properties()));
    }

     public static final DeferredHolder<Block, Block> blockRedAlloyWire = BLOCKS.register(RedwireType.RED_ALLOY.getName() + "_wire", () -> new BlockAlloyWire(RedwireType.RED_ALLOY.getName()).setWIP(true));
     public static final DeferredHolder<Block, Block> blockBlueAlloyWire = BLOCKS.register( RedwireType.BLUESTONE.getName() + "_wire", () -> new BlockAlloyWire(RedwireType.BLUESTONE.getName()).setWIP(true));

     public static DeferredHolder<Block, Block> sortron;

    static {
        BPItems.ITEMS.register(blockRedAlloyWire.getKey().location().getPath(), () -> new BlockItem(blockRedAlloyWire.get(), new Item.Properties()));
        BPItems.ITEMS.register(blockBlueAlloyWire.getKey().location().getPath(), () -> new BlockItem(blockBlueAlloyWire.get(), new Item.Properties()));

        microblocks.add(half_block);
        microblocks.add(panel);
        microblocks.add(cover);

        //Register Microblock Items
        for(DeferredHolder<Block, Block> block : microblocks){
            BPItems.ITEMS.register(block.getKey().location().getPath(), () -> new ItemBPPart(block.get(), new Item.Properties()));
        }


        // cpu = new BlockCPU();
        // monitor = new BlockMonitor();
        // disk_drive = new BlockDiskDrive();
        // io_expander = new BlockIOExpander();

        //Regular Lamp
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++) {
            int color = i;
            blockLamp.add(BLOCKS.register(Refs.LAMP_NAME + "_" + MinecraftColor.VALID_COLORS[i].name().toLowerCase(), () -> new BlockLamp( false, MinecraftColor.VALID_COLORS[color])));
        }
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            int color = i;
            blockLampInverted.add(BLOCKS.register(Refs.LAMP_NAME + "inverted_" + MinecraftColor.VALID_COLORS[i].name().toLowerCase(), () -> new BlockLamp( true, MinecraftColor.VALID_COLORS[color])));
        }

        allLamps.addAll(blockLamp);
        allLamps.addAll(blockLampInverted);
        allLamps.add(blockLampRGB);
        allLamps.add(blockLampRGBInverted);

        //Cage Lamp
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            int color = i;
            cagedLamp.add(BLOCKS.register(Refs.CAGELAMP_NAME + "_" + MinecraftColor.VALID_COLORS[i].name().toLowerCase(), () -> new BlockLampSurface(false, MinecraftColor.VALID_COLORS[color], Refs.CAGELAMP_AABB)));
        }
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            int color = i;
            cagedLampInverted.add(BLOCKS.register(Refs.CAGELAMP_NAME + "inverted_" + MinecraftColor.VALID_COLORS[i].name().toLowerCase(), () -> new BlockLampSurface(true, MinecraftColor.VALID_COLORS[color], Refs.CAGELAMP_AABB)));
        }

        allLamps.addAll(cagedLamp);
        allLamps.addAll(cagedLampInverted);
        allLamps.add(cagedLampRGB);
        allLamps.add(cagedLampRGBInverted);

        //Fixture Lamp
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            int color = i;
            fixedLamp.add(BLOCKS.register(Refs.FIXTURELAMP_NAME + "_" + MinecraftColor.VALID_COLORS[i].name().toLowerCase(), () -> new BlockLampSurface(false, MinecraftColor.VALID_COLORS[color], Refs.FIXTURELAMP_AABB)));
        }
        for (int i = 0; i < MinecraftColor.VALID_COLORS.length; i++){
            int color = i;
            fixedLampInverted.add(BLOCKS.register(Refs.FIXTURELAMP_NAME + "inverted_" + MinecraftColor.VALID_COLORS[i].name().toLowerCase(), () -> new BlockLampSurface(true, MinecraftColor.VALID_COLORS[color], Refs.FIXTURELAMP_AABB)));
        }

        allLamps.addAll(fixedLamp);
        allLamps.addAll(fixedLampInverted);
        allLamps.add(fixedLampRGB);
        allLamps.add(fixedLampRGBInverted);

        //Register Lamp Items
        for(DeferredHolder<Block, Block> block : allLamps){
            BPItems.ITEMS.register(block.getKey().location().getPath(), () -> new BlockItem(block.get(), new Item.Properties()));
        }

        //Wires
        //blockInsulatedBlueAlloyWire = new BlockInsulatedAlloyWire(RedwireType.BLUESTONE.getName());
        //blockInsulatedRedAlloyWire = new BlockInsulatedAlloyWire(RedwireType.RED_ALLOY.getName());

        //Init Mod Dependant Blocks
        if (ModList.get().isLoaded(Dependencies.COMPUTER_CRAFT) || ModList.get().isLoaded(Dependencies.OPEN_COMPUTERS)) {
            sortron = BLOCKS.register(Refs.BLOCKSORTRON_NAME, BlockSortron::new);
        }
    }

}
