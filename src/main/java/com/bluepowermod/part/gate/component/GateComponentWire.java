package com.bluepowermod.part.gate.component;

import java.awt.Rectangle;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Scale;
import uk.co.qmunity.lib.vec.Vec2dRect;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.part.wire.redstone.WireCommons;

public class GateComponentWire extends GateComponentLocationArray {

    private RedwireType type;

    private GateConnectionBase connection;

    private byte power;
    private boolean enabled = true;

    public GateComponentWire(GateBase<?, ?, ?, ?, ?, ?> gate, int color, RedwireType type) {

        super(gate, color);

        this.type = type;
    }

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

        byte power = getPower();
        int colorMin = isEnabled() ? type.getMinColor() : 0x999999;
        int colorMax = isEnabled() ? type.getMaxColor() : 0x999999;

        renderer.setColor(WireCommons.getColorForPowerLevel(colorMin, colorMax, power));
        double height = 1 / 48D;
        // double size = 1 / ((double) pixels.length);
        double scale = 1D / getGate().getLayout().getLayout(layoutColor).getWidth();

        renderer.setRenderSides(false, true, true, true, true, true);
        for (Rectangle r : getGate().getLayout().getSimplifiedLayout(layoutColor).getRectangles())
            renderer.renderBox(
                    new Vec2dRect(r).extrude(height).transform(new Scale(scale, 1, scale)).add(-0.5 + 1 / 64D, 2 / 16D, -0.5 + 1 / 64D),
                    IconSupplier.wire);

        // for (int x = 0; x < pixels.length; x++) {
        // boolean[] p = pixels[x];
        // for (int y = 0; y < p.length; y++) {
        // if (p[y]) {
        // double dx = x / (double) pixels.length;
        // double dy = y / (double) p.length;
        //
        // boolean west = x == 0 || !pixels[x - 1][y];
        // boolean east = x == pixels.length - 1 || !pixels[x + 1][y];
        // boolean north = y == 0 || !pixels[x][y - 1];
        // boolean south = y == p.length - 1 || !pixels[x][y + 1];
        //
        // renderer.setRenderSides(false, true, west, east, north, south);
        // renderer.renderBox(new Vec3dCube(dx, 2 / 16D, dy, dx + size, 2 / 16D + height, dy + size), IconSupplier.wire);
        // }
        // }
        // }

        renderer.resetRenderedSides();
        renderer.setColor(0xFFFFFF);
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

        if (connection != null && getGate().getParent() != null && (getGate().getParent().isSimulated() || !getGate().getWorld().isRemote)) {
            if (connection.isEnabled()) {
                return (byte) (connection.getSignal() * 255);
            } else {
                return (byte) (255 / 2);
            }
        }

        return power;
    }

    public boolean isEnabled() {

        if (connection != null && getGate().getParent() != null && (getGate().getParent().isSimulated() || !getGate().getWorld().isRemote))
            return connection.isEnabled();

        return enabled;
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
