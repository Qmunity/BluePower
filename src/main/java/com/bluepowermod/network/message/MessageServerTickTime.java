package com.bluepowermod.network.message;

import java.io.DataOutput;
import java.io.IOException;
import java.util.function.Supplier;

import com.bluepowermod.container.stack.TubeStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.network.NetworkEvent;

public class MessageServerTickTime{
    private double tickTime;

    public MessageServerTickTime(double tickTime) {
        this.tickTime = tickTime;
    }

    public static void handle(MessageServerTickTime msg, Supplier<NetworkEvent.Context> contextSupplier) {
        TubeStack.tickTimeMultiplier = Math.min(1, 50D / Math.max(msg.tickTime - 5, 0.01));//Let the client stack go a _little_ bit faster than the real value (50 / tickTime), as else if the server stacks arrive first, glitches happen.
    }

    public static MessageServerTickTime decode(FriendlyByteBuf buffer){
       double tickTime = buffer.readDouble();
       return new MessageServerTickTime(tickTime);
    }

    public static void encode(MessageServerTickTime message, FriendlyByteBuf buffer) {
        buffer.writeDouble(message.tickTime);
    }

}
