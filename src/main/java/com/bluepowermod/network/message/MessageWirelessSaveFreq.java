package com.bluepowermod.network.message;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import net.minecraft.world.entity.player.Player;
import com.bluepowermod.api.misc.Accessibility;

public class MessageWirelessSaveFreq{

    //private Frequency freq;
    private Accessibility acc;
    private String name;

   // public MessageWirelessSaveFreq(Frequency freq, Accessibility newAccessibility, String newName) {

      //  this.freq = freq;
      //  acc = newAccessibility;
      //  name = newName;
    //}

    public MessageWirelessSaveFreq() {

    }

    public void handleClientSide(Player player) {

    }

    public void handleServerSide(Player player) {

        //BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (ServerPlayer) player);
    }

    public void write(DataOutput buffer) throws IOException {

        //freq.writeToBuffer(buffer);

        buffer.writeInt(acc.ordinal());
        buffer.writeUTF(name);
    }

    public void read(DataInput buffer) throws IOException {

        // freq = new Frequency();
        // freq.readFromBuffer(buffer);
        // freq = (Frequency) WirelessManager.COMMON_INSTANCE.getFrequency(freq.getAccessibility(), freq.getFrequencyName(), freq.getOwner());

        // freq.setAccessibility(Accessibility.values()[buffer.readInt()]);
        // freq.setFrequency(buffer.readUTF());
    }

}
