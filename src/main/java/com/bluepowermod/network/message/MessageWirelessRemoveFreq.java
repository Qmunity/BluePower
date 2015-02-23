package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import uk.co.qmunity.lib.network.Packet;

import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.part.gate.wireless.Frequency;
import com.bluepowermod.part.gate.wireless.WirelessManager;

public class MessageWirelessRemoveFreq extends Packet<MessageWirelessRemoveFreq> {

    private Frequency freq;

    public MessageWirelessRemoveFreq(Frequency freq) {

        this.freq = freq;
    }

    public MessageWirelessRemoveFreq() {

    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        freq.writeToBuffer(buffer);
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        freq = new Frequency();
        freq.readFromBuffer(buffer);
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        WirelessManager.COMMON_INSTANCE.unregisterFrequency(WirelessManager.COMMON_INSTANCE.getFrequency(freq.getAccessibility(),
                freq.getFrequencyName(), freq.getOwner()));

        BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);

    }
}
