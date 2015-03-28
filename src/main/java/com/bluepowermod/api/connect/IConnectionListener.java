package com.bluepowermod.api.connect;

public interface IConnectionListener {

    public void onConnect(IConnection<?> connection);

    public void onDisconnect(IConnection<?> connection);

}
