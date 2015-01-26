package com.bluepowermod.api.wire;

public interface IConnectionListener {

    public void onConnect(IConnection<?> connection);

    public void onDisconnect(IConnection<?> connection);

}
