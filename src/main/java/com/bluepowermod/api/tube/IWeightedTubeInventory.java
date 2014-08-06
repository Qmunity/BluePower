/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.tube;

import net.minecraftforge.common.util.ForgeDirection;

/**
    This interface is implemented by inventories with a buffer inventory, in which the tube _can_ but doesn't prefer to
    insert items back into the buffer. An arbitrarily large number is returned, 1000000. A Restriction Tube has a weight
    of 5000, a normal tube 1.
    @author MineMaarten
*/
public interface IWeightedTubeInventory {
    
    /**
        By default this can be seen as 0 for non implementing inventories. return a high value to make it less prefered
        by the tubes.
    */
    public int getWeight(ForgeDirection from);
    
}
