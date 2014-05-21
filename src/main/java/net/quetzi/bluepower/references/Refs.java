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

    public static final String BLOCKMARBLE_NAME = "Marble";    
    public static final String ITEMMARBLE_NAME = "Marble";   
    public static final String MARBLE_TEXTURE_NAME = "bluepower:marble";
    
    public static final String BLOCKBASALT_NAME = "Basalt";
    public static final String ITEMBASALT_NAME = "Basalt";
    public static final String BASALT_TEXTURE_NAME = "bluepower:basalt";
    
    public static final String BLOCKBASALTCOBBLE_NAME = "Basalt Cobblestone";
    public static final String ITEMBASALTCOBBLE_NAME = "Basalt Cobblestone";
    public static final String BASALTCOBBLE_TEXTURE_NAME = "bluepower:basaltcobble";
    
    public static final String BLOCKBASALTBRICK_NAME = "Basalt Brick";
    public static final String ITEMBASALTBRICK_NAME = "Basalt Brick";
    public static final String BASALTBRICK_TEXTURE_NAME = "bluepower:basaltbrick";
    
    public static final String BLOCKMARBLEBRICK_NAME = "Marble Brick";
    public static final String ITEMMARBLEBRICK_NAME = "Marble Brick";
    public static final String MARBLEBRICK_TEXTURE_NAME = "bluepower:marblebrick";
}