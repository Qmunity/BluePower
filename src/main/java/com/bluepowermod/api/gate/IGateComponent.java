package com.bluepowermod.api.gate;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IGateComponent {

    public IGate<?, ?, ?, ?, ?, ?> getGate();

    public void addCollisionBoxes(List<Vec3dCube> boxes);

    public List<Vec3dCube> getOcclusionBoxes();

    public void tick();

    @SideOnly(Side.CLIENT)
    public void renderStatic(Vec3i translation, RenderHelper renderer, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(Vec3d translation, double delta, int pass);

    public void onLayoutRefresh();

    public void writeToNBT(NBTTagCompound tag);

    public void readFromNBT(NBTTagCompound tag);

    public void writeData(DataOutput buffer) throws IOException;

    public void readData(DataInput buffer) throws IOException;

    public boolean needsSyncing();
}
