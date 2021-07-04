package com.bluepowermod.network.message;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * @Author MoreThanHidden
 */
public class MessageCraftingSync{

    public static void handle(MessageCraftingSync msg, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player != null) {
                Container container = player.containerMenu;
                if (container != null) {
                    container.slotsChanged(null);
                }
            }
        });
        contextSupplier.get().setPacketHandled(true);
    }

    public static MessageCraftingSync decode(PacketBuffer buffer){
        return new MessageCraftingSync();
    }

    public static void encode(MessageCraftingSync message, PacketBuffer buffer) {
    }

}
