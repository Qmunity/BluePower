package com.bluepowermod.network.message;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.network.LocatedPacket;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.api.misc.Accessibility;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.part.gate.old.wireless.Frequency;
import com.bluepowermod.part.gate.old.wireless.IWirelessGate;
import com.bluepowermod.part.gate.old.wireless.WirelessManager;

public class MessageWirelessNewFreq extends LocatedPacket<MessageWirelessNewFreq> {

    private Accessibility acc;
    private String name;
    private boolean bundled;
    private ForgeDirection face;

    public MessageWirelessNewFreq(IWirelessGate gate, Accessibility newAccessibility, String newName, boolean bundled) {

        super(gate);

        acc = newAccessibility;
        name = newName;
        this.bundled = bundled;
        face = gate.getFace();
    }

    public MessageWirelessNewFreq() {

    }

    @Override
    public void handleClientSide(MessageWirelessNewFreq message, EntityPlayer player) {

    }

    @Override
    public void handleServerSide(MessageWirelessNewFreq message, EntityPlayer player) {

        Frequency freq = (Frequency) WirelessManager.COMMON_INSTANCE.registerFrequency(player, name, acc, bundled);

        ITilePartHolder h = MultipartCompatibility.getPartHolder(player.worldObj, x, y, z);
        if (h == null)
            return;

        IWirelessGate p = null;
        for (IPart pa : h.getParts())
            if (pa instanceof IWirelessGate && ((IWirelessGate) pa).getFace() == face)
                p = (IWirelessGate) pa;
        if (p == null)
            return;

        p.setFrequency(freq);

        NetworkHandler.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
    }

    @Override
    public void write(NBTTagCompound tag) {

        super.write(tag);

        tag.setInteger("acc", acc.ordinal());
        tag.setString("name", name);
        tag.setBoolean("bundled", bundled);
        tag.setInteger("gate", face.ordinal());
    }

    @Override
    public void read(NBTTagCompound tag) {

        super.read(tag);

        acc = Accessibility.values()[tag.getInteger("acc")];
        name = tag.getString("name");
        bundled = tag.getBoolean("bundled");
        face = ForgeDirection.getOrientation(tag.getInteger("gate"));
    }
}
