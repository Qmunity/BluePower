package com.bluepowermod.part.gate.component;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.vec.Vec3d;

import com.bluepowermod.client.render.RenderHelper;
import com.bluepowermod.part.gate.GateBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateComponentPointer extends GateComponentTorch {

    private double angle = 0;
    private double increment = 0;

    private boolean sync = true;

    public GateComponentPointer(GateBase gate, int color, double height, boolean digital) {

        super(gate, color, height, digital);
    }

    public GateComponentPointer(GateBase gate, double x, double z, double height, boolean digital) {

        super(gate, x, z, height, digital);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass) {

        RenderHelper
                .renderPointer((1 - x) - 9 / 16D, height - 4 / 32D, (1 - (z + 1 / 16D)) - 8 / 16D, angle + (getState() ? (delta * increment) : 0));

    }

    public GateComponentPointer setAngle(double angle) {

        if (this.angle != angle)
            setNeedsSyncing(shouldSync());
        this.angle = angle;

        return this;
    }

    public GateComponentPointer setIncrement(double increment) {

        if (this.increment != increment)
            setNeedsSyncing(shouldSync());
        this.increment = increment;

        return this;
    }

    public double getAngle() {

        return angle;
    }

    public double getIncrement() {

        return increment;
    }

    public GateComponentPointer setShouldSync(boolean sync) {

        this.sync = sync;

        return this;
    }

    public boolean shouldSync() {

        return sync;
    }

    @Override
    public void tick() {

        super.tick();

        if (getState())
            angle = angle + increment;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setDouble("angle", angle);
        tag.setDouble("increment", increment);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        angle = tag.getDouble("angle");
        increment = tag.getDouble("increment");
    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        super.writeData(buffer);
        if (shouldSync()) {
            buffer.writeDouble(angle);
            buffer.writeDouble(increment);
        }
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

        super.readData(buffer);
        if (shouldSync()) {
            angle = buffer.readDouble();
            increment = buffer.readDouble();
        }
    }

}
