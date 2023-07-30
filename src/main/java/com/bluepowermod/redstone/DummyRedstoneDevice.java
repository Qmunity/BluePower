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

import java.util.ArrayList;
import java.util.List;

import com.bluepowermod.api.misc.IWorldLocation;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.helper.RedstoneHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RedStoneWireBlock;

public class DummyRedstoneDevice implements IRedstoneDevice, IWorldLocation {

    private static final List<DummyRedstoneDevice> dummyDevices = new ArrayList<DummyRedstoneDevice>();

    public static DummyRedstoneDevice getDeviceAt(Level world,  BlockPos loc) {

        for (DummyRedstoneDevice dev : new ArrayList<DummyRedstoneDevice>(dummyDevices))
            if (dev.blockPos != null && dev.blockPos.equals(loc))
                return dev;

        DummyRedstoneDevice dev = new DummyRedstoneDevice(world, loc);
        dummyDevices.add(dev);
        return dev;
    }

    private BlockPos blockPos;
    private Level level;
    private RedstoneConnectionCache connections;

    private DummyRedstoneDevice(Level level, BlockPos blockPos) {

        this.blockPos = blockPos;
        this.level = level;
        if (blockPos != null)
            connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    }

    public BlockPos getBlockPos() {
        return blockPos;
    }

    public Level getLevel() {
        return level;
    }

    @Override
    public RedstoneConnectionCache getRedstoneConnectionCache() {

        return connections;
    }

    @Override
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type) {

        if (type == ConnectionType.STRAIGHT)
            return RedstoneHelper.canConnect(getLevel(), getBlockPos(), side, dev instanceof IFace ? ((IFace) dev).getFace()
                    : null);

        return false;
    }

    @Override
    public byte getRedstonePower(Direction side) {

        // if (loc.getBlock() instanceof BlockRedstoneWire) {
        // boolean wiresHandledUpdates = RedstoneApi.getInstance().shouldWiresHandleUpdates();
        // boolean wiresOutputtedPower = RedstoneApi.getInstance().shouldWiresOutputPower();
        // RedstoneApi.getInstance().setWiresOutputPower(false);
        // RedstoneApi.getInstance().setWiresHandleUpdates(false);
        // loc.getBlock().onNeighborBlockChange(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), new Vec3i(this).getBlock());
        // RedstoneApi.getInstance().setWiresHandleUpdates(wiresHandledUpdates);
        // RedstoneApi.getInstance().setWiresOutputPower(wiresOutputtedPower);
        // }

        if (side == null)
            return 0;

        return (byte) MathHelper.map(RedstoneHelper.getOutput(getLevel(), getBlockPos(), side), 0, 15, 0, 255);
    }

    @Override
    public void setRedstonePower(Direction side, byte power) {

        // if (loc.getBlock() instanceof BlockRedstoneWire) {
        // boolean wiresHandledUpdates = RedstoneApi.getInstance().shouldWiresHandleUpdates();
        // boolean wiresOutputtedPower = RedstoneApi.getInstance().shouldWiresOutputPower();
        // RedstoneApi.getInstance().setWiresOutputPower(false);
        // RedstoneApi.getInstance().setWiresHandleUpdates(false);
        // loc.getBlock().onNeighborBlockChange(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), new Vec3i(this).getBlock());
        // RedstoneApi.getInstance().setWiresHandleUpdates(wiresHandledUpdates);
        // RedstoneApi.getInstance().setWiresOutputPower(wiresOutputtedPower);
        // }
    }

    public int getRedstoneOutput(int def) {

        if (level.getBlockState(blockPos).getBlock() instanceof RedStoneWireBlock) {
            // boolean wiresHandledUpdates = RedstoneApi.getInstance().shouldWiresHandleUpdates();
            // boolean wiresOutputtedPower = RedstoneApi.getInstance().shouldWiresOutputPower();
            // RedstoneApi.getInstance().setWiresOutputPower(false);
            // RedstoneApi.getInstance().setWiresHandleUpdates(false);
            // // loc.getBlock().onNeighborBlockChange(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), new Vec3i(this).getBlock());
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
    public boolean isNormalFace(Direction side) {
        return true;
    }

}