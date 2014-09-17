/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.tube;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.part.tube.TubeStack;

/**
 * 
 * @author MineMaarten
 */

public interface ITubeConnection {
    
    public boolean isConnectedTo(ForgeDirection from);
    
    /**
     * 
     * @param stack TubeStack, as it needs to save the color if it bounced into the buffer.
     * @param from
     * @param simulate when true, only return what would have been accepted, but don't actually accept.
     * @return The TubeStack that was unable to enter this ITubeConnection
     */
    public TubeStack acceptItemFromTube(TubeStack stack, ForgeDirection from, boolean simulate);
}
