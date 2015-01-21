package com.bluepowermod.part.wire.redstone.propagation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;

public class BundledDeviceWrapper implements IRedstoneDevice, IRedstoneConductor {

    private static Map<Pair<IBundledDevice, MinecraftColor>, BundledDeviceWrapper> cache //
    = new HashMap<Pair<IBundledDevice, MinecraftColor>, BundledDeviceWrapper>();

    public static BundledDeviceWrapper getWrapper(IBundledDevice dev, MinecraftColor color) {

        Pair<IBundledDevice, MinecraftColor> p = new Pair<IBundledDevice, MinecraftColor>(dev, color);
        for (Pair<IBundledDevice, MinecraftColor> pa : cache.keySet())
            if (pa.getKey().equals(dev) && pa.getValue() == color)
                return cache.get(pa);

        BundledDeviceWrapper w = null;
        if (dev instanceof IBundledConductor) {
            w = new BundledConductorWrapper((IBundledConductor) dev, color);
        } else {
            w = new BundledDeviceWrapper(dev, color);
        }
        cache.put(p, w);
        return w;
    }

    public static void clearCache() {

        cache.clear();
    }

    private IBundledDevice dev;
    private MinecraftColor color;

    protected BundledDeviceWrapper(IBundledDevice dev, MinecraftColor color) {

        this.dev = dev;
        this.color = color;
    }

    @Override
    public World getWorld() {

        return dev.getWorld();
    }

    @Override
    public int getX() {

        return dev.getX();
    }

    @Override
    public int getY() {

        return dev.getY();
    }

    @Override
    public int getZ() {

        return dev.getZ();
    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        // Not intended to be used
        return false;
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        // Not intended to be used
        return false;
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        // Not intended to be used
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        // Not intended to be used
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        IBundledDevice d = dev.getBundledDeviceOnSide(side);
        if (d != null)
            return getWrapper(d, color);
        return null;
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        return dev.getBundledOutput(side)[color.ordinal()];
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        byte[] pow = dev.getBundledPower(side);
        pow[color.ordinal()] = power;
        dev.setBundledPower(side, pow);
    }

    @Override
    public void onRedstoneUpdate() {

        dev.onBundledUpdate();
    }

    @Override
    public MinecraftColor getInsulationColor(ForgeDirection side) {

        return color;
    }

    @Override
    public boolean isNormalBlock() {

        return dev.isNormalBlock();
    }

    @Override
    public boolean hasLoss() {

        return false;
    }

    @Override
    public boolean isAnalog() {

        return false;
    }

    @Override
    public List<Pair<IRedstoneDevice, ForgeDirection>> propagate(ForgeDirection fromSide) {

        List<Pair<IRedstoneDevice, ForgeDirection>> list = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();

        if (dev instanceof IRedstoneDevice) {
            MinecraftColor color = ((IRedstoneDevice) dev).getInsulationColor(fromSide);
            if (color != null && !color.matches(getInsulationColor(fromSide)))
                return list;
        }

        if (dev instanceof IRedstoneDevice) {
            list.add(new Pair<IRedstoneDevice, ForgeDirection>((IRedstoneDevice) dev, fromSide));
            if (dev instanceof IRedstoneConductor)
                list.addAll(((IRedstoneConductor) dev).propagate(fromSide));
        }

        return list;
    }

}
