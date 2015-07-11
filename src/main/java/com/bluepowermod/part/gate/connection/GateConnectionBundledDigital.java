package com.bluepowermod.part.gate.connection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.part.gate.GateBase;

public class GateConnectionBundledDigital extends GateConnectionBase {

    private boolean[] input = new boolean[16], output = new boolean[16];

    public GateConnectionBundledDigital(GateBase gate, Dir direction) {

        super(gate, direction);
    }

    @Override
    public void refresh() {

    }

    @Override
    public boolean canConnect(IRedstoneDevice device) {

        return false;
    }

    @Override
    public boolean canConnect(IBundledDevice device) {

        return true;
    }

    @Override
    public boolean isBundled() {

        return true;
    }

    @Override
    public byte getRedstoneOutput() {

        return 0;
    }

    @Override
    public byte[] getBundledOutput() {

        byte[] out = new byte[16];

        for (int i = 0; i < out.length; i++)
            out[i] = (byte) (output[i] ? 255 : 0);

        return out;
    }

    @Override
    public void setRedstonePower(byte power) {

    }

    @Override
    public void setBundledPower(byte[] power) {

        for (int i = 0; i < power.length; i++)
            input[i] = (power[i] & 0xFF) > 0;
    }

    @Override
    public byte getRedstoneInput() {

        return (byte) 0;
    }

    @Override
    public byte[] getBundledInput() {

        byte[] in = new byte[16];

        for (int i = 0; i < in.length; i++)
            in[i] = (byte) (input[i] ? 255 : 0);

        return in;
    }

    public boolean[] getInput() {

        return input;
    }

    public boolean[] getOutput() {

        return output;
    }

    public boolean getInput(MinecraftColor color) {

        return input[color.ordinal()];
    }

    public boolean getOutput(MinecraftColor color) {

        return output[color.ordinal()];
    }

    public void setInput(boolean[] input) {

        this.input = input;
    }

    public void setOutput(boolean[] output) {

        this.output = output;
    }

    public void setInput(boolean input, MinecraftColor color) {

        this.input[color.ordinal()] = input;
    }

    public void setOutput(boolean output, MinecraftColor color) {

        this.output[color.ordinal()] = output;
    }

    // Override super method calls to return this class instead of the interface

    @Override
    public GateConnectionBundledDigital disable() {

        super.disable();
        return this;
    }

    @Override
    public GateConnectionBundledDigital enable() {

        super.enable();
        return this;
    }

    @Override
    public GateConnectionBundledDigital setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        return this;
    }

    @Override
    public GateConnectionBundledDigital setOutputOnly() {

        super.setOutputOnly();
        return this;
    }

    @Override
    public GateConnectionBundledDigital setBidirectional() {

        super.setBidirectional();
        return this;
    }

    @Override
    public double getSignal() {

        return 0;
    }

    // Read/write to NBT and update packets

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        for (int i = 0; i < 16; i++)
            tag.setBoolean("input_" + i, input[i]);
        for (int i = 0; i < 16; i++)
            tag.setBoolean("output_" + i, output[i]);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        for (int i = 0; i < 16; i++)
            input[i] = tag.getBoolean("input_" + i);
        for (int i = 0; i < 16; i++)
            output[i] = tag.getBoolean("output_" + i);
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        for (int i = 0; i < 16; i++)
            buffer.writeBoolean(input[i]);
        for (int i = 0; i < 16; i++)
            buffer.writeBoolean(output[i]);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        for (int i = 0; i < 16; i++)
            input[i] = buffer.readBoolean();
        for (int i = 0; i < 16; i++)
            output[i] = buffer.readBoolean();
    }

    @Override
    public void notifyUpdateIfNeeded() {

    }

    @Override
    public GateConnectionBundledDigital reset() {

        super.reset();

        input = new boolean[16];
        output = new boolean[16];

        return this;
    }

}
