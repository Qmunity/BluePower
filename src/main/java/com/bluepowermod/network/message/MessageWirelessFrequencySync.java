package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.entity.player.Player;

import com.bluepowermod.api.wireless.IFrequency;

public class MessageWirelessFrequencySync {

    //private List<Frequency> frequencies = new ArrayList<Frequency>();
    private Player player;

    public MessageWirelessFrequencySync(Player player) {

        this.player = player;
    }

    public MessageWirelessFrequencySync() {

    }

    public void write(DataOutput buffer) throws IOException {

        List<IFrequency> frequencies = null;
        if (player == null || player.isCreative()) {
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

    public void read(DataInput buffer) throws IOException {

        int amt = buffer.readInt();
        //for (int i = 0; i < amt; i++) {
            //Frequency f = new Frequency();
            //f.readFromBuffer(buffer);
            //frequencies.add(f);
        //}
    }

    public void handleClientSide(Player player) {

       // WirelessManager m = WirelessManager.CLIENT_INSTANCE;

       // m.unloadFrequencies();

       // for (Frequency f : frequencies)
       //     m.registerFrequency(f);
    }

    public void handleServerSide(Player player) {

    }

}
