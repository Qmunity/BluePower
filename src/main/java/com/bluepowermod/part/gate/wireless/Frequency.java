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

import com.bluepowermod.api.misc.Accessability;
import com.bluepowermod.api.wireless.IBundledFrequency;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IRedstoneFrequency;

public class Frequency implements IFrequency {

    private Accessability accessability;
    private UUID owner;
    private String frequency;

    public Frequency(Accessability accessability, UUID owner, String frequency) {

        this.accessability = accessability;
        this.owner = owner;
        this.frequency = frequency;
    }

    @Override
    public Accessability getAccessability() {

        return accessability;
    }

    @Override
    public UUID getOwner() {

        return owner;
    }

    @Override
    public String getFrequencyName() {

        return frequency;
    }

    public static final class RedstoneFrequency extends Frequency implements IRedstoneFrequency {

        private byte signal = (byte) 0;

        public RedstoneFrequency(Accessability accessability, UUID owner, String frequency) {

            super(accessability, owner, frequency);
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

        public BundledFrequency(Accessability accessability, UUID owner, String frequency) {

            super(accessability, owner, frequency);
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
