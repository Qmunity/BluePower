package com.bluepowermod.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.IConnectionCache;
import com.bluepowermod.api.wire.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.part.wire.redstone.WireHelper;

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
    public RedstoneConnection getConnectionOnSide(ForgeDirection side) {

        return connections[side.ordinal()];
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice connectable, ForgeDirection connectableSide, ConnectionType type) {

        RedstoneConnection con = connections[side.ordinal()] = RedstoneApi.getInstance().createConnection(getSelf(), connectable, side,
                connectableSide, type);
        if (listening)
            ((IConnectionListener) dev).onConnect(con);
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        RedstoneConnection con = connections[side.ordinal()];
        connections[side.ordinal()] = null;
        if (listening)
            ((IConnectionListener) dev).onDisconnect(con);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void recalculateConnections() {

        IRedstoneDevice self = getSelf();
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            boolean wasConnected = getConnectionOnSide(d) != null;
            RedstoneConnection con = WireHelper.getNeighbor(self, d);
            if (con != null) {
                onConnect(con.getSideA(), con.getB(), con.getSideB(), con.getType());
                ((IConnectionCache<IRedstoneDevice>) con.getB().getRedstoneConnectionCache()).onConnect(con.getSideB(), con.getA(),
                        con.getSideA(), con.getType());

                IConnection<IRedstoneDevice> con2 = (IConnection<IRedstoneDevice>) con.getB().getRedstoneConnectionCache()
                        .getConnectionOnSide(con.getSideB());
                con = connections[d.ordinal()];

                con.setComplementaryConnection(con2);
                con2.setComplementaryConnection(con);
            } else if (wasConnected) {
                con = connections[d.ordinal()];

                onDisconnect(con.getSideA());
                con.getB().getRedstoneConnectionCache().onDisconnect(con.getSideB());
            }
        }
    }

    @Override
    public void listen() {

        listening = dev instanceof IConnectionListener;
    }

}
