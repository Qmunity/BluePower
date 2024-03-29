package com.bluepowermod.api.connect;


import net.minecraft.core.Direction;

public interface IConnectionCache<T> {

    /**
     * Gets the instance of T this cache is bound to.
     */
    public T getSelf();

    /**
     * Returns the connection on the specified side, or null if not connected.
     */
    public IConnection<T> getConnectionOnSide(Direction side);

    /**
     * Called when one device connects to the other.
     */
    public void onConnect(Direction side, T connectable, Direction connectableSide, ConnectionType type);

    /**
     * Called when one device disconnects from the other.
     */
    public void onDisconnect(Direction side);

    /**
     * Checks for new devices and removes old ones.
     */
    public void recalculateConnections();

    /**
     * Disconnects all the devices from this one.
     */
    public void disconnectAll();

    /**
     * Makes the object the cache is bound to, if instance of {@link IConnectionListener}, listen to connect/disconnect events.
     */
    public void listen();

}
