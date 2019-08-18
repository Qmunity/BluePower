package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import com.bluepowermod.container.stack.TubeStack;
import net.minecraft.entity.player.PlayerEntity;

public class MessageServerTickTime{
    private double tickTime;

    public MessageServerTickTime() {
    }

    public MessageServerTickTime(double tickTime) {
        this.tickTime = tickTime;
    }

    public void handleClientSide(PlayerEntity player) {
        TubeStack.tickTimeMultiplier = Math.min(1, 50D / Math.max(tickTime - 5, 0.01));//Let the client stack go a _little_ bit faster than the real value (50 / tickTime), as else if the server stacks arrive first, glitches happen.
    }

    public void handleServerSide(PlayerEntity player) {

    }

    public void read(DataInput buffer) throws IOException {
        tickTime = buffer.readDouble();
    }

    public void write(DataOutput buffer) throws IOException {
        buffer.writeDouble(tickTime);
    }

}
