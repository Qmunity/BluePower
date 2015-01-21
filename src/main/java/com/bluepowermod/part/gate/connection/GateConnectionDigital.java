package com.bluepowermod.part.gate.connection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.part.gate.GateBase;

public class GateConnectionDigital extends GateConnectionBase {

    private boolean input = false, output = false;
    private boolean lastOutput = false;

    public GateConnectionDigital(GateBase<?, ?, ?, ?, ?, ?> gate, Dir direction) {

        super(gate, direction);
    }

    @Override
    public void refesh() {

    }

    @Override
    public boolean canConnect(IRedstoneDevice device) {

        return true;
    }

    @Override
    public boolean canConnect(IBundledDevice device) {

        return false;
    }

    @Override
    public boolean isBundled() {

        return false;
    }

    @Override
    public byte getRedstoneOutput() {

        return (byte) (output ? 255 : 0);
    }

    @Override
    public byte[] getBundledOutput() {

        return new byte[16];
    }

    @Override
    public void setRedstonePower(byte power) {

        input = (power & 0xFF) > 0;
    }

    @Override
    public void setBundledPower(byte[] power) {

    }

    public boolean getInput() {

        return input;
    }

    public boolean getOutput() {

        return output;
    }

    public GateConnectionDigital setInput(boolean input) {

        this.input = input;

        setNeedsSyncing(true);

        return this;
    }

    public GateConnectionDigital setOutput(boolean output) {

        this.output = output;

        setNeedsSyncing(true);

        return this;
    }

    // Override super method calls to return this class instead of the interface

    @Override
    public GateConnectionDigital disable() {

        super.disable();
        return this;
    }

    @Override
    public GateConnectionDigital enable() {

        super.enable();
        return this;
    }

    @Override
    public GateConnectionDigital setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        return this;
    }

    @Override
    public GateConnectionDigital setOutputOnly() {

        super.setOutputOnly();
        return this;
    }

    @Override
    public GateConnectionDigital setBidirectional() {

        super.setBidirectional();
        return this;
    }

    @Override
    public double getSignal() {

        if (output)
            return 1;
        if (!isOutputOnly() && input)
            return 1;
        return 0;
    }

    // Read/write to NBT and update packets

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("input", input);
        tag.setBoolean("output", output);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        input = tag.getBoolean("input");
        output = tag.getBoolean("output");
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        buffer.writeBoolean(input);
        buffer.writeBoolean(output);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        input = buffer.readBoolean();
        output = buffer.readBoolean();
    }

    @Override
    public void notifyUpdate() {

        super.notifyUpdate();

        lastOutput = output;
    }

    @Override
    public void notifyUpdateIfNeeded() {

        if (lastOutput != output)
            notifyUpdate();
    }

    @Override
    public boolean needsSyncing() {

        // TODO Auto-generated method stub
        return false;
    }

}
