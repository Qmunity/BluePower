/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
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
