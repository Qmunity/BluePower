package com.bluepowermod.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.IConnectionCache;
import com.bluepowermod.api.wire.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.part.wire.redstone.WireHelper;

public class BundledConnectionCache implements IConnectionCache<IBundledDevice> {

    private IBundledDevice dev;
    private BundledConnection[] connections = new BundledConnection[7];
    private boolean listening = false;

    public BundledConnectionCache(IBundledDevice dev) {

        this.dev = dev;
    }

    @Override
    public IBundledDevice getSelf() {

        return dev;
    }

    @Override
    public IConnection<IBundledDevice> getConnectionOnSide(ForgeDirection side) {

        return connections[side.ordinal()];
    }

    @Override
    public void onConnect(ForgeDirection side, IBundledDevice connectable, ForgeDirection connectableSide, ConnectionType type) {

        BundledConnection con = connections[side.ordinal()] = RedstoneApi.getInstance().createConnection(getSelf(), connectable, side,
                connectableSide, type);
        if (listening)
            ((IConnectionListener) dev).onConnect(con);
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        BundledConnection con = connections[side.ordinal()];
        connections[side.ordinal()] = null;
        if (listening)
            ((IConnectionListener) dev).onDisconnect(con);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void recalculateConnections() {

        if (dev.getWorld().isRemote)
            return;

        IBundledDevice self = getSelf();
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            boolean wasConnected = connections[d.ordinal()] != null;
            BundledConnection con = WireHelper.getBundledNeighbor(self, d);
            if (con != null) {
                if (!wasConnected || connections[d.ordinal()].getB() != con.getB() || connections[d.ordinal()].getSideB() != con.getSideB()
                        || connections[d.ordinal()].getType() != con.getType()) {
                    onConnect(con.getSideA(), con.getB(), con.getSideB(), con.getType());
                    ((IConnectionCache<IBundledDevice>) con.getB().getBundledConnectionCache()).onConnect(con.getSideB(), con.getA(),
                            con.getSideA(), con.getType());

                    IConnection<IBundledDevice> con2 = (IConnection<IBundledDevice>) con.getB().getBundledConnectionCache()
                            .getConnectionOnSide(con.getSideB());
                    con = connections[d.ordinal()];

                    if (con == null)
                        return;

                    con.setComplementaryConnection(con2);
                    con2.setComplementaryConnection(con);
                }
            } else if (wasConnected) {
                con = connections[d.ordinal()];

                if (con != null) {
                    onDisconnect(con.getSideA());
                    con.getB().getBundledConnectionCache().onDisconnect(con.getSideB());
                }
            }
        }
    }

    @Override
    public void disconnectAll() {

        for (BundledConnection con : connections) {
            if (con == null)
                continue;
            con.getB().getBundledConnectionCache().onDisconnect(con.getSideB());
            onDisconnect(con.getSideA());
        }
    }

    @Override
    public void listen() {

        listening = dev instanceof IConnectionListener;
    }

}
