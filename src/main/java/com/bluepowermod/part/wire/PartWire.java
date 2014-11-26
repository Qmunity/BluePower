package com.bluepowermod.part.wire;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.BPPartFace;

public abstract class PartWire extends BPPartFace implements IFaceRedstoneDevice, IRedstoneConductor {

    protected IRedstoneDevice[] devices = new IRedstoneDevice[6];
    private byte power = 0;

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        boxes.add(new Vec3dCube(0, 0, 0, 1, 2 / 16D, 1));

        VectorHelper.rotateBoxes(boxes, getFace(), 0);
        return boxes;
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        boxes.add(new Vec3dCube(2 / 16D, 0, 2 / 16D, 14 / 16D, 2 / 16D, 14 / 16D));

        VectorHelper.rotateBoxes(boxes, getFace(), 0);
        return boxes;
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        double height = 2 / 16D;

        ForgeDirection d1 = ForgeDirection.NORTH;
        ForgeDirection d2 = ForgeDirection.SOUTH;
        ForgeDirection d3 = ForgeDirection.WEST;
        ForgeDirection d4 = ForgeDirection.EAST;

        if (getFace() == ForgeDirection.NORTH) {
            d1 = ForgeDirection.UP;
            d2 = ForgeDirection.DOWN;
        } else if (getFace() == ForgeDirection.SOUTH) {
            d1 = ForgeDirection.DOWN;
            d2 = ForgeDirection.UP;
        } else if (getFace() == ForgeDirection.WEST) {
            d3 = ForgeDirection.UP;
            d4 = ForgeDirection.DOWN;
        } else if (getFace() == ForgeDirection.EAST) {
            d3 = ForgeDirection.DOWN;
            d4 = ForgeDirection.UP;
        } else if (getFace() == ForgeDirection.UP) {
            d3 = ForgeDirection.EAST;
            d4 = ForgeDirection.WEST;
        }

        boolean north = getDeviceOnSide(d1) != null;
        boolean south = getDeviceOnSide(d2) != null;
        boolean west = getDeviceOnSide(d3) != null;
        boolean east = getDeviceOnSide(d4) != null;

        renderBlocks.setOverrideBlockTexture(power > 0 ? IconSupplier.bluestoneOn : IconSupplier.bluestoneOff);
        renderBlocks.renderAllFaces = true;

        // Center
        renderBox(translation, renderBlocks, new Vec3dCube(7 / 16D, 0, 7 / 16D, 9 / 16D, height, 9 / 16D));
        // Sides
        if (east || west) {
            if (west || (!west && east && !north && !south))
                renderBox(translation, renderBlocks, new Vec3dCube(west ? 0 : 5 / 16D, 0, 7 / 16D, 7 / 16D, height, 9 / 16D));
            if (east || (west && !east && !north && !south))
                renderBox(translation, renderBlocks, new Vec3dCube(9 / 16D, 0, 7 / 16D, east ? 1 : 11 / 16D, height, 9 / 16D));
            if (north)
                renderBox(translation, renderBlocks, new Vec3dCube(7 / 16D, 0, north ? 0 : 5 / 16D, 9 / 16D, height, 7 / 16D));
            if (south)
                renderBox(translation, renderBlocks, new Vec3dCube(7 / 16D, 0, 9 / 16D, 9 / 16D, height, south ? 1 : 11 / 16D));
        } else {
            renderBox(translation, renderBlocks, new Vec3dCube(7 / 16D, 0, north ? 0 : 5 / 16D, 9 / 16D, height, 7 / 16D));
            renderBox(translation, renderBlocks, new Vec3dCube(7 / 16D, 0, 9 / 16D, 9 / 16D, height, south ? 1 : 11 / 16D));
        }

        renderBlocks.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderBlocks.setOverrideBlockTexture(null);
        renderBlocks.renderAllFaces = false;
        return true;
    }

    private void renderBox(Vec3i translation, RenderBlocks renderer, Vec3dCube box) {

        box = box.clone();
        VectorHelper.rotateBox(box, getFace(), 0);

        renderer.setRenderBounds(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ());
        renderer.renderStandardBlock(Blocks.stone, translation.getX(), translation.getY(), translation.getZ());
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);

    }

    protected boolean canConnect(IRedstoneDevice device) {

        return true;
    }

    @Override
    public boolean canConnectStraight(IRedstoneDevice device) {

        return canConnect(device);
    }

    @Override
    public boolean canConnectOpenCorner(IRedstoneDevice device) {

        return canConnect(device);
    }

    @Override
    public boolean canConnectClosedCorner(IRedstoneDevice device) {

        return canConnect(device);
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        devices[side.ordinal()] = device;
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public byte getRedstoneOutput(ForgeDirection side) {

        if (!isAnalog())
            return (byte) (power > 0 ? 256 : 0);

        return power;
    }

    @Override
    public void onRedstoneUpdate(ForgeDirection side, byte power) {

        this.power = power;
    }

    public abstract boolean isAnalog();

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public List<IRedstoneDevice> propagate(ForgeDirection fromSide) {

        List<IRedstoneDevice> devices = new ArrayList<IRedstoneDevice>();
        for (IRedstoneDevice d : this.devices)
            if (d != null)
                devices.add(d);

        return devices;
    }

}
