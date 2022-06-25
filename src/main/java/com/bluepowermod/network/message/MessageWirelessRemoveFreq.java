package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.world.entity.player.Player;

public class MessageWirelessRemoveFreq {

    //private Frequency freq;

    //public MessageWirelessRemoveFreq(Frequency freq) {

     //   this.freq = freq;
   // }

    public MessageWirelessRemoveFreq() {

    }

    public void write(DataOutput buffer) throws IOException {

        //freq.writeToBuffer(buffer);
    }

    public void read(DataInput buffer) throws IOException {

        //freq = new Frequency();
        //freq.readFromBuffer(buffer);
    }

    public void handleClientSide(Player player) {

    }

    public void handleServerSide(Player player) {

        // WirelessManager.COMMON_INSTANCE.unregisterFrequency(WirelessManager.COMMON_INSTANCE.getFrequency(freq.getAccessibility(),
        //        freq.getFrequencyName(), freq.getOwner()));

        //BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (ServerPlayer) player);

    }
}
