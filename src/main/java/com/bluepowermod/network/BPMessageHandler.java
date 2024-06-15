package com.bluepowermod.network;
import com.bluepowermod.reference.Refs;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public abstract class BPMessageHandler {

    protected BPMessageHandler(IEventBus modEventBus) {
        modEventBus.addListener(RegisterPayloadHandlersEvent.class, event -> {
            PayloadRegistrar registrar = event.registrar(Refs.fullVersionString());
            registerClientToServer(new PacketRegistrar(registrar, true));
            registerServerToClient(new PacketRegistrar(registrar, false));
        });
    }

    protected abstract void registerClientToServer(PacketRegistrar registrar);

    protected abstract void registerServerToClient(PacketRegistrar registrar);

    protected record PacketRegistrar(PayloadRegistrar registrar, boolean toServer) {

        public <MSG extends IBPMessage> void play(CustomPacketPayload.Type<MSG> type, StreamCodec<? super RegistryFriendlyByteBuf, MSG> reader) {
            if (toServer) {
                registrar.playToServer(type, reader, IBPMessage::handle);
            } else {
                registrar.playToClient(type, reader, IBPMessage::handle);
            }
        }

    }
}