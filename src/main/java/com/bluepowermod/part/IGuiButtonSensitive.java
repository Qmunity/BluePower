package com.bluepowermod.part;

import net.minecraft.entity.player.EntityPlayer;

/**
 * Implemented by BPParts or TileEntities, this interface can be used to sync client side gui input with server.
 * @author MineMaarten
 */
public interface IGuiButtonSensitive {
    
    public void onButtonPress(EntityPlayer player, int messageId, int value);
}
