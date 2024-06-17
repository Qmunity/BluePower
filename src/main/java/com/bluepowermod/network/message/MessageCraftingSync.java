package com.bluepowermod.network.message;

import com.bluepowermod.network.IBPMessage;
import com.bluepowermod.reference.Refs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/**
 * @Author MoreThanHidden
 */
public record MessageCraftingSync(int value) implements IBPMessage {

    public static final CustomPacketPayload.Type<MessageCraftingSync> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(Refs.MODID, "message_crafting_sync"));
    public static final StreamCodec<ByteBuf,MessageCraftingSync> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MessageCraftingSync::value,
            MessageCraftingSync::new
    );


    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        AbstractContainerMenu container = player.containerMenu;
        container.slotsChanged(null);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
