package com.bluepowermod.network.messages;

import net.minecraft.entity.player.EntityPlayer;

import com.bluepowermod.helper.Debugger;

/**
 * 
 * @author MineMaarten
 */

public class MessageDebugBlock extends LocationIntPacket<MessageDebugBlock> {
    
    public MessageDebugBlock() {
    
    }
    
    public MessageDebugBlock(int x, int y, int z) {
    
        super(x, y, z);
    }
    
    @Override
    public void handleClientSide(MessageDebugBlock message, EntityPlayer player) {
    
        Debugger.indicateBlock(player.worldObj, message.x, message.y, message.z);
    }
    
    @Override
    public void handleServerSide(MessageDebugBlock message, EntityPlayer player) {
    
    }
    
}
