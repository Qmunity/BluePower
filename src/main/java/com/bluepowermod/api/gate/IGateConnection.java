package com.bluepowermod.api.gate;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.util.Dir;

import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;

public interface IGateConnection {

    public IGate<?, ?, ?, ?, ?, ?> getGate();

    public Dir getDirection();

    public void notifyUpdate();

    public boolean isEnabled();

    public IGateConnection setEnabled(boolean enabled);

    public IGateConnection enable();

    public IGateConnection disable();

    public IGateConnection setOutputOnly();

    public IGateConnection setBidirectional();

    public boolean isOutputOnly();

    public void refresh();

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    public void writeData(DataOutput buffer) throws IOException;

    public void readData(DataInput buffer) throws IOException;

    public boolean canConnectRedstone();

    public boolean canConnect(IRedstoneDevice device);

    public boolean canConnect(IBundledDevice device);

    public boolean isBundled();

    public byte getRedstoneOutput();

    public byte[] getBundledOutput();

    public void setRedstonePower(byte power);

    public void setBundledPower(byte[] power);

}
