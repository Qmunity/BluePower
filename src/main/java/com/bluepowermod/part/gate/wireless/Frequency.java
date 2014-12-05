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
