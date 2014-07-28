package com.bluepowermod.api.tube;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * later there will be a way for API implementers to get an IPneumaticTube for a given TileEntity.
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
