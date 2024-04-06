package com.bluepowermod.network.message;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

/**
 * @Author MoreThanHidden
 */
public class MessageCraftingSync implements CustomPacketPayload {

    public void handle(PlayPayloadContext context) {
        Player player = context.player().orElse(null);
        if (player != null) {
            AbstractContainerMenu container = player.containerMenu;
            if (container != null) {
                container.slotsChanged(null);
            }
        }
    }


    public MessageCraftingSync(FriendlyByteBuf buf) {
    }
    public MessageCraftingSync() {

    }

    @Override
    public void write(FriendlyByteBuf buffer) {

    }

    @Override
    public ResourceLocation id() {
        return new ResourceLocation("bluepowermod", "message_crafting_sync");
    }
}
