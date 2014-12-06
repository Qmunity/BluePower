package com.bluepowermod.part.wire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartWAILAProvider;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IFaceBundledDevice;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.RedstoneColor;
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.BPPartFace;
import com.bluepowermod.part.wire.propagation.WirePropagator;

public abstract class PartWireFace extends BPPartFace implements IFaceRedstoneDevice, IRedstoneConductor, IFaceBundledDevice,
        IBundledConductor, IPartRedstone, IPartWAILAProvider {

    protected IRedstoneDevice[] devices = new IRedstoneDevice[6];
    protected IBundledDevice[] bundledDevices = new IBundledDevice[6];
    protected boolean[] connections = new boolean[6];

    protected RedstoneColor insulationColor = RedstoneColor.NONE;
    protected RedstoneColor bundleColor = RedstoneColor.NONE;
    protected byte power = 0;
    protected byte[] bundledPower = new byte[16];

    protected boolean bundled = false;

    public PartWireFace(RedstoneColor insulationColor) {

        this.insulationColor = insulationColor;

        Arrays.fill(bundledPower, (byte) -128);
    }

    public PartWireFace(RedstoneColor bundleColor, boolean unused) {

        this.bundleColor = bundleColor;
        bundled = true;

        Arrays.fill(bundledPower, (byte) -128);
    }

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
        double width = 1 / 16D;

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

        switch (getFace()) {
        case DOWN:
            break;
        case UP:
            renderer.addTransformation(new Rotation(180, 0, 0, Vec3d.center));
            break;
        case NORTH:
            renderer.addTransformation(new Rotation(90, 90, 0, Vec3d.center));
            d1 = d1.getRotation(getFace());
            d2 = d2.getRotation(getFace());
            d3 = d3.getRotation(getFace());
            d4 = d4.getRotation(getFace());
            break;
        case SOUTH:
            renderer.addTransformation(new Rotation(-90, 90, 0, Vec3d.center));
            d1 = d1.getRotation(getFace());
            d2 = d2.getRotation(getFace());
            d3 = d3.getRotation(getFace());
            d4 = d4.getRotation(getFace());
            break;
        case WEST:
            renderer.addTransformation(new Rotation(0, 0, -90, Vec3d.center));
            break;
        case EAST:
            renderer.addTransformation(new Rotation(0, 0, 90, Vec3d.center));
            break;
        default:
            break;
        }

        boolean north = connections[d1.ordinal()];
        boolean northOpen = false;// connections[d1.ordinal()];
        boolean south = connections[d2.ordinal()];
        boolean southOpen = false;// connections[d2.ordinal()];
        boolean west = connections[d3.ordinal()];
        boolean westOpen = false;// connections[d3.ordinal()];
        boolean east = connections[d4.ordinal()];
        boolean eastOpen = false;// connections[d4.ordinal()];

        IIcon icon = IconSupplier.wire;

        renderer.setColor(WireCommons.getColorForPowerLevel(getColor(), power));

        // Center
        renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D - width, 8 / 16D + width, height, 8 / 16D + width), icon);
        // Sides
        if (east || west) {
            if (west || (!west && east && !north && !south))
                renderer.renderBox(new Vec3dCube(west ? (westOpen ? -2 / 16D : 0) : 5 / 16D, 0, 8 / 16D - width, 8 / 16D - width, height,
                        8 / 16D + width), icon);
            if (east || (west && !east && !north && !south))
                renderer.renderBox(new Vec3dCube(8 / 16D + width, 0, 8 / 16D - width, east ? (eastOpen ? 18 / 16D : 1) : 11 / 16D, height,
                        8 / 16D + width), icon);
            if (north)
                renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, north ? (northOpen ? -2 / 16D : 0) : 5 / 16D, 8 / 16D + width, height,
                        8 / 16D - width), icon);
            if (south)
                renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D + width, 8 / 16D + width, height,
                        south ? (southOpen ? 18 / 16D : 1) : 11 / 16D), icon);
        } else {
            renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, north ? 0 : 5 / 16D, 8 / 16D + width, height, 8 / 16D - width), icon);
            renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D + width, 8 / 16D + width, height, south ? 1 : 11 / 16D), icon);
        }

        renderer.setColor(0xFFFFFF);
        renderer.resetTransformations();

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        power = (byte) 255;

        RenderHelper rh = RenderHelper.instance;
        rh.setRenderCoords(null, 0, 0, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glTranslated(0, 0.5, 0);
        GL11.glScaled(2, 2, 2);
        Tessellator.instance.startDrawingQuads();
        renderStatic(new Vec3i(0, 0, 0), rh, RenderBlocks.getInstance(), 0);
        Tessellator.instance.draw();
        GL11.glScaled(0.5, 0.5, 0.5);

        rh.reset();
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);

        for (int i = 0; i < 6; i++)
            tag.setBoolean("connected_" + i, devices[i] != null || bundledDevices[i] != null);

        tag.setInteger("insulationColor", insulationColor.ordinal());
        tag.setInteger("bundleColor", bundleColor.ordinal());
        tag.setBoolean("bundled", bundled);

        for (int i = 0; i < 16; i++)
            tag.setByte("power_" + i, bundledPower[i]);
        tag.setByte("power", power);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);

        for (int i = 0; i < 6; i++)
            connections[i] = tag.getBoolean("connected_" + i);

        insulationColor = RedstoneColor.values()[tag.getInteger("insulationColor")];
        bundleColor = RedstoneColor.values()[tag.getInteger("bundleColor")];
        bundled = tag.getBoolean("bundled");

        for (int i = 0; i < 16; i++)
            bundledPower[i] = tag.getByte("power_" + i);
        power = tag.getByte("power");

        try {
            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
        } catch (Exception ex) {
        }
    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        return side != getFace().getOpposite() && WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        return WireCommons.canConnect(this, device);
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        devices[side.ordinal()] = device;
        sendUpdatePacket();
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
        sendUpdatePacket();
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (!isAnalog())
            return (byte) ((power & 0xFF) > 0 ? 255 : 0);

        return power;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        this.power = isAnalog() ? power : (((power & 0xFF) > 0) ? (byte) 255 : (byte) 0);
    }

    @Override
    public void onRedstoneUpdate() {

        sendUpdatePacket();
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public RedstoneColor getInsulationColor() {

        return insulationColor;
    }

    @Override
    public List<Pair<IRedstoneDevice, ForgeDirection>> propagate(ForgeDirection fromSide) {

        List<Pair<IRedstoneDevice, ForgeDirection>> devices = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();
        for (int i = 0; i < 6; i++) {
            IRedstoneDevice d = this.devices[i];
            if (d != null)
                devices.add(new Pair<IRedstoneDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
        }

        return devices;
    }

    @Override
    public void onAdded() {

        super.onAdded();
    }

    @Override
    public void onLoaded() {

        super.onLoaded();
    }

    @Override
    public void onRemoved() {

        if (getWorld().isRemote)
            return;

        WireCommons.disconnect(this, this);
    }

    @Override
    public void onUpdate() {

        super.onUpdate();

        if (getWorld().isRemote)
            return;

        WireCommons.refreshConnections(this, this);

        int input = 0;
        for (int i = 0; i < 6; i++) {
            IRedstoneDevice d = devices[i];
            if (d != null && !(d instanceof IRedstoneConductor)) {
                input = Math.max(input, d.getRedstonePower(ForgeDirection.getOrientation(i)) & 0xFF);
            }
        }

        for (ForgeDirection s : ForgeDirection.VALID_DIRECTIONS) {
            if (s != getFace()) {
                WirePropagator.INSTANCE.onPowerLevelChange(this, s, power, (byte) input);
                break;
            }
        }
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        return false;// side != getFace();
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        // if (!shouldOutput)
        // return 0;
        //
        // if (isBundled())
        // return 0;
        //
        // if (side != getFace())
        // return 0;
        //
        // return MathHelper.map(power, 0, 255, 0, 15);
        return 0;
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        // if (!shouldOutput)
        // return 0;
        //
        // if (isBundled())
        // return 0;
        //
        // IRedstoneDevice device = getDeviceOnSide(side);
        // if (device == null)
        // return 0;
        // if (!(device instanceof DummyRedstoneDevice))
        // return 0;
        //
        // return MathHelper.map(power, 0, 255, 0, 15);
        return 0;
    }

    public abstract int getColor();

    @Override
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectBundledClosedCorner(ForgeDirection side, IBundledDevice device) {

        return WireCommons.canConnect(this, device);
    }

    @Override
    public void onConnect(ForgeDirection side, IBundledDevice device) {

        bundledDevices[side.ordinal()] = device;
        sendUpdatePacket();
    }

    @Override
    public IBundledDevice getBundledDeviceOnSide(ForgeDirection side) {

        return bundledDevices[side.ordinal()];
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        return bundledPower;
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        bundledPower = power;
    }

    @Override
    public void onBundledUpdate() {

        sendUpdatePacket();
    }

    @Override
    public RedstoneColor getBundleColor() {

        return bundleColor;
    }

    @Override
    public List<Pair<IBundledDevice, ForgeDirection>> propagateBundled(ForgeDirection fromSide) {

        List<Pair<IBundledDevice, ForgeDirection>> devices = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();
        // for (IBundledDevice d : bundledDevices)
        // if (d != null)
        // devices.add(d);

        return devices;
    }

    @Override
    public boolean isBundled() {

        return bundled;
    }

    @Override
    public void addWAILABody(List<String> text) {

        text.add("Power: " + (power & 0xFF) + "/255");
    }

}
