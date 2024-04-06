package com.bluepowermod.network.message;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class MessageGuiUpdate implements CustomPacketPayload {

    private int messageId;
    private int value;

    public MessageGuiUpdate(int messageId, int value) {
        this.messageId = messageId;
        this.value = value;
    }

    public MessageGuiUpdate(FriendlyByteBuf buf) {
        this.messageId = buf.readByte();
        this.value = buf.readByte();
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeByte(this.messageId);
        buf.writeByte(this.value);
    }

    @Override
    public ResourceLocation id() {
        return new ResourceLocation("bluepowermod", "message_gui_update");
    }

    public void handle(PlayPayloadContext context) {
        Player player = context.player().orElse(null);
        if (player == null) {
            return;
        }

        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof IGuiButtonSensitive) {
            ((IGuiButtonSensitive) container).onButtonPress(player, messageId, value);
        }
    }
}