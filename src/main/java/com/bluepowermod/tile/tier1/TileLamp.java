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
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.block.machine.BlockLampRGB;
import com.bluepowermod.client.render.RenderLamp;
import com.bluepowermod.tile.TileBase;

/**
 * @author Koen Beckers (K4Unl) Yes. I only need this class to do the getPower() function.. damn :(
 */
public class TileLamp extends TileBase implements IBundledDevice {

    private int power;

    private byte[] bundledPower = new byte[16];

    private IBundledDevice[] devices = new IBundledDevice[6];

    public int getPower() {

        return power;
    }

    public void onUpdate() {

        if (blockType instanceof BlockLampRGB) {
            int pow = 0;
            if (((BlockLamp) blockType).isInverted()) {
                pow = 255 - Math.min(
                        Math.min(bundledPower[MinecraftColor.RED.ordinal()] & 0xFF, bundledPower[MinecraftColor.GREEN.ordinal()] & 0xFF),
                        bundledPower[MinecraftColor.BLUE.ordinal()] & 0xFF);
            } else {
                pow = Math.max(
                        Math.max(bundledPower[MinecraftColor.RED.ordinal()] & 0xFF, bundledPower[MinecraftColor.GREEN.ordinal()] & 0xFF),
                        bundledPower[MinecraftColor.BLUE.ordinal()] & 0xFF);
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
            tCompound.setByte("red", bundledPower[MinecraftColor.RED.ordinal()]);
            tCompound.setByte("green", bundledPower[MinecraftColor.GREEN.ordinal()]);
            tCompound.setByte("blue", bundledPower[MinecraftColor.BLUE.ordinal()]);
        }
    }

    @Override
    protected void readFromPacketNBT(NBTTagCompound tCompound) {

        power = tCompound.getInteger("power");
        if (tCompound.hasKey("red")) {
            byte[] pow = bundledPower;
            pow[MinecraftColor.RED.ordinal()] = tCompound.getByte("red");
            pow[MinecraftColor.GREEN.ordinal()] = tCompound.getByte("green");
            pow[MinecraftColor.BLUE.ordinal()] = tCompound.getByte("blue");
            bundledPower = pow;
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

    @Override
    public World getWorld() {

        return getWorldObj();
    }

    @Override
    public int getX() {

        return xCoord;
    }

    @Override
    public int getY() {

        return yCoord;
    }

    @Override
    public int getZ() {

        return zCoord;
    }

    @Override
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        return side != ForgeDirection.UNKNOWN;
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        return false;
    }

    @Override
    public void onConnect(ForgeDirection side, IBundledDevice device) {

        devices[side.ordinal()] = device;
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
    }

    @Override
    public IBundledDevice getBundledDeviceOnSide(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public byte[] getBundledOutput(ForgeDirection side) {

        return new byte[16];
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        bundledPower = power;
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        return bundledPower;
    }

    @Override
    public void onBundledUpdate() {

        onUpdate();
    }

    @Override
    public MinecraftColor getBundledColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isBundled() {

        return blockType instanceof BlockLampRGB;
    }

    @Override
    public boolean isNormalBlock() {

        return true;
    }

    public int getColor() {

        int r = MathHelper.map(bundledPower[MinecraftColor.RED.ordinal()] & 0xFF, 0, 255, 20, 235);
        int g = MathHelper.map(bundledPower[MinecraftColor.GREEN.ordinal()] & 0xFF, 0, 255, 20, 235);
        int b = MathHelper.map(bundledPower[MinecraftColor.BLUE.ordinal()] & 0xFF, 0, 255, 20, 235);

        return (r << 16) + (g << 8) + b;
    }
}
