package com.bluepowermod.part.gate.component;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.part.wire.redstone.WireHelper;

public class GateComponentWire extends GateComponentCubes {

    private RedwireType type;

    private GateConnectionBase connection;

    private byte power;
    private boolean enabled = true;

    public GateComponentWire(GateBase gate, int color, RedwireType type) {

        super(gate, color);

        this.type = type;
    }

    @Override
    public int getColor() {

        byte power = getPower();
        int colorMin = isEnabled() ? type.getMinColor() : 0x6A6A6A;
        int colorMax = isEnabled() ? type.getMaxColor() : 0x6A6A6A;

        return WireHelper.getColorForPowerLevel(colorMin, colorMax, power);
    }

    @Override
    public IIcon getIcon() {

        return IconSupplier.wire;
    }

    @Override
    public double getHeight() {

        return 1 / 32D;
    }

    public GateComponentWire bind(GateConnectionBase connection) {

        this.connection = connection;

        return this;
    }

    public GateComponentWire setPower(byte power) {

        if (power != this.power)
            setNeedsSyncing(true);
        this.power = power;

        return this;
    }

    public byte getPower() {

        if (connection != null && getGate().getParent() != null && getGate().getWorld() != null
                && (getGate().getParent().isSimulated() || !getGate().getWorld().isRemote)) {
            if (connection.isEnabled()) {
                return (byte) (connection.getSignal() * 255);
            } else {
                return (byte) (255 / 2);
            }
        }

        return power;
    }

    public boolean isEnabled() {

        if (!enabled)
            return false;
        if (connection != null && getGate().getParent() != null && getGate().getWorld() != null
                && (getGate().getParent().isSimulated() || !getGate().getWorld().isRemote))
            return connection.isEnabled();

        return true;
    }

    public void setEnabled(boolean enabled) {

        if (enabled != this.enabled)
            setNeedsSyncing(true);
        this.enabled = enabled;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setByte("power", getPower());
        tag.setBoolean("enabled", isEnabled());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        power = tag.getByte("power");
        enabled = tag.getBoolean("enabled");
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        buffer.writeByte(getPower());
        buffer.writeBoolean(isEnabled());
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        power = buffer.readByte();
        enabled = buffer.readBoolean();
    }

    @Override
    public boolean needsSyncing() {

        if (connection != null && connection.needsSyncing())
            return true;

        return super.needsSyncing();
    }

}
