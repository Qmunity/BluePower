package com.bluepowermod.api.gate;

import com.bluepowermod.client.render.RenderHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.core.Vec3i;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

public interface IGateComponent {

    public IGate<?, ?, ?, ?, ?, ?> getGate();

    public void addCollisionBoxes(List<AABB> boxes);

    public List<AABB> getOcclusionBoxes();

    public void tick();

    @OnlyIn(Dist.CLIENT)
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass);

    @OnlyIn(Dist.CLIENT)
    public void renderDynamic(Vec3 translation, double delta, int pass);

    public void onLayoutRefresh();

    public void writeToNBT(CompoundTag tag);

    public void readFromNBT(CompoundTag tag);

    public void writeData(DataOutput buffer) throws IOException;

    public void readData(DataInput buffer) throws IOException;

    public boolean needsSyncing();
}
