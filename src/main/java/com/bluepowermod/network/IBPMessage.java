package com.bluepowermod.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public interface IBPMessage extends CustomPacketPayload {
    void handle(IPayloadContext context);
}
