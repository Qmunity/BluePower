package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;

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

    public void writeNBT(ByteBuf buf, NBTTagCompound tag) {

        try {
            byte[] bytes = CompressedStreamTools.compress(tag);
            buf.writeInt(bytes.length);
            buf.writeBytes(bytes);
        } catch (Exception ex) {
        }
    }

    public NBTTagCompound readNBT(ByteBuf buf) {

        try {
            int length = buf.readInt();
            byte[] bytes = buf.readBytes(length).array();
            return CompressedStreamTools.func_152457_a(bytes, NBTSizeTracker.field_152451_a);
        } catch (Exception ex) {
        }
        return null;
    }
}
