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
    public static final String MARBLE_TEXTURE_NAME = "";
    public static final String BLOCKBASALT_NAME = "Basalt";
    public static final String BASALT_TEXTURE_NAME = "";
}
