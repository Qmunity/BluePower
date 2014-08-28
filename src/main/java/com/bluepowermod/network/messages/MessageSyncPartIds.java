package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import com.bluepowermod.part.PartRegistry;

import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageSyncPartIds extends AbstractPacket<MessageSyncPartIds> {
    List<String> idMap = new ArrayList<String>();

    public MessageSyncPartIds() {

    }

    public MessageSyncPartIds(List<String> idMap) {
        this.idMap = idMap;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int tags = buf.readInt();
        for (int i = 0; i < tags; i++) {
            idMap.add(ByteBufUtils.readUTF8String(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(idMap.size());
        for (String s : idMap) {
            ByteBufUtils.writeUTF8String(buf, s);
        }
    }

    @Override
    public void handleClientSide(MessageSyncPartIds message, EntityPlayer player) {
        PartRegistry.getInstance().partIds = message.idMap;
    }

    @Override
    public void handleServerSide(MessageSyncPartIds message, EntityPlayer player) {
    }

}
