package com.bluepowermod.api.connect;

public enum ConnectionType {
    /**
     * Both of the devices share an axis (They're one next to the other)
     */
    STRAIGHT, //
    /**
     * Both devices are placed ON the same block and connect through a third one.
     */
    OPEN_CORNER, //
    /**
     * Both devices are placed IN the same block.
     */
    CLOSED_CORNER;
}