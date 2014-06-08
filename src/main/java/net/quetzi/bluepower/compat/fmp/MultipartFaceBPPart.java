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

package net.quetzi.bluepower.compat.fmp;

import net.minecraft.item.ItemStack;
import net.quetzi.bluepower.api.part.BPPart;
import net.quetzi.bluepower.api.part.IBPFacePart;
import codechicken.lib.vec.Vector3;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.TFacePart;
import codechicken.multipart.TileMultipart;

public class MultipartFaceBPPart extends MultipartBPPart implements TFacePart, IFaceRedstonePart {
    
    private IBPFacePart facePart;
    
    public MultipartFaceBPPart(IBPFacePart part) {
    
        super((BPPart) part);
        facePart = part;
    }
    
    @Override
    public int getSlotMask() {
    
        return 1 << this.facePart.getFace();
    }
    
    @Override
    public int redstoneConductionMap() {
    
        return 0;
    }
    
    @Override
    public boolean solid(int side) {
    
        return false;
    }
    
    @Override
    public int getFace() {
    
        return facePart.getFace();
    }
    
    @Override
    public void onNeighborChanged() {
    
        if (!facePart.canStay()) {
            for (ItemStack is : getPart().getDrops()) {
                TileMultipart.dropItem(is, world(), Vector3.fromTileEntityCenter(tile()));
            }
            tile().remPart(this);
            return;
        }
        
        super.onNeighborChanged();
    }
    
    @Override
    public void update() {
    
        super.update();
    }
    
}
