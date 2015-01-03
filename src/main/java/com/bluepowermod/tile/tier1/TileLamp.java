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

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.block.machine.BlockLampRGB;
import com.bluepowermod.block.machine.BlockLampRGB.RGBLampBundledDevice;
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

        if (blockType instanceof BlockLampRGB) {
            RGBLampBundledDevice lamp = BlockLampRGB.RGBLampBundledDevice.getDeviceAt(worldObj, xCoord, yCoord, zCoord);
            int pow = 0;
            if (((BlockLamp) blockType).isInverted()) {
                pow = 255 - Math.min(Math.min(lamp.getBundledPower(null)[MinecraftColor.RED.ordinal()] & 0xFF,
                        lamp.getBundledPower(null)[MinecraftColor.GREEN.ordinal()] & 0xFF), lamp.getBundledPower(null)[MinecraftColor.BLUE
                                                                                                                       .ordinal()] & 0xFF);
            } else {
                pow = Math.max(Math.max(lamp.getBundledPower(null)[MinecraftColor.RED.ordinal()] & 0xFF,
                        lamp.getBundledPower(null)[MinecraftColor.GREEN.ordinal()] & 0xFF), lamp.getBundledPower(null)[MinecraftColor.BLUE
                                                                                                                       .ordinal()] & 0xFF);
            }
            power = (int) ((pow / 256D) * 15);
            sendUpdatePacket();
        } else {
            int pow = RedstoneHelper.getInput(getWorldObj(), xCoord, yCoord, zCoord);
            if (pow != power) {
                power = pow;
                sendUpdatePacket();
                getWorldObj().markBlockRangeForRenderUpdate(xCoord, yCoord, zCoord, xCoord, yCoord, zCoord);
                getWorldObj().updateLightByType(EnumSkyBlock.Block, xCoord, yCoord, zCoord);
                getWorldObj().notifyBlocksOfNeighborChange(xCoord, yCoord, zCoord, getBlockType());
            }
        }
    }

    @Override
    protected void writeToPacketNBT(NBTTagCompound tCompound) {

        tCompound.setInteger("power", power);
        if (blockType instanceof BlockLampRGB) {
            RGBLampBundledDevice lamp = BlockLampRGB.RGBLampBundledDevice.getDeviceAt(worldObj, xCoord, yCoord, zCoord);
            tCompound.setByte("red", lamp.getBundledPower(null)[MinecraftColor.RED.ordinal()]);
            tCompound.setByte("green", lamp.getBundledPower(null)[MinecraftColor.GREEN.ordinal()]);
            tCompound.setByte("blue", lamp.getBundledPower(null)[MinecraftColor.BLUE.ordinal()]);
        }
    }

    @Override
    protected void readFromPacketNBT(NBTTagCompound tCompound) {

        power = tCompound.getInteger("power");
        if (tCompound.hasKey("red")) {
            RGBLampBundledDevice lamp = BlockLampRGB.RGBLampBundledDevice.getDeviceAt(worldObj, xCoord, yCoord, zCoord);
            byte[] pow = lamp.getBundledPower(null);
            pow[MinecraftColor.RED.ordinal()] = tCompound.getByte("red");
            pow[MinecraftColor.GREEN.ordinal()] = tCompound.getByte("green");
            pow[MinecraftColor.BLUE.ordinal()] = tCompound.getByte("blue");
            lamp.setBundledPower(null, pow);
        }
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
