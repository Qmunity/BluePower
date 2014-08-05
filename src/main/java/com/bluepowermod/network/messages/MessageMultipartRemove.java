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
