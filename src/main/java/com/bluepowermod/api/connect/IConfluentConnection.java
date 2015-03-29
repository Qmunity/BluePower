package com.bluepowermod.api.connect;

import java.util.Collection;

public interface IConfluentConnection<T> extends IConnection<T> {

    /**
     * Gets all the devices connected to A
     */
    public Collection<IConnection<? extends T>> getConfluentConnections();

}
