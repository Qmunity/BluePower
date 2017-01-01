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

package com.bluepowermod.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;

import java.util.ArrayList;
import java.util.List;

public class DummyRedstoneDevice implements IRedstoneDevice {

    private static final List<DummyRedstoneDevice> dummyDevices = new ArrayList<DummyRedstoneDevice>();

    public static DummyRedstoneDevice getDeviceAt(BlockPos loc, World world, Block block) {

        for (DummyRedstoneDevice dev : new ArrayList<DummyRedstoneDevice>(dummyDevices))
            if (dev.loc != null && dev.loc.equals(loc))
                return dev;

        DummyRedstoneDevice dev = new DummyRedstoneDevice(loc, block, world);
        dummyDevices.add(dev);
        return dev;
    }

    private BlockPos loc;
    private Block block;
    private World world;
    private RedstoneConnectionCache connections;

    private DummyRedstoneDevice(BlockPos loc, Block block, World world) {
        this.block = block;
        this.world = world;
        this.loc = loc;
        if (loc != null)
            connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    }


    @Override
    public RedstoneConnectionCache getRedstoneConnectionCache() {

        return connections;
    }

    @Override
    public boolean canConnect(EnumFacing side, IRedstoneDevice dev, ConnectionType type) {

        if (type == ConnectionType.STRAIGHT)
            return  RedstoneHelper.canConnect(world, loc, side, dev instanceof IFace ? ((IFace) dev).getFace()
                    : null);

        return false;
    }

    @Override
    public byte getRedstonePower(EnumFacing side) {

        // if (loc.getBlock() instanceof BlockRedstoneWire) {
        // boolean wiresHandledUpdates = RedstoneApi.getInstance().shouldWiresHandleUpdates();
        // boolean wiresOutputtedPower = RedstoneApi.getInstance().shouldWiresOutputPower();
        // RedstoneApi.getInstance().setWiresOutputPower(false);
        // RedstoneApi.getInstance().setWiresHandleUpdates(false);
        // loc.getBlock().onNeighborBlockChange(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), new BlockPos(this).getBlock());
        // RedstoneApi.getInstance().setWiresHandleUpdates(wiresHandledUpdates);
        // RedstoneApi.getInstance().setWiresOutputPower(wiresOutputtedPower);
        // }

        if (side == null)
            return 0;

        return (byte) MathHelper.map(RedstoneHelper.getOutput(world, loc, side), 0, 15, 0, 255);
    }

    @Override
    public void setRedstonePower(EnumFacing side, byte power) {

        // if (loc.getBlock() instanceof BlockRedstoneWire) {
        // boolean wiresHandledUpdates = RedstoneApi.getInstance().shouldWiresHandleUpdates();
        // boolean wiresOutputtedPower = RedstoneApi.getInstance().shouldWiresOutputPower();
        // RedstoneApi.getInstance().setWiresOutputPower(false);
        // RedstoneApi.getInstance().setWiresHandleUpdates(false);
        // loc.getBlock().onNeighborBlockChange(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), new BlockPos(this).getBlock());
        // RedstoneApi.getInstance().setWiresHandleUpdates(wiresHandledUpdates);
        // RedstoneApi.getInstance().setWiresOutputPower(wiresOutputtedPower);
        // }
    }

    public int getRedstoneOutput(int def) {

        if (block instanceof BlockRedstoneWire) {
            // boolean wiresHandledUpdates = RedstoneApi.getInstance().shouldWiresHandleUpdates();
            // boolean wiresOutputtedPower = RedstoneApi.getInstance().shouldWiresOutputPower();
            // RedstoneApi.getInstance().setWiresOutputPower(false);
            // RedstoneApi.getInstance().setWiresHandleUpdates(false);
            // // loc.getBlock().onNeighborBlockChange(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), new BlockPos(this).getBlock());
            // RedstoneApi.getInstance().setWiresHandleUpdates(wiresHandledUpdates);
            // RedstoneApi.getInstance().setWiresOutputPower(wiresOutputtedPower);
            // int powNow = loc.getBlockMeta();

            // if (def >= powNow)
            // return def;
            return 0;
        }

        return def;
    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public boolean equals(Object obj) {

        return super.equals(obj);
    }

    @Override
    public boolean isNormalFace(EnumFacing side) {

        return block.isSideSolid(world.getBlockState(loc), world, loc, side);
    }

    @Override
    public World getWorld() {
        return world;
    }

    @Override
    public BlockPos getPos() {
        return loc;
    }
}
