/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.EnumSkyBlock;
import uk.co.qmunity.lib.helper.RedstoneHelper;

import com.bluepowermod.client.render.RenderLamp;
import com.bluepowermod.tile.TileBase;

/**
 * @author Koen Beckers (K4Unl) Yes. I only need this class to do the getPower() function.. damn :(
 */
public class TileLamp extends TileBase {

    private int power;

    public int getPower() {

        return power;
    }

    public void onUpdate() {

        int pow = RedstoneHelper.getInput(getWorldObj(), xCoord, yCoord, zCoord);
        if (pow != power) {
            power = pow;
            sendUpdatePacket();
            getWorldObj().markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            getWorldObj().updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
            getWorldObj().notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
        }
    }

    @Override
    protected void writeToPacketNBT(NBTTagCompound tCompound) {

        tCompound.setInteger("power", power);
    }

    @Override
    protected void readFromPacketNBT(NBTTagCompound tCompound) {

        power = tCompound.getInteger("power");
        if (getWorldObj() != null) {
            getWorldObj().markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
            getWorldObj().updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {

        RenderLamp.pass = pass;
        return true;
    }
}
