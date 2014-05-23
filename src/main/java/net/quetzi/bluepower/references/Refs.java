package net.quetzi.bluepower.references;

public class Refs {
    public static final String NAME = "Blue Power";
    public static final String MODID = "bluepower";
    private static final String MAJOR = "0";
    private static final String MINOR = "0";
    private static final String BUILD = "1";
    private static final String MCVERSION = "1.7.2";
    
    public static String fullVersionString() {
        return String.format("%s-%s.%s.%s", MCVERSION, MAJOR, MINOR, BUILD);
    }

    public static final String MARBLE_NAME = "marble";
    public static final String BASALT_NAME = "basalt";
    public static final String BASALTCOBBLE_NAME = "basalt_cobble";
    public static final String BASALTBRICK_NAME = "basalt_brick";
    public static final String MARBLEBRICK_NAME = "marble_brick";
    public static final String ALLOYFURNACE_NAME = "alloyfurnace";
    public static final String MALACHITEORE_NAME = "malachite_ore";
    public static final String RUBYORE_NAME = "ruby_ore";
    public static final String SAPPHIREORE_NAME = "sapphire_ore";
    public static final String NIKOLITEORE_NAME = "nikolite_ore";
    public static final String MALACHITEBLOCK_NAME = "malachite_block";
    public static final String SAPPHIREBLOCK_NAME = "sapphire_block";
    public static final String RUBYBLOCK_NAME = "ruby_block";
    public static final String NIKOLITEBLOCK_NAME = "nikolite_block";
    
    public static final String ITEMMALACHITE_NAME = "malachite";
    public static final String ITEMSAPPHIRE_NAME = "sapphire";
    public static final String ITEMRUBY_NAME = "ruby";
    public static final String ITEMNIKOLITE_NAME = "nikolite";
    
    public static final String COPPERORE_NAME = "copper_ore";
    public static final String SILVERORE_NAME = "silver_ore";
    public static final String TINORE_NAME = "tin_ore";
}