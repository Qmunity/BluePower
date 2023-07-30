package com.bluepowermod.redstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.wire.redstone.IPropagator;
import com.bluepowermod.api.wire.redstone.IRedConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import net.minecraft.core.Direction;
import oshi.util.tuples.Pair;

@SuppressWarnings("unchecked")
public abstract class RedstonePropagator implements IPropagator<IRedstoneDevice> {

    private List<RedstonePropagator> scheduledPropagations = new ArrayList<RedstonePropagator>();
    private IRedstoneDevice device;
    private Direction side;

    public RedstonePropagator(IRedstoneDevice device, Direction side) {
        this.device = device;
        this.side = side;
    }

    @Override
    public void propagate() {

        if (shouldPreventStackOverflows())
            return;

        doPropagate();

        // for (RedstonePropagator p : scheduledPropagations)
        // p.propagate();
    }

    public IRedstoneDevice getDevice() {

        return device;
    }

    public Direction getSide() {
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

    protected void getPropagation(IRedstoneDevice dev, Direction fromSide, Collection<IConnection<IRedstoneDevice>> propagation) {

        if (dev instanceof IRedstoneConductor) {
            if (dev instanceof IAdvancedRedstoneConductor) {
                ((IAdvancedRedstoneConductor) dev).propagate(fromSide, propagation);
                return;
            } else {
                for (Direction d : Direction.values()) {
                    IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) dev.getRedstoneConnectionCache().getConnectionOnSide(d);
                    if (c != null)
                        propagation.add(c);
                }
                return;
            }
        }

        IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) dev.getRedstoneConnectionCache().getConnectionOnSide(fromSide);
        if (c != null)
            propagation.add(c);
    }

    protected List<IConnection<IRedstoneDevice>> performPropagation() {

        List<IConnection<IRedstoneDevice>> connections = new ArrayList<IConnection<IRedstoneDevice>>();

        IConnection<IRedstoneDevice> firstCon = (IConnection<IRedstoneDevice>) getDevice().getRedstoneConnectionCache()
                .getConnectionOnSide(getSide());

        if (firstCon == null)
            return connections;

        Set<IConnection<IRedstoneDevice>> currentPass = new HashSet<IConnection<IRedstoneDevice>>();
        Set<IConnection<IRedstoneDevice>> nextPass = new HashSet<IConnection<IRedstoneDevice>>();

        currentPass.add(firstCon);

        while (!currentPass.isEmpty()) {
            Iterator<IConnection<IRedstoneDevice>> it = currentPass.iterator();
            while (it.hasNext()) {
                IConnection<IRedstoneDevice> current = it.next();
                it.remove();
                if (connections.contains(current))
                    continue;
                connections.add(current);

                getPropagation(current.getB(), current.getSideB(), nextPass);
            }
            Set<IConnection<IRedstoneDevice>> after = currentPass;
            currentPass = nextPass;
            nextPass = after;
        }

        // List<IConnection<IRedstoneDevice>> current = new ArrayList<IConnection<IRedstoneDevice>>();
        // for (Entry<IConnection<IRedstoneDevice>, Boolean> p : getPropagation(getDevice(), getSide())) {
        // if (p.getValue()) {
        // schedule(new RedPropagator(p.getKey().getB(), p.getKey().getSideB()));
        // } else {
        // if (p.getKey().getB() instanceof IRedstoneConductor
        // && ((IRedstoneConductor) p.getKey().getB()).hasLoss(p.getKey().getSideB()) != (this instanceof LossyPropagator)) {
        // schedule(new RedPropagator(p.getKey().getB(), p.getKey().getSideB()));
        // } else {
        // current.add(p.getKey());
        // }
        // }
        // }
        //
        // if (current.size() == 0 && connections.size() == 0)
        // return connections;
        //
        // List<IConnection<IRedstoneDevice>> newDevices = new ArrayList<IConnection<IRedstoneDevice>>();
        //
        // while (current.size() > 0) {
        // List<Entry<IConnection<IRedstoneDevice>, Boolean>> tmp = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();
        // for (IConnection<IRedstoneDevice> c : current) {
        // tmp.addAll(getPropagation(c.getB(), c.getSideB()));
        //
        // for (Entry<IConnection<IRedstoneDevice>, Boolean> p : tmp) {
        // if (p.getValue()) {
        // schedule(new RedPropagator(p.getKey().getB(), p.getKey().getSideB()));
        // } else if (!connections.contains(p.getKey()) && !newDevices.contains(p.getKey())) {
        // newDevices.add(p.getKey());
        // }
        // }
        //
        // tmp.clear();
        // }
        //
        // connections.addAll(current);
        // current.clear();
        //
        // for (IConnection<IRedstoneDevice> c : newDevices)
        // if (!connections.contains(c))
        // current.add(c);
        //
        // newDevices.clear();
        // }

        return connections;
    }

    protected List<Pair<IRedstoneDevice, Direction>> simplifyDeviceList(List<IConnection<IRedstoneDevice>> connections) {

        List<Pair<IRedstoneDevice, Direction>> l = new ArrayList<Pair<IRedstoneDevice, Direction>>();

        for (IConnection<IRedstoneDevice> c : connections) {
            Pair<IRedstoneDevice, Direction> p1 = new Pair<IRedstoneDevice, Direction>(c.getA(), c.getSideA());
            if (!l.contains(p1) && l != null && p1.getA() != null && p1.getB() != null)
                l.add(p1);
            Pair<IRedstoneDevice, Direction> p2 = new Pair<IRedstoneDevice, Direction>(c.getB(), c.getSideB());
            if (!l.contains(p2) && l != null && p2.getA() != null && p2.getB() != null)
                l.add(p2);
        }

        return l;
    }

    protected boolean shouldPreventStackOverflows() {

        return false;// Thread.getAllStackTraces().size() > 200;
    }

    // Propagator implementation

    public static class LosslessPropagator extends RedstonePropagator {

        public LosslessPropagator(IRedstoneDevice device, Direction side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

            List<IConnection<IRedstoneDevice>> connections = performPropagation();

            if (connections.size() == 0) {
                RedstoneApi.getInstance().setWiresOutputPower(false, false);

                getDevice().setRedstonePower(getSide(), (byte) 0);

                boolean did = RedstoneApi.getInstance().shouldWiresHandleUpdates();
                RedstoneApi.getInstance().setWiresHandleUpdates(false);
                getDevice().onRedstoneUpdate();
                RedstoneApi.getInstance().setWiresHandleUpdates(did);

                RedstoneApi.getInstance().setWiresOutputPower(true, false);

                return;
            }

            List<Pair<IRedstoneDevice, Direction>> l = simplifyDeviceList(connections);

            boolean did = RedstoneApi.getInstance().shouldWiresHandleUpdates();
            RedstoneApi.getInstance().setWiresOutputPower(false, false);
            RedstoneApi.getInstance().setWiresHandleUpdates(false);
            for (Pair<IRedstoneDevice, Direction> p : l)
                p.getA().setRedstonePower(p.getB(), (byte) 0);
            for (Pair<IRedstoneDevice, Direction> p : l)
                p.getA().onRedstoneUpdate();
            RedstoneApi.getInstance().setWiresHandleUpdates(did);
            RedstoneApi.getInstance().setWiresOutputPower(true, false);

            int pow = 0;

            RedstoneApi.getInstance().setWiresOutputPower(false, false);
            for (Pair<IRedstoneDevice, Direction> p : l)
                pow = Math.max(pow, p.getA().getRedstonePower(p.getB()) & 0xFF);
            RedstoneApi.getInstance().setWiresOutputPower(true, false);

            for (Pair<IRedstoneDevice, Direction> p : l)
                p.getA().setRedstonePower(p.getB(), (byte) pow);

            RedstoneApi.getInstance().setWiresHandleUpdates(false);
            for (Pair<IRedstoneDevice, Direction> p : l)
                p.getA().onRedstoneUpdate();
            RedstoneApi.getInstance().setWiresHandleUpdates(did);

            // Clear the arrays to free up memory
            l.clear();
            connections.clear();

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

        public LossyPropagator(IRedstoneDevice device, Direction side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

            List<IConnection<IRedstoneDevice>> connections = performPropagation();

            if (connections.size() == 0) {
                getDevice().setRedstonePower(getSide(), (byte) 0);

                boolean did = RedstoneApi.getInstance().shouldWiresHandleUpdates();
                RedstoneApi.getInstance().setWiresHandleUpdates(false);
                getDevice().onRedstoneUpdate();
                RedstoneApi.getInstance().setWiresHandleUpdates(did);
                RedstoneApi.getInstance().setWiresOutputPower(true, true);

                return;
            }

            List<IRedstoneDevice> devs = new ArrayList<IRedstoneDevice>();
            for (IConnection<IRedstoneDevice> p : connections)
                if (!devs.contains(p.getA()))
                    devs.add(p.getA());

            for (IConnection<IRedstoneDevice> c : connections)
                c.getA().setRedstonePower(c.getSideA(), (byte) 0);

            RedstoneApi.getInstance().setWiresOutputPower(false, true);
            boolean did = RedstoneApi.getInstance().shouldWiresHandleUpdates();
            RedstoneApi.getInstance().setWiresHandleUpdates(false);
            for (IRedstoneDevice d : devs)
                d.onRedstoneUpdate();
            RedstoneApi.getInstance().setWiresHandleUpdates(did);
            RedstoneApi.getInstance().setWiresOutputPower(true, true);

            for (IConnection<IRedstoneDevice> c : connections) {
                RedstoneApi.getInstance().setWiresOutputPower(false, true);
                byte power = c.getA().getRedstonePower(c.getSideA());
                RedstoneApi.getInstance().setWiresOutputPower(true, true);
                if ((power & 0xFF) > 0)
                    propagate(c.getA(), c.getSideA(), power);
            }

            RedstoneApi.getInstance().setWiresHandleUpdates(false);
            for (IRedstoneDevice d : devs)
                d.onRedstoneUpdate();
            RedstoneApi.getInstance().setWiresHandleUpdates(did);

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

        private void propagate(IRedstoneDevice dev, Direction side, byte power) {

            if (shouldPreventStackOverflows())
                return;

            if ((power & 0xFF) <= 0)
                return;
            if ((dev.getRedstonePower(side) & 0xFF) > (power & 0xFF))
                return;

            List<IConnection<IRedstoneDevice>> runList = new ArrayList<IConnection<IRedstoneDevice>>();
            getPropagation(dev, side, runList);
            for (IConnection<IRedstoneDevice> c : runList) {
                c.getA().setRedstonePower(c.getSideA(),
                        !(dev instanceof IRedstoneConductor) ? ((byte) Math.max(0, Math.min((power & 0xFF) + 1, 255))) : power);
                boolean found = false;
                for (RedstonePropagator p : getScheduledPropagations()) {
                    if (p.getDevice() == c.getB() && p.getSide() == c.getSideB()) {
                        found = true;
                        break;
                    }
                }
                if (!found)
                    propagate(c.getB(), c.getSideB(), (byte) ((power & 0xFF) - (dev instanceof IRedstoneConductor ? 1 : 0)));
            }
            runList.clear();
        }
    }

    public static class RedPropagator extends RedstonePropagator {

        public RedPropagator(IRedstoneDevice device, Direction side) {

            super(device, side);
        }

        @Override
        protected void doPropagate() {

            if (getDevice().getLevel() == null || getDevice().getLevel().isClientSide)
                return;

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
                return;
            /*} else if (getDevice() instanceof BlockGateBase) {
                IGateConnection c = ((BlockGateBase) getDevice()).getConnection(getSide());
                IConnection<IRedstoneDevice> con = (IConnection<IRedstoneDevice>) getDevice().getRedstoneConnectionCache().getConnectionOnSide(
                        getSide());
                if (c != null) {
                    if (con != null && con.getB() instanceof IRedConductor && ((IRedConductor) con.getB()).hasLoss(con.getSideB())) {
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

                    return;
                }*/
            }
            try {
                new LosslessPropagator(getDevice(), getSide()).propagate();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

}