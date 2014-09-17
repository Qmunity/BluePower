/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.messages;

import io.netty.buffer.ByteBuf;

import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bluepowermod.tileentities.BPTileMultipart;

import cpw.mods.fml.common.network.ByteBufUtils;

public class MessageMultipartRemove extends LocationIntPacket<MessageMultipartRemove> {

    private UUID id;
    private World world;

    public MessageMultipartRemove(UUID id, World world) {

        this.id = id;
        this.world = world;
    }

    public MessageMultipartRemove() {

    }

    @Override
    public void toBytes(ByteBuf buf) {

        super.toBytes(buf);

        ByteBufUtils.writeUTF8String(buf, id.toString());
        buf.writeInt(world.provider.dimensionId);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        super.fromBytes(buf);

        id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        int dimId = buf.readInt();
        for (World w : MinecraftServer.getServer().worldServers)
            if (w.provider.dimensionId == dimId) {
                world = w;
                break;
            }
    }

    @Override
    public void handleClientSide(MessageMultipartRemove message, EntityPlayer player) {

    }

    @Override
    public void handleServerSide(MessageMultipartRemove message, EntityPlayer player) {

        TileEntity tile = world.getTileEntity(message.x, message.y, message.z);
        if (!(tile instanceof BPTileMultipart))
            return;
        ((BPTileMultipart) tile).onClientBreakPart(id);
    }

}
