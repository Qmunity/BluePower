package com.bluepowermod.network.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

import com.bluepowermod.network.NetworkHandler;

public class MessageWirelessRemoveFreq extends AbstractPacket<MessageWirelessRemoveFreq> {

    private Frequency freq;

    public MessageWirelessRemoveFreq(Frequency freq) {

        this.freq = freq;
    }

    public MessageWirelessRemoveFreq() {

    }

    @Override
    public void handleClientSide(MessageWirelessRemoveFreq message, EntityPlayer player) {

    }

    @Override
    public void handleServerSide(MessageWirelessRemoveFreq message, EntityPlayer player) {

        WirelessManager.COMMON_INSTANCE.unregisterFrequency(WirelessManager.COMMON_INSTANCE.getFrequency(message.freq.getAccessibility(),
                message.freq.getFrequencyName(), message.freq.getOwner()));

        NetworkHandler.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
    }

    @Override
    public void fromBytes(ByteBuf buf) {

        freq = new Frequency();
        freq.readFromBuffer(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {

        freq.writeToBuffer(buf);
    }
}
