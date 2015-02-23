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
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.MathHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartTicking;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IIntegratedCircuitPart;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.IConnectionCache;
import com.bluepowermod.api.wire.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IBundledConductor.IAdvancedBundledConductor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IInsulatedRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.gate.ic.FakeMultipartTileIC;
import com.bluepowermod.part.wire.PartWireFace;
import com.bluepowermod.redstone.BundledConnectionCache;
import com.bluepowermod.redstone.BundledDeviceWrapper;
import com.bluepowermod.redstone.DummyRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnection;
import com.bluepowermod.redstone.RedstoneConnectionCache;

public abstract class PartRedwireFace extends PartWireFace implements IRedwire, IRedConductor, IIntegratedCircuitPart, IPartRedstone {

    private RedwireType type;

    public PartRedwireFace(int width, int height, RedwireType type) {

        this.width = width;
        this.height = height;
        this.type = type;
    }

    @Override
    public RedwireType getRedwireType(ForgeDirection side) {

        return type;
    }

    // Part methods

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    // Wire methods

    private int width, height;
    private boolean connections[] = new boolean[6];

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

        if (getParent() == null || getWorld() == null)
            return isConnected(side);

        return connections[side.ordinal()];
    }

    protected abstract boolean isConnected(ForgeDirection side);

    @Override
    protected double getWidth() {

        return width;
    }

    @Override
    protected double getHeight() {

        return height;
    }

    @Override
    protected boolean extendsToCorner(ForgeDirection side) {

        return cornerRender[side.ordinal()];
    }

    // Selection and occlusion boxes

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
        double d = 4 / 16D;

        boxes.add(new Vec3dCube(d, 0, d, 1 - d, h, 1 - d));

        VectorHelper.rotateBoxes(boxes, getFace(), 0);
        return boxes;
    }

    // Conductor

    @Override
    public boolean hasLoss(ForgeDirection side) {

        return getRedwireType(ForgeDirection.UNKNOWN).hasLoss();
    }

    @Override
    public boolean isAnalogue(ForgeDirection side) {

        return getRedwireType(ForgeDirection.UNKNOWN).isAnalogue();
    }

    // NBT

    protected boolean[] cornerConnect = new boolean[6];
    protected boolean[] cornerRender = new boolean[6];

    @Override
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);

        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            buffer.writeBoolean(isConnected(d));
    }

    @Override
    public void readUpdateData(DataInput buffer) throws IOException {

        super.readUpdateData(buffer);
        for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS)
            connections[d.ordinal()] = buffer.readBoolean();

        for (int i = 0; i < 6; i++) {
            cornerConnect[i] = buffer.readBoolean();
            cornerRender[i] = buffer.readBoolean();
        }
    }

    @Override
    public boolean canPlaceOnIntegratedCircuit() {

        return true;
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.wiring;
    }

    public static class PartRedwireFaceUninsulated extends PartRedwireFace implements IAdvancedRedstoneConductor, IConnectionListener,
    IPartTicking {

        private RedstoneConnectionCache connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
        private boolean hasUpdated = false;
        private byte power = 0;
        private boolean scheduled = false;

        public PartRedwireFaceUninsulated(RedwireType type) {

            super(2, 2, type);
            connections.listen();
        }

        @Override
        public String getType() {

            return "wire." + getRedwireType(ForgeDirection.UNKNOWN).getName();
        }

        @Override
        protected boolean isConnected(ForgeDirection side) {

            return connections.getConnectionOnSide(side) != null;
        }

        @Override
        protected IIcon getWireIcon(ForgeDirection side) {

            return IconSupplier.wire;
        }

        @Override
        protected int getColorMultiplier() {

            return WireHelper.getColorForPowerLevel(getRedwireType(ForgeDirection.UNKNOWN), power);
        }

        @Override
        public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

            if (type == ConnectionType.STRAIGHT)
                if ((side == getFace().getOpposite() || side == ForgeDirection.UNKNOWN) && device instanceof DummyRedstoneDevice)
                    return false;
            if (type == ConnectionType.CLOSED_CORNER) {
                if (side == getFace())
                    return false;
                if (side == getFace().getOpposite())
                    return false;
                if (side == ForgeDirection.UNKNOWN)
                    return false;
            }

            if (device instanceof IRedwire) {
                RedwireType rwt = getRedwireType(side);
                if (type == null)
                    return false;
                RedwireType rwt_ = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                        : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
                if (rwt_ == null)
                    return false;
                if (!rwt.canConnectTo(rwt_))
                    return false;
            }

            if (!OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 2, getFace(), side))
                return false;

            return true;
        }

        @Override
        public RedstoneConnectionCache getRedstoneConnectionCache() {

            return connections;
        }

        @Override
        public void onConnect(IConnection<?> connection) {

            sendUpdatePacket();
        }

        @Override
        public void onDisconnect(IConnection<?> connection) {

            scheduled = true;
            sendUpdatePacket();
        }

        @Override
        public byte getRedstonePower(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return 0;

            if (!isAnalogue(side))
                return (byte) ((power & 0xFF) > 0 ? 255 : 0);

            return power;
        }

        @Override
        public void setRedstonePower(ForgeDirection side, byte power) {

            byte pow = hasLoss(side) ? power : (((power & 0xFF) > 0) ? (byte) 255 : (byte) 0);
            hasUpdated = hasUpdated | (pow != this.power);
            this.power = pow;
        }

        @Override
        public void onRedstoneUpdate() {

            if (getParent() instanceof FakeMultipartTileIC)
                ((FakeMultipartTileIC) getParent()).getIC().loadWorld();

            if (hasUpdated) {
                sendUpdatePacket();

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    IConnection<IRedstoneDevice> c = connections.getConnectionOnSide(dir);
                    IRedstoneDevice dev = null;
                    if (c != null)
                        dev = c.getB();
                    if (dir == getFace()) {
                        RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), dir, true);
                    } else if (dev == null || dev instanceof DummyRedstoneDevice) {
                        RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), dir, false);
                    }
                }

                hasUpdated = false;
            }
        }

        @Override
        public boolean canPropagateFrom(ForgeDirection fromSide) {

            return true;
        }

        @Override
        public List<Entry<IConnection<IRedstoneDevice>, Boolean>> propagate(ForgeDirection fromSide) {

            if (getParent() instanceof FakeMultipartTileIC)
                ((FakeMultipartTileIC) getParent()).getIC().loadWorld();

            List<Entry<IConnection<IRedstoneDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();

            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                IConnection<IRedstoneDevice> c = connections.getConnectionOnSide(d);
                if (c != null)
                    l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(c, c.getB() instanceof IRedwire
                            && ((IRedwire) c.getB()).getRedwireType(c.getSideB()) != getRedwireType(c.getSideA())));
            }

            return l;
        }

        @Override
        public void onUpdate() {

            // Don't to anything if propagation-related stuff is going on
            if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
                return;

            super.onUpdate();

            // Do not do anything if we're on the client
            if (getWorld().isRemote)
                return;

            // Refresh connections
            connections.recalculateConnections();
            // Add bottom device (forced)
            if (connections.getConnectionOnSide(getFace()) == null) {
                IRedstoneDevice drd = DummyRedstoneDevice.getDeviceAt(new Vec3i(this).add(getFace()));
                connections.onConnect(getFace(), drd, getFace().getOpposite(), ConnectionType.STRAIGHT);
                drd.getRedstoneConnectionCache().onConnect(getFace().getOpposite(), null, getFace(), ConnectionType.STRAIGHT);
            }

            RedstoneApi.getInstance().getRedstonePropagator(this, getFace()).propagate();
        }

        @Override
        public void update() {

            if (scheduled)
                onUpdate();
            scheduled = false;
        }

        @Override
        public void onRemoved() {

            if (!getWorld().isRemote) {
                power = 0;
                hasUpdated = true;
                onRedstoneUpdate();
            }

            // Don't to anything if propagation-related stuff is going on
            if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
                return;

            super.onRemoved();

            // Do not do anything if we're on the client
            if (getWorld().isRemote)
                return;

            connections.disconnectAll();
        }

        // Rendering methods

        @Override
        public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

            power = (byte) 255;
            double scale = 2;
            double translation = 0.5;
            double droppedTranslation = -0.5;

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

                IConnection<IRedstoneDevice> c = getRedstoneConnectionCache().getConnectionOnSide(ForgeDirection.getOrientation(i));
                if (c != null) {
                    IRedstoneDevice dev = c.getB();
                    if (dev instanceof IFace && ((IFace) dev).getFace() == ForgeDirection.getOrientation(i).getOpposite()) {
                        if (dev instanceof IRedwire) {
                            if (dev instanceof IInsulatedRedstoneDevice
                                    && ((IInsulatedRedstoneDevice) dev).getInsulationColor(c.getSideB()) != MinecraftColor.NONE)
                                render = true;
                            if (getFace().ordinal() > ((PartRedwireFace) dev).getFace().ordinal())
                                render = true;
                        } else {
                            connected = true;
                            render = true;
                        }
                    }
                }

                buffer.writeBoolean(connected);
                buffer.writeBoolean(render);
            }

            buffer.writeByte(power);
        }

        @Override
        public void readUpdateData(DataInput buffer) throws IOException {

            super.readUpdateData(buffer);
            power = buffer.readByte();

            if (getParent() != null && getWorld() != null)
                getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
        }

        @Override
        public void addWAILABody(List<String> text) {

            super.addWAILABody(text);

            text.add("Power: " + (power & 0xFF));
        }

        @Override
        public boolean canConnectRedstone(ForgeDirection side) {

            return side != getFace().getOpposite();
        }

        @Override
        public int getWeakPower(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return 0;

            if (new Vec3i(this).add(side).getBlock() instanceof BlockRedstoneWire)
                return 0;

            if (side == getFace().getOpposite())
                return 0;

            return MathHelper.map(power & 0xFF, 0, 255, 0, 15);
        }

        @Override
        public int getStrongPower(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return 0;

            if (side != getFace())
                return 0;

            return MathHelper.map(power & 0xFF, 0, 255, 0, 15);
        }

        @Override
        public boolean isNormalFace(ForgeDirection side) {

            return false;
        }

    }

    public static class PartRedwireFaceInsulated extends PartRedwireFace implements IAdvancedRedstoneConductor, IInsulatedRedstoneDevice,
    IAdvancedBundledConductor, IConnectionListener, IInsulatedRedwire, IPartTicking {

        private RedstoneConnectionCache connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
        private BundledConnectionCache bundledConnections = RedstoneApi.getInstance().createBundledConnectionCache(this);
        private boolean hasUpdated = false;
        private byte power = 0;
        private MinecraftColor color;
        private boolean scheduled = false;

        public PartRedwireFaceInsulated(RedwireType type, MinecraftColor color) {

            super(4, 3, type);
            this.color = color;

            connections.listen();
        }

        @Override
        public String getType() {

            return "wire." + getRedwireType(ForgeDirection.UNKNOWN).getName() + "." + color.name().toLowerCase();
        }

        @Override
        protected boolean isConnected(ForgeDirection side) {

            if (getParent() == null || getWorld() == null)
                return true;

            return connections.getConnectionOnSide(side) != null || bundledConnections.getConnectionOnSide(side) != null;
        }

        @Override
        protected IIcon getWireIcon(ForgeDirection side) {

            return side == ForgeDirection.UP || side == ForgeDirection.DOWN ? IconSupplier.wireInsulation1 : IconSupplier.wireInsulation2;
        }

        @Override
        protected int getColorMultiplier() {

            return color.getHex();
        }

        @Override
        public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

            if (type == ConnectionType.STRAIGHT)
                if ((side == getFace().getOpposite() || side == ForgeDirection.UNKNOWN) && device instanceof DummyRedstoneDevice)
                    return false;
            if (type == ConnectionType.CLOSED_CORNER) {
                if (side == getFace())
                    return false;
                if (side == getFace().getOpposite())
                    return false;
                if (side == ForgeDirection.UNKNOWN)
                    return false;
            }

            if (device instanceof IInsulatedRedstoneDevice) {
                MinecraftColor c = ((IInsulatedRedstoneDevice) device).getInsulationColor(type == ConnectionType.STRAIGHT ? side
                        .getOpposite() : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
                if (c != null && c != getInsulationColor(side))
                    return false;
            }

            if (device instanceof IRedwire) {
                RedwireType rwt = getRedwireType(side);
                if (type == null)
                    return false;
                RedwireType rwt_ = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                        : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
                if (rwt_ == null)
                    return false;
                if (!rwt.canConnectTo(rwt_))
                    return false;
            }

            if (!OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 2, getFace(), side))
                return false;

            return true;
        }

        @Override
        public boolean canConnect(ForgeDirection side, IBundledDevice device, ConnectionType type) {

            if (device instanceof IInsulatedRedstoneDevice)
                return false;

            if (device instanceof IRedwire) {
                RedwireType rwt = getRedwireType(side);
                if (type == null)
                    return false;
                RedwireType rwt_ = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                        : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
                if (rwt_ == null)
                    return false;
                if (!rwt.canConnectTo(rwt_))
                    return false;
            }

            if (!OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 2, getFace(), side))
                return false;

            return true;
        }

        @Override
        public RedstoneConnectionCache getRedstoneConnectionCache() {

            return connections;
        }

        @Override
        public IConnectionCache<? extends IBundledDevice> getBundledConnectionCache() {

            return bundledConnections;
        }

        @Override
        public void onConnect(IConnection<?> connection) {

            sendUpdatePacket();
        }

        @Override
        public void onDisconnect(IConnection<?> connection) {

            scheduled = true;
            sendUpdatePacket();
        }

        @Override
        public byte getRedstonePower(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return 0;

            if (!isAnalogue(side))
                return (byte) ((power & 0xFF) > 0 ? 255 : 0);

            return power;
        }

        @Override
        public void setRedstonePower(ForgeDirection side, byte power) {

            byte pow = isAnalogue(side) ? power : (((power & 0xFF) > 0) ? (byte) 255 : (byte) 0);
            hasUpdated = hasUpdated | (pow != this.power);
            this.power = pow;
        }

        @Override
        public byte[] getBundledOutput(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return new byte[16];

            return getBundledPower(side);
        }

        @Override
        public void setBundledPower(ForgeDirection side, byte[] power) {

            this.power = power[getInsulationColor(side).ordinal()];
            hasUpdated = true;
        }

        @Override
        public byte[] getBundledPower(ForgeDirection side) {

            byte[] val = new byte[16];
            val[color.ordinal()] = power;
            return val;
        }

        @Override
        public void onRedstoneUpdate() {

            if (getParent() == null || getWorld() == null)
                return;

            if (hasUpdated) {
                sendUpdatePacket();

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    IConnection<IRedstoneDevice> c = connections.getConnectionOnSide(dir);
                    if (c == null)
                        continue;
                    IRedstoneDevice dev = c.getB();
                    if (dir == getFace())
                        RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), dir, true);
                    else if (dev == null || dev instanceof DummyRedstoneDevice)
                        RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), dir, false);
                }

                hasUpdated = false;
            }
        }

        @Override
        public void onBundledUpdate() {

            onRedstoneUpdate();
        }

        @Override
        public boolean canPropagateFrom(ForgeDirection fromSide) {

            return true;
        }

        @Override
        public boolean canPropagateBundledFrom(ForgeDirection fromSide) {

            return true;
        }

        @Override
        public List<Entry<IConnection<IRedstoneDevice>, Boolean>> propagate(ForgeDirection fromSide) {

            List<Entry<IConnection<IRedstoneDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();

            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                IConnection<IRedstoneDevice> c = connections.getConnectionOnSide(d);
                if (c != null)
                    l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(c, c.getB() instanceof IRedwire
                            && ((IRedwire) c.getB()).getRedwireType(c.getSideB()) != getRedwireType(c.getSideA())));

                IConnection<IBundledDevice> cB = bundledConnections.getConnectionOnSide(d);
                if (cB != null)
                    l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(new RedstoneConnection(this, BundledDeviceWrapper.wrap(cB.getB(),
                            color), cB.getSideA(), cB.getSideB(), cB.getType()), cB.getB() instanceof IRedwire
                            && ((IRedwire) cB.getB()).getRedwireType(cB.getSideB()) != getRedwireType(cB.getSideA())));
            }

            return l;
        }

        @Override
        public Collection<Entry<IConnection<IBundledDevice>, Boolean>> propagateBundled(ForgeDirection fromSide) {

            List<Entry<IConnection<IBundledDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IBundledDevice>, Boolean>>();

            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                IConnection<IBundledDevice> c = bundledConnections.getConnectionOnSide(d);
                if (c != null)
                    l.add(new Pair<IConnection<IBundledDevice>, Boolean>(c, c.getB() instanceof IRedwire
                            && ((IRedwire) c.getB()).getRedwireType(c.getSideB()) != getRedwireType(c.getSideA())));
            }

            return l;
        }

        @Override
        public MinecraftColor getBundledColor(ForgeDirection side) {

            return MinecraftColor.NONE;
        }

        @Override
        public void onUpdate() {

            // Don't to anything if propagation-related stuff is going on
            if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
                return;

            super.onUpdate();

            // Do not do anything if we're on the client
            if (getWorld().isRemote)
                return;

            // Refresh connections
            connections.recalculateConnections();
            bundledConnections.recalculateConnections();

            RedstoneApi.getInstance().getRedstonePropagator(this, getFace()).propagate();
            RedstoneApi.getInstance().getBundledPropagator(this, getFace()).propagate();
        }

        @Override
        public void update() {

            if (scheduled)
                onUpdate();
            scheduled = false;
        }

        @Override
        public void onRemoved() {

            // Don't to anything if propagation-related stuff is going on
            if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
                return;

            super.onRemoved();

            // Do not do anything if we're on the client
            if (getWorld().isRemote)
                return;

            connections.disconnectAll();
            bundledConnections.disconnectAll();
        }

        // Rendering methods

        @Override
        public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

            power = (byte) 255;
            double scale = 1.25;
            double translation = 0.25;
            double droppedTranslation = 0;

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

            double size = 1 / 64D;

            double width = 1 / 32D;
            double y = 0.001;// getHeight() / 16D;
            double height = getHeight() / 16D;

            renderer.setColor(WireHelper.getColorForPowerLevel(getRedwireType(ForgeDirection.UNKNOWN), power));

            // Center
            if ((s1 && s3) || (s3 && s2) || (s2 && s4) || (s4 && s1)) {
                renderer.renderBox(new Vec3dCube(8 / 16D - width - size, y, 8 / 16D - width - size, 8 / 16D + width + size, height + size,
                        8 / 16D + width + size), IconSupplier.wire);
            } else {
                renderer.renderBox(new Vec3dCube(8 / 16D - width, y, 8 / 16D - width, 8 / 16D + width, height + size, 8 / 16D + width),
                        IconSupplier.wire);
            }
            // Sides
            if (s4 || s3) {
                if (s3 || (!s1 && !s2))
                    renderer.renderBox(new Vec3dCube(s3 ? (cornerConnect[d3.ordinal()] ? -height - size : 0.001) : 5 / 16D, y,
                            8 / 16D - width, 8 / 16D - width, height + size, 8 / 16D + width), IconSupplier.wire);
                if (s4 || (!s1 && !s2))
                    renderer.renderBox(new Vec3dCube(8 / 16D + width, y, 8 / 16D - width, s4 ? (cornerConnect[d4.ordinal()] ? 1 + height
                            + size : 0.999) : 11 / 16D, height + size, 8 / 16D + width), IconSupplier.wire);
                if (s1)
                    renderer.renderBox(new Vec3dCube(8 / 16D - width, y, s1 ? (cornerConnect[d1.ordinal()] ? -height - size : 0.001)
                            : 4 / 16D, 8 / 16D + width, height + size, 8 / 16D - width), IconSupplier.wire);
                if (s2)
                    renderer.renderBox(new Vec3dCube(8 / 16D - width, y, 8 / 16D + width, 8 / 16D + width, height + size,
                            s2 ? (cornerConnect[d2.ordinal()] ? 1 + height + size : 0.999) : 12 / 16D), IconSupplier.wire);
            } else {
                renderer.renderBox(new Vec3dCube(8 / 16D - width, y, s1 ? (cornerConnect[d1.ordinal()] ? -height - size : 0.001) : 5 / 16D,
                        8 / 16D + width, height + size, 8 / 16D - width), IconSupplier.wire);
                renderer.renderBox(new Vec3dCube(8 / 16D - width, y, 8 / 16D + width, 8 / 16D + width, height + size,
                        s2 ? (cornerConnect[d2.ordinal()] ? 1 + height + size : 0.999) : 11 / 16D), IconSupplier.wire);
            }

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

            return true;
        }

        @Override
        public void writeUpdateData(DataOutput buffer) throws IOException {

            super.writeUpdateData(buffer);

            for (int i = 0; i < 6; i++) {
                boolean connected = false;
                boolean render = false;

                IConnection<? extends IRedstoneDevice> c = getRedstoneConnectionCache().getConnectionOnSide(
                        ForgeDirection.getOrientation(i));
                if (c != null) {
                    IRedstoneDevice dev = c.getB();
                    if (dev instanceof IFace && ((IFace) dev).getFace() == ForgeDirection.getOrientation(i).getOpposite()) {
                        if (dev instanceof IRedwire) {
                            if (dev instanceof IInsulatedRedstoneDevice
                                    && ((IInsulatedRedstoneDevice) dev).getInsulationColor(c.getSideB()) != MinecraftColor.NONE)
                                connected = true;
                            if (getFace().ordinal() > ((PartRedwireFace) dev).getFace().ordinal()) {
                                if (dev instanceof IInsulatedRedstoneDevice
                                        && ((IInsulatedRedstoneDevice) dev).getInsulationColor(c.getSideB()) == getInsulationColor(c
                                                .getSideA()))
                                    render = true;
                                if (getInsulationColor(c.getSideA()) == MinecraftColor.NONE)
                                    render = true;
                            }
                        } else {
                            connected = true;
                            render = true;
                        }
                    }
                }
                IConnection<? extends IBundledDevice> bc = getBundledConnectionCache()
                        .getConnectionOnSide(ForgeDirection.getOrientation(i));
                if (bc != null) {
                    IBundledDevice dev = bc.getB();
                    if (dev instanceof IFace && ((IFace) dev).getFace() == ForgeDirection.getOrientation(i).getOpposite()) {
                        if (dev instanceof IRedwire) {
                            if (dev instanceof IInsulatedRedstoneDevice
                                    && ((IInsulatedRedstoneDevice) dev).getInsulationColor(bc.getSideB()) != MinecraftColor.NONE)
                                connected = true;
                            if (getFace().ordinal() > ((PartRedwireFace) dev).getFace().ordinal()) {
                                if (dev instanceof IInsulatedRedstoneDevice
                                        && ((IInsulatedRedstoneDevice) dev).getInsulationColor(bc.getSideB()) == getInsulationColor(bc
                                                .getSideA()))
                                    render = true;
                                if (getInsulationColor(bc.getSideA()) == MinecraftColor.NONE)
                                    render = true;
                            }
                            if (!(dev instanceof IInsulatedRedstoneDevice)) {
                                render = true;
                                connected = true;
                            }
                        } else {
                            connected = true;
                            render = true;
                        }
                    }
                }

                buffer.writeBoolean(connected);
                buffer.writeBoolean(render);
            }

            buffer.writeByte(power);
        }

        @Override
        public void readUpdateData(DataInput buffer) throws IOException {

            super.readUpdateData(buffer);
            power = buffer.readByte();

            if (getParent() != null && getWorld() != null)
                getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
        }

        @Override
        public void addWAILABody(List<String> text) {

            super.addWAILABody(text);

            text.add("Power: " + (power & 0xFF));
        }

        @Override
        public MinecraftColor getInsulationColor(ForgeDirection side) {

            return color;
        }

        @Override
        public boolean canConnectRedstone(ForgeDirection side) {

            return side != getFace().getOpposite();
        }

        @Override
        public int getWeakPower(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return 0;

            if (new Vec3i(this).add(side).getBlock() instanceof BlockRedstoneWire)
                return 0;

            if (side == getFace() || side == getFace().getOpposite())
                return 0;

            return MathHelper.map(power & 0xFF, 0, 255, 0, 15);
        }

        @Override
        public int getStrongPower(ForgeDirection side) {

            return 0;
        }

        @Override
        public boolean isNormalFace(ForgeDirection side) {

            return false;
        }

    }

    public static class PartRedwireFaceBundled extends PartRedwireFace implements IAdvancedBundledConductor, IConnectionListener {

        private BundledConnectionCache bundledConnections = RedstoneApi.getInstance().createBundledConnectionCache(this);
        private byte[] power = new byte[16];
        private MinecraftColor color;

        public PartRedwireFaceBundled(RedwireType type, MinecraftColor color) {

            super(6, 4, type);
            this.color = color;

            bundledConnections.listen();
        }

        @Override
        public String getType() {

            return "wire." + getRedwireType(ForgeDirection.UNKNOWN).getName() + ".bundled"
                    + (color != MinecraftColor.NONE ? ("." + color.name().toLowerCase()) : "");
        }

        @Override
        protected boolean isConnected(ForgeDirection side) {

            if (getParent() == null || getWorld() == null)
                return true;

            return bundledConnections.getConnectionOnSide(side) != null;
        }

        @Override
        protected IIcon getWireIcon(ForgeDirection side) {

            return null;
        }

        @Override
        protected IIcon getWireIcon(ForgeDirection side, ForgeDirection face) {

            if (face == ForgeDirection.UP || face == ForgeDirection.DOWN) {
                if (side == ForgeDirection.UNKNOWN) {
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

                    if ((s1 || s2) && !(s3 || s4))
                        return IconSupplier.wireBundledStraight1;
                    if (!(s1 || s2) && (s3 || s4))
                        return IconSupplier.wireBundledStraight2;
                }
                return IconSupplier.wireBundledCross;
            }

            if (side == face)
                return IconSupplier.wireBundledConnection;

            if (face == ForgeDirection.UP || face == ForgeDirection.WEST || face == ForgeDirection.NORTH)
                return IconSupplier.wireBundledSide1;

            return IconSupplier.wireBundledSide2;
        }

        @Override
        protected int getColorMultiplier() {

            return 0xFFFFFF;
        }

        @Override
        public boolean canConnect(ForgeDirection side, IBundledDevice device, ConnectionType type) {

            if (type == ConnectionType.CLOSED_CORNER) {
                if (side == getFace())
                    return false;
                if (side == getFace().getOpposite())
                    return false;
                if (side == ForgeDirection.UNKNOWN)
                    return false;
            }

            if (device instanceof IRedwire) {
                RedwireType rwt = getRedwireType(side);
                if (type == null)
                    return false;
                RedwireType rwt_ = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                        : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
                if (rwt_ == null)
                    return false;
                if (!rwt.canConnectTo(rwt_))
                    return false;
            }

            if (!color.canConnect(device.getBundledColor(type == ConnectionType.STRAIGHT ? side.getOpposite()
                    : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()))))
                return false;

            if (!OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 2, getFace(), side))
                return false;

            return true;
        }

        @Override
        public IConnectionCache<? extends IBundledDevice> getBundledConnectionCache() {

            return bundledConnections;
        }

        @Override
        public void onConnect(IConnection<?> connection) {

            sendUpdatePacket();
        }

        @Override
        public void onDisconnect(IConnection<?> connection) {

            sendUpdatePacket();
        }

        @Override
        public byte[] getBundledOutput(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return new byte[16];

            return getBundledPower(side);
        }

        @Override
        public void setBundledPower(ForgeDirection side, byte[] power) {

            this.power = power;
        }

        @Override
        public byte[] getBundledPower(ForgeDirection side) {

            return power;
        }

        @Override
        public void onBundledUpdate() {

        }

        @Override
        public boolean canPropagateBundledFrom(ForgeDirection fromSide) {

            return true;
        }

        @Override
        public Collection<Entry<IConnection<IBundledDevice>, Boolean>> propagateBundled(ForgeDirection fromSide) {

            List<Entry<IConnection<IBundledDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IBundledDevice>, Boolean>>();
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                IConnection<IBundledDevice> c = bundledConnections.getConnectionOnSide(d);
                if (c != null)
                    l.add(new Pair<IConnection<IBundledDevice>, Boolean>(c, c.getB() instanceof IRedwire
                            && ((IRedwire) c.getB()).getRedwireType(c.getSideB()) != getRedwireType(c.getSideA())));
            }

            return l;
        }

        @Override
        public MinecraftColor getBundledColor(ForgeDirection side) {

            return color;
        }

        @Override
        public void onUpdate() {

            // Don't to anything if propagation-related stuff is going on
            if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
                return;

            super.onUpdate();

            // Do not do anything if we're on the client
            if (getWorld().isRemote)
                return;

            // Refresh connections
            bundledConnections.recalculateConnections();
            RedstoneApi.getInstance().getBundledPropagator(this, getFace()).propagate();
        }

        @Override
        public void onRemoved() {

            // Don't to anything if propagation-related stuff is going on
            if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
                return;

            super.onRemoved();

            // Do not do anything if we're on the client
            if (getWorld().isRemote)
                return;

            bundledConnections.disconnectAll();
        }

        // Rendering methods

        @Override
        public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

            double scale = 1.25;
            double translation = 0.25;
            double droppedTranslation = 0;

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

            double size = 1 / 64D;

            double width = 1 / 48D;
            double y = 0;
            double height = getHeight() / 16D;

            renderer.setColor(WireHelper.getColorForPowerLevel(getRedwireType(ForgeDirection.UNKNOWN), (byte) (255 / 2)/* power */));

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
                    renderer.renderBox(new Vec3dCube(s3 ? (cornerConnect[d3.ordinal()] ? -height - size : -size) : 5 / 16D, y,
                            8 / 16D - width, 8 / 16D - width, height + size, 8 / 16D + width), IconSupplier.wire);
                if (s4 || (!s1 && !s2))
                    renderer.renderBox(new Vec3dCube(8 / 16D + width, y, 8 / 16D - width, s4 ? (cornerConnect[d4.ordinal()] ? 1 + height
                            + size : 1 + size) : 11 / 16D, height + size, 8 / 16D + width), IconSupplier.wire);
                if (s1)
                    renderer.renderBox(new Vec3dCube(8 / 16D - width, y, s1 ? (cornerConnect[d1.ordinal()] ? -height - size : -size)
                            : 4 / 16D, 8 / 16D + width, height + size, 8 / 16D - width), IconSupplier.wire);
                if (s2)
                    renderer.renderBox(new Vec3dCube(8 / 16D - width, y, 8 / 16D + width, 8 / 16D + width, height + size,
                            s2 ? (cornerConnect[d2.ordinal()] ? 1 + height + size : 1 + size) : 12 / 16D), IconSupplier.wire);
            } else {
                renderer.renderBox(new Vec3dCube(8 / 16D - width, y, s1 ? (cornerConnect[d1.ordinal()] ? -height - size : -size) : 5 / 16D,
                        8 / 16D + width, height + size, 8 / 16D - width), IconSupplier.wire);
                renderer.renderBox(new Vec3dCube(8 / 16D - width, y, 8 / 16D + width, 8 / 16D + width, height + size,
                        s2 ? (cornerConnect[d2.ordinal()] ? 1 + height + size : 1 + size) : 11 / 16D), IconSupplier.wire);
            }

            return true;
        }

        @Override
        public void writeUpdateData(DataOutput buffer) throws IOException {

            super.writeUpdateData(buffer);

            for (int i = 0; i < 6; i++) {
                boolean connected = false;
                boolean render = false;
                IConnection<? extends IBundledDevice> bc = getBundledConnectionCache()
                        .getConnectionOnSide(ForgeDirection.getOrientation(i));
                if (bc != null) {
                    IBundledDevice dev = bc.getB();
                    if (dev instanceof IFace && ((IFace) dev).getFace() == ForgeDirection.getOrientation(i).getOpposite()) {
                        if (dev instanceof IRedwire) {
                            if (getFace().ordinal() > ((PartRedwireFace) dev).getFace().ordinal()) {
                                if (!(dev instanceof IInsulatedRedstoneDevice) && dev instanceof IRedwire) {
                                    render = true;
                                    connected = true;
                                }
                            } else if (dev instanceof PartRedwireFaceBundled) {
                                connected = true;
                            }
                        } else {
                            connected = true;
                            render = true;
                        }
                    }
                }

                buffer.writeBoolean(connected);
                buffer.writeBoolean(render);
            }
        }

        @Override
        public void readUpdateData(DataInput buffer) throws IOException {

            super.readUpdateData(buffer);

            if (getParent() != null && getWorld() != null)
                getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
        }

        @Override
        public boolean canConnectRedstone(ForgeDirection side) {

            return false;
        }

        @Override
        public int getWeakPower(ForgeDirection side) {

            return 0;
        }

        @Override
        public int getStrongPower(ForgeDirection side) {

            return 0;
        }

        @Override
        public boolean isNormalFace(ForgeDirection side) {

            return false;
        }

    }

}