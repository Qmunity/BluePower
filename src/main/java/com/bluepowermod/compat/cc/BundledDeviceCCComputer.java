package com.bluepowermod.compat.cc;

/*import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.redstone.BundledConnectionCache;
import com.bluepowermod.redstone.RedstoneApi;
import dan200.computercraft.api.ComputerCraftAPI;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

;

public class BundledDeviceCCComputer implements IBundledDevice {

    private static List<BundledDeviceCCComputer> list = new ArrayList<BundledDeviceCCComputer>();

    public static BundledDeviceCCComputer getDeviceAt(World world, BlockPos pos) {
        for (BundledDeviceCCComputer c : list)
            if (c.loc.equals(pos))
                return c;

        BundledDeviceCCComputer c = new BundledDeviceCCComputer(world, pos);
        list.add(c);
        return c;
    }

    private BlockPos loc;
    private World world;
    private byte[][] curPow = new byte[6][16];
    private BundledConnectionCache connections = RedstoneApi.getInstance().createBundledConnectionCache(this);

    public BundledDeviceCCComputer(World world, BlockPos loc) {
        this.world = world;
        this.loc = loc;
    }

    @Override
    public World getWorld() {

        return world;

    }

    @Override
    public BlockPos getPos() {

        return loc;
    }
    @Override
    public boolean canConnect(EnumFacing side, IBundledDevice dev, ConnectionType type) {

        return type == ConnectionType.STRAIGHT || side != null;
    }

    @Override
    public IConnectionCache<? extends IBundledDevice> getBundledConnectionCache() {

        return connections;
    }

    @Override
    public byte[] getBundledOutput(EnumFacing side) {

        int out = ComputerCraftAPI.getBundledRedstoneOutput(getWorld(), getPos(), side.ordinal());

        if (out < 0)
            return new byte[16];

        return CCUtils.unpackDigital(out);
    }

    @Override
    public void setBundledPower(EnumFacing side, byte[] power) {

        if (side == null)
            return;

        curPow[side.ordinal()] = power;
    }

    @Override
    public byte[] getBundledPower(EnumFacing side) {

        return getBundledOutput(side);
    }

    @Override
    public void onBundledUpdate() {

        getWorld().updateNeighborsAt(getPos(), Blocks.AIR, true);
    }

    public byte[] getCurPow(EnumFacing side) {

        return curPow[side.ordinal()];
    }

    @Override
    public MinecraftColor getBundledColor(EnumFacing side) {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalFace(EnumFacing side) {

        return true;
    }

}*/
