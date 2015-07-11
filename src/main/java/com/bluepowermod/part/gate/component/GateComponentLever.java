package com.bluepowermod.part.gate.component;

import java.awt.image.BufferedImage;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;

import com.bluepowermod.part.gate.GateBase;

public abstract class GateComponentLever extends GateComponent {

    protected boolean state = false;

    private int layoutColor = -1;
    protected double x = 0, z = 0;

    public GateComponentLever(GateBase gate, int color) {

        super(gate);

        layoutColor = color;
        onLayoutRefresh();
    }

    public GateComponentLever(GateBase gate, double x, double z) {

        super(gate);

        this.x = x;
        this.z = z;
    }

    @Override
    public void onLayoutRefresh() {

        if (layoutColor == -1)
            return;

        BufferedImage img = getGate().getLayout().getLayout(layoutColor);
        x = img.getWidth();
        z = img.getHeight();
        for (int x = 0; x < img.getWidth(); x++) {
            for (int y = 0; y < img.getWidth(); y++) {
                if ((img.getRGB(x, y) & 0xFFFFFF) != 0) {
                    this.x = Math.min(this.x, x);
                    z = Math.min(z, y);
                }
            }
        }
        x = x / (img.getWidth());
        z = z / (img.getHeight());
    }

    public GateComponentLever setState(boolean state) {

        this.state = state;

        return this;
    }

    public boolean getState() {

        return state;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setBoolean("state", state);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        state = tag.getBoolean("state");
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        buffer.writeBoolean(state);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        state = buffer.readBoolean();
    }

}
