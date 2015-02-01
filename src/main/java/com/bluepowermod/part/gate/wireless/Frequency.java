/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.gate.wireless;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import com.bluepowermod.api.misc.Accessibility;
import com.bluepowermod.api.wireless.IBundledFrequency;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IRedstoneFrequency;
import com.bluepowermod.api.wireless.IWirelessDevice;
import com.mojang.authlib.GameProfile;

public class Frequency implements IFrequency {

    private Accessibility accessibility;
    private UUID owner;
    private String ownerName;
    private String frequency;
    private boolean bundled;
    private int devices = 0;

    public Frequency(Accessibility accessibility, UUID owner, String frequency) {

        this.accessibility = accessibility;
        this.owner = owner;
        this.frequency = frequency;

        for (GameProfile p : MinecraftServer.getServer().func_152357_F()) {
            if (p.getId().equals(owner)) {
                ownerName = p.getName();
                break;
            }
        }
    }

    public Frequency() {

    }

    @Override
    public Accessibility getAccessibility() {

        return accessibility;
    }

    @Override
    public UUID getOwner() {

        return owner;
    }

    @Override
    public String getFrequencyName() {

        return frequency;
    }

    public String getOwnerName() {

        return ownerName;
    }

    public void setAccessibility(Accessibility accessibility) {

        this.accessibility = accessibility;
    }

    public void setFrequency(String frequency) {

        this.frequency = frequency;
    }

    public void writeToNBT(NBTTagCompound tag) {

        tag.setInteger("freq_accessibility", accessibility.ordinal());
        tag.setString("freq_owner", owner.toString());
        tag.setString("freq_name", frequency);
        tag.setBoolean("freq_bundled", isBundled());
    }

    public void readFromNBT(NBTTagCompound tag) {

        accessibility = Accessibility.values()[tag.getInteger("freq_accessibility")];
        owner = UUID.fromString(tag.getString("freq_owner"));
        frequency = tag.getString("freq_name");
        bundled = tag.getBoolean("freq_bundled");
    }

    public void writeToBuffer(DataOutput buf) throws IOException {

        buf.writeInt(accessibility.ordinal());
        buf.writeUTF(owner.toString());
        buf.writeUTF(ownerName.toString());
        buf.writeUTF(frequency.toString());
        buf.writeBoolean(isBundled());

        int amt = 0;
        for (IWirelessDevice d : WirelessManager.COMMON_INSTANCE.getDevices())
            if (d.getFrequency() != null && d.getFrequency().equals(this))
                amt++;
        buf.writeInt(amt);
    }

    public void readFromBuffer(DataInput buf) throws IOException {

        accessibility = Accessibility.values()[buf.readInt()];
        owner = UUID.fromString(buf.readUTF());
        ownerName = buf.readUTF();
        frequency = buf.readUTF();
        bundled = buf.readBoolean();
        devices = buf.readInt();
    }

    @Override
    public void notifyClients() {

        // TODO: Notify clients!
    }

    public boolean isBundled() {

        if (this instanceof IBundledFrequency)
            return true;

        return bundled;
    }

    public int getDevices() {

        return devices;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj != null && obj instanceof Frequency) {
            Frequency f = (Frequency) obj;
            return f.accessibility == accessibility && f.owner.equals(owner) && f.frequency.equals(frequency)
                    && f.isBundled() == isBundled();
        }

        return false;
    }

    public static final class RedstoneFrequency extends Frequency implements IRedstoneFrequency {

        private byte signal = (byte) 0;

        public RedstoneFrequency(Accessibility accessibility, UUID owner, String frequency) {

            super(accessibility, owner, frequency);
        }

        @Override
        public byte getSignal() {

            return signal;
        }

        @Override
        public void setSignal(byte signal) {

            this.signal = signal;
        }

    }

    public static final class BundledFrequency extends Frequency implements IBundledFrequency {

        private byte[] signal = new byte[1];

        public BundledFrequency(Accessibility accessibility, UUID owner, String frequency) {

            super(accessibility, owner, frequency);
        }

        @Override
        public byte[] getSignal() {

            return signal;
        }

        @Override
        public void setSignal(byte[] signal) {

            if (signal == null) {
                Arrays.fill(signal, (byte) 0);
                return;
            }

            this.signal = signal;
        }

    }

}
