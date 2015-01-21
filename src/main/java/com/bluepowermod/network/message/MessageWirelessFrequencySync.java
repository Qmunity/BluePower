package com.bluepowermod.network.message;

import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;

import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.part.gate.old.wireless.Frequency;
import com.bluepowermod.part.gate.old.wireless.WirelessManager;

public class MessageWirelessFrequencySync extends AbstractPacket<MessageWirelessFrequencySync> {

    private List<Frequency> frequencies = new ArrayList<Frequency>();
    private EntityPlayer player;

    public MessageWirelessFrequencySync(EntityPlayer player) {

        this.player = player;
    }

    public MessageWirelessFrequencySync() {

    }

    @Override
    public void fromBytes(ByteBuf buf) {

        int amt = buf.readInt();
        for (int i = 0; i < amt; i++) {
            Frequency f = new Frequency();
            f.readFromBuffer(buf);
            frequencies.add(f);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {

        List<IFrequency> frequencies = null;
        if (player == null || player.capabilities.isCreativeMode) {
            frequencies = WirelessManager.COMMON_INSTANCE.getFrequencies();
        } else {
            frequencies = new ArrayList<IFrequency>();
            frequencies.addAll(WirelessManager.COMMON_INSTANCE.getAvailableRedstoneFrequencies(player));
            frequencies.addAll(WirelessManager.COMMON_INSTANCE.getAvailableBundledFrequencies(player));
        }

        buf.writeInt(frequencies.size());
        for (IFrequency f : frequencies)
            ((Frequency) f).writeToBuffer(buf);
    }

    @Override
    public void handleClientSide(MessageWirelessFrequencySync message, EntityPlayer player) {

        WirelessManager m = WirelessManager.CLIENT_INSTANCE;

        m.unloadFrequencies();

        for (Frequency f : message.frequencies)
            m.registerFrequency(f);
    }

    @Override
    public void handleServerSide(MessageWirelessFrequencySync message, EntityPlayer player) {

    }

}
