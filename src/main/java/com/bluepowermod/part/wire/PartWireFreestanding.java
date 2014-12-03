package com.bluepowermod.part.wire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartWAILAProvider;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.RedstoneColor;
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.part.BPPart;

public abstract class PartWireFreestanding extends BPPart implements IRedstoneConductor, IBundledConductor, IPartRedstone,
IPartWAILAProvider {

    protected IRedstoneDevice[] devices = new IRedstoneDevice[6];
    protected IBundledDevice[] bundledDevices = new IBundledDevice[6];
    protected boolean[] connections = new boolean[6];

    protected RedstoneColor insulationColor = RedstoneColor.NONE;
    protected RedstoneColor bundleColor = RedstoneColor.NONE;
    protected byte power = 0;
    protected byte[] bundledPower = new byte[16];

    protected boolean bundled = false;

    public PartWireFreestanding(RedstoneColor insulationColor) {

        this.insulationColor = insulationColor;

        Arrays.fill(bundledPower, (byte) -128);
    }

    public PartWireFreestanding(RedstoneColor bundleColor, boolean unused) {

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

        double size = 8 / 16D;

        boxes.add(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 0.5 + (size / 2)));

        return boxes;
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        double size = 8 / 16D;

        boxes.add(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 0.5 + (size / 2)));

        return boxes;
    }

    @Override
    public void addCollisionBoxesToList(List<Vec3dCube> boxes, Entity entity) {

        double size = 8 / 16D;

        boxes.add(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 0.5 + (size / 2)));
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        double size = 2 / 16D;

        double separation = 4 / 16D;
        double thickness = 1 / 16D;

        boolean down = connections[ForgeDirection.DOWN.ordinal()];
        boolean up = connections[ForgeDirection.UP.ordinal()];
        boolean north = connections[ForgeDirection.NORTH.ordinal()];
        boolean south = connections[ForgeDirection.SOUTH.ordinal()];
        boolean west = connections[ForgeDirection.WEST.ordinal()];
        boolean east = connections[ForgeDirection.EAST.ordinal()];

        IIcon icon = IconSupplier.wire;

        renderer.setColor(WireCommons.getColorForPowerLevel(getColor(), power));

        // Wire
        renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2),
                0.5 + (size / 2)), icon);
        if (up)
            renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 + (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 1, 0.5 + (size / 2)),
                    icon);
        if (down)
            renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0, 0.5 - (size / 2), 0.5 + (size / 2), 0.5 - (size / 2), 0.5 + (size / 2)),
                    icon);
        if (north)
            renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0, 0.5 + (size / 2), 0.5 + (size / 2), 0.5 - (size / 2)),
                    icon);
        if (south)
            renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 1),
                    icon);
        if (west)
            renderer.renderBox(new Vec3dCube(0, 0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2)),
                    icon);
        if (east)
            renderer.renderBox(new Vec3dCube(0.5 + (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 1, 0.5 + (size / 2), 0.5 + (size / 2)),
                    icon);

        renderer.setColor(0xFFFFFF);

        // Frame
        IIcon planks = Blocks.planks.getIcon(0, 0);
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2),
        // 0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2)), planks);

        // Top
        renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2), 0.5
                - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5
                + ((size + separation) / 2) + thickness), planks);
        renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2)
                - thickness, 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2) + thickness, 0.5
                + ((size + separation) / 2) + thickness), planks);
        renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2),
                0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2) + thickness),
                planks);
        renderer.renderBox(
                new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2),
                        0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2)
                        + thickness, 0.5 - ((size + separation) / 2)), planks);
        // Bottom
        renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2) - thickness, 0.5
                - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5
                + ((size + separation) / 2) + thickness), planks);
        renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness, 0.5
                - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2) + thickness, 0.5 - ((size + separation) / 2), 0.5
                + ((size + separation) / 2) + thickness), planks);
        renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness,
                0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5
                + ((size + separation) / 2) + thickness), planks);
        renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness, 0.5
                - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2),
                0.5 - ((size + separation) / 2)), planks);

        // Sides
        renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5
                - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2),
                0.5 - ((size + separation) / 2)), planks);
        renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2),
                0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5
                + ((size + separation) / 2) + thickness), planks);
        renderer.renderBox(
                new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2),
                        0.5 - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2) + thickness,
                        0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2)), planks);
        renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2),
                0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness),
                planks);

        renderer.setRotation(0, 0, 0, Vec3d.center);

        return true;
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
    public boolean canConnectStraight(IRedstoneDevice device) {

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectOpenCorner(IRedstoneDevice device) {

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

        this.power = power;
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
        power = (byte) input;

        // for (ForgeDirection s : ForgeDirection.VALID_DIRECTIONS) {
        // if (s != getFace()) {
        // WirePropagator.INSTANCE.onPowerLevelChange(this, s, power, (byte) input);
        // break;
        // }
        // }
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
    public boolean canConnectBundledStraight(IBundledDevice device) {

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectBundledOpenCorner(IBundledDevice device) {

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
    public List<IBundledDevice> propagateBundled(ForgeDirection fromSide) {

        List<IBundledDevice> devices = new ArrayList<IBundledDevice>();
        for (IBundledDevice d : bundledDevices)
            if (d != null)
                devices.add(d);

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
