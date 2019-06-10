package com.bluepowermod.network.message;

import com.bluepowermod.network.Packet;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Author MoreThanHidden
 */
public class MessageCraftingSync extends Packet<MessageCraftingSync> {

    @Override
    public void handleClientSide(PlayerEntity player) {
        throw new UnsupportedOperationException("This isn't the Server");
    }

    @Override
    public void handleServerSide(PlayerEntity player) {
        Container container = player.openContainer;
        if(container != null) {
            container.onCraftMatrixChanged(null);
        }
    }

    @Override
    public void read(DataInput buffer) throws IOException {

    }

    @Override
    public void write(DataOutput buffer) throws IOException {

    }
}
