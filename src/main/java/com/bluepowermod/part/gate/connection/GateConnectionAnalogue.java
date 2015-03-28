package com.bluepowermod.part.gate.connection;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.redstone.DummyRedstoneDevice;

public class GateConnectionAnalogue extends GateConnectionBase {

    private byte input = 0, output = 0;
    private byte lastOutput = 0;

    public GateConnectionAnalogue(GateBase<?, ?, ?, ?, ?, ?> gate, Dir direction) {

        super(gate, direction);
    }

    @Override
    public void refresh() {

        IConnection<? extends IRedstoneDevice> c = gate.getRedstoneConnectionCache().getConnectionOnSide(getForgeDirection());

        if (c == null || c.getB() instanceof DummyRedstoneDevice)
            input = (byte) MathHelper.map(RedstoneHelper.getInput(getGate().getWorld(), getGate().getX(), getGate().getY(), getGate()
                    .getZ(), getForgeDirection(), getGate().getFace()), 0, 15, 0, 255);
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

        return output;
    }

    @Override
    public byte[] getBundledOutput() {

        return new byte[16];
    }

    @Override
    public void setRedstonePower(byte power) {

        input = power;
    }

    @Override
    public void setBundledPower(byte[] power) {

    }

    public byte getInput() {

        return input;
    }

    public byte getOutput() {

        return output;
    }

    public GateConnectionAnalogue setInput(byte input) {

        if (this.input != input)
            setNeedsSyncing(true);
        this.input = input;

        return this;
    }

    public GateConnectionAnalogue setOutput(byte output) {

        if (this.output != output)
            setNeedsSyncing(true);
        this.output = output;

        return this;
    }

    // Override super method calls to return this class instead of the interface

    @Override
    public GateConnectionAnalogue disable() {

        super.disable();
        return this;
    }

    @Override
    public GateConnectionAnalogue enable() {

        super.enable();
        return this;
    }

    @Override
    public GateConnectionAnalogue setEnabled(boolean enabled) {

        super.setEnabled(enabled);
        return this;
    }

    @Override
    public GateConnectionAnalogue setOutputOnly() {

        super.setOutputOnly();
        return this;
    }

    @Override
    public GateConnectionAnalogue setBidirectional() {

        super.setBidirectional();
        return this;
    }

    @Override
    public double getSignal() {

        int tot = output & 0xFF;
        if (!isOutputOnly())
            tot = Math.max(tot, input & 0xFF);
        return tot / 255D;
    }

    // Read/write to NBT and update packets

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setByte("input", input);
        tag.setByte("output", output);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        input = tag.getByte("input");
        output = tag.getByte("output");
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        buffer.writeByte(input);
        buffer.writeByte(output);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        input = buffer.readByte();
        output = buffer.readByte();
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
    public GateConnectionAnalogue reset() {

        super.reset();

        input = 0;
        output = 0;
        lastOutput = (byte) 255;

        return this;
    }

}
