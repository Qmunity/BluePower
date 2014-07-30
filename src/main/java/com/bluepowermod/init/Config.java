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

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;
import com.bluepowermod.BluePower;
import com.bluepowermod.references.Refs;

public class Config {

    public static boolean generateTungsten;
    public static int     minTungstenY;
    public static int     maxTungstenY;
    public static int     veinCountTungsten;
    public static int     veinSizeTungsten;
    public static boolean generateCopper;
    public static int     minCopperY;
    public static int     maxCopperY;
    public static int     veinCountCopper;
    public static int     veinSizeCopper;
    public static boolean generateSilver;
    public static int     minSilverY;
    public static int     maxSilverY;
    public static int     veinCountSilver;
    public static int     veinSizeSilver;
    public static boolean generateZinc;
    public static int     minZincY;
    public static int     maxZincY;
    public static int     veinCountZinc;
    public static int     veinSizeZinc;
    public static boolean generateNikolite;
    public static int     minNikoliteY;
    public static int     maxNikoliteY;
    public static int     veinCountNikolite;
    public static int     veinSizeNikolite;
    public static boolean generateRuby;
    public static int     minRubyY;
    public static int     maxRubyY;
    public static int     veinCountRuby;
    public static int     veinSizeRuby;
    public static boolean generateAmethyst;
    public static int     minAmethystY;
    public static int     maxAmethystY;
    public static int     veinCountAmethyst;
    public static int     veinSizeAmethyst;
    public static boolean generateSapphire;
    public static int     minSapphireY;
    public static int     maxSapphireY;
    public static int     veinCountSapphire;
    public static int     veinSizeSapphire;
    public static double  volcanoActiveToInactiveRatio;
    public static double  volcanoSpawnChance;          // chance of a volcano spawning per chunk.
    public static boolean useAltScrewdriverRecipe;
    public static int     vorpalEnchantmentId;
    public static int     disjunctionEnchantmentId;

    public static String[] alloyFurnaceBlacklist;

