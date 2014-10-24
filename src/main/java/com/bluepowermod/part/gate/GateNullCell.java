package com.bluepowermod.part.gate;

import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneColor;
import com.bluepowermod.api.bluestone.ConductionMapHelper;
import com.bluepowermod.api.bluestone.IBluestoneConductor;
import com.bluepowermod.api.bluestone.IBluestoneDevice;
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.bluestone.BluestoneApi;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class GateNullCell extends GateBase implements IBluestoneConductor {

    private IBluestoneDevice[] devices = new IBluestoneDevice[6];

    private boolean on2 = false;
    private boolean on3 = false;

    @Override
    public void initializeConnections() {

    }

    @Override
    public String getId() {

        return "nullcell";
    }

    @Override
    protected void renderTop(float frame) {

    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderBlocks renderer, int pass) {

        super.renderStatic(translation, renderer, pass);
        double height = 2 / 16D;

        renderer.renderAllFaces = true;

        renderer.setOverrideBlockTexture(Blocks.planks.getIcon(0, 0));
        renderBox(translation, renderer, new Vec3dCube(2 / 16D, 2 / 16D, 2 / 16D, 3 / 16D, 10 / 16D, 3 / 16D));
        renderBox(translation, renderer, new Vec3dCube(2 / 16D, 2 / 16D, 13 / 16D, 3 / 16D, 10 / 16D, 14 / 16D));
        renderBox(translation, renderer, new Vec3dCube(2 / 16D, 9 / 16D, 3 / 16D, 3 / 16D, 10 / 16D, 13 / 16D));

        renderBox(translation, renderer, new Vec3dCube(13 / 16D, 2 / 16D, 2 / 16D, 14 / 16D, 10 / 16D, 3 / 16D));
        renderBox(translation, renderer, new Vec3dCube(13 / 16D, 2 / 16D, 13 / 16D, 14 / 16D, 10 / 16D, 14 / 16D));
        renderBox(translation, renderer, new Vec3dCube(13 / 16D, 9 / 16D, 3 / 16D, 14 / 16D, 10 / 16D, 13 / 16D));

        renderer.setOverrideBlockTexture(on2 ? IconSupplier.bluestoneOn : IconSupplier.bluestoneOff);
        renderBox(translation, renderer, new Vec3dCube(7 / 16D, 2 / 16D, 1 / 16D, 9 / 16D, 2 / 16D + height, 15 / 16D));
        renderBox(translation, renderer, new Vec3dCube(7 / 16D, 2 / 16D, 0 / 16D, 9 / 16D, 2 / 16D + (height / 2), 1 / 16D));
        renderBox(translation, renderer, new Vec3dCube(7 / 16D, 2 / 16D, 15 / 16D, 9 / 16D, 2 / 16D + (height / 2), 16 / 16D));

        renderer.setOverrideBlockTexture(on3 ? IconSupplier.bluestoneOn : IconSupplier.bluestoneOff);
        renderBox(translation, renderer, new Vec3dCube(0 / 16D, 2 / 16D, 7 / 16D, 2 / 16D, 12 / 16D, 9 / 16D));
        renderBox(translation, renderer, new Vec3dCube(14 / 16D, 2 / 16D, 7 / 16D, 16 / 16D, 12 / 16D, 9 / 16D));
        renderBox(translation, renderer, new Vec3dCube(2 / 16D, 10 / 16D, 7 / 16D, 14 / 16D, 12 / 16D, 9 / 16D));

        renderer.setRenderBounds(0, 0, 0, 1, 1, 1);
        renderer.setOverrideBlockTexture(null);
        renderer.renderAllFaces = false;
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
    public IBluestoneDevice getConnectedDevice(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public void onPowerUpdate(int network, int oldValue, int newValue) {

        if (network == 2) {
            on2 = newValue > 0;
        }
        if (network == 3) {
            on3 = newValue > 0;
        }
    }

    @Override
    public void listConnected(List<IBluestoneDevice> visited, BluestoneColor insulationColor, ForgeDirection from) {

        visited.add(this);
        refreshConnections();

        int map = getConductionMap(null);
        int net = ConductionMapHelper.getNetwork(map, from);

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (side == getFace() || side == getFace().getOpposite())
                continue;
            if (net != ConductionMapHelper.getNetwork(map, side))
                continue;

            IBluestoneDevice dev = getConnectedDevice(side);
            if (dev != null && !visited.contains(dev))
                dev.listConnected(visited, BluestoneColor.NONE, side.getOpposite());
        }
    }

    @Override
    public boolean canConnect(BluestoneColor insulationColor, BluestoneColor bundleColor) {

        return bundleColor == BluestoneColor.INVALID;
    }

    @Override
    public int getConductionMap(BluestoneColor insulationColor) {

        return 0x012233;
    }

    private void refreshConnections() {

        devices = new IBluestoneDevice[6];

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (side == getFace() || side == getFace().getOpposite())
                continue;

            IBluestoneDevice dev = BluestoneApi.getInstance().getDevice(getWorld(), getX() + side.offsetX, getY() + side.offsetY,
                    getZ() + side.offsetZ, getFace());
            if (dev != null && dev.canConnect(BluestoneColor.NONE, BluestoneColor.INVALID)) {
                devices[side.ordinal()] = dev;
            }
        }

        sendUpdatePacket();
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
            mop = new QMovingObjectPosition(mop, mop.getPart(), new Vec3dCube(0, 0, 0, 1, 12 / 16D, 1));

        return mop;
    }

}
