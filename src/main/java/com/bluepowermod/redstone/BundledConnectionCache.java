package com.bluepowermod.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.connect.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import net.minecraft.core.Direction;

public class BundledConnectionCache implements IConnectionCache<IBundledDevice> {

    private IBundledDevice dev;
    private BundledConnection[] connections = new BundledConnection[7];
    private IConnectionListener listener;

    public BundledConnectionCache(IBundledDevice dev) {

        this.dev = dev;
    }

    @Override
    public IBundledDevice getSelf() {

        return dev;
    }

    @Override
    public IConnection<IBundledDevice> getConnectionOnSide(Direction side) {

        return connections[side.ordinal()];
    }

    @Override
    public void onConnect(Direction side, IBundledDevice connectable, Direction connectableSide, ConnectionType type) {

        BundledConnection con = connections[side.ordinal()] = RedstoneApi.getInstance().createConnection(getSelf(), connectable, side,
                connectableSide, type);
        if (listener != null)
            listener.onConnect(con);
    }

    @Override
    public void onDisconnect(Direction side) {

        BundledConnection con = connections[side.ordinal()];
        connections[side.ordinal()] = null;
        if (listener != null)
            listener.onDisconnect(con);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void recalculateConnections() {
        IBundledDevice self = getSelf();
        for (Direction d : Direction.values()) {
            boolean wasConnected = connections[d.ordinal()] != null;
            BundledConnection con = RedConnectionHelper.getBundledNeighbor(self, d);
            if (con != null) {
                if (!wasConnected || connections[d.ordinal()].getB() != con.getB() || connections[d.ordinal()].getSideB() != con.getSideB()
                        || connections[d.ordinal()].getType() != con.getType()) {
                    onConnect(con.getSideA(), con.getB(), con.getSideB(), con.getType());
                    ((IConnectionCache<IBundledDevice>) con.getB().getBundledConnectionCache()).onConnect(con.getSideB(), con.getA(), con.getSideA(),
                            con.getType());

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
        if (dev instanceof IConnectionListener)
            listener = (IConnectionListener) dev;
    }

}