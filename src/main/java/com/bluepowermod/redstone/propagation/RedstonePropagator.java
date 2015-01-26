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

        if (shouldPreventStackOverflows())
            return;

        doPropagate();

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

    protected List<RedstonePropagator> getScheduledPropagations() {

        return scheduledPropagations;
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

    protected List<IConnection<IRedstoneDevice>> performPropagation() {

        List<IConnection<IRedstoneDevice>> connections = new ArrayList<IConnection<IRedstoneDevice>>();

        IConnection<IRedstoneDevice> firstCon = (IConnection<IRedstoneDevice>) getDevice().getRedstoneConnectionCache()
                .getConnectionOnSide(getSide());
        if (firstCon == null)
            return connections;

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

        return connections;
    }

    protected List<Pair<IRedstoneDevice, ForgeDirection>> simplifyDeviceList(List<IConnection<IRedstoneDevice>> connections) {

        List<Pair<IRedstoneDevice, ForgeDirection>> l = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();

        for (IConnection<IRedstoneDevice> c : connections) {
            Pair<IRedstoneDevice, ForgeDirection> p1 = new Pair<IRedstoneDevice, ForgeDirection>(c.getA(), c.getSideA());
            if (!l.contains(p1) && l != null && p1.getKey() != null && p1.getValue() != null)
                l.add(p1);
            Pair<IRedstoneDevice, ForgeDirection> p2 = new Pair<IRedstoneDevice, ForgeDirection>(c.getB(), c.getSideB());
            if (!l.contains(p2) && l != null && p2.getKey() != null && p2.getValue() != null)
                l.add(p2);
        }

        return l;
    }

    protected boolean shouldPreventStackOverflows() {

        return Thread.getAllStackTraces().size() > 500;
    }

    // Propagator implementation

    public static class LosslessPropagator extends RedstonePropagator {

        public LosslessPropagator(IRedstoneDevice device, ForgeDirection side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

            RedstoneApi.getInstance().setWiresOutputPower(false, false);

            List<IConnection<IRedstoneDevice>> connections = performPropagation();
            List<Pair<IRedstoneDevice, ForgeDirection>> l = simplifyDeviceList(connections);

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

            RedstoneApi.getInstance().setWiresOutputPower(true, false);

            List<RedstonePropagator> scheduled = new ArrayList<RedstonePropagator>();
            scheduled.addAll(getScheduledPropagations());
            getScheduledPropagations().clear();

            for (RedstonePropagator p : scheduled) {
                IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) p.getDevice().getRedstoneConnectionCache()
                        .getConnectionOnSide(p.getSide());
                if (c.getB().getRedstonePower(c.getSideB()) != c.getA().getRedstonePower(c.getSideA()))
                    schedule(p);
            }
        }

    }

    public static class LossyPropagator extends RedstonePropagator {

        public LossyPropagator(IRedstoneDevice device, ForgeDirection side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

            System.out.println("Hey!");

            List<IConnection<IRedstoneDevice>> connections = performPropagation();
            List<Pair<IRedstoneDevice, ForgeDirection>> l = simplifyDeviceList(connections);

            for (Pair<IRedstoneDevice, ForgeDirection> p : l)
                p.getKey().setRedstonePower(p.getValue(), (byte) 0);

            for (Pair<IRedstoneDevice, ForgeDirection> pair : l) {
                RedstoneApi.getInstance().setWiresOutputPower(false, true);
                byte power = pair.getKey().getRedstonePower(pair.getValue());
                RedstoneApi.getInstance().setWiresOutputPower(true, true);
                if ((power & 0xFF) > 0)
                    propagate(pair.getKey(), pair.getValue(), power);
            }

            for (Pair<IRedstoneDevice, ForgeDirection> p : l)
                p.getKey().onRedstoneUpdate();

            List<RedstonePropagator> scheduled = new ArrayList<RedstonePropagator>();
            scheduled.addAll(getScheduledPropagations());
            getScheduledPropagations().clear();

            for (RedstonePropagator p : scheduled) {
                IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) p.getDevice().getRedstoneConnectionCache()
                        .getConnectionOnSide(p.getSide());
                if (c.getB().getRedstonePower(c.getSideB()) != c.getA().getRedstonePower(c.getSideA()))
                    schedule(p);
            }
        }

        private void propagate(IRedstoneDevice dev, ForgeDirection side, byte power) {

            if (shouldPreventStackOverflows())
                return;

            if ((power & 0xFF) < 0)
                return;
            if ((dev.getRedstonePower(side) & 0xFF) > (power & 0xFF))
                return;

            for (Entry<IConnection<IRedstoneDevice>, Boolean> e : getPropagation(dev, side)) {
                e.getKey().getA().setRedstonePower(e.getKey().getSideA(), power);
                boolean found = false;
                for (RedstonePropagator p : getScheduledPropagations()) {
                    if (p.getDevice() == e.getKey().getB() && p.getSide() == e.getKey().getSideB()) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    propagate(e.getKey().getB(), e.getKey().getSideB(), (byte) ((power & 0xFF) - 1));
            }
        }

    }

    public static class RedPropagator extends RedstonePropagator {

        public RedPropagator(IRedstoneDevice device, ForgeDirection side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

            if (getDevice() instanceof IRedConductor) {
                if (((IRedConductor) getDevice()).hasLoss(getSide())) {
                    try {
                        new LossyPropagator(getDevice(), getSide()).propagate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else {
                    try {
                        new LosslessPropagator(getDevice(), getSide()).propagate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                try {
                    new LosslessPropagator(getDevice(), getSide()).propagate();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }

    }

}
