/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.part.wire.redstone;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartFace;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartWAILAProvider;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IFaceBundledDevice;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.IRedwire;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.wire.PartWireFace;
import com.bluepowermod.part.wire.redstone.propagation.BundledDeviceWrapper;
import com.bluepowermod.part.wire.redstone.propagation.WirePropagator;
import com.bluepowermod.util.DebugHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PartRedwireFace extends PartWireFace implements IFaceRedstoneDevice, IRedstoneConductor, IFaceBundledDevice,
        IBundledConductor, IPartRedstone, IPartWAILAProvider, IRedwire {

    protected final IRedstoneDevice[] devices = new IRedstoneDevice[6];
    protected final IBundledDevice[] bundledDevices = new IBundledDevice[6];
    protected final boolean[] connections = new boolean[6];
    protected final boolean[] renderedOpenConnections = new boolean[6];
    protected final boolean[] openConnections = new boolean[6];

    protected final RedwireType type;
    protected final boolean bundled;
    protected final MinecraftColor color;

    protected byte power = 0;
    protected byte[] bundledPower = new byte[16];

    private boolean hasUpdated = false;
    private boolean disconnected = false;

    public PartRedwireFace(RedwireType type, MinecraftColor color, Boolean bundled) {

        this.type = type;
        this.color = color;
        this.bundled = bundled;
    }

    @Override
    protected double getWidth() {

        return bundled ? 6 : (color == MinecraftColor.NONE ? 2 : 4);
    }

    @Override
    protected double getHeight() {

        return bundled ? 4 : (color == MinecraftColor.NONE ? 2 : 3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getWireIcon(ForgeDirection side) {

        if (bundled) {
            if (side == ForgeDirection.NORTH || side == ForgeDirection.WEST)
                return IconSupplier.wireBundled2;
            if (side == ForgeDirection.SOUTH || side == ForgeDirection.EAST)
                return IconSupplier.wireBundled3;
            return IconSupplier.wireBundled;
        }
        if (color == MinecraftColor.NONE)
            return IconSupplier.wire;

        return side == ForgeDirection.UP || side == ForgeDirection.DOWN ? IconSupplier.wireInsulation1 : IconSupplier.wireInsulation2;
    }

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

        return connections[side.ordinal()];
    }

    @Override
    protected int getColorMultiplier() {

        return bundled ? 0xFFFFFF : (color == MinecraftColor.NONE ? WireCommons.getColorForPowerLevel(type.getColor(), power) : color
                .getHex());
    }

    @Override
    protected boolean extendsToCorner(ForgeDirection side) {

        return renderedOpenConnections[side.ordinal()];
    }

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    @Override
    public List<Vec3dCube> getSelectionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        boxes.add(new Vec3dCube(0, 0, 0, 1, getHeight() / 16D, 1).expand(-0.000001));

        VectorHelper.rotateBoxes(boxes, getFace(), 0);
        return boxes;
    }

    @Override
    public List<Vec3dCube> getOcclusionBoxes() {

        List<Vec3dCube> boxes = new ArrayList<Vec3dCube>();

        double h = getHeight() / 16D;

        boxes.add(new Vec3dCube(h, 0, h, 1 - h, h, 1 - h));

        VectorHelper.rotateBoxes(boxes, getFace(), 0);
        return boxes;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        super.renderStatic(translation, renderer, renderBlocks, pass);

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

        if (getFace() == ForgeDirection.NORTH || getFace() == ForgeDirection.SOUTH) {
            d1 = d1.getRotation(getFace());
            d2 = d2.getRotation(getFace());
            d3 = d3.getRotation(getFace());
            d4 = d4.getRotation(getFace());
        }

        boolean s1 = shouldRenderConnection(d1);
        boolean s2 = shouldRenderConnection(d2);
        boolean s3 = shouldRenderConnection(d3);
        boolean s4 = shouldRenderConnection(d4);

        if (isBundled(ForgeDirection.DOWN)) {
            double size = 1 / 64D;

            double width = 1 / 32D;
            double height = getHeight() / 16D;

            byte power = bundled ? (byte) 128 : this.power;

            renderer.setColor(WireCommons.getColorForPowerLevel(type.getColor(), power));

            // Center
            if ((s1 && s3) || (s3 && s2) || (s2 && s4) || (s4 && s1)) {
                renderer.renderBox(new Vec3dCube(8 / 16D - width - size, height, 8 / 16D - width - size, 8 / 16D + width + size, height
                        + size, 8 / 16D + width + size), IconSupplier.wire);
            } else {
                renderer.renderBox(
                        new Vec3dCube(8 / 16D - width, height, 8 / 16D - width, 8 / 16D + width, height + size, 8 / 16D + width),
                        IconSupplier.wire);
            }
            // Sides
            if (s4 || s3) {
                if (s3 || (!s1 && !s2))
                    renderer.renderBox(new Vec3dCube(s3 ? (openConnections[d3.ordinal()] ? -height - size : 0) : 5 / 16D, height,
                            8 / 16D - width, 8 / 16D - width, height + size, 8 / 16D + width), IconSupplier.wire);
                if (s4 || (!s1 && !s2))
                    renderer.renderBox(new Vec3dCube(8 / 16D + width, height, 8 / 16D - width, s4 ? (openConnections[d4.ordinal()] ? 1
                            + height + size : 1) : 11 / 16D, height + size, 8 / 16D + width), IconSupplier.wire);
                if (s1)
                    renderer.renderBox(new Vec3dCube(8 / 16D - width, height, s1 ? (openConnections[d1.ordinal()] ? -height - size : 0)
                            : 4 / 16D, 8 / 16D + width, height + size, 8 / 16D - width), IconSupplier.wire);
                if (s2)
                    renderer.renderBox(new Vec3dCube(8 / 16D - width, height, 8 / 16D + width, 8 / 16D + width, height + size,
                            s2 ? (openConnections[d2.ordinal()] ? 1 + height + size : 1) : 12 / 16D), IconSupplier.wire);
            } else {
                renderer.renderBox(new Vec3dCube(8 / 16D - width, height, s1 ? (openConnections[d1.ordinal()] ? -height - size : 0)
                        : 5 / 16D, 8 / 16D + width, height + size, 8 / 16D - width), IconSupplier.wire);
                renderer.renderBox(new Vec3dCube(8 / 16D - width, height, 8 / 16D + width, 8 / 16D + width, height + size,
                        s2 ? (openConnections[d2.ordinal()] ? 1 + height + size : 1) : 11 / 16D), IconSupplier.wire);
            }

            if (!bundled) {
                double len = 1 / 16D;
                width = 1 / 16D;

                if (s4 || s3) {
                    if (s3 || (!s1 && !s2))
                        renderer.renderBox(new Vec3dCube(4 / 16D - len, 0, 8 / 16D - width, 4 / 16D, 2 / 16D, 8 / 16D + width),
                                IconSupplier.wire);

                    if (s4 || (!s1 && !s2)) {
                        renderer.renderBox(new Vec3dCube(12 / 16D, 0, 8 / 16D - width, 12 / 16D + len, 2 / 16D, 8 / 16D + width),
                                IconSupplier.wire);
                    }
                } else {
                    if (!s1)
                        renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 4 / 16D - len, 8 / 16D + width, 2 / 16D, 4 / 16D),
                                IconSupplier.wire);
                    if (!s2)
                        renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 12 / 16D, 8 / 16D + width, 2 / 16D, 12 / 16D + len),
                                IconSupplier.wire);
                }
            }
        }

        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        power = (byte) 255;
        double scale = 2;
        double translation = 0.5;
        double droppedTranslation = -0.5;

        if (bundled || getInsulationColor(ForgeDirection.DOWN) != MinecraftColor.NONE) {
            Arrays.fill(connections, true);
            connections[0] = false;
            connections[1] = false;
            scale = 1.25;
            translation = 0.25;
            droppedTranslation = 0;
        }

        RenderHelper rh = RenderHelper.instance;
        rh.setRenderCoords(null, 0, 0, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        {
            if (type == ItemRenderType.ENTITY)
                GL11.glTranslated(droppedTranslation, 0, droppedTranslation);
            GL11.glTranslated(0, translation, 0);
            GL11.glScaled(scale, scale, scale);
            Tessellator.instance.startDrawingQuads();
            renderStatic(new Vec3i(0, 0, 0), rh, RenderBlocks.getInstance(), 0);
            Tessellator.instance.draw();
        }
        GL11.glPopMatrix();

        rh.reset();
    }

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        for (int i = 0; i < 6; i++) {
            boolean connected = false;
            boolean render = false;

            {
                IRedstoneDevice dev = devices[i];
                if (dev != null) {
                    if (dev instanceof IPartFace && ((IPartFace) dev).getFace() == ForgeDirection.getOrientation(i).getOpposite()) {
                        if (dev instanceof PartRedwireFace) {
                            MinecraftColor insulation = dev.getInsulationColor(WireHelper.getConnectionSide(dev, this));
                            if (insulation != MinecraftColor.NONE)
                                connected = true;
                            if (getInsulationColor(ForgeDirection.DOWN) == MinecraftColor.NONE)
                                render = true;
                            if (getFace().ordinal() > ((PartRedwireFace) dev).getFace().ordinal()
                                    && insulation == getInsulationColor(ForgeDirection.getOrientation(i)))
                                render = true;
                            render = true;
                            connected = true;
                        } else {
                            connected = true;
                            render = true;
                        }
                    }
                }
            }
            {
                IBundledDevice dev = bundledDevices[i];
                if (dev != null) {
                    if (dev instanceof IPartFace && ((IPartFace) dev).getFace() == ForgeDirection.getOrientation(i).getOpposite()) {
                        if (dev instanceof PartRedwireFace) {
                            PartRedwireFace rw = (PartRedwireFace) dev;
                            if (!bundled && rw.bundled) {
                                render = true;
                                connected = true;
                            }
                            if (bundled && rw.bundled && getFace().ordinal() > ((PartRedwireFace) dev).getFace().ordinal()) {
                                render = true;
                            }
                            if (bundled && rw.bundled)
                                connected = true;
                        } else {
                            connected = true;
                            render = true;
                        }
                    }
                }
            }

            buffer.writeBoolean(devices[i] != null || bundledDevices[i] != null);
            buffer.writeBoolean(connected);
            buffer.writeBoolean(render);
        }

        for (int i = 0; i < 16; i++)
            buffer.writeByte(bundledPower[i]);
        buffer.writeByte(power);
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        for (int i = 0; i < 6; i++) {
            connections[i] = buffer.readBoolean();
            openConnections[i] = buffer.readBoolean();
            renderedOpenConnections[i] = buffer.readBoolean();
        }

        for (int i = 0; i < 16; i++)
            bundledPower[i] = buffer.readByte();
        power = buffer.readByte();

        getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        if ((side == getFace().getOpposite() || side == ForgeDirection.UNKNOWN) && device instanceof DummyRedstoneDevice)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return !bundled && WireCommons.canConnect(this, device, side, side.getOpposite());
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return !bundled && WireCommons.canConnect(this, device, side, getFace().getOpposite());
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        if (side == getFace())
            return false;
        if (side == getFace().getOpposite())
            return false;
        if (side == ForgeDirection.UNKNOWN)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return !bundled && WireCommons.canConnect(this, device, side, getFace());
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return;

        devices[side.ordinal()] = device;
        sendUpdatePacket();
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
        bundledDevices[side.ordinal()] = null;
        disconnected = true;
        sendUpdatePacket();
    }

    @Override
    public IRedstoneDevice getDeviceOnSide(ForgeDirection side) {

        return devices[side.ordinal()];
    }

    @Override
    public byte getRedstonePower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        if (!isAnalog())
            return (byte) ((power & 0xFF) > 0 ? 255 : 0);

        return power;
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        byte pow = isAnalog() ? power : (((power & 0xFF) > 0) ? (byte) 255 : (byte) 0);
        hasUpdated = hasUpdated | (pow != this.power);
        this.power = pow;
    }

    @Override
    public void onRedstoneUpdate() {

        if (!bundled && hasUpdated) {
            sendUpdatePacket();

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                IRedstoneDevice dev = devices[dir.ordinal()];
                if ((dev != null && (dev instanceof DummyRedstoneDevice)) || dir == getFace())
                    RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), dir, true);
            }

            hasUpdated = false;
        }
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public MinecraftColor getInsulationColor(ForgeDirection side) {

        return bundled ? null : color;
    }

    @Override
    public List<Pair<IRedstoneDevice, ForgeDirection>> propagate(ForgeDirection fromSide) {

        List<Pair<IRedstoneDevice, ForgeDirection>> devices = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();

        if (bundled)
            return devices;

        for (int i = 0; i < 6; i++) {
            IRedstoneDevice d = this.devices[i];
            if (d != null) {
                devices.add(new Pair<IRedstoneDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
            } else {
                IBundledDevice dev = bundledDevices[i];
                if (dev != null) {
                    devices.add(new Pair<IRedstoneDevice, ForgeDirection>(BundledDeviceWrapper
                            .getWrapper(dev, getInsulationColor(fromSide)), ForgeDirection.getOrientation(i)));
                }
            }
        }

        return devices;
    }

    @Override
    public void breakAndDrop(boolean creative) {

        WireCommons.disconnect(this, this);

        super.breakAndDrop(creative);
    }

    private byte lastInput = 1;

    @Override
    public void onUpdate() {

        // StackTraceElement[] e = Thread.getAllStackTraces().get(Thread.currentThread());
        // for (int i = 2; i < 11; i++) {
        // }

        if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
            return;

        super.onUpdate();

        if (getWorld().isRemote)
            return;

        // Stopwatch timer = Stopwatch.createStarted();
        WireCommons.refreshConnections(this, this);
        // timer.stop();

        if (!bundled) {
            devices[getFace().ordinal()] = DummyRedstoneDevice.getDeviceAt(new Vec3i(this).add(getFace()));
            devices[getFace().ordinal()].onConnect(getFace().getOpposite(), this);

            RedstoneApi.getInstance().setWiresHandleUpdates(false);
            WirePropagator.INSTANCE.onPowerLevelChange(this, getFace(), disconnected ? -1 : lastInput, (byte) -1);
            RedstoneApi.getInstance().setWiresHandleUpdates(true);

            lastInput = (byte) 0;
        } else {
            for (MinecraftColor c : MinecraftColor.VALID_COLORS) {
                RedstoneApi.getInstance().setWiresHandleUpdates(false);
                WirePropagator.INSTANCE.onPowerLevelChange(BundledDeviceWrapper.getWrapper(this, c), getFace(), disconnected ? -1
                        : lastInput, (byte) -1);
                RedstoneApi.getInstance().setWiresHandleUpdates(true);
            }
        }
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        // if (getWorld().isRemote) {
        // }

        return false;// side != getFace().getOpposite() && !bundled;
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        if (side != getFace())
            return 0;

        return (devices[side.ordinal()] != null && devices[side.ordinal()] instanceof DummyRedstoneDevice) ? ((DummyRedstoneDevice) devices[side
                .ordinal()]).getRedstoneOutput(MathHelper.map(power & 0xFF, 0, 255, 0, 15)) : 0;
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        if (side == getFace().getOpposite())
            return 0;
        if (devices[side.ordinal()] == null && !(devices[side.ordinal()] instanceof DummyRedstoneDevice))
            return MathHelper.map(power & 0xFF, 0, 255, 0, 15);

        return (devices[side.ordinal()] != null && devices[side.ordinal()] instanceof DummyRedstoneDevice) ? ((DummyRedstoneDevice) devices[side
                                                                                                                                            .ordinal()]).getRedstoneOutput(MathHelper.map(power & 0xFF, 0, 255, 0, 15)) : 0;
    }

    @Override
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        if (side == ForgeDirection.UNKNOWN)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        if (device instanceof IRedstoneDevice) {
            MinecraftColor insulation = ((IRedstoneDevice) device).getInsulationColor(side.getOpposite());
            MinecraftColor myInsulation = getInsulationColor(side);
            if (insulation != null && getInsulationColor(side) != null)
                if (!insulation.matches(myInsulation))
                    return false;
        }

        return isBundled(side) && WireCommons.canConnect(this, device, side, side.getOpposite());
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        if (device instanceof PartRedwireFace)
            if (!bundled && !((PartRedwireFace) device).bundled && !color.matches(((PartRedwireFace) device).color))
                return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return WireCommons.canConnect(this, device, side, getFace().getOpposite());
    }

    @Override
    public boolean canConnectBundledClosedCorner(ForgeDirection side, IBundledDevice device) {

        if (device instanceof PartRedwireFace)
            if (!bundled && !((PartRedwireFace) device).bundled && !color.matches(((PartRedwireFace) device).color))
                return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return WireCommons.canConnect(this, device, side, getFace().getOpposite());
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
    public byte[] getBundledOutput(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return new byte[16];

        return getBundledPower(side);
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        bundledPower = power;
        if (!bundled)
            this.power = power[getInsulationColor(side).ordinal()];
        hasUpdated = true;
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        return bundledPower;
    }

    @Override
    public void onBundledUpdate() {

        if (!bundled)
            onRedstoneUpdate();
    }

    @Override
    public MinecraftColor getBundledColor(ForgeDirection side) {

        return bundled ? color : (color == MinecraftColor.NONE ? null : MinecraftColor.NONE);
    }

    @Override
    public List<Pair<IBundledDevice, ForgeDirection>> propagateBundled(ForgeDirection fromSide) {

        List<Pair<IBundledDevice, ForgeDirection>> devices = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();

        for (int i = 0; i < 6; i++) {
            IBundledDevice d = bundledDevices[i];
            if (d != null) {
                if (d instanceof IRedstoneDevice && ((IRedstoneDevice) d).getInsulationColor(WireHelper.getConnectionSide(d, this)) != null
                        && getInsulationColor(ForgeDirection.getOrientation(i)) != null)
                    continue;
                devices.add(new Pair<IBundledDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
            }
        }

        return devices;
    }

    @Override
    public boolean isBundled(ForgeDirection side) {

        return bundled || color != MinecraftColor.NONE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> text) {

        if (!bundled) {
            if (isAnalog())
                text.add("Power: " + (power & 0xFF) + "/255");
            else
                text.add("Power: " + ((power & 0xFF) > 0 ? 1 : 0) + "/1");
        } else {
            text.add("Bundled");
        }
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.wiring;
    }

    @Override
    public boolean hasLoss() {

        return type.hasLoss();
    }

    @Override
    public boolean isAnalog() {

        return type.isAnalog();
    }

    @Override
    public String getType() {

        return "wire." + type.getName() + (bundled ? ".bundled" : "")
                + (color != MinecraftColor.NONE ? "." + color.name().toLowerCase() : "");
    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        if ((bundled || type == RedwireType.RED_ALLOY) && !DebugHelper.isDebugModeEnabled())
            return null;

        return super.getPlacement(part, world, location, face, mop, player);
    }

    @Override
    public void addTooltip(List<String> tip) {

        if ((bundled || type == RedwireType.RED_ALLOY) && !DebugHelper.isDebugModeEnabled())
            tip.add(MinecraftColor.RED + I18n.format("Disabled temporarily. Still not fully working."));
    }

    public RedwireType getWireType() {

        return type;
    }

}
