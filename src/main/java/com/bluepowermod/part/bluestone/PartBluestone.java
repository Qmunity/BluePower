package com.bluepowermod.part.bluestone;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.bluestone.BluestoneColor;
import com.bluepowermod.api.bluestone.ConductionMapHelper;
import com.bluepowermod.api.bluestone.DummyBluestoneDevice;
import com.bluepowermod.api.bluestone.IBluestoneConductor;
import com.bluepowermod.api.bluestone.IBluestoneDevice;
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.BPPartFace;
import com.qmunity.lib.helper.RedstoneHelper;
import com.qmunity.lib.raytrace.QMovingObjectPosition;
import com.qmunity.lib.vec.Vec3dCube;
import com.qmunity.lib.vec.Vec3i;

public class PartBluestone extends BPPartFace implements IBluestoneConductor {

    private IBluestoneDevice[] devices = new IBluestoneDevice[6];

    private BluestoneColor insulationColor = BluestoneColor.NONE;
    private BluestoneColor bundleColor = BluestoneColor.INVALID;

    private boolean on = false;

    @Override
    public String getType() {

        return "bluestone";
    }

    @Override
    public String getUnlocalizedName() {

        return "bluestone";
    }

    @Override
    public IBluestoneDevice getConnectedDevice(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public void onPowerUpdate(int network, int oldValue, int newValue) {

        on = newValue > 0;
        getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public void listConnected(List<IBluestoneDevice> visited, BluestoneColor insulationColor, ForgeDirection from) {

        visited.add(this);
        refreshConnections();

        if (insulationColor == null)
            insulationColor = this.insulationColor;

        int map = getConductionMap(insulationColor);
        int net = ConductionMapHelper.getNetwork(map, from);

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (side == getFace() || side == getFace().getOpposite())
                continue;
            if (net != ConductionMapHelper.getNetwork(map, side))
                continue;

            IBluestoneDevice dev = getConnectedDevice(side);
            if (dev != null && !visited.contains(dev))
                dev.listConnected(visited, this.insulationColor, side.getOpposite());
        }
    }

    @Override
    public int getConductionMap(BluestoneColor insulationColor) {

        return 0x012233;
    }

    @Override
    public boolean canConnect(BluestoneColor insulationColor, BluestoneColor bundleColor) {

        if (this.insulationColor == insulationColor && this.bundleColor == bundleColor)
            return true;

        return false;
    }

    private void refreshConnections() {

        devices = new IBluestoneDevice[6];

        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (side == getFace() || side == getFace().getOpposite())
                continue;

            IBluestoneDevice dev = BluestoneApi.getInstance().getDevice(getWorld(), getX() + side.offsetX, getY() + side.offsetY,
                    getZ() + side.offsetZ, getFace());
            if (dev != null && dev.canConnect(insulationColor, bundleColor)) {
                devices[side.ordinal()] = dev;
            }
        }

        sendUpdatePacket();
    }

    @Override
    public void onUpdate() {

        super.onUpdate();

        on = RedstoneHelper.getInputWeak(getWorld(), getX(), getY(), getZ(), ForgeDirection.SOUTH, getFace()) > 0;

        refreshConnections();
    }

    @Override
    public boolean onActivated(EntityPlayer player, QMovingObjectPosition hit, ItemStack item) {

        if (!player.worldObj.isRemote) {
            refreshConnections();
            // player.addChatMessage(new ChatComponentText("Neighbours: " + Arrays.asList(devices).toString()));
            //
            // List<IBluestoneDevice> l = new ArrayList<IBluestoneDevice>();
            // listConnected(l, null, ForgeDirection.NORTH);
            // player.addChatMessage(new ChatComponentText("Network: " + l.toString()));
            on = !on;
        }

        return true;
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
    public boolean renderStatic(Vec3i translation, RenderBlocks renderer, int pass) {

        double height = 2 / 16D;

        boolean north = getConnectedDevice(ForgeDirection.NORTH) != null;
        boolean south = getConnectedDevice(ForgeDirection.SOUTH) != null;
        boolean west = getConnectedDevice(ForgeDirection.WEST) != null;
        boolean east = getConnectedDevice(ForgeDirection.EAST) != null;

        renderer.setOverrideBlockTexture(on ? IconSupplier.bluestoneOn : IconSupplier.bluestoneOff);
        renderer.renderAllFaces = true;

        // Center
        renderBox(translation, renderer, new Vec3dCube(7 / 16D, 0, 7 / 16D, 9 / 16D, height, 9 / 16D));
        // Sides
        if (east || west) {
            if (west)
                renderBox(translation, renderer, new Vec3dCube(west ? 0 : 5 / 16D, 0, 7 / 16D, 7 / 16D, height, 9 / 16D));
            if (east)
                renderBox(translation, renderer, new Vec3dCube(9 / 16D, 0, 7 / 16D, east ? 1 : 11 / 16D, height, 9 / 16D));

            if (north)
                renderBox(translation, renderer, new Vec3dCube(7 / 16D, 0, north ? 0 : 5 / 16D, 9 / 16D, height, 7 / 16D));
            if (south)
                renderBox(translation, renderer, new Vec3dCube(7 / 16D, 0, 9 / 16D, 9 / 16D, height, south ? 1 : 11 / 16D));
        } else {
            renderBox(translation, renderer, new Vec3dCube(7 / 16D, 0, north ? 0 : 5 / 16D, 9 / 16D, height, 7 / 16D));
            renderBox(translation, renderer, new Vec3dCube(7 / 16D, 0, 9 / 16D, 9 / 16D, height, south ? 1 : 11 / 16D));
        }

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
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);

        NBTTagCompound connections = new NBTTagCompound();
        for (int i = 0; i < devices.length; i++)
            if (devices[i] != null)
                connections.setBoolean(i + "", true);
        tag.setTag("connections", connections);

        tag.setBoolean("isOn", on);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);

        NBTTagCompound connections = tag.getCompoundTag("connections");
        for (int i = 0; i < devices.length; i++) {
            if (connections.hasKey(i + "")) {
                devices[i] = new DummyBluestoneDevice();
            } else {
                devices[i] = null;
            }
        }

        boolean oldState = on;
        on = tag.getBoolean("isOn");
        if (on != oldState) {
            for (int i = 0; i < 2; i++) {
                ForgeDirection di = i == 0 ? ForgeDirection.NORTH : ForgeDirection.EAST;
                List<IBluestoneDevice> l = new ArrayList<IBluestoneDevice>();
                listConnected(l, null, di);
                int net = ConductionMapHelper.getNetwork(getConductionMap(insulationColor), di);
                System.out.println(l);
                for (IBluestoneDevice d : l) {
                    d.onPowerUpdate(net, oldState ? 15 : 0, on ? 15 : 0);
                }
            }
        }
    }
}
