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

import java.util.Arrays;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.api.misc.Accessibility;
import com.bluepowermod.api.wireless.IBundledFrequency;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IRedstoneFrequency;

public class Frequency implements IFrequency {

    private Accessibility accessibility;
    private UUID owner;
    private String frequency;

    public Frequency(Accessibility accessibility, UUID owner, String frequency) {

        this.accessibility = accessibility;
        this.owner = owner;
        this.frequency = frequency;
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
    }

    public void readFromNBT(NBTTagCompound tag) {

        accessibility = Accessibility.values()[tag.getInteger("freq_accessibility")];
        owner = UUID.fromString(tag.getString("freq_owner"));
        frequency = tag.getString("freq_name");
    }

    @Override
    public void notifyClients() {

        // TODO: Notify clients!
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
