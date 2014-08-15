/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.network;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import com.bluepowermod.network.messages.LocationDoublePacket;
import com.bluepowermod.network.messages.LocationIntPacket;
import com.bluepowermod.network.messages.MessageCircuitDatabaseTemplate;
import com.bluepowermod.network.messages.MessageDebugBlock;
import com.bluepowermod.network.messages.MessageGuiUpdate;
import com.bluepowermod.network.messages.MessageMultipartRemove;
import com.bluepowermod.network.messages.MessageSendClientServerTemplates;
import com.bluepowermod.network.messages.MessageUpdateTextfield;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

/**
 * 
 * @author MineMaarten
 */

public class NetworkHandler {
    
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Refs.MODID);
    private static int                       discriminant;
    
    /*
     * The integer is the ID of the message, the Side is the side this message will be handled (received) on!
     */
    public static void init() {
    
        INSTANCE.registerMessage(MessageGuiUpdate.class, MessageGuiUpdate.class, discriminant++, Side.SERVER);
        INSTANCE.registerMessage(MessageUpdateTextfield.class, MessageUpdateTextfield.class, discriminant++, Side.SERVER);
        INSTANCE.registerMessage(MessageMultipartRemove.class, MessageMultipartRemove.class, discriminant++, Side.SERVER);
        INSTANCE.registerMessage(MessageCircuitDatabaseTemplate.class, MessageCircuitDatabaseTemplate.class, discriminant++, Side.SERVER);
        INSTANCE.registerMessage(MessageCircuitDatabaseTemplate.class, MessageCircuitDatabaseTemplate.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(MessageDebugBlock.class, MessageDebugBlock.class, discriminant++, Side.CLIENT);
        INSTANCE.registerMessage(MessageSendClientServerTemplates.class, MessageSendClientServerTemplates.class, discriminant++, Side.CLIENT);
    }
    
    /*
     * public static void INSTANCE.registerMessage(Class<? extends AbstractPacket<? extends IMessage>> clazz){ INSTANCE.registerMessage(clazz, clazz,
     * discriminant++, Side.SERVER, discriminant++, Side.SERVER); }
     */
    
    public static void sendToAll(IMessage message) {
    
        INSTANCE.sendToAll(message);
    }
    
    public static void sendTo(IMessage message, EntityPlayerMP player) {
    
        INSTANCE.sendTo(message, player);
    }
    
    @SuppressWarnings("rawtypes")
    public static void sendToAllAround(LocationIntPacket message, World world, double distance) {
    
        sendToAllAround(message, message.getTargetPoint(world, distance));
    }
    
    @SuppressWarnings("rawtypes")
    public static void sendToAllAround(LocationIntPacket message, World world) {
    
        sendToAllAround(message, message.getTargetPoint(world));
    }
    
    @SuppressWarnings("rawtypes")
    public static void sendToAllAround(LocationDoublePacket message, World world) {
    
        sendToAllAround(message, message.getTargetPoint(world));
    }
    
    public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint point) {
    
        INSTANCE.sendToAllAround(message, point);
    }
    
    public static void sendToDimension(IMessage message, int dimensionId) {
    
        INSTANCE.sendToDimension(message, dimensionId);
    }
    
    public static void sendToServer(IMessage message) {
    
        INSTANCE.sendToServer(message);
    }
}
