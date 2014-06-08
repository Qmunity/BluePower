package net.quetzi.bluepower.init;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.quetzi.bluepower.blocks.*;
import net.quetzi.bluepower.references.Refs;

public class BPBlocks
{

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

    public static Block ruby_block;
    public static Block sapphire_block;
    public static Block amethyst_block;
    public static Block nikolite_block;
    public static Block copper_block;
    public static Block silver_block;
    public static Block tin_block;

    public static Block flax_crop;
    public static Block indigo_flower;

    public static Block alloy_furnace;

    public static Block lamp_white;
    public static Block invertedlamp_white;
    public static Block lamp_orange;
    public static Block invertedlamp_orange;
    public static Block lamp_magenta;
    public static Block invertedlamp_magenta;
    public static Block lamp_lightblue;
    public static Block invertedlamp_lightblue;
    public static Block lamp_yellow;
    public static Block invertedlamp_yellow;
    public static Block lamp_lime;
    public static Block invertedlamp_lime;
    public static Block lamp_pink;
    public static Block invertedlamp_pink;
    public static Block lamp_gray;
    public static Block invertedlamp_gray;
    public static Block lamp_lightgray;
    public static Block invertedlamp_lightgray;
    public static Block lamp_cyan;
    public static Block invertedlamp_cyan;
    public static Block lamp_purple;
    public static Block invertedlamp_purple;
    public static Block lamp_blue;
    public static Block invertedlamp_blue;
    public static Block lamp_brown;
    public static Block invertedlamp_brown;
    public static Block lamp_green;
    public static Block invertedlamp_green;
    public static Block lamp_red;
    public static Block invertedlamp_red;
    public static Block lamp_black;
    public static Block invertedlamp_black;

    public static void init()
    {

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

        ruby_block = new BlockStoneOre(Refs.RUBYBLOCK_NAME);
        sapphire_block = new BlockStoneOre(Refs.SAPPHIREBLOCK_NAME);
        amethyst_block = new BlockStoneOre(Refs.AMETHYSTBLOCK_NAME);
        nikolite_block = new BlockStoneOre(Refs.NIKOLITEBLOCK_NAME);
        copper_block = new BlockStoneOre(Refs.COPPERBLOCK_NAME);
        silver_block = new BlockStoneOre(Refs.SILVERBLOCK_NAME);
        tin_block = new BlockStoneOre(Refs.TINBLOCK_NAME);

        flax_crop = new BlockCrop().setBlockName(Refs.FLAXCROP_NAME);
        indigo_flower = new BlockCustomFlower(Refs.INDIGOFLOWER_NAME);

        alloy_furnace = new BlockAlloyFurnace();

        lamp_white = new BlockCustomLamp(Refs.LAMP_WHITE, 0, false);
        invertedlamp_white = new BlockCustomLamp(Refs.INVERTEDLAMP_WHITE, 0, true);
        lamp_orange = new BlockCustomLamp(Refs.LAMP_ORANGE, 1, false);
        invertedlamp_orange = new BlockCustomLamp(Refs.INVERTEDLAMP_ORANGE, 1, true);
        lamp_magenta = new BlockCustomLamp(Refs.LAMP_MAGENTA, 2, false);
        invertedlamp_magenta = new BlockCustomLamp(Refs.INVERTEDLAMP_MAGENTA, 2, true);
        lamp_lightblue = new BlockCustomLamp(Refs.LAMP_LIGHTBLUE, 3, false);
        invertedlamp_lightblue = new BlockCustomLamp(Refs.INVERTEDLAMP_LIGHTBLUE, 3, true);
        lamp_yellow = new BlockCustomLamp(Refs.LAMP_YELLOW, 4, false);
        invertedlamp_yellow = new BlockCustomLamp(Refs.INVERTEDLAMP_YELLOW, 4, true);
        lamp_lime = new BlockCustomLamp(Refs.LAMP_LIME, 5, false);
        invertedlamp_lime = new BlockCustomLamp(Refs.INVERTEDLAMP_LIME, 5, true);
        lamp_pink = new BlockCustomLamp(Refs.LAMP_PINK, 6, false);
        invertedlamp_pink = new BlockCustomLamp(Refs.INVERTEDLAMP_PINK, 6, true);
        lamp_gray = new BlockCustomLamp(Refs.LAMP_GRAY, 7, false);
        invertedlamp_gray = new BlockCustomLamp(Refs.INVERTEDLAMP_GRAY, 7, true);
        lamp_lightgray = new BlockCustomLamp(Refs.LAMP_LIGHTGRAY, 8, false);
        invertedlamp_lightgray = new BlockCustomLamp(Refs.INVERTEDLAMP_LIGHTGRAY, 8, true);
        lamp_cyan = new BlockCustomLamp(Refs.LAMP_CYAN, 9, false);
        invertedlamp_cyan = new BlockCustomLamp(Refs.INVERTEDLAMP_CYAN, 9, true);
        lamp_purple = new BlockCustomLamp(Refs.LAMP_PURPLE, 10, false);
        invertedlamp_purple = new BlockCustomLamp(Refs.INVERTEDLAMP_PURPLE, 10, true);
        lamp_blue = new BlockCustomLamp(Refs.LAMP_BLUE, 11, false);
        invertedlamp_blue = new BlockCustomLamp(Refs.INVERTEDLAMP_BLUE, 11, true);
        lamp_brown = new BlockCustomLamp(Refs.LAMP_BROWN, 12, false);
        invertedlamp_brown = new BlockCustomLamp(Refs.INVERTEDLAMP_BROWN, 12, true);
        lamp_green = new BlockCustomLamp(Refs.LAMP_GREEN, 13, false);
        invertedlamp_green = new BlockCustomLamp(Refs.INVERTEDLAMP_GREEN, 13, true);
        lamp_red = new BlockCustomLamp(Refs.LAMP_RED, 14, false);
        invertedlamp_red = new BlockCustomLamp(Refs.INVERTEDLAMP_RED, 14, true);
        lamp_black = new BlockCustomLamp(Refs.LAMP_BLACK, 15, false);
        invertedlamp_black = new BlockCustomLamp(Refs.INVERTEDLAMP_BLACK, 15, true);

        registerBlocks();
    }

