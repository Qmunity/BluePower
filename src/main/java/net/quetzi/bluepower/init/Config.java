package net.quetzi.bluepower.init;

import net.minecraftforge.common.config.Configuration;

public class Config {
    public static void setUp(Configuration config) {
        config.addCustomCategoryComment("World Gen", "Toggle blocks being generated into the world");
        generateCopper = config.get("World Gen", "generateCopper", true).getBoolean(true);
        minCopperY = config.get("World Gen", "minCopperY", 0).getInt();
        maxCopperY = config.get("World Gen", "maxCopperY", 64).getInt();
        generateTin = config.get("World Gen", "generateTin", true).getBoolean(true);
        minTinY = config.get("World Gen", "minTinY", 0).getInt();
        maxTinY = config.get("World Gen", "maxTinY", 48).getInt();
        generateSilver = config.get("World Gen", "generateSilver", true).getBoolean(true);
        minSilverY = config.get("World Gen", "minSilverY", 0).getInt();
        maxSilverY = config.get("World Gen", "maxSilverY", 32).getInt();
        generateNikolite = config.get("World Gen", "generateNikolite", true).getBoolean(true);
        minNikoliteY = config.get("World Gen", "minNikoliteY", 0).getInt();
        maxNikoliteY = config.get("World Gen", "maxNikoliteY", 16).getInt();
        generateRuby = config.get("World Gen", "generateRuby", true).getBoolean(true);
        minRubyY = config.get("World Gen", "minRubyY", 0).getInt();
        maxRubyY = config.get("World Gen", "maxRubyY", 48).getInt();
        generateMalachite = config.get("World Gen", "generateMalachite", true).getBoolean(true);
        minMalachiteY = config.get("World Gen", "minMalachiteY", 0).getInt();
        maxMalachiteY = config.get("World Gen", "maxMalachiteY", 48).getInt();
        generateSapphire = config.get("World Gen", "generateSapphire", true).getBoolean(true);
        minSapphireY = config.get("World Gen", "minSapphireY", 0).getInt();
        maxSapphireY = config.get("World Gen", "maxSapphireY", 48).getInt();
    }
    public static boolean generateCopper;
    public static int minCopperY;
    public static int maxCopperY;
    public static boolean generateSilver;
    public static int minSilverY;
    public static int maxSilverY;
    public static boolean generateTin;
    public static int minTinY;
    public static int maxTinY;
    public static boolean generateNikolite;
    public static int minNikoliteY;
    public static int maxNikoliteY;
    public static boolean generateRuby;
    public static int minRubyY;
    public static int maxRubyY;
    public static boolean generateMalachite;
    public static int minMalachiteY;
    public static int maxMalachiteY;
    public static boolean generateSapphire;
    public static int minSapphireY;
    public static int maxSapphireY;
}
