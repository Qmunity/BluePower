package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bluepowermod.network.Packet;
import net.minecraft.entity.player.EntityPlayer;

import com.bluepowermod.api.wireless.IFrequency;

public class MessageWirelessFrequencySync extends Packet<MessageWirelessFrequencySync> {

    //private List<Frequency> frequencies = new ArrayList<Frequency>();
    private EntityPlayer player;

    public MessageWirelessFrequencySync(EntityPlayer player) {

        this.player = player;
    }

    public MessageWirelessFrequencySync() {

    }

    @Override
    public void write(DataOutput buffer) throws IOException {

        List<IFrequency> frequencies = null;
        if (player == null || player.capabilities.isCreativeMode) {
           // frequencies = WirelessManager.COMMON_INSTANCE.getFrequencies();
        } else {
            frequencies = new ArrayList<IFrequency>();
            //frequencies.addAll(WirelessManager.COMMON_INSTANCE.getAvailableRedstoneFrequencies(player));
            //frequencies.addAll(WirelessManager.COMMON_INSTANCE.getAvailableBundledFrequencies(player));
        }

        //buffer.writeInt(frequencies.size());
        //for (IFrequency f : frequencies)
            //((Frequency) f).writeToBuffer(buffer);
    }

    @Override
    public void read(DataInput buffer) throws IOException {

        int amt = buffer.readInt();
        //for (int i = 0; i < amt; i++) {
            //Frequency f = new Frequency();
            //f.readFromBuffer(buffer);
            //frequencies.add(f);
        //}
    }

    @Override
    public void handleClientSide(EntityPlayer player) {

       // WirelessManager m = WirelessManager.CLIENT_INSTANCE;

       // m.unloadFrequencies();

       // for (Frequency f : frequencies)
       //     m.registerFrequency(f);
    }

    @Override
    public void handleServerSide(EntityPlayer player) {

    }

}
