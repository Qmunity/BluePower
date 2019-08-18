package com.bluepowermod.network.message;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Author MoreThanHidden
 */
public class MessageCraftingSync{

    public void handleClientSide(PlayerEntity player) {
        throw new UnsupportedOperationException("This isn't the Server");
    }

    public void handleServerSide(PlayerEntity player) {
        Container container = player.openContainer;
        if(container != null) {
            container.onCraftMatrixChanged(null);
        }
    }

    public void read(DataInput buffer) throws IOException {

    }

    public void write(DataOutput buffer) throws IOException {

    }
}
