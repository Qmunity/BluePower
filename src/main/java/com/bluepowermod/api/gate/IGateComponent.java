package com.bluepowermod.api.gate;

import com.bluepowermod.client.render.RenderHelper;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

public interface IGateComponent {

    public IGate<?, ?, ?, ?, ?, ?> getGate();

    public void addCollisionBoxes(List<AxisAlignedBB> boxes);

    public List<AxisAlignedBB> getOcclusionBoxes();

    public void tick();

    @OnlyIn(Dist.CLIENT)
    public void renderStatic(Vector3i translation, RenderHelper renderer, int pass);

    @OnlyIn(Dist.CLIENT)
    public void renderDynamic(Vector3d translation, double delta, int pass);

    public void onLayoutRefresh();

    public void writeToNBT(CompoundNBT tag);

    public void readFromNBT(CompoundNBT tag);

    public void writeData(DataOutput buffer) throws IOException;

    public void readData(DataInput buffer) throws IOException;

    public boolean needsSyncing();
}
