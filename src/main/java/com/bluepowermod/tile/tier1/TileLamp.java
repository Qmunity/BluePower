/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier1;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IInsulatedRedstoneDevice;
import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.block.machine.BlockLampRGB;
import com.bluepowermod.client.render.RenderLamp;
import com.bluepowermod.redstone.BundledConnectionCache;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.tile.TileBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.World;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;

;

/**
 * @author Koen Beckers (K4Unl) and Amadornes. Yes. I only need this class to do the getPower() function.. damn :(
 */
public class TileLamp extends TileBase implements IBundledDevice {

    private int power;

    private byte[] bundledPower = new byte[16];

    private BundledConnectionCache connections = RedstoneApi.getInstance().createBundledConnectionCache(this);

    public int getPower() {

        return power;
    }

    public void onUpdate() {

        if (blockType instanceof BlockLampRGB) {
            connections.recalculateConnections();
            int connected = 0;
            for (EnumFacing s : EnumFacing.VALUES)
                connected += (connections.getConnectionOnSide(s) != null) ? 1 : 0;

            if (connected == 0)
                bundledPower = new byte[16];

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
            int pow = RedstoneHelper.getInput(getWorld(), pos);
            if (pow != power) {
                power = pow;
                sendUpdatePacket();
                getWorld().markBlockRangeForRenderUpdate(pos, pos);
                try {
                    getWorld().checkLightFor(EnumSkyBlock.BLOCK, pos);
                } catch (Exception ex) {
                }
                getWorld().notifyNeighborsOfStateChange(pos, getBlockType(), true);
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
        if (getWorld() != null) {
            getWorld().markBlockRangeForRenderUpdate(pos, pos);
            getWorld().checkLightFor(EnumSkyBlock.BLOCK, pos);
        }
    }

    @Override
    public boolean shouldRenderInPass(int pass) {

        RenderLamp.pass = pass;
        return true;
    }

    @Override
    public World getWorld() {

        return getWorld();
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public boolean canConnect(EnumFacing side, IBundledDevice dev, ConnectionType type) {

        if (!(getWorld().getBlockState(getPos()).getBlock() instanceof BlockLampRGB))
            return false;
        if (dev instanceof IInsulatedRedstoneDevice)
            return false;
        if (dev instanceof TileLamp)
            return false;

        return type == ConnectionType.STRAIGHT && side != null;
    }

    @Override
    public BundledConnectionCache getBundledConnectionCache() {

        return connections;
    }

    @Override
    public byte[] getBundledOutput(EnumFacing side) {

        return new byte[16];
    }

    @Override
    public void setBundledPower(EnumFacing side, byte[] power) {

        bundledPower = power;
    }

    @Override
    public byte[] getBundledPower(EnumFacing side) {

        return bundledPower;
    }

    @Override
    public void onBundledUpdate() {

        onUpdate();
    }

    @Override
    public MinecraftColor getBundledColor(EnumFacing side) {

        return MinecraftColor.NONE;
    }

    public int getColor() {

        int r = MathHelper.map(bundledPower[MinecraftColor.RED.ordinal()] & 0xFF, 0, 255, 20, 235);
        int g = MathHelper.map(bundledPower[MinecraftColor.GREEN.ordinal()] & 0xFF, 0, 255, 20, 235);
        int b = MathHelper.map(bundledPower[MinecraftColor.BLUE.ordinal()] & 0xFF, 0, 255, 20, 235);

        return (r << 16) + (g << 8) + b;
    }

    @Override
    public boolean isNormalFace(EnumFacing side) {

        return true;
    }


}