    public static void syncConfig(Configuration config) {

        config.addCustomCategoryComment(Refs.CONFIG_WORLDGEN, "Toggle blocks being generated into the world");
        generateTungsten = config.get(Refs.CONFIG_TUNGSTEN, "generateTungsten", true).getBoolean(true);
        minTungstenY = config.get(Refs.CONFIG_TUNGSTEN, "minTungstenY", 5).getInt();
        maxTungstenY = config.get(Refs.CONFIG_TUNGSTEN, "maxTungstenY", 32).getInt();
        veinCountTungsten = config.get(Refs.CONFIG_TUNGSTEN, "veinCountTungsten", 2).getInt();
        veinSizeTungsten = config.get(Refs.CONFIG_TUNGSTEN, "veinSizeTungsten", 3).getInt();
        generateCopper = config.get(Refs.CONFIG_COPPER, "generateCopper", true).getBoolean(true);
        minCopperY = config.get(Refs.CONFIG_COPPER, "minCopperY", 0).getInt();
        maxCopperY = config.get(Refs.CONFIG_COPPER, "maxCopperY", 64).getInt();
        veinCountCopper = config.get(Refs.CONFIG_COPPER, "veinCountCopper", 20).getInt();
        veinSizeCopper = config.get(Refs.CONFIG_COPPER, "veinSizeCopper", 10).getInt();
        generateZinc = config.get(Refs.CONFIG_ZINC, "generateZinc", true).getBoolean(true);
        minZincY = config.get(Refs.CONFIG_ZINC, "minZincY", 0).getInt();
        maxZincY = config.get(Refs.CONFIG_ZINC, "maxZincY", 48).getInt();
        veinCountZinc = config.get(Refs.CONFIG_ZINC, "veinCountZinc", 10).getInt();
        veinSizeZinc = config.get(Refs.CONFIG_ZINC, "veinSizeZinc", 8).getInt();
        generateSilver = config.get(Refs.CONFIG_SILVER, "generateSilver", true).getBoolean(true);
        minSilverY = config.get(Refs.CONFIG_SILVER, "minSilverY", 0).getInt();
        maxSilverY = config.get(Refs.CONFIG_SILVER, "maxSilverY", 32).getInt();
        veinCountSilver = config.get(Refs.CONFIG_SILVER, "veinCountSilver", 4).getInt();
        veinSizeSilver = config.get(Refs.CONFIG_SILVER, "veinSizeSilver", 8).getInt();
        generateNikolite = config.get(Refs.CONFIG_NIKOLITE, "generateNikolite", true).getBoolean(true);
        minNikoliteY = config.get(Refs.CONFIG_NIKOLITE, "minNikoliteY", 0).getInt();
        maxNikoliteY = config.get(Refs.CONFIG_NIKOLITE, "maxNikoliteY", 16).getInt();
        veinCountNikolite = config.get(Refs.CONFIG_NIKOLITE, "veinCountNikolite", 4).getInt();
        veinSizeNikolite = config.get(Refs.CONFIG_NIKOLITE, "veinSizeNikolite", 10).getInt();
        generateRuby = config.get(Refs.CONFIG_RUBY, "generateRuby", true).getBoolean(true);
        minRubyY = config.get(Refs.CONFIG_RUBY, "minRubyY", 0).getInt();
        maxRubyY = config.get(Refs.CONFIG_RUBY, "maxRubyY", 48).getInt();
        veinCountRuby = config.get(Refs.CONFIG_RUBY, "veinCountRuby", 2).getInt();
        veinSizeRuby = config.get(Refs.CONFIG_RUBY, "veinSizeRuby", 7).getInt();
        generateAmethyst = config.get(Refs.CONFIG_AMETHYST, "generateAmethyst", true).getBoolean(true);
        minAmethystY = config.get(Refs.CONFIG_AMETHYST, "minAmethystY", 0).getInt();
        maxAmethystY = config.get(Refs.CONFIG_AMETHYST, "maxAmethystY", 48).getInt();
        veinCountAmethyst = config.get(Refs.CONFIG_AMETHYST, "veinCountAmethyst", 2).getInt();
        veinSizeAmethyst = config.get(Refs.CONFIG_AMETHYST, "veinSizeAmethyst", 7).getInt();
        generateSapphire = config.get(Refs.CONFIG_SAPPHIRE, "generateSapphire", true).getBoolean(true);
        minSapphireY = config.get(Refs.CONFIG_SAPPHIRE, "minSapphireY", 0).getInt();
        maxSapphireY = config.get(Refs.CONFIG_SAPPHIRE, "maxSapphireY", 48).getInt();
        veinCountSapphire = config.get(Refs.CONFIG_SAPPHIRE, "veinCountSapphire", 7).getInt();
        veinSizeSapphire = config.get(Refs.CONFIG_SAPPHIRE, "veinSizeSapphire", 2).getInt();
        volcanoSpawnChance = config.get(Refs.CONFIG_WORLDGEN, "volcanoSpawnChance", 0.02).getDouble(0);
        volcanoActiveToInactiveRatio = config.get(Refs.CONFIG_WORLDGEN, "volcanoActiveToInactiveRatio", 0.5).getDouble(0);
        useAltScrewdriverRecipe = config.get(Refs.CONFIG_SETTINGS, "useAltScrewdriverRecipe", false).getBoolean(false);
        
        config.addCustomCategoryComment(Refs.CONFIG_RECIPES, "Toggle recipes to be enabled or not");
        alloyFurnaceBlacklist = config.get(Refs.CONFIG_RECIPES, "alloyFurnaceBlacklist", new String[0]).getStringList();
        
        config.addCustomCategoryComment(Refs.CONFIG_ENCHANTS, "Toggle enchantment ids");
        vorpalEnchantmentId = config.get(Refs.CONFIG_ENCHANTS, "vorpalEnchantmentId", 100).getInt();
        disjunctionEnchantmentId = config.get(Refs.CONFIG_ENCHANTS, "disjunctionEnchantmentId", 101).getInt();

        if(config.hasChanged()) {
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
