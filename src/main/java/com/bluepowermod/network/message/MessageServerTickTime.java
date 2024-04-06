package com.bluepowermod.network.message;

import com.bluepowermod.container.stack.TubeStack;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MessageServerTickTime{
    private double tickTime;

    public MessageServerTickTime(double tickTime) {
        this.tickTime = tickTime;
    }

    public static void handle(MessageServerTickTime msg, PlayPayloadContext contextSupplier) {
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
