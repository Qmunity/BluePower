package com.bluepowermod.redstone;

import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.IConnectionCache;
import com.bluepowermod.api.wire.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IBundledDevice;

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

    @Override
    public void recalculateConnections() {

        if (dev.getWorld().isRemote)
            return;

        // FIXME Recalculate connections
    }

    @Override
    public void listen() {

        listening = dev instanceof IConnectionListener;
    }

}
