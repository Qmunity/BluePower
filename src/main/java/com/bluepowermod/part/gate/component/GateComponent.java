package com.bluepowermod.part.gate.component;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IGateComponent;
import com.bluepowermod.part.gate.GateBase;

public abstract class GateComponent implements IGateComponent {

    private GateBase<?, ?, ?, ?, ?, ?> gate;

    private boolean needsSyncing = false;

    public GateComponent(GateBase<?, ?, ?, ?, ?, ?> gate) {

        this.gate = gate;
    }

    @Override
    public GateBase<?, ?, ?, ?, ?, ?> getGate() {

        return gate;
    }

    @Override
    public void addCollisionBoxes(List<Vec3dCube> boxes) {

    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        return Arrays.asList();
    }

    @Override
    public void tick() {

    }

    @Override
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass) {

    }

    @Override
    public void renderDynamic(Vec3d translation, double delta, int pass) {

    }

    @Override
    public void onLayoutRefresh() {

    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

    }

    @Override
    public void writeData(DataOutput buffer) throws IOException {

        setNeedsSyncing(false);
    }

    @Override
    public void readData(DataInput buffer) throws IOException {

    }

    @Override
    public boolean needsSyncing() {

        return needsSyncing;
    }

    protected void setNeedsSyncing(boolean needsSyncing) {

        this.needsSyncing = needsSyncing;
    }

}
