package com.bluepowermod.redstone;

import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionListener;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedstoneStorage;
import com.bluepowermod.helper.MathHelper;
import com.bluepowermod.helper.RedstoneHelper;
import com.bluepowermod.tile.TileBase;
import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class RedwireFaceStorage extends RedstoneStorage implements IAdvancedRedstoneConductor, IFace, IConnectionListener {
    IRedwire wire;
    boolean hasUpdated = false;
    public RedwireFaceStorage(IRedwire wire) {
        super(wire);
        this.wire = wire;
    }

    @Override
    public byte getRedstonePower(Direction side) {
        if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
            return 0;

        if (!isAnalogue(side))
            return (byte) ((power & 0xFF) > 0 ? 255 : 0);
        return super.getRedstonePower(side);
    }

    @Override
    public void setRedstonePower(Direction side, byte power) {
        byte pow = hasLoss(side) ? power : (((power & 0xFF) > 0) ? (byte) 255 : (byte) 0);
        hasUpdated = hasUpdated | (pow != this.power);
        super.setRedstonePower(side, power);
    }

    @Override
    public byte getVanillaRedstonePower(Direction side) {
        if (side != null && !wire.canOutputPower(side)) return 0;
        return super.getVanillaRedstonePower(side);
    }

    @Override
    public boolean hasLoss(Direction side) {
        return wire.getRedwireType(side).hasLoss();
    }

    @Override
    public boolean isAnalogue(Direction side) {
        return wire.getRedwireType(side).isAnalogue();
    }

    @Override
    public Direction getFace() {
        return wire instanceof IFace face ? face.getFace() : null;
    }

    @Override
    public boolean canPropagateFrom(Direction fromSide) {
        return true;
    }

    @Override
    public void onRedstoneUpdate() {
        if (hasUpdated) {
            if (wire instanceof TileBase base) base.markBlockForUpdate();

            for (Direction dir : Direction.values()) {
                IConnection<IRedstoneDevice> c = redstoneConnections.getConnectionOnSide(dir);
                IRedstoneDevice dev = null;
                if (c != null)
                    dev = c.getB();
                if (dir == getFace()) {
                    RedstoneHelper.notifyRedstoneUpdate(getLevel(), getBlockPos(), dir, true);
                } else if ((dev == null || dev instanceof DummyRedstoneDevice) && dir != getFace().getOpposite()) {
                    RedstoneHelper.notifyRedstoneUpdate(getLevel(), getBlockPos(), dir, false);
                }
            }

            hasUpdated = false;
        }
    }

    @Override
    public List<Entry<IConnection<IRedstoneDevice>, Boolean>> propagate(Direction fromSide, Collection<IConnection<IRedstoneDevice>> propagation) {

        List<Entry<IConnection<IRedstoneDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();

        for (Direction d : Direction.values()) {
            IConnection<IRedstoneDevice> c = redstoneConnections.getConnectionOnSide(d);
            if (c != null)
                l.add(Map.entry(c, c.getB() instanceof IRedwire redwire
                        && redwire.getRedwireType(c.getSideB()) != wire.getRedwireType(c.getSideA())));
        }

        return l;
    }

    public void onUpdate(){

        // Don't to anything if propagation-related stuff is going on
        if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
            return;

        // Do not do anything if we're on the client
        if (getLevel().isClientSide())
            return;

        // Refresh connections
        redstoneConnections.recalculateConnections();
        // Add bottom device (forced)
        if (redstoneConnections.getConnectionOnSide(getFace()) == null) {
            DummyRedstoneDevice drd = DummyRedstoneDevice.getDeviceAt(getLevel(), this.getBlockPos().relative(getFace()));
            redstoneConnections.onConnect(getFace(), drd, getFace().getOpposite(), com.bluepowermod.api.connect.ConnectionType.STRAIGHT);
            drd.getRedstoneConnectionCache().onConnect(getFace().getOpposite(), this, getFace(), com.bluepowermod.api.connect.ConnectionType.STRAIGHT);
        }

        RedstoneApi.getInstance().getRedstonePropagator(this, getFace()).propagate();
    }

    @Override
    public void onConnect(IConnection<?> connection) {

    }

    @Override
    public void onDisconnect(IConnection<?> connection) {

    }
}
