package com.bluepowermod.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.connect.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import net.minecraft.core.Direction;

public class RedstoneConnectionCache implements IConnectionCache<IRedstoneDevice> {

    private IRedstoneDevice dev;
    private RedstoneConnection[] connections = new RedstoneConnection[7];
    private boolean listening = false;

    public RedstoneConnectionCache(IRedstoneDevice dev) {
        this.dev = dev;
    }

    @Override
    public IRedstoneDevice getSelf() {

        return dev;
    }

    @Override
    public IConnection<IRedstoneDevice> getConnectionOnSide(Direction side) {
        return connections[side.ordinal()];
    }

    @Override
    public void onConnect(Direction side, IRedstoneDevice connectable, Direction connectableSide, ConnectionType type) {

        RedstoneConnection con = connections[side.ordinal()] = RedstoneApi.getInstance().createConnection(getSelf(), connectable, side,
                connectableSide, type);
        if (listening)
            ((IConnectionListener) dev).onConnect(con);
    }

    @Override
    public void onDisconnect(Direction side) {

        RedstoneConnection con = connections[side.ordinal()];
        connections[side.ordinal()] = null;
        if (listening)
            ((IConnectionListener) dev).onDisconnect(con);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void recalculateConnections() {
        IRedstoneDevice self = getSelf();
        for (Direction d : Direction.values()) {
            boolean wasConnected = connections[d.ordinal()] != null;
            RedstoneConnection con = RedConnectionHelper.getNeighbor(self, d);
            if (con != null) {
                if (!wasConnected || connections[d.ordinal()].getB() != con.getB() || connections[d.ordinal()].getSideB() != con.getSideB()
                        || connections[d.ordinal()].getType() != con.getType()) {
                    onConnect(con.getSideA(), con.getB(), con.getSideB(), con.getType());
                    ((IConnectionCache<IRedstoneDevice>) con.getB().getRedstoneConnectionCache()).onConnect(con.getSideB(), con.getA(),
                            con.getSideA(), con.getType());

                    IConnection<IRedstoneDevice> con2 = (IConnection<IRedstoneDevice>) con.getB().getRedstoneConnectionCache()
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
                    con.getB().getRedstoneConnectionCache().onDisconnect(con.getSideB());
                }
            }
        }
    }

    @Override
    public void disconnectAll() {

        for (RedstoneConnection con : connections) {
            if (con == null)
                continue;
            con.getB().getRedstoneConnectionCache().onDisconnect(con.getSideB());
            onDisconnect(con.getSideA());
        }
    }

    @Override
    public void listen() {

        listening = dev instanceof IConnectionListener;
    }

}