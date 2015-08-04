package com.bluepowermod.redstone;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.wire.redstone.IPropagator;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedwireType;

@SuppressWarnings("unchecked")
public class RedstonePropagator implements IPropagator<IRedstoneDevice> {

    private List<RedstonePropagator> scheduledPropagations = new ArrayList<RedstonePropagator>();
    private IRedstoneDevice device;
    private ForgeDirection side;

    public RedstonePropagator(IRedstoneDevice device, ForgeDirection side) {

        this.device = device;
        this.side = side;
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

    private <T extends Collection<IConnection<IRedstoneDevice>>> T getPropagation(IRedstoneDevice dev, ForgeDirection fromSide,
            T propagation) {

        if (dev instanceof IRedstoneConductor) {
            if (dev instanceof IAdvancedRedstoneConductor) {
                ((IAdvancedRedstoneConductor) dev).propagate(fromSide, propagation);
                return propagation;
            } else {
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) dev.getRedstoneConnectionCache().getConnectionOnSide(d);
                    if (c != null)
                        propagation.add(c);
                }

                return propagation;
            }
        }

        IConnection<IRedstoneDevice> c = (IConnection<IRedstoneDevice>) dev.getRedstoneConnectionCache().getConnectionOnSide(fromSide);
        if (c != null)
            propagation.add(c);

        return propagation;
    }

    @Override
    public void propagate() {

        if (!(device instanceof IRedstoneConductor)) {
            IConnection<? extends IRedstoneDevice> c = device.getRedstoneConnectionCache().getConnectionOnSide(side);
            if (c == null)
                return;
            byte pow = device.getRedstonePower(c.getSideA());
            byte nPow = c.getB().getRedstonePower(c.getSideB());
            if (pow == nPow)
                return;

            int strength = 0;
            if (c.getB() instanceof IRedwire) {
                RedwireType t = ((IRedwire) c.getB()).getRedwireType(c.getSideB());
                if (t != null && !t.hasLoss())
                    strength = 255;
            }

            if ((pow & 0xFF) > (nPow & 0xFF))
                propagateHigh(device, side, pow, strength);
            else
                propagateLow(device, side, pow, strength);
        }

    }

    private void propagateHigh(IRedstoneDevice dev, ForgeDirection side, byte power, int strength) {

        if ((power & 0xFF) <= 0)
            return;
        if ((dev.getRedstonePower(side) & 0xFF) > (power & 0xFF))
            return;

        List<IConnection<IRedstoneDevice>> runList = getPropagation(dev, side, new ArrayList<IConnection<IRedstoneDevice>>());
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
                propagateHigh(c.getB(), c.getSideB(),
                        (byte) ((power & 0xFF) - (dev instanceof IRedstoneConductor && strength == 0 ? 1 : 0)), Math.max(strength - 1, 0));
        }
        runList.clear();
    }

    private void propagateLow(IRedstoneDevice dev, ForgeDirection side, byte power, int strength) {

        if ((power & 0xFF) <= 0)
            return;
        if ((dev.getRedstonePower(side) & 0xFF) > (power & 0xFF))
            return;
    }
}