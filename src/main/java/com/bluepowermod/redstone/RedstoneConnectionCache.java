package com.bluepowermod.redstone;

import net.minecraft.util.EnumFacing;;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.connect.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;

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
    public IConnection<IRedstoneDevice> getConnectionOnSide(EnumFacing side) {

        return connections[side.ordinal()];
    }

    @Override
    public void onConnect(EnumFacing side, IRedstoneDevice connectable, EnumFacing connectableSide, ConnectionType type) {

        RedstoneConnection con = connections[side.ordinal()] = RedstoneApi.getInstance().createConnection(getSelf(), connectable, side,
                connectableSide, type);
        if (listening)
            ((IConnectionListener) dev).onConnect(con);
    }

    @Override
    public void onDisconnect(EnumFacing side) {

        RedstoneConnection con = connections[side.ordinal()];
        connections[side.ordinal()] = null;
        if (listening)
            ((IConnectionListener) dev).onDisconnect(con);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void recalculateConnections() {

        if (dev.getWorld().isRemote)
            return;

        IRedstoneDevice self = getSelf();
        for (EnumFacing d : EnumFacing.VALUES) {
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
