package net.quetzi.bluepower.init;

import net.minecraftforge.common.config.Configuration;

public class Config {
    public static void setUp(Configuration config) {
        config.addCustomCategoryComment("World Gen", "Toggle blocks being generated into the world");
        generateCopper = config.get("World Gen", "generateCopper", true).getBoolean(true);
        minCopperY = config.get("World Gen", "minCopperY", 0).getInt();
        maxCopperY = config.get("World Gen", "maxCopperY", 64).getInt();
        veinCountCopper = config.get("World Gen", "veinCountCopper", 20).getInt();
        veinSizeCopper = config.get("World Gen", "veinSizeCopper", 10).getInt();
        generateTin = config.get("World Gen", "generateTin", true).getBoolean(true);
        minTinY = config.get("World Gen", "minTinY", 0).getInt();
        maxTinY = config.get("World Gen", "maxTinY", 48).getInt();
        veinCountTin = config.get("World Gen", "veinCountTin", 10).getInt();
        veinSizeTin = config.get("World Gen", "veinSizeTin", 8).getInt();
        generateSilver = config.get("World Gen", "generateSilver", true).getBoolean(true);
        minSilverY = config.get("World Gen", "minSilverY", 0).getInt();
        maxSilverY = config.get("World Gen", "maxSilverY", 32).getInt();
        veinCountSilver = config.get("World Gen", "veinCountSilver", 4).getInt();
        veinSizeSilver = config.get("World Gen", "veinSizeSilver", 8).getInt();
        generateNikolite = config.get("World Gen", "generateNikolite", true).getBoolean(true);
        minNikoliteY = config.get("World Gen", "minNikoliteY", 0).getInt();
        maxNikoliteY = config.get("World Gen", "maxNikoliteY", 16).getInt();
        veinCountNikolite = config.get("World Gen", "veinCountNikolite", 4).getInt();
        veinSizeNikolite = config.get("World Gen", "veinSizeNikolite", 10).getInt();
        generateRuby = config.get("World Gen", "generateRuby", true).getBoolean(true);
        minRubyY = config.get("World Gen", "minRubyY", 0).getInt();
        maxRubyY = config.get("World Gen", "maxRubyY", 48).getInt();
        veinCountRuby = config.get("World Gen", "veinCountRuby", 2).getInt();
        veinSizeRuby = config.get("World Gen", "veinSizeRuby", 7).getInt();
        generateAmethyst = config.get("World Gen", "generateAmethyst", true).getBoolean(true);
        minAmethystY = config.get("World Gen", "minMalachiteY", 0).getInt();
        maxAmethystY = config.get("World Gen", "maxMalachiteY", 48).getInt();
        veinCountAmethyst = config.get("World Gen", "veinCountAmethyst", 2).getInt();
        veinSizeAmethyst = config.get("World Gen", "veinSizeAmethyst", 7).getInt();
        generateSapphire = config.get("World Gen", "generateSapphire", true).getBoolean(true);
        minSapphireY = config.get("World Gen", "minSapphireY", 0).getInt();
        maxSapphireY = config.get("World Gen", "maxSapphireY", 48).getInt();
        veinCountSapphire = config.get("World Gen", "veinCountSapphire", 7).getInt();
        veinSizeSapphire = config.get("World Gen", "veinSizeSapphire", 2).getInt();
        volcanoSpawnChance = config.get("World Gen", "volcanoSpawnChance", 0.2).getDouble(0);
        volcanoActiveToInactiveRatio = config.get("World Gen", "volcanoActiveToInactiveRatio", 0.5).getDouble(0);
    }
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
    public static boolean generateTin;
    public static int minTinY;
    public static int maxTinY;
    public static int veinCountTin;
    public static int veinSizeTin;
    public static boolean generateNikolite;
    public static int minNikoliteY;
    public static int maxNikoliteY;
    public static int veinCountNikolite;
    public static int veinSizeNikolite;
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
    public static double volcanoSpawnChance;//chance of a volcano spawning per chunk.
}
