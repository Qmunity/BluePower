package com.bluepowermod.part;

/**
 * Implemented by BPParts or TileEntities, this interface can be used to sync client side gui input with server.
 * @author MineMaarten
 */
public interface IGuiButtonSensitive {
    
    public void onButtonPress(int messageId, int value);
}
