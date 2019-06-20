package com.bluepowermod.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class Packet<REQ extends Packet<REQ>> implements IMessage, IMessageHandler<REQ, REQ> {

    @Override
    public REQ onMessage(REQ message, MessageContext ctx) {

        if (ctx.side == Side.SERVER) {
            if (message.getClass() == getClass())
                message.handleServerSide(ctx.getServerHandler().player);
            else
                message.handleServerSide(ctx.getServerHandler().player);
        } else {
            if (message.getClass() == getClass())
                message.handleClientSide(getPlayerClient());
            else
                message.handleClientSide(getPlayerClient());
        }
        return null;
    }

    @OnlyIn(Dist.CLIENT)
    public PlayerEntity getPlayerClient() {

        return Minecraft.getInstance().player;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void handleClientSide(PlayerEntity player);

    public abstract void handleServerSide(PlayerEntity player);

    @Override
    public void fromBytes(ByteBuf buf) {

        try {
            ByteBufInputStream bbis = new ByteBufInputStream(buf);
            read(bbis);
            bbis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        try {
            ByteBufOutputStream bbos = new ByteBufOutputStream(buf);
            write(bbos);
            bbos.flush();
            bbos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public abstract void read(DataInput buffer) throws IOException;

    public abstract void write(DataOutput buffer) throws IOException;
}
