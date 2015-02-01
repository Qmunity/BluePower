package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import uk.co.qmunity.lib.network.Packet;

import com.bluepowermod.api.misc.Accessibility;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.part.gate.wireless.Frequency;
import com.bluepowermod.part.gate.wireless.WirelessManager;

public class MessageWirelessSaveFreq extends Packet<MessageWirelessSaveFreq> {

    private Frequency freq;
    private Accessibility acc;
    private String name;

    public MessageWirelessSaveFreq(Frequency freq, Accessibility newAccessibility, String newName) {

        this.freq = freq;
        acc = newAccessibility;
        name = newName;
    }

    public MessageWirelessSaveFreq() {

    }

    @Override
    public void handleClientSide(EntityPlayer player) {

    }

    @Override
    public void handleServerSide(EntityPlayer player) {

        BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        freq.writeToBuffer(buffer);

        buffer.writeInt(acc.ordinal());
        buffer.writeUTF(name);
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        freq = new Frequency();
        freq.readFromBuffer(buffer);
        freq = (Frequency) WirelessManager.COMMON_INSTANCE.getFrequency(freq.getAccessibility(), freq.getFrequencyName(), freq.getOwner());

        freq.setAccessibility(Accessibility.values()[buffer.readInt()]);
        freq.setFrequency(buffer.readUTF());
    }

}
