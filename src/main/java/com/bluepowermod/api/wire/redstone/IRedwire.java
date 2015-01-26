package com.bluepowermod.api.wire.redstone;


/**
 * Interface implemented by BluePower's redstone wires. This makes other wires check the wire type before connecting.
 */
public interface IRedwire {

    public RedwireType getRedwireType();

}
