package com.bluepowermod.redstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.IConnectionCache;
import com.bluepowermod.api.wire.redstone.IBundledConductor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IInsulatedRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.IRedwire.IInsulatedRedwire;

public class BundledDeviceWrapper implements IAdvancedRedstoneConductor {

    private static List<BundledDeviceWrapper> l = new ArrayList<BundledDeviceWrapper>();

    public static BundledDeviceWrapper wrap(IBundledDevice device, MinecraftColor color) {

        for (BundledDeviceWrapper w : l)
            if (w.device.equals(device) && w.color == color)
                return w;

        BundledDeviceWrapper w = new BundledDeviceWrapper(device, color);
        l.add(w);
        return w;
    }

    private IBundledDevice device;
    private MinecraftColor color;

    private RedstoneConnectionCacheWrapper connections = new RedstoneConnectionCacheWrapper(this);

    private BundledDeviceWrapper(IBundledDevice device, MinecraftColor color) {

        this.device = device;
        this.color = color;
    }

    @Override
    public World getWorld() {

        return device.getWorld();
    }

    @Override
    public int getX() {

        return device.getX();
    }

    @Override
    public int getY() {

        return device.getY();
    }

    @Override
    public int getZ() {

        return device.getZ();
    }

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice dev, ConnectionType type) {

        return true;
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {

        return connections;
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        byte[] b = device.getBundledOutput(side);

        if (b == null)
            return 0;

        return b[color.ordinal()];
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        byte[] b = device.getBundledPower(side);
        if (b == null)
            b = new byte[16];

        b[color.ordinal()] = power;

        device.setBundledPower(side, b);
    }

    @Override
    public void onRedstoneUpdate() {

        device.onBundledUpdate();
    }

    @Override
    public boolean canPropagateFrom(ForgeDirection fromSide) {

        if (!(device instanceof IBundledConductor))
            return false;

        return ((IBundledConductor) device).canPropagateBundledFrom(fromSide);
    }

    @Override
    public boolean hasLoss(ForgeDirection side) {

        if (!(device instanceof IBundledConductor))
            return false;

        return ((IBundledConductor) device).hasLoss(side);
    }

    @Override
    public boolean isAnalogue(ForgeDirection side) {

        if (!(device instanceof IBundledConductor))
            return false;

        return ((IBundledConductor) device).isAnalogue(side);
    }

    @Override
    public Collection<Entry<IConnection<IRedstoneDevice>, Boolean>> propagate(ForgeDirection fromSide) {

        List<Entry<IConnection<IRedstoneDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            IConnection<IRedstoneDevice> c = connections.getConnectionOnSide(d);
            if (c != null)
                l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(c, c.getB() instanceof IRedwire && device instanceof IRedwire
                        && ((IRedwire) c.getB()).getRedwireType(c.getSideB()) != ((IRedwire) device).getRedwireType(c.getSideA())));
        }

        return l;
    }

    @Override
    public boolean isNormalFace(ForgeDirection side) {

        return device.isNormalFace(side);
    }

    @SuppressWarnings("rawtypes")
    private class RedstoneConnectionCacheWrapper extends RedstoneConnectionCache {

        private BundledDeviceWrapper wrapper;

        private IConnection[] originalCons = new IConnection[7];
        private IConnection[] cons = new IConnection[7];

        public RedstoneConnectionCacheWrapper(BundledDeviceWrapper dev) {

            super(dev);

            wrapper = dev;
        }

        @SuppressWarnings("unchecked")
        @Override
        public IConnection<IRedstoneDevice> getConnectionOnSide(ForgeDirection side) {

            if (wrapper.device instanceof IInsulatedRedwire && ((IRedstoneDevice) wrapper.device).getRedstoneConnectionCache() != null) {
                return (IConnection<IRedstoneDevice>) ((IRedstoneDevice) wrapper.device).getRedstoneConnectionCache().getConnectionOnSide(
                        side);
            } else {
                IConnection<? extends IBundledDevice> original = wrapper.device.getBundledConnectionCache().getConnectionOnSide(side);

                if (original != originalCons[side.ordinal()]) {
                    if (original != null) {
                        if (!(original.getB() instanceof IInsulatedRedstoneDevice)
                                || (original.getB() instanceof IInsulatedRedstoneDevice && wrapper.color
                                        .equals(((IInsulatedRedstoneDevice) original.getB()).getInsulationColor(original.getSideB()))))
                            cons[side.ordinal()] = new RedstoneConnection(BundledDeviceWrapper.this, wrap(original.getB(), color), side,
                                    original.getSideB(), original.getType());
                        else
                            cons[side.ordinal()] = null;
                    } else {
                        cons[side.ordinal()] = null;
                    }
                    originalCons[side.ordinal()] = original;
                }

                return cons[side.ordinal()];
            }
        }

        @Override
        public void recalculateConnections() {

        }

    }

}
