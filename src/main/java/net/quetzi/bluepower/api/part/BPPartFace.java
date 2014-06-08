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

package net.quetzi.bluepower.api.part;

import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.api.vec.Vector3;

public abstract class BPPartFace extends BPPart implements IBPFacePart, IBPRedstonePart {
    
    @Override
    public int getFace() {
    
        return 0;// TODO: Determine what face the BPPartFace is on
    }
    
    @Override
    public boolean canStay() {
    
        Vector3 v = new Vector3(this.x, this.y, this.z, this.world).getRelative(ForgeDirection.getOrientation(getFace()));
        return !v.isBlock(null, true);
    }
    
    @Override
    public final boolean canConnect(ForgeDirection side) {
    
        return false;
    }
    
    @Override
    public final int getStrongOutput(ForgeDirection side) {
    
        return 0;
    }
    
    @Override
    public final int getWeakOutput(ForgeDirection side) {
    
        return 0;
    }
    
}
