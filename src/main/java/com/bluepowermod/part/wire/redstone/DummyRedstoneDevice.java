package com.bluepowermod.part.wire.redstone;

import net.minecraft.block.Block;
import net.minecraft.util.Direction;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IRedstoneDevice;

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
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        return RedstoneHelper.canConnect(getWorld(), getX(), getY(), getZ(), side, device instanceof IFace ? ((IFace) device).getFace()
                : ForgeDirection.UNKNOWN);
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        return RedstoneHelper.canConnect(getWorld(), getX(), getY(), getZ(), side);
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

        // int original = RedstoneHelper.getOutput(getWorld(), getX(), getY(), getZ(), side);
        Block b = getWorld().getBlock(getX(), getY(), getZ());
        int original = Math.max(
                b.isProvidingWeakPower(getWorld(), getX(), getY(), getZ(),
                        Direction.getMovementDirection(ForgeDirection.SOUTH.offsetX, ForgeDirection.SOUTH.offsetZ)),
                        b.isProvidingStrongPower(getWorld(), getX(), getY(), getZ(),
                                Direction.getMovementDirection(ForgeDirection.SOUTH.offsetX, ForgeDirection.SOUTH.offsetZ)));
        return (byte) MathHelper.map(original, 0, 15, 0, 255);
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public MinecraftColor getInsulationColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalBlock() {

        return true;
    }

}
