package com.bluepowermod.part.wire;

import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.RedstoneColor;

public class DummyRedstoneDevice implements IRedstoneDevice {

    private Vec3i loc;

    public DummyRedstoneDevice(Vec3i loc) {

        this.loc = loc;
    }

    @Override
    public World getWorld() {

        return loc.getWorld();
    }

    @Override
    public int getX() {

        return loc.getX();
    }

    @Override
    public int getY() {

        return loc.getY();
    }

    @Override
    public int getZ() {

        return loc.getZ();
    }

    @Override
    public boolean canConnectStraight(IRedstoneDevice device) {

        return loc.getBlock().canConnectRedstone(getWorld(), getX(), getY(), getZ(), 0);
    }

    @Override
    public boolean canConnectOpenCorner(IRedstoneDevice device) {

        return canConnectStraight(device);
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

    }

    @Override
    public void onDisconnect(ForgeDirection side) {

    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return null;
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        int original = Math.max(
                loc.getBlock().isProvidingWeakPower(getWorld(), getX(), getY(), getZ(),
                        Direction.getMovementDirection(side.offsetX, side.offsetZ)),
                loc.getBlock().isProvidingStrongPower(getWorld(), getX(), getY(), getZ(),
                        Direction.getMovementDirection(side.offsetX, side.offsetZ)));

        return (byte) MathHelper.map(original, 0, 15, 0, 255);
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public RedstoneColor getInsulationColor(ForgeDirection side) {

        return null;
    }

    @Override
    public boolean isNormalBlock() {

        return true;
    }

}
