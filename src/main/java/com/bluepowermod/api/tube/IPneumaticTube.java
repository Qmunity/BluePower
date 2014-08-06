package com.bluepowermod.api.tube;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * This interface is implemented by the Pneumatic Tube's logic. you can get the tube from a block by doing 
 * @author MineMaarten
 */
public interface IPneumaticTube {
    
    /**
     * The colors that a tube can have. These are in the same order as dye. Also note the 'NONE' as last entry.
     */
    public static enum TubeColor {
        BLACK, RED, GREEN, BROWN, BLUE, PURPLE, CYAN, SILVER, GRAY, PINK, LIME, YELLOW, LIGHT_BLUE, MAGENTA, ORANGE, WHITE, NONE
    }
    
    /**
     * Returns true if the network accepted the stack.
     * @param stack
     * @param from
     * @param itemColor
     * @param simulate
     * @return
     */
    public boolean injectStack(ItemStack stack, ForgeDirection from, TubeColor itemColor, boolean simulate);
}
