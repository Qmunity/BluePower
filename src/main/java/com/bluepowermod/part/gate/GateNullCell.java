package com.bluepowermod.part.gate;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneColor;
import com.bluepowermod.api.bluestone.IBluestoneDevice;
import com.bluepowermod.api.bluestone.IBluestoneHandler;
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.bluestone.BluestoneApi;
import com.qmunity.lib.misc.ForgeDirectionUtils;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.vec.Vec3d;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class GateNullCell extends GateBase implements IBluestoneDevice {

    private boolean on2 = false;
    private boolean on3 = false;

    private IBluestoneHandler handler;
    private List<IBluestoneHandler> handlers = new ArrayList<IBluestoneHandler>();

    public GateNullCell() {

        handlers.add(handler = BluestoneApi.getInstance().createDefaultBluestoneHandler(this, BluestoneColor.NONE, 0x012233));
    }

    @Override
    public void initializeConnections() {

    }

    @Override
    public String getId() {

        return "nullcell";
    }

    @Override
    protected void renderTop(float frame) {

        boolean old2 = on2;
        boolean old3 = on3;

        on2 = handler.getPower(2) > 0;
        on3 = handler.getPower(3) > 0;

        if (old2 != on2 || old3 != on3)
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
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
    public void onUpdate() {

        super.onUpdate();
        handler.refreshConnections(true);
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
    public BluestoneColor getBundleColor() {

        return BluestoneColor.INVALID;
    }

    @Override
    public List<IBluestoneHandler> getHandlers() {

        return handlers;
    }

    @Override
    public IBluestoneDevice getNeighbor(ForgeDirection side) {

        Vec3i loc = new Vec3i(this).add(ForgeDirectionUtils.getOnFace(getFace(), side));
        return BluestoneApi.getInstance().getDevice(getWorld(), loc.getX(), loc.getY(), loc.getZ(), getFace(), true);
    }

}
