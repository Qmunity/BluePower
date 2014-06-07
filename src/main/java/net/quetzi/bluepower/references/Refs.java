package net.quetzi.bluepower.references;

public class Refs {
    
    public static final String  NAME      = "Blue Power";
    public static final String  MODID     = "bluepower";
    private static final String MAJOR     = "@MAJOR@";
    private static final String MINOR     = "@MINOR@";
    private static final String BUILD     = "@BUILD_NUMBER@";
    private static final String MCVERSION = "1.7.2";
    
    public static String fullVersionString() {
    
        return String.format("%s-%s.%s.%s", MCVERSION, MAJOR, MINOR, BUILD);
    }
    
    public static final String PROXY_LOCATION         = "net.quetzi.bluepower";
    public static final double PACKET_UPDATE_DISTANCE = 64;
    
    public static final String MARBLE_NAME            = "marble";
    public static final String BASALT_NAME            = "basalt";
    public static final String BASALTCOBBLE_NAME      = "basalt_cobble";
    public static final String BASALTBRICK_NAME       = "basalt_brick";
    public static final String MARBLEBRICK_NAME       = "marble_brick";
    public static final String CRACKED_BASALT         = "cracked_basalt_lava";
    public static final String ALLOYFURNACE_NAME      = "alloyfurnace";
    public static final String AMETHYSTORE_NAME       = "amethyst_ore";
    public static final String RUBYORE_NAME           = "ruby_ore";
    public static final String SAPPHIREORE_NAME       = "sapphire_ore";
    public static final String NIKOLITEORE_NAME       = "nikolite_ore";
    public static final String AMETHYSTBLOCK_NAME     = "amethyst_block";
    public static final String SAPPHIREBLOCK_NAME     = "sapphire_block";
    public static final String RUBYBLOCK_NAME         = "ruby_block";
    public static final String NIKOLITEBLOCK_NAME     = "nikolite_block";
    
    public static final String ITEMMALACHITE_NAME     = "malachite_gem";
    public static final String ITEMSAPPHIRE_NAME      = "sapphire_gem";
    public static final String ITEMRUBY_NAME          = "ruby_gem";
    public static final String ITEMNIKOLITE_NAME      = "nikolite_dust";
    
    public static final String COPPERORE_NAME         = "copper_ore";
    public static final String SILVERORE_NAME         = "silver_ore";
    public static final String TINORE_NAME            = "tin_ore";
    public static final String COPPERBLOCK_NAME       = "copper_block";
    public static final String SILVERBLOCK_NAME       = "silver_block";
    public static final String TINBLOCK_NAME          = "tin_block";
    public static final String ITEMCOPPERINGOT_NAME   = "copper_ingot";
    public static final String ITEMSILVERINGOT_NAME   = "silver_ingot";
    public static final String ITEMTININGOT_NAME      = "tin_ingot";
    
    public static final String RUBYAXE_NAME           = "ruby_axe";
    public static final String RUBYSWORD_NAME         = "ruby_sword";
    public static final String RUBYPICKAXE_NAME       = "ruby_pickaxe";
    public static final String RUBYSPADE_NAME         = "ruby_shovel";
    public static final String RUBYHOE_NAME           = "ruby_hoe";
    public static final String RUBYSICKLE_NAME        = "ruby_sickle";
    
    public static final String SAPPHIREAXE_NAME       = "sapphire_axe";
    public static final String SAPPHIRESWORD_NAME     = "sapphire_sword";
    public static final String SAPPHIREPICKAXE_NAME   = "sapphire_pickaxe";
    public static final String SAPPHIRESPADE_NAME     = "sapphire_shovel";
    public static final String SAPPHIREHOE_NAME       = "sapphire_hoe";
    public static final String SAPPHIRESICKLE_NAME    = "sapphire_sickle";
    
    public static final String AMETHYSTAXE_NAME      = "amethyst_axe";
    public static final String AMETHYSTSWORD_NAME    = "amethyst_sword";
    public static final String AMETHYSTPICKAXE_NAME  = "amethyst_pickaxe";
    public static final String AMETHYSTSPADE_NAME    = "amethyst_shovel";
    public static final String AMETHYSTHOE_NAME      = "amethyst_hoe";
    public static final String AMETHYSTSICKLE_NAME   = "amethyst_sickle";
    
    public static final String FLAXSEED_NAME          = "flax_seeds";
    public static final String FLAXCROP_NAME          = "flax_crop";
    public static final String INDIGOFLOWER_NAME      = "indigo_flower";
    public static final String INDIGODYE_NAME         = "indigo_dye";
    
    public static final String WOODSICKLE_NAME        = "wood_sickle";
    public static final String STONESICKLE_NAME       = "stone_sickle";
    public static final String IRONSICKLE_NAME        = "iron_sickle";
    public static final String GOLDSICKLE_NAME        = "gold_sickle";
    public static final String DIAMONDSICKLE_NAME     = "diamond_sickle";
    
    public static final String MULTIPART_NAME         = "bluepower_multipart";
    
    public static final String CRACKEDBASALTBRICK_NAME  = "basaltbrick_cracked";
    public static final String SMALLBASALTBRICK_NAME    = "basalt_brick_small";
    public static final String SMALLMARBLEBRICK_NAME    = "marble_brick_small";
    public static final String CHISELEDBASALTBRICK_NAME = "fancy_basalt";
    public static final String CHISELEDMARBLEBRICK_NAME = "fancy_marble";
    public static final String MARBLETILE_NAME          = "marble_tile";
    public static final String BASALTTILE_NAME          = "basalt_tile";
    
}
