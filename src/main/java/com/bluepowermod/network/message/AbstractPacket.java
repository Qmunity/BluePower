/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import net.minecraft.entity.player.EntityPlayer;

import com.bluepowermod.BluePower;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;

/**
 * 
 * @author MineMaarten
 */
public abstract class AbstractPacket<REQ extends IMessage> implements IMessage, IMessageHandler<REQ, REQ> {
    
    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {
    
        if (ctx.side == Side.SERVER) {
            handleServerSide(message, ctx.getServerHandler().playerEntity);
        } else {
            handleClientSide(message, BluePower.proxy.getPlayer());
        }
        return null;
    }
    
    /**
     * Handle a packet on the client side.
     * 
     * @param message
     *            TODO
     * @param player
     *            the player reference
     */
    public abstract void handleClientSide(REQ message, EntityPlayer player);
    
    /**
     * Handle a packet on the server side.
     * 
     * @param message
     *            TODO
     * @param player
     *            the player reference
     */
    public abstract void handleServerSide(REQ message, EntityPlayer player);
}
