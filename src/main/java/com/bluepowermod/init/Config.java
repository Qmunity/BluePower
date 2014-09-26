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

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.bluepowermod.BluePower;
import com.bluepowermod.part.tube.TubeStack;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class Config {

    public static boolean generateTungsten;
    public static int minTungstenY;
    public static int maxTungstenY;
    public static int veinCountTungsten;
    public static int veinSizeTungsten;
    public static boolean generateCopper;
    public static int minCopperY;
    public static int maxCopperY;
    public static int veinCountCopper;
    public static int veinSizeCopper;
    public static boolean generateSilver;
    public static int minSilverY;
    public static int maxSilverY;
    public static int veinCountSilver;
    public static int veinSizeSilver;
    public static boolean generateZinc;
    public static int minZincY;
    public static int maxZincY;
    public static int veinCountZinc;
    public static int veinSizeZinc;
    public static boolean generateTeslatite;
    public static int minTeslatiteY;
    public static int maxTeslatiteY;
    public static int veinCountTeslatite;
    public static int veinSizeTeslatite;
    public static boolean generateRuby;
    public static int minRubyY;
    public static int maxRubyY;
    public static int veinCountRuby;
    public static int veinSizeRuby;
    public static boolean generateAmethyst;
    public static int minAmethystY;
    public static int maxAmethystY;
    public static int veinCountAmethyst;
    public static int veinSizeAmethyst;
    public static boolean generateSapphire;
    public static int minSapphireY;
    public static int maxSapphireY;
    public static int veinCountSapphire;
    public static int veinSizeSapphire;
    public static double volcanoActiveToInactiveRatio;
    public static double volcanoSpawnChance; // chance of a volcano spawning per chunk.
    public static boolean useAltScrewdriverRecipe;
    public static int vorpalEnchantmentId;
    public static int disjunctionEnchantmentId;
    public static String[] alloyFurnaceBlacklist;

    public static boolean enableTubeCaching;
    public static boolean enableGateSounds;

    public static int veinSizeMarble;

    public static boolean serverCircuitSavingOpOnly;

    public static boolean convertLegacyPartsOnChunkLoad;

    public static void syncConfig(Configuration config) {

        config.addCustomCategoryComment(Refs.CONFIG_WORLDGEN, "Toggle blocks being generated into the world");
        generateTungsten = config.get(Refs.CONFIG_TUNGSTEN, "generateTungsten", true).getBoolean(true);
        minTungstenY = config.get(Refs.CONFIG_TUNGSTEN, "minTungstenY", 1).getInt();
        maxTungstenY = config.get(Refs.CONFIG_TUNGSTEN, "maxTungstenY", 10).getInt();
        veinCountTungsten = config.get(Refs.CONFIG_TUNGSTEN, "veinCountTungsten", 2).getInt();
        veinSizeTungsten = config.get(Refs.CONFIG_TUNGSTEN, "veinSizeTungsten", 3).getInt();
        generateCopper = config.get(Refs.CONFIG_COPPER, "generateCopper", true).getBoolean(true);
        minCopperY = config.get(Refs.CONFIG_COPPER, "minCopperY", 35).getInt();
        maxCopperY = config.get(Refs.CONFIG_COPPER, "maxCopperY", 90).getInt();
        veinCountCopper = config.get(Refs.CONFIG_COPPER, "veinCountCopper", 8).getInt();
        veinSizeCopper = config.get(Refs.CONFIG_COPPER, "veinSizeCopper", 7).getInt();
        generateZinc = config.get(Refs.CONFIG_ZINC, "generateZinc", true).getBoolean(true);
        minZincY = config.get(Refs.CONFIG_ZINC, "minZincY", 15).getInt();
        maxZincY = config.get(Refs.CONFIG_ZINC, "maxZincY", 40).getInt();
        veinCountZinc = config.get(Refs.CONFIG_ZINC, "veinCountZinc", 6).getInt();
        veinSizeZinc = config.get(Refs.CONFIG_ZINC, "veinSizeZinc", 6).getInt();
        generateSilver = config.get(Refs.CONFIG_SILVER, "generateSilver", true).getBoolean(true);
        minSilverY = config.get(Refs.CONFIG_SILVER, "minSilverY", 1).getInt();
        maxSilverY = config.get(Refs.CONFIG_SILVER, "maxSilverY", 20).getInt();
        veinCountSilver = config.get(Refs.CONFIG_SILVER, "veinCountSilver", 3).getInt();
        veinSizeSilver = config.get(Refs.CONFIG_SILVER, "veinSizeSilver", 6).getInt();
        generateTeslatite = config.get(Refs.CONFIG_TESLATITE, "generateTeslatite", true).getBoolean(true);
        minTeslatiteY = config.get(Refs.CONFIG_TESLATITE, "minTeslatiteY", 1).getInt();
        maxTeslatiteY = config.get(Refs.CONFIG_TESLATITE, "maxTeslatiteY", 20).getInt();
        veinCountTeslatite = config.get(Refs.CONFIG_TESLATITE, "veinCountTeslatite", 4).getInt();
        veinSizeTeslatite = config.get(Refs.CONFIG_TESLATITE, "veinSizeTeslatite", 8).getInt();
        generateRuby = config.get(Refs.CONFIG_RUBY, "generateRuby", true).getBoolean(true);
        minRubyY = config.get(Refs.CONFIG_RUBY, "minRubyY", 0).getInt();
        maxRubyY = config.get(Refs.CONFIG_RUBY, "maxRubyY", 48).getInt();
        veinCountRuby = config.get(Refs.CONFIG_RUBY, "veinCountRuby", 2).getInt();
        veinSizeRuby = config.get(Refs.CONFIG_RUBY, "veinSizeRuby", 5).getInt();
        generateAmethyst = config.get(Refs.CONFIG_AMETHYST, "generateAmethyst", true).getBoolean(true);
        minAmethystY = config.get(Refs.CONFIG_AMETHYST, "minAmethystY", 0).getInt();
        maxAmethystY = config.get(Refs.CONFIG_AMETHYST, "maxAmethystY", 48).getInt();
        veinCountAmethyst = config.get(Refs.CONFIG_AMETHYST, "veinCountAmethyst", 2).getInt();
        veinSizeAmethyst = config.get(Refs.CONFIG_AMETHYST, "veinSizeAmethyst", 5).getInt();
        generateSapphire = config.get(Refs.CONFIG_SAPPHIRE, "generateSapphire", true).getBoolean(true);
        minSapphireY = config.get(Refs.CONFIG_SAPPHIRE, "minSapphireY", 0).getInt();
        maxSapphireY = config.get(Refs.CONFIG_SAPPHIRE, "maxSapphireY", 48).getInt();
        veinCountSapphire = config.get(Refs.CONFIG_SAPPHIRE, "veinCountSapphire", 2).getInt();
        veinSizeSapphire = config.get(Refs.CONFIG_SAPPHIRE, "veinSizeSapphire", 5).getInt();
        volcanoSpawnChance = config.get(Refs.CONFIG_WORLDGEN, "volcanoSpawnChance", 0.02).getDouble(0);
        volcanoActiveToInactiveRatio = config.get(Refs.CONFIG_WORLDGEN, "volcanoActiveToInactiveRatio", 0.5).getDouble(0);
        useAltScrewdriverRecipe = config.get(Refs.CONFIG_SETTINGS, "useAltScrewdriverRecipe", false).getBoolean(false);
        veinSizeMarble = config.get(Refs.CONFIG_WORLDGEN, "veinSizeMarble", 2048).getInt();

        config.addCustomCategoryComment(Refs.CONFIG_RECIPES, "Toggle recipes to be enabled or not");
        alloyFurnaceBlacklist = config.get(Refs.CONFIG_RECIPES, "alloyFurnaceBlacklist", new String[0]).getStringList();

        Property prop = config.get(Refs.CONFIG_TUBES, "Enable Tube Caching", true);
        prop.comment = "When enabled, the Tube routing is more friendly for the CPU. In return it uses a bit more RAM. Caching also may contain bugs still.";
        enableTubeCaching = prop.getBoolean();

        prop = config.get(Refs.CONFIG_TUBES, "Tube Render Mode", "auto");
        prop.comment = "When encountering FPS issues with tubes with lots of items in it. Valid modes: \"normal\": Normal rendering, \"reduced\": All items going through tubes will display as 'one' item, \"none\": Only a small dot renders, \"auto\": will switch to \"normal\" on fancy graphics mode, and to \"reduced\" otherwise.";
        String tubeRenderMode = prop.getString();
        if (!tubeRenderMode.equals("normal") && !tubeRenderMode.equals("reduced") && !tubeRenderMode.equals("none")) {
            tubeRenderMode = "auto";
        }
        TubeStack.renderMode = TubeStack.RenderMode.valueOf(tubeRenderMode.toUpperCase());

        serverCircuitSavingOpOnly = config.get(Refs.CONFIG_CIRCUIT_DATABASE, "Server Template Saving by Ops only", false).getBoolean(false);

        config.addCustomCategoryComment(Refs.CONFIG_ENCHANTS, "Toggle enchantment ids");
        vorpalEnchantmentId = config.get(Refs.CONFIG_ENCHANTS, "vorpalEnchantmentId", 100).getInt();
        disjunctionEnchantmentId = config.get(Refs.CONFIG_ENCHANTS, "disjunctionEnchantmentId", 101).getInt();

        enableGateSounds = config.get(Refs.CONFIG_SETTINGS, "Enable Gate Ticking Sounds", true).getBoolean();

        convertLegacyPartsOnChunkLoad = config.getBoolean("Convert legacy parts on chunk load", Configuration.CATEGORY_GENERAL, true,
                "Set to false when getting 'concurrentModificationExceptions'.");

        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event) {

        if (event.modID.equals(Refs.MODID)) {
            syncConfig(BluePower.config);
        }
    }
}
