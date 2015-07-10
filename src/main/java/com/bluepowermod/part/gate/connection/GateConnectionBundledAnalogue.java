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

public class GateConnectionBundledAnalogue extends GateConnectionBase {

    private byte[] input = new byte[16], output = new byte[16];

    public GateConnectionBundledAnalogue(GateBase<?, ?, ?, ?, ?, ?> gate, Dir direction) {

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

        return output;
    }

    @Override
    public void setRedstonePower(byte power) {

    }

    @Override
    public void setBundledPower(byte[] power) {

        System.arraycopy(power, 0, input, 0, power.length);
    }

    public byte[] getInput() {

        return input;
    }

    public byte[] getOutput() {

        return output;
    }

    public byte getInput(MinecraftColor color) {

        return input[color.ordinal()];
    }

    public byte getOutput(MinecraftColor color) {

        return output[color.ordinal()];
    }

    public void setInput(byte[] input) {

        this.input = input;
    }

    public void setOutput(byte[] output) {

        this.output = output;
    }

    public void setInput(byte input, MinecraftColor color) {

        this.input[color.ordinal()] = input;
    }

    public void setOutput(byte output, MinecraftColor color) {

        this.output[color.ordinal()] = output;
    }

    // Override super method calls to return this class instead of the interface

    @Override
    public GateConnectionBundledAnalogue disable() {

        super.disable();
        return this;
    }

    @Override
    public GateConnectionBundledAnalogue enable() {

        super.enable();
        return this;
    }

    @Override
    public GateConnectionBundledAnalogue setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        return this;
    }

    @Override
    public GateConnectionBundledAnalogue setOutputOnly() {

        super.setOutputOnly();
        return this;
    }

    @Override
    public GateConnectionBundledAnalogue setBidirectional() {

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
            tag.setByte("input_" + i, input[i]);
        for (int i = 0; i < 16; i++)
            tag.setByte("output_" + i, output[i]);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        for (int i = 0; i < 16; i++)
            input[i] = tag.getByte("input_" + i);
        for (int i = 0; i < 16; i++)
            output[i] = tag.getByte("output_" + i);
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        for (int i = 0; i < 16; i++)
            buffer.writeByte(input[i]);
        for (int i = 0; i < 16; i++)
            buffer.writeByte(output[i]);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        for (int i = 0; i < 16; i++)
            input[i] = buffer.readByte();
        for (int i = 0; i < 16; i++)
            output[i] = buffer.readByte();
    }

    @Override
    public void notifyUpdateIfNeeded() {

    }

    @Override
    public GateConnectionBundledAnalogue reset() {

        super.reset();

        input = new byte[16];
        output = new byte[16];

        return this;
    }

}
