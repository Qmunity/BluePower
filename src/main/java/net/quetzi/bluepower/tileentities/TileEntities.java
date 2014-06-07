package net.quetzi.bluepower.tileentities;

import net.quetzi.bluepower.tileentities.tier1.TileAlloyFurnace;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileEntities {
    
    /**
     * Method to register the TE's to the game. If a TE is not registered, it
     * _will_ cause issues!
     */
    public static void init() {
    
        GameRegistry.registerTileEntity(TileAlloyFurnace.class,
                "tileAlloyFurnace");
    }
}
