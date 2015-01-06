package com.bluepowermod.compat.cc;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledDevice;

import dan200.computercraft.api.ComputerCraftAPI;

public class BundledDeviceCCComputer implements IBundledDevice {

    private static List<BundledDeviceCCComputer> list = new ArrayList<BundledDeviceCCComputer>();

    public static BundledDeviceCCComputer getDeviceAt(World world, int x, int y, int z) {

        Vec3i loc = new Vec3i(x, y, z, world);

        for (BundledDeviceCCComputer c : list)
            if (c.loc.equals(loc))
                return c;

        BundledDeviceCCComputer c = new BundledDeviceCCComputer(loc);
        list.add(c);
        return c;
    }

    private Vec3i loc;

    private IBundledDevice[] devices = new IBundledDevice[6];

    private byte[][] curPow = new byte[6][16];

    public BundledDeviceCCComputer(Vec3i loc) {

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
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        return side != ForgeDirection.UNKNOWN;
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        return false;// side != ForgeDirection.UNKNOWN;
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

        int out = ComputerCraftAPI.getBundledRedstoneOutput(getWorld(), getX(), getY(), getZ(), side.ordinal());

        if (out < 0)
            return new byte[16];

        return CCUtils.unpackDigital(out);
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        if (side == ForgeDirection.UNKNOWN)
            return;

        curPow[side.ordinal()] = power;
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        return getBundledOutput(side);
    }

    @Override
    public void onBundledUpdate() {

        getWorld().notifyBlockOfNeighborChange(getX(), getY(), getZ(), Blocks.air);
    }

    public byte[] getCurPow(ForgeDirection side) {

        return curPow[side.ordinal()];
    }

    @Override
    public MinecraftColor getBundledColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isBundled() {

        return true;
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

}
