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
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;
import net.quetzi.bluepower.tileentities.TileBase;

public class TileIgniter extends TileBase {

    @Override
    protected void redstoneChanged(boolean newValue) {

        super.redstoneChanged(newValue);
        ForgeDirection direction = this.getFacingDirection();
        if (this.getIsRedstonePowered()) {
            ignite(direction);
        } else {
            extinguish(direction);
        }
    }

    private void ignite(ForgeDirection direction) {

        if (this.getIsRedstonePowered() && worldObj.isAirBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ)) {
            worldObj.setBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, Blocks.fire);
        }
    }

    private void extinguish(ForgeDirection direction) {

        Block target = worldObj.getBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ);
        if (!this.getIsRedstonePowered() && (target == Blocks.fire || target == Blocks.portal)) {
            worldObj.setBlock(xCoord + direction.offsetX, yCoord + direction.offsetY, zCoord + direction.offsetZ, Blocks.air);
        }
    }

    @Override
    public void updateEntity() {

        if (this.getTicker() % 5 == 0) {
            this.ignite(this.getFacingDirection());
        }
        super.updateEntity();
    }
}
