package com.bluepowermod.network.message;

import com.bluepowermod.network.LocatedPacket;
import com.bluepowermod.network.Packet;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * @Author MoreThanHidden
 */
public class MessageCraftingSync extends Packet<MessageCraftingSync> {

    @Override
    public void handleClientSide(EntityPlayer player) {
        throw new UnsupportedOperationException("This isn't the Server");
    }

    @Override
    public void handleServerSide(EntityPlayer player) {
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
