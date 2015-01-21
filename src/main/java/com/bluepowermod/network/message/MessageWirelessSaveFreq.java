package com.bluepowermod.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.network.Packet;

import com.bluepowermod.api.misc.Accessibility;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.part.gate.old.wireless.Frequency;
import com.bluepowermod.part.gate.old.wireless.WirelessManager;

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
    public void handleClientSide(MessageWirelessSaveFreq message, EntityPlayer player) {

    }

    @Override
    public void handleServerSide(MessageWirelessSaveFreq message, EntityPlayer player) {

        NetworkHandler.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
    }

    @Override
    public void write(NBTTagCompound tag) {

        freq.writeToNBT(tag);

        tag.setInteger("acc", acc.ordinal());
        tag.setString("name", name);
    }

    @Override
    public void read(NBTTagCompound tag) {

        freq = new Frequency();
        freq.readFromNBT(tag);
        freq = (Frequency) WirelessManager.COMMON_INSTANCE.getFrequency(freq.getAccessibility(), freq.getFrequencyName(), freq.getOwner());

        freq.setAccessibility(Accessibility.values()[tag.getInteger("acc")]);
        freq.setFrequency(tag.getString("name"));
    }

}
