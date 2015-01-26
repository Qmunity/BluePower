package com.bluepowermod.redstone.propagation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;

import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.redstone.IPropagator;
import com.bluepowermod.api.wire.redstone.IRedConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;

@SuppressWarnings("unchecked")
public abstract class RedstonePropagator implements IPropagator<IRedstoneDevice> {

    private List<RedstonePropagator> scheduledPropagations = new ArrayList<RedstonePropagator>();
    private IRedstoneDevice device;
    private ForgeDirection side;

    public RedstonePropagator(IRedstoneDevice device, ForgeDirection side) {

        this.device = device;
        this.side = side;
    }

    @Override
    public void propagate() {

        doPropagate();

        if (Thread.getAllStackTraces().size() > 100)
            return;

        for (RedstonePropagator p : scheduledPropagations)
            p.propagate();
    }

    public IRedstoneDevice getDevice() {

        return device;
    }

    public ForgeDirection getSide() {

        return side;
    }

    protected void schedule(RedstonePropagator propagation) {

        scheduledPropagations.add(propagation);
    }

    protected abstract void doPropagate();

    // Utilities

    protected Collection<Entry<IConnection<IRedstoneDevice>, Boolean>> getPropagation(IRedstoneDevice dev, ForgeDirection fromSide) {

        if (dev instanceof IRedstoneConductor) {
            if (dev instanceof IAdvancedRedstoneConductor) {
                return ((IAdvancedRedstoneConductor) dev).propagate(fromSide);
            } else {
                List<Entry<IConnection<IRedstoneDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();

                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) dev.getRedstoneConnectionCache().getConnectionOnSide(d);
                    if (c != null)
                        l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(c, false));
                }

                return l;
            }
        }

        IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) dev.getRedstoneConnectionCache().getConnectionOnSide(fromSide);
        if (c != null)
            return Arrays.asList((Entry<IConnection<IRedstoneDevice>, Boolean>) new Pair<IConnection<IRedstoneDevice>, Boolean>(c, false));

        return Arrays.asList();
    }

    // Propagator implementation

    public static class LosslessPropagator extends RedstonePropagator {

        private List<IConnection<IRedstoneDevice>> connections = new ArrayList<IConnection<IRedstoneDevice>>();

        public LosslessPropagator(IRedstoneDevice device, ForgeDirection side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

            RedstoneApi.getInstance().setWiresOutputPower(false);

            findConnections();

            List<Pair<IRedstoneDevice, ForgeDirection>> l = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();

            for (IConnection<IRedstoneDevice> c : connections) {
                Pair<IRedstoneDevice, ForgeDirection> p1 = new Pair<IRedstoneDevice, ForgeDirection>(c.getA(), c.getSideA());
                if (!l.contains(p1) && l != null && p1.getKey() != null && p1.getValue() != null)
                    l.add(p1);
                Pair<IRedstoneDevice, ForgeDirection> p2 = new Pair<IRedstoneDevice, ForgeDirection>(c.getB(), c.getSideB());
                if (!l.contains(p2) && l != null && p2.getKey() != null && p2.getValue() != null)
                    l.add(p2);
            }

            int pow = 0;

            for (Pair<IRedstoneDevice, ForgeDirection> p : l)
                pow = Math.max(pow, p.getKey().getRedstonePower(p.getValue()) & 0xFF);

            for (Pair<IRedstoneDevice, ForgeDirection> p : l)
                p.getKey().setRedstonePower(p.getValue(), (byte) pow);

            for (Pair<IRedstoneDevice, ForgeDirection> p : l)
                p.getKey().onRedstoneUpdate();

            // Clear the arrays to free up memory
            l.clear();
            connections.clear();

            RedstoneApi.getInstance().setWiresOutputPower(true);
        }

        private void findConnections() {

            IConnection<IRedstoneDevice> firstCon = (IConnection<IRedstoneDevice>) getDevice().getRedstoneConnectionCache()
                    .getConnectionOnSide(getSide());
            if (firstCon == null)
                return;

            connections.add(firstCon);

            List<IConnection<IRedstoneDevice>> current = new ArrayList<IConnection<IRedstoneDevice>>();
            for (Entry<IConnection<IRedstoneDevice>, Boolean> p : getPropagation(getDevice(), getSide())) {
                if (p.getValue()) {
                    schedule(new RedPropagator(p.getKey().getB(), p.getKey().getSideB()));
                } else {
                    current.add(p.getKey());
                }
            }

            List<IConnection<IRedstoneDevice>> newDevices = new ArrayList<IConnection<IRedstoneDevice>>();

            while (current.size() > 0) {
                List<Entry<IConnection<IRedstoneDevice>, Boolean>> tmp = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();
                for (IConnection<IRedstoneDevice> c : current) {
                    tmp.addAll(getPropagation(c.getB(), c.getSideB()));

                    for (Entry<IConnection<IRedstoneDevice>, Boolean> p : tmp) {
                        if (p.getValue()) {
                            schedule(new RedPropagator(p.getKey().getB(), p.getKey().getSideB()));
                        } else if (!connections.contains(p)) {
                            newDevices.add(p.getKey());
                        }
                    }

                    tmp.clear();
                }

                connections.addAll(current);
                current.clear();

                for (IConnection<IRedstoneDevice> c : newDevices)
                    if (!connections.contains(c))
                        current.add(c);

                newDevices.clear();
            }
        }

    }

    public static class LossyPropagator extends RedstonePropagator {

        public LossyPropagator(IRedstoneDevice device, ForgeDirection side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

        }

    }

    public static class RedPropagator extends RedstonePropagator {

        public RedPropagator(IRedstoneDevice device, ForgeDirection side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

            if (getDevice() instanceof IRedConductor)
                if (((IRedConductor) getDevice()).isAnalog(getSide()))
                    return;

            try {
                new LosslessPropagator(getDevice(), getSide()).propagate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

}