    private static void registerBlocks()
    {

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

        GameRegistry.registerBlock(flax_crop, Refs.FLAXCROP_NAME);
        GameRegistry.registerBlock(indigo_flower, Refs.INDIGOFLOWER_NAME);

        GameRegistry.registerBlock(alloy_furnace, Refs.ALLOYFURNACE_NAME);

        GameRegistry.registerBlock(lamp_white, Refs.LAMP_WHITE);
        GameRegistry.registerBlock(invertedlamp_white, Refs.INVERTEDLAMP_WHITE);
        GameRegistry.registerBlock(lamp_orange, Refs.LAMP_ORANGE);
        GameRegistry.registerBlock(invertedlamp_orange, Refs.INVERTEDLAMP_ORANGE);
        GameRegistry.registerBlock(lamp_magenta, Refs.LAMP_MAGENTA);
        GameRegistry.registerBlock(invertedlamp_magenta, Refs.INVERTEDLAMP_MAGENTA);
        GameRegistry.registerBlock(lamp_lightblue, Refs.LAMP_LIGHTBLUE);
        GameRegistry.registerBlock(invertedlamp_lightblue, Refs.INVERTEDLAMP_LIGHTBLUE);
        GameRegistry.registerBlock(lamp_yellow, Refs.LAMP_YELLOW);
        GameRegistry.registerBlock(invertedlamp_yellow, Refs.INVERTEDLAMP_YELLOW);
        GameRegistry.registerBlock(lamp_lime, Refs.LAMP_LIME);
        GameRegistry.registerBlock(invertedlamp_lime, Refs.INVERTEDLAMP_LIME);
        GameRegistry.registerBlock(lamp_pink, Refs.LAMP_PINK);
        GameRegistry.registerBlock(invertedlamp_pink, Refs.INVERTEDLAMP_PINK);
        GameRegistry.registerBlock(lamp_gray, Refs.LAMP_GRAY);
        GameRegistry.registerBlock(invertedlamp_gray, Refs.INVERTEDLAMP_GRAY);
        GameRegistry.registerBlock(lamp_lightgray, Refs.LAMP_LIGHTGRAY);
        GameRegistry.registerBlock(invertedlamp_lightgray, Refs.INVERTEDLAMP_LIGHTGRAY);
        GameRegistry.registerBlock(lamp_cyan, Refs.LAMP_CYAN);
        GameRegistry.registerBlock(invertedlamp_cyan, Refs.INVERTEDLAMP_CYAN);
        GameRegistry.registerBlock(lamp_purple, Refs.LAMP_PURPLE);
        GameRegistry.registerBlock(invertedlamp_purple, Refs.INVERTEDLAMP_PURPLE);
        GameRegistry.registerBlock(lamp_blue, Refs.LAMP_BLUE);
        GameRegistry.registerBlock(invertedlamp_blue, Refs.INVERTEDLAMP_BLUE);
        GameRegistry.registerBlock(lamp_brown, Refs.LAMP_BROWN);
        GameRegistry.registerBlock(invertedlamp_brown, Refs.INVERTEDLAMP_BROWN);
        GameRegistry.registerBlock(lamp_green, Refs.LAMP_GREEN);
        GameRegistry.registerBlock(invertedlamp_green, Refs.INVERTEDLAMP_GREEN);
        GameRegistry.registerBlock(lamp_red, Refs.LAMP_RED);
        GameRegistry.registerBlock(invertedlamp_red, Refs.INVERTEDLAMP_RED);
        GameRegistry.registerBlock(lamp_black, Refs.LAMP_BLACK);
        GameRegistry.registerBlock(invertedlamp_black, Refs.INVERTEDLAMP_BLACK);
    }
}
