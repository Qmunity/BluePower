/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.network.message;

import com.bluepowermod.client.gui.IGuiButtonSensitive;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import java.util.function.Supplier;

/**
 *
 * @author MineMaarten
 */

public class MessageGuiUpdate{

    private int messageId;
    private int value;

    public MessageGuiUpdate(int messageId, int value) {
        this.messageId = messageId;
        this.value = value;
    }

    public static MessageGuiUpdate decode(PacketBuffer buffer){
        int id = buffer.readByte();
        int value = buffer.readByte();
        return new MessageGuiUpdate(id, value);
    }

    public static void encode(MessageGuiUpdate message, PacketBuffer buffer) {
        buffer.writeByte(message.messageId);
        buffer.writeByte(message.value);
    }

    public static void handle(MessageGuiUpdate msg, Supplier<NetworkEvent.Context> contextSupplier) {
       NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity player = context.getSender();
            if (player == null) {
                return;
            }

            Container container = player.openContainer;
            if (container instanceof IGuiButtonSensitive) {
                ((IGuiButtonSensitive) container).onButtonPress(player, msg.messageId, msg.value);
            }
        });
    }
}
