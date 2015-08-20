package com.bluepowermod.part.gate.ic;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.redstone.BundledConnectionCache;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;

public class RSTileIC extends TileEntity implements IRedstoneDevice, IBundledDevice {

    private RedstoneConnectionCache cacheRedstone = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
    private BundledConnectionCache cacheBundled = RedstoneApi.getInstance().createBundledConnectionCache(this);

    protected GateIntegratedCircuit ic;
    protected int x, z;

    protected Dir dir;
    protected int i;

    protected byte rsRW, rsFW;
    protected byte[] bRW, bFW;

    public RSTileIC(GateIntegratedCircuit ic, Dir dir, int i) {

        this.ic = ic;

        this.dir = dir;
        this.i = i;

        if (dir == Dir.FRONT) {
            x = i;
            z = -1;
        } else if (dir == Dir.BACK) {
            x = ic.getSize() - 1 - i;
            z = ic.getSize();
        } else if (dir == Dir.LEFT) {
            x = -1;
            z = ic.getSize() - 1 - i;
        } else if (dir == Dir.RIGHT) {
            x = ic.getSize();
            z = i;
        }
    }

    @Override
    public World getWorld() {

        return ic.load();
    }

    @Override
    public int getX() {

        return x;
    }

    @Override
    public int getY() {

        return 64;
    }

    @Override
    public int getZ() {

        return z;
    }

    @Override
    public boolean canConnect(ForgeDirection side, IBundledDevice dev, ConnectionType type) {

        if (side != dir.getFD().getOpposite())
            return false;

        return ic.modes[dir.ordinal()][i] == 3 || ic.modes[dir.ordinal()][i] == 4;
    }

    @Override
    public IConnectionCache<? extends IBundledDevice> getBundledConnectionCache() {

        return cacheBundled;
    }

    @Override
    public byte[] getBundledOutput(ForgeDirection side) {

        if (ic.modes[dir.ordinal()][i] == 3)
            return bRW;
        return new byte[16];
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        return bFW;
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        bFW = power;
    }

    @Override
    public void onBundledUpdate() {

        onRedstoneUpdate();
    }

    @Override
    public MinecraftColor getBundledColor(ForgeDirection side) {

        return null;
    }

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice dev, ConnectionType type) {

        if (side != dir.getFD().getOpposite())
            return false;

        return ic.modes[dir.ordinal()][i] == 1 || ic.modes[dir.ordinal()][i] == 2;
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {

        return cacheRedstone;
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (ic.modes[dir.ordinal()][i] == 1)
            return rsRW;
        return 0;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        rsFW = power;
    }

    @Override
    public void onRedstoneUpdate() {

        if (ic.modes[dir.ordinal()][i] == 2)
            RedstoneApi.getInstance().getRedstonePropagator(ic, dir.toForgeDirection(ic.getFace(), ic.getRotation())).propagate();
        else if (ic.modes[dir.ordinal()][i] == 4)
            RedstoneApi.getInstance().getBundledPropagator(ic, dir.toForgeDirection(ic.getFace(), ic.getRotation())).propagate();
    }

    public void repropagate() {

        if (ic.modes[dir.ordinal()][i] == 1)
            RedstoneApi.getInstance().getRedstonePropagator(this, dir.getFD().getOpposite()).propagate();
        else if (ic.modes[dir.ordinal()][i] == 3)
            RedstoneApi.getInstance().getBundledPropagator(this, dir.getFD().getOpposite()).propagate();
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return true;
    }

    public void notifyUpdate() {

        getWorld().notifyBlockChange(getX(), getY(), getZ(), Blocks.stone);
    }

}
