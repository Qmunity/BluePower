package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.IBluestoneDevice;
import com.bluepowermod.api.bluestone.IMultiNetworkBluestoneConductor;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.bluestone.BluestoneManager;
import com.qmunity.lib.client.render.RenderHelper;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class GateNullCell extends GateBase implements IMultiNetworkBluestoneConductor {

    private boolean on2 = false;
    private boolean on3 = false;

    private IBluestoneDevice[] connections = new IBluestoneDevice[6];

    @Override
    public void initializeConnections() {

    }

    @Override
    public String getId() {

        return "nullcell";
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        // super.renderStatic(translation, renderer, pass);
        // double height = 2 / 16D;
        //
        // renderer.renderAllFaces = true;
        //
        // renderer.setOverrideBlockTexture(Blocks.planks.getIcon(0, 0));
        // renderBox(translation, renderer, new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 3 / 16D, 10 / 16D, 3 / 16D));
        // renderBox(translation, renderer, new Vec3dCube(2 / 16D, 2 / 16D, 13 / 16D, 3 / 16D, 10 / 16D, 14 / 16D));
        // renderBox(translation, renderer, new Vec3dCube(2 / 16D, 9 / 16D, 3 / 16D, 3 / 16D, 10 / 16D, 13 / 16D));
        //
        // renderBox(translation, renderer, new Vec3dCube(13 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 10 / 16D, 3 / 16D));
        // renderBox(translation, renderer, new Vec3dCube(13 / 16D, 2 / 16D, 13 / 16D, 14 / 16D, 10 / 16D, 14 / 16D));
        // renderBox(translation, renderer, new Vec3dCube(13 / 16D, 9 / 16D, 3 / 16D, 14 / 16D, 10 / 16D, 13 / 16D));
        //
        // renderer.setOverrideBlockTexture(on2 ? IconSupplier.bluestoneOn : IconSupplier.bluestoneOff);
        // renderBox(translation, renderer, new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D));
        // renderBox(translation, renderer, new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D, 2 / 16D + (height / 2), 1 / 16D));
        // renderBox(translation, renderer, new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D, 2 / 16D + (height / 2), 16 / 16D));
        //
        // renderer.setOverrideBlockTexture(on3 ? IconSupplier.bluestoneOn : IconSupplier.bluestoneOff);
        // renderBox(translation, renderer, new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 12 / 16D, 9 / 16D));
        // renderBox(translation, renderer, new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D));
        // renderBox(translation, renderer, new Vec3dCube(2 / 16D, 10 / 16D, 7 / 16D, 14 / 16D, 12 / 16D, 9 / 16D));
        //
        // renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        // renderer.setOverrideBlockTexture(null);
        // renderer.renderAllFaces = false;
        return true;
    }

    private void renderBox(Vec3i translation, RenderBlocks renderer, Vec3dCube box) {

        box = box.clone();
        VectorHelper.rotateBox(box, getFace(), 0);

        renderer.setRenderBounds(box.getMinX(), box.getMinY(), box.getMinZ(), box.getMaxX(), box.getMaxY(), box.getMaxZ());
        renderer.renderStandardBlock(Blocks.stone, translation.getX(), translation.getY(), translation.getZ());
    }

    @Override
    public void doLogic() {

    }

    @Override
    public void onUpdate() {

        super.onUpdate();

        BluestoneManager.INSTANCE.recalculateConnections(this);
    }

    @Override
    public void addSelectionBoxes(List<Vec3dCube> boxes) {

        super.addSelectionBoxes(boxes);
        addBoxes(boxes);
    }

    @Override
    public void addCollisionBoxes(List<Vec3dCube> boxes, Entity entity) {

        super.addCollisionBoxes(boxes, entity);
        addBoxes(boxes);
    }

    private void addBoxes(List<Vec3dCube> boxes) {

        double height = 2 / 16D;

        boxes.add(new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 3 / 16D, 10 / 16D, 3 / 16D));
        boxes.add(new Vec3dCube(2 / 16D, 2 / 16D, 13 / 16D, 3 / 16D, 10 / 16D, 14 / 16D));
        boxes.add(new Vec3dCube(2 / 16D, 9 / 16D, 3 / 16D, 3 / 16D, 10 / 16D, 13 / 16D));

        boxes.add(new Vec3dCube(13 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 10 / 16D, 3 / 16D));
        boxes.add(new Vec3dCube(13 / 16D, 2 / 16D, 13 / 16D, 14 / 16D, 10 / 16D, 14 / 16D));
        boxes.add(new Vec3dCube(13 / 16D, 9 / 16D, 3 / 16D, 14 / 16D, 10 / 16D, 13 / 16D));

        boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D));
        boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D, 2 / 16D + (height / 2), 1 / 16D));
        boxes.add(new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D, 2 / 16D + (height / 2), 16 / 16D));

        boxes.add(new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 12 / 16D, 9 / 16D));
        boxes.add(new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D));
        boxes.add(new Vec3dCube(2 / 16D, 10 / 16D, 7 / 16D, 14 / 16D, 12 / 16D, 9 / 16D));
    }

    @Override
    public QMovingObjectPosition rayTrace(Vec3d start, Vec3d end) {

        QMovingObjectPosition mop = super.rayTrace(start, end);

        if (mop != null)
            mop = new QMovingObjectPosition(mop, mop.getPart(), Vec3dCube.merge(getSelectionBoxes()));

        return mop;
    }

    @Override
    public void onBluestoneUpdate() {

        sendUpdatePacket();
    }

    @Override
    public void onConnect(ForgeDirection side, IBluestoneDevice device) {

        connections[side.ordinal()] = device;
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        connections[side.ordinal()] = null;
    }

    @Override
    public IBluestoneDevice getDeviceOnSide(ForgeDirection side) {

        return connections[side.ordinal()];
    }

    @Override
    public boolean canConnectOnSide(ForgeDirection side) {

        return side != getFace() && side != getFace().getOpposite();
    }

    @Override
    public double getInput(ForgeDirection side) {

        return 0;
    }

    @Override
    public int getConductionMap() {

        return 0x012233;
    }

    @Override
    public void setBluestonePowerLevel(int network, double power) {

        if (network == 2) {
            on2 = power > 0;
        }
        if (network == 3) {
            on3 = power > 0;
        }
    }

    @Override
    protected void renderTop(float frame) {

    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);

        tag.setBoolean("on2", on2);
        tag.setBoolean("on3", on3);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);

        on2 = tag.getBoolean("on2");
        on3 = tag.getBoolean("on3");
    }

}
