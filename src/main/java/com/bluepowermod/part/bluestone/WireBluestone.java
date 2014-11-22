package com.bluepowermod.part.bluestone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneColor;
import com.bluepowermod.api.bluestone.BluestoneHelper;
import com.bluepowermod.api.bluestone.IBluestoneDevice;
import com.bluepowermod.api.bluestone.IBluestoneHandler;
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.BPPartFace;
import com.qmunity.lib.client.render.RenderHelper;
import com.qmunity.lib.helper.RedstoneHelper;
import com.qmunity.lib.part.IPartRedstone;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class WireBluestone extends BPPartFace implements IBluestoneDevice, IPartRedstone {

    private BluestoneColor insulationColor = BluestoneColor.NONE;
    private BluestoneColor bundleColor = BluestoneColor.INVALID;

    private List<IBluestoneHandler> handlers = new ArrayList<IBluestoneHandler>();

    public WireBluestone() {

        if (bundleColor != BluestoneColor.INVALID) {
            for (BluestoneColor c : BluestoneColor.VALID_COLORS)
                handlers.add(BluestoneApi.getInstance().createDefaultBluestoneHandler(this, c, 0x012222));
        } else {
            handlers.add(new WireBluestoneHandler(this, insulationColor, 0x012222));
        }
    }

    @Override
    public String getType() {

        return "bluestone";
    }

    @Override
    public String getUnlocalizedName() {

        return "bluestone";
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

        boolean north = getConnectedDevice(d1) != null;
        boolean south = getConnectedDevice(d2) != null;
        boolean west = getConnectedDevice(d3) != null;
        boolean east = getConnectedDevice(d4) != null;

        renderBlocks.setOverrideBlockTexture(isOn() ? IconSupplier.bluestoneOn : IconSupplier.bluestoneOff);
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

    private static boolean updating = false;

    @Override
    public void onUpdate() {

        if (updating)
            return;

        super.onUpdate();

        updating = true;

        for (IBluestoneHandler h : handlers)
            h.refreshConnections(true);

        if (bundleColor == BluestoneColor.INVALID) {
            IBluestoneHandler handler = handlers.get(0);
            List<Entry<IBluestoneHandler, Integer>> netHandlers = BluestoneHelper.listHandlersInNetwork(handler, 2);

            boolean p = false;
            for (Entry<IBluestoneHandler, Integer> e : netHandlers) {
                if (p)
                    break;
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
                    if (!p)
                        p = e.getKey().getInput(d) > 0;
            }

            for (Entry<IBluestoneHandler, Integer> e : netHandlers) {
                e.getKey().onUpdate(e.getValue(), p ? 15 : 0);
                IBluestoneDevice d = e.getKey().getDevice();
                if (d instanceof WireBluestone) {
                    ((WireBluestone) d).notifyNeighbors();
                }
            }
        }

        sendUpdatePacket();

        updating = false;
    }

    private boolean isRedstoneDeviceOnSide(ForgeDirection side) {

        IBluestoneHandler handler = getUncoveredCableHandler();
        if (handler == null)
            return false;

        IBluestoneHandler h = handler.getConnectedHandler(side);
        if (h == null)
            return false;
        if (h.getDevice() instanceof WireBluestone)
            return false;

        return true;
    }

    private void notifyNeighbors() {

        if (getUncoveredCableHandler() == null)
            return;

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS)
            if (isRedstoneDeviceOnSide(side))
                RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), side, true);
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);

        tag.setBoolean("isOn", isOn());
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);

        for (IBluestoneHandler h : handlers)
            h.refreshConnections(true);

        IBluestoneHandler handler = handlers.get(0);
        List<Entry<IBluestoneHandler, Integer>> netHandlers = BluestoneHelper.listHandlersInNetwork(handler, 2);

        boolean p = tag.getBoolean("isOn");

        for (Entry<IBluestoneHandler, Integer> e : netHandlers)
            e.getKey().onUpdate(e.getValue(), p ? 15 : 0);

        getWorld().markBlockRangeForRenderUpdate(getX() - 1, getY() - 1, getZ() - 1, getX() + 1, getY() + 1, getZ() + 1);
    }

    @Override
    public BluestoneColor getBundleColor() {

        return bundleColor;
    }

    @Override
    public List<IBluestoneHandler> getHandlers() {

        return handlers;
    }

    @Override
    public IBluestoneDevice getNeighbor(ForgeDirection side) {

        // Same block check
        {
            Vec3i loc = new Vec3i(this);
            IBluestoneDevice neighbor = BluestoneApi.getInstance().getDevice(getWorld(), loc.getX(), loc.getY(), loc.getZ(), side, true);
            if (neighbor instanceof WireBluestone) {
                return neighbor;
            }
        }

        // Diagonal check
        {
            Vec3i loc = new Vec3i(this).add(side).add(getFace());
            IBluestoneDevice neighbor = BluestoneApi.getInstance().getDevice(getWorld(), loc.getX(), loc.getY(), loc.getZ(),
                    side.getOpposite(), true);
            if (neighbor instanceof WireBluestone) {
                return neighbor;
            }
        }

        // Side check
        {
            Vec3i loc = new Vec3i(this).add(side);
            IBluestoneDevice neighbor = BluestoneApi.getInstance().getDevice(getWorld(), loc.getX(), loc.getY(), loc.getZ(), getFace(),
                    true);
            return neighbor;
        }
    }

    @Override
    public void onRemoved() {

        super.onRemoved();

        List<IBluestoneHandler> updatedHandlers = new ArrayList<IBluestoneHandler>();

        for (IBluestoneHandler h : handlers) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                IBluestoneHandler handler = h.getConnectedHandler(side);
                if (handler != null && !updatedHandlers.contains(handler))
                    updatedHandlers.add(handler);
            }
        }

        updatedHandlers.removeAll(handlers);

        for (IBluestoneHandler h : updatedHandlers)
            h.refreshConnections(false);
        updatedHandlers.clear();
    }

    private IBluestoneDevice getConnectedDevice(ForgeDirection side) {

        for (IBluestoneHandler h : handlers) {
            IBluestoneHandler con = h.getConnectedHandler(side);
            if (con != null)
                return con.getDevice();
        }

        return null;
    }

    private boolean isOn() {

        for (IBluestoneHandler h : handlers)
            if (h.getPower(2) > 0)
                return true;

        return false;
    }

    private IBluestoneHandler getUncoveredCableHandler() {

        if (handlers.size() == 1)
            return handlers.get(0);

        return null;
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        IBluestoneHandler handler = getUncoveredCableHandler();
        if (handler == null)
            return 0;
        if (!canConnectRedstone(side) || !isRedstoneDeviceOnSide(side))
            return 0;

        int power = handler.getPower(2);

        if (side == getFace())
            return power;

        return 0;
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        IBluestoneHandler handler = getUncoveredCableHandler();
        if (handler == null)
            return 0;
        if (!canConnectRedstone(side) || !isRedstoneDeviceOnSide(side))
            return 0;

        return handler.getPower(2);
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        if (getUncoveredCableHandler() == null)
            return false;

        return side != getFace() && side != getFace().getOpposite();
    }
}
