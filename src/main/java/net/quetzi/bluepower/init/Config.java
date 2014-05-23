package net.quetzi.bluepower.init;

import net.minecraftforge.common.config.Configuration;

public class Config {
    public static void setUp(Configuration config) {
        config.addCustomCategoryComment("World Gen", "Toggle blocks being generated into the world");
        generateCopper = config.get("World Gen", "generateCopper", true).getBoolean(true);
        generateTin = config.get("World Gen", "generateTin", true).getBoolean(true);
        generateSilver = config.get("World Gen", "generateSilver", true).getBoolean(true);
        generateNikolite = config.get("World Gen", "generateNikolite", true).getBoolean(true);
        generateRuby = config.get("World Gen", "generateRuby", true).getBoolean(true);
        generateMalachite = config.get("World Gen", "generateMalachite", true).getBoolean(true);
        generateSapphire = config.get("World Gen", "generateSapphire", true).getBoolean(true);
    }
    public static boolean generateCopper;
    public static boolean generateSilver;
    public static boolean generateTin;
    public static boolean generateNikolite;
    public static boolean generateRuby;
    public static boolean generateMalachite;
    public static boolean generateSapphire;
}
