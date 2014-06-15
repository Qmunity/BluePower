package net.quetzi.bluepower.api.tube;

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
    
    public ItemStack injectStack(ItemStack stack, ForgeDirection from, TubeColor itemColor, boolean simulate);
}
