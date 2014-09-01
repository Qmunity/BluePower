/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
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
