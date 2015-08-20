package com.bluepowermod.power;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.connect.IConnectionListener;
import com.bluepowermod.api.power.IPowerBase;

public class PowerConnectionCache implements IConnectionCache<IPowerBase> {

    private IPowerBase dev;
    private PowerConnection[] connections = new PowerConnection[7];
    private IConnectionListener listener;

    public PowerConnectionCache(IPowerBase dev) {

        this.dev = dev;
    }

    @Override
    public IPowerBase getSelf() {

        return dev;
    }

    @Override
    public IConnection<IPowerBase> getConnectionOnSide(ForgeDirection side) {

        return connections[side.ordinal()];
    }

    @Override
    public void onConnect(ForgeDirection side, IPowerBase connectable, ForgeDirection connectableSide, ConnectionType type) {

        PowerConnection con = connections[side.ordinal()] = new PowerConnection(getSelf(), connectable, side, connectableSide, type);
        if (listener != null)
            listener.onConnect(con);
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        PowerConnection con = connections[side.ordinal()];
        connections[side.ordinal()] = null;
        if (listener != null)
            listener.onDisconnect(con);
    }

    @Override
    public void recalculateConnections() {

        if (dev.getDevice() == null || dev.getDevice().getWorld() == null || dev.getDevice().getWorld().isRemote)
            return;

        IPowerBase self = getSelf();
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
            boolean wasConnected = connections[d.ordinal()] != null;
            PowerConnection con = PowerConnectionHelper.getNeighbor(self, d);
            if (con != null) {
                if (!wasConnected || connections[d.ordinal()].getB() != con.getB() || connections[d.ordinal()].getSideB() != con.getSideB()
                        || connections[d.ordinal()].getType() != con.getType()) {
                    onConnect(con.getSideA(), con.getB(), con.getSideB(), con.getType());
                    con.getB().getConnectionCache().onConnect(con.getSideB(), con.getA(), con.getSideA(), con.getType());

                    IConnection<IPowerBase> con2 = con.getB().getConnectionCache().getConnectionOnSide(con.getSideB());
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
                    con.getB().getConnectionCache().onDisconnect(con.getSideB());
                }
            }
        }
    }

    @Override
    public void disconnectAll() {

        for (PowerConnection con : connections) {
            if (con == null)
                continue;
            con.getB().getConnectionCache().onDisconnect(con.getSideB());
            onDisconnect(con.getSideA());
        }
    }

    @Override
    public void listen() {

        if (dev instanceof IConnectionListener)
            listener = (IConnectionListener) dev;
    }

    @Override
    public void listen(IConnectionListener listener) {

        this.listener = listener;
    }

}
