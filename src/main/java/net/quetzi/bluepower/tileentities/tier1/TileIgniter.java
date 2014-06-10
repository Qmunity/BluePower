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

package net.quetzi.bluepower.tileentities.tier1;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPortal;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.tileentities.TileBase;

public class TileIgniter extends TileBase {

    boolean isActive;

    @Override
    protected void redstoneChanged(boolean newValue) {
        ForgeDirection direction = this.getFacingDirection();
        if (this.getIsRedstonePowered()) {
            if (isActive) {
                Block targetBlock = worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
                if (targetBlock instanceof BlockPortal) {
                    // TODO: Disable Portal
                }
                this.isActive = false;
            } else {
                this.isActive = true;
                ignite(direction);
            }
        }
    }

    private void ignite(ForgeDirection direction) {
        if (this.isActive) {
            worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ).isBurning(worldObj,
                    xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        }
    }

}
