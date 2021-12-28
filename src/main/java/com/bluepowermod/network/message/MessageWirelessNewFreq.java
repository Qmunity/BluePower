package com.bluepowermod.network.message;

import com.bluepowermod.api.misc.Accessibility;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class MessageWirelessNewFreq{

    private Accessibility acc;
    private String name;
    private boolean bundled;
    private Direction face;

   // public MessageWirelessNewFreq(IWirelessGate gate, Accessibility newAccessibility, String newName, boolean bundled) {

       // super(gate);

       // acc = newAccessibility;
       // name = newName;
       // this.bundled = bundled;
       // face = gate.getFace();
   // }

    public MessageWirelessNewFreq() {

    }

    public void handleClientSide(Player player) {

    }

    public void handleServerSide(Player player) {

        //Frequency freq = (Frequency) WirelessManager.COMMON_INSTANCE.registerFrequency(player, name, acc, bundled);
       // ITilePartHolder h = MultipartCompatibility.getPartHolder(player.world, pos);

       // if (h != null) {
        //    IWirelessGate p = null;
           // for (IPart pa : h.getParts())
          //      if (pa instanceof IWirelessGate && ((IWirelessGate) pa).getFace() == face)
          //          p = (IWirelessGate) pa;
          //  if (p == null)
          //      return;

        //    p.setFrequency(freq);

        //    BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
       // }
    }

    public void write(DataOutput buffer) throws IOException {

        //super.saveAdditional(buffer);

        buffer.writeInt(acc.ordinal());
        buffer.writeUTF(name);
        buffer.writeBoolean(bundled);
        buffer.writeInt(face.ordinal());
    }

    public void read(DataInput buffer) throws IOException {

        //super.load(buffer);

        acc = Accessibility.values()[buffer.readInt()];
        name = buffer.readUTF();
        bundled = buffer.readBoolean();
        face = Direction.from3DDataValue(buffer.readInt());
    }
}
