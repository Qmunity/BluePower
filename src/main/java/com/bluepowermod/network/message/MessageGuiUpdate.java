package com.bluepowermod.network.message;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
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

public record MessageGuiUpdate (int messageId, int value) implements IBPMessage {
    public static final CustomPacketPayload.Type<MessageGuiUpdate> TYPE = new CustomPacketPayload.Type<>(new ResourceLocation(Refs.MODID, "message_gui_update"));
    public static final StreamCodec<ByteBuf, MessageGuiUpdate> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, MessageGuiUpdate::messageId,
            ByteBufCodecs.INT, MessageGuiUpdate::value,
            MessageGuiUpdate::new
    );

    @Override
    public void handle(IPayloadContext context) {
        Player player = context.player();
        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof IGuiButtonSensitive) {
            ((IGuiButtonSensitive) container).onButtonPress(player, messageId, value);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}