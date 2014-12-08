package com.bluepowermod.part.wire;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartSolid;
import uk.co.qmunity.lib.part.IPartThruHole;
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
import com.bluepowermod.part.wire.propagation.WirePropagator;

public abstract class PartWireFreestanding extends BPPart implements IRedstoneConductor, IBundledConductor, IPartRedstone,
IPartWAILAProvider, IPartSolid, IPartThruHole {

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

        if (getParent() == null || getWorld() == null)
            return boxes;

        Vec3dCube box = new Vec3dCube(0.5 - (size / 2), 0, 0.5 - (size / 2), 0.5 + (size / 2), 0.5 - (size / 2), 0.5 + (size / 2));

        if (getWorld().isRemote) {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (connections[d.ordinal()])
                    boxes.add(box.clone().rotate(d, Vec3d.center));
            }
        } else {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (devices[d.ordinal()] != null || bundledDevices[d.ordinal()] != null)
                    boxes.add(box.clone().rotate(d, Vec3d.center));
            }
        }

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

        boxes.addAll(getSelectionBoxes());
    }

    @Override
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        IIcon normalIcon = IconSupplier.wire;
        IIcon insulationIcon = IconSupplier.wireInsulation;
        IIcon bundleIcon = IconSupplier.wireBundled;

        double size = 2 / 16D;
        double separation = 4 / 16D;
        double thickness = 1 / 16D;

        IIcon icon = normalIcon;
        int color = WireCommons.getColorForPowerLevel(getColor(), power);

        if (insulationColor != RedstoneColor.NONE) {
            size += 1 / 16D;
            separation -= 1 / 16D;
            icon = insulationIcon;
            color = ItemDye.field_150922_c[15 - insulationColor.ordinal()];
        }
        if (isBundled()) {
            size += 4 / 16D;
            separation -= 4 / 16D;
            icon = bundleIcon;
            color = 0xFFFFFF;
        }

        boolean isInWorld = getParent() != null;

        boolean down = connections[ForgeDirection.DOWN.ordinal()];
        boolean up = connections[ForgeDirection.UP.ordinal()];
        boolean north = connections[ForgeDirection.NORTH.ordinal()];
        boolean south = connections[ForgeDirection.SOUTH.ordinal()];
        boolean west = connections[ForgeDirection.WEST.ordinal()];
        boolean east = connections[ForgeDirection.EAST.ordinal()];

        renderer.setColor(color);

        // Wire
        renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2),
                0.5 + (size / 2)), icon);
        if (up || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 + (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 1, 0.5 + (size / 2)),
                    icon);
        if (down || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0, 0.5 - (size / 2), 0.5 + (size / 2), 0.5 - (size / 2), 0.5 + (size / 2)),
                    icon);
        if (north || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0, 0.5 + (size / 2), 0.5 + (size / 2), 0.5 - (size / 2)),
                    icon);
        if (south || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 1),
                    icon);
        if (west || !isInWorld)
            renderer.renderBox(new Vec3dCube(0, 0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2)),
                    icon);
        if (east || !isInWorld)
            renderer.renderBox(new Vec3dCube(0.5 + (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 1, 0.5 + (size / 2), 0.5 + (size / 2)),
                    icon);

        renderer.setColor(0xFFFFFF);

        // Frame
        {
            IIcon planks = Blocks.planks.getIcon(0, 0);

            // Top
            if (west == up || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2),
                        0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness,
                        0.5 + ((size + separation) / 2)), planks);
            if (east == up || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2),
                        0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2)
                        + thickness, 0.5 + ((size + separation) / 2)), planks);
            if (south == up || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2),
                        0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5
                        + ((size + separation) / 2) + thickness), planks);
            if (north == up || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5
                        - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2)
                        + thickness, 0.5 - ((size + separation) / 2)), planks);
            // Bottom
            if (west == down || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2) - thickness,
                        0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2),
                        0.5 + ((size + separation) / 2)), planks);
            if (east == down || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness,
                        0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 - ((size + separation) / 2),
                        0.5 + ((size + separation) / 2)), planks);
            if (south == down || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness,
                        0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5
                        + ((size + separation) / 2) + thickness), planks);
            if (north == down || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness, 0.5
                        - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2),
                        0.5 - ((size + separation) / 2)), planks);

            // Sides
            if (north == west || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5
                        - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2),
                        0.5 - ((size + separation) / 2)), planks);
            if (south == west || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2),
                        0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5
                        + ((size + separation) / 2) + thickness), planks);
            if (north == east || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5
                        - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2) + thickness,
                        0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2)), planks);
            if (south == east || !isInWorld)
                renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2),
                        0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2), 0.5
                        + ((size + separation) / 2) + thickness), planks);

            // Corners
            renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2), 0.5
                    - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness,
                    0.5 - ((size + separation) / 2)), planks);
            renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2),
                    0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5
                    + ((size + separation) / 2) + thickness), planks);
            renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5
                    - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2)
                    + thickness, 0.5 - ((size + separation) / 2)), planks);
            renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2),
                    0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2)
                    + thickness, 0.5 + ((size + separation) / 2) + thickness), planks);

            renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2) - thickness, 0.5
                    - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2),
                    0.5 - ((size + separation) / 2)), planks);
            renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2) - thickness,
                    0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5
                    + ((size + separation) / 2) + thickness), planks);
            renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness, 0.5
                    - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2) + thickness, 0.5 - ((size + separation) / 2),
                    0.5 - ((size + separation) / 2)), planks);
            renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness,
                    0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 - ((size + separation) / 2), 0.5
                    + ((size + separation) / 2) + thickness), planks);

            if (isInWorld) {
                // Connections
                Vec3dCube box = new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0, 0.5 - ((size + separation) / 2) - thickness,
                        0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2));
                for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                    if (!connections[d.ordinal()])
                        continue;
                    for (int i = 0; i < 4; i++)
                        renderer.renderBox(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(d, Vec3d.center), planks);
                }
            }
        }
        renderer.resetTransformations();

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        power = (byte) 255;
        connections[ForgeDirection.EAST.ordinal()] = true;
        connections[ForgeDirection.WEST.ordinal()] = true;

        RenderHelper rh = RenderHelper.instance;
        rh.setRenderCoords(null, 0, 0, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glTranslated(0, -0.125, 0);
        GL11.glScaled(1.25, 1.25, 1.25);
        Tessellator.instance.startDrawingQuads();
        renderStatic(new Vec3i(0, 0, 0), rh, RenderBlocks.getInstance(), 0);
        Tessellator.instance.draw();
        GL11.glScaled(1 / 1.25, 1 / 1.25, 1 / 1.25);

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

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

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
        power = (byte) input;

        WirePropagator.INSTANCE.onPowerLevelChange(this, ForgeDirection.UP, power, (byte) input);
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        return false;// side != getFace();
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        // if (!shouldOutput)
        // return 0;

        if (isBundled())
            return 0;

        return MathHelper.map(power & 0xFF, 0, 255, 0, 15);
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

        if (isBundled())
            return 0;

        return MathHelper.map(power & 0xFF, 0, 255, 0, 15);
        // return 0;
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
        for (int i = 0; i < 6; i++) {
            IBundledDevice d = bundledDevices[i];
            if (d != null)
                devices.add(new Pair<IBundledDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
        }

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

    @Override
    public boolean isSideSolid(ForgeDirection face) {

        return true;
    }

    @Override
    public int getHollowSize(ForgeDirection side) {

        return 1;
    }

}
