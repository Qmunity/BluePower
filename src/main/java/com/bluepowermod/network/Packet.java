package com.bluepowermod.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class Packet<REQ extends Packet<REQ>>  {

    @OnlyIn(Dist.CLIENT)
    public PlayerEntity getPlayerClient() {

        return Minecraft.getInstance().player;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract void handleClientSide(PlayerEntity player);

    public abstract void handleServerSide(PlayerEntity player);

    public abstract void read(DataInput buffer) throws IOException;

    public abstract void write(DataOutput buffer) throws IOException;
}
