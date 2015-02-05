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
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
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
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IIntegratedCircuitPart;
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
import com.bluepowermod.part.gate.ic.FakeMultipartTileIC;
import com.bluepowermod.part.wire.PartWireFreestanding;
import com.bluepowermod.redstone.BundledConnectionCache;
import com.bluepowermod.redstone.DummyRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;

public abstract class PartRedwireFreestanding extends PartWireFreestanding implements IRedwire, IRedConductor, IIntegratedCircuitPart,
IPartRedstone {

    private RedwireType type;

    public PartRedwireFreestanding(int size, RedwireType type) {

        this.size = size;
        this.type = type;
    }

    @Override
    public RedwireType getRedwireType() {

        return type;
    }

    // Part methods

    @Override
    public String getUnlocalizedName() {

        return getType();
    }

    // Wire methods

    private int size;
    private boolean connections[] = new boolean[6];

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

        if (getParent() == null || getWorld() == null)
            return isConnected(side);

        return connections[side.ordinal()];
    }

    protected abstract boolean isConnected(ForgeDirection side);

    @Override
    protected int getSize() {

        return size;
    }

    @Override
    protected IIcon getFrameIcon() {

        return Blocks.planks.getIcon(0, 0);
    }

    // Selection and occlusion boxes

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
                if (shouldRenderConnection(d))
                    boxes.add(box.clone().rotate(d, Vec3d.center));
            }
        } else {
            for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                if (isConnected(d))
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

    // Conductor

    @Override
    public boolean hasLoss(ForgeDirection side) {

        return getRedwireType().hasLoss();
    }

    @Override
    public boolean isAnalogue(ForgeDirection side) {

        return getRedwireType().isAnalogue();
    }

    // NBT

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
    }

    @Override
    public boolean canPlaceOnIntegratedCircuit() {

        return true;
    }

    public static class PartRedwireFreestandingUninsulated extends PartRedwireFreestanding implements IAdvancedRedstoneConductor,
    IConnectionListener {

        private RedstoneConnectionCache connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
        private boolean hasUpdated = false;
        private byte power = 0;

        public PartRedwireFreestandingUninsulated(RedwireType type) {

            super(2, type);
            connections.listen();
        }

        @Override
        public String getType() {

            return "wire.freestanding." + getRedwireType().getName();
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

            return WireHelper.getColorForPowerLevel(getRedwireType(), power);
        }

        @Override
        public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

            if (type == ConnectionType.STRAIGHT)
                if (side == ForgeDirection.UNKNOWN && device instanceof DummyRedstoneDevice)
                    return false;
            if (type == ConnectionType.CLOSED_CORNER) {
                if (side == ForgeDirection.UNKNOWN)
                    return false;
            }

            if (device instanceof IRedwire && !getRedwireType().canConnectTo(((IRedwire) device).getRedwireType()))
                return false;

            if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.FACE_HOLLOW, 1, side))
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
        public void onRedstoneUpdate() {

            if (getParent() instanceof FakeMultipartTileIC)
                ((FakeMultipartTileIC) getParent()).getIC().loadWorld();

            if (hasUpdated) {
                sendUpdatePacket();

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    IConnection<IRedstoneDevice> c = connections.getConnectionOnSide(dir);
                    if (c == null)
                        continue;
                    IRedstoneDevice dev = c.getB();
                    if (dev == null || dev instanceof DummyRedstoneDevice)
                        RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), dir, false);
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
                            && ((IRedwire) c.getB()).getRedwireType() != getRedwireType()));
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
            ForgeDirection d = ForgeDirection.UNKNOWN;
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                if (getRedstoneConnectionCache().getConnectionOnSide(dir) != null)
                    d = dir;
            RedstoneApi.getInstance().getRedstonePropagator(this, d).propagate();
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
        }

        // Rendering methods

        @Override
        public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

            power = (byte) 255;

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
        public void writeUpdateData(DataOutput buffer) throws IOException {

            super.writeUpdateData(buffer);
            buffer.writeByte(power);
        }

        @Override
        public void readUpdateData(DataInput buffer) throws IOException {

            super.readUpdateData(buffer);
            power = buffer.readByte();

            getWorld().markBlockRangeForRenderUpdate(getX(), getY(), getZ(), getX(), getY(), getZ());
        }

        @Override
        public void addWAILABody(List<String> text) {

            super.addWAILABody(text);

            text.add("Power: " + (power & 0xFF));
        }

        @Override
        public boolean canConnectRedstone(ForgeDirection side) {

            return true;
        }

        @Override
        public int getWeakPower(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return 0;

            if (new Vec3i(this).add(side).getBlock() instanceof BlockRedstoneWire)
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

    public static class PartRedwireFreestandingInsulated extends PartRedwireFreestanding implements IAdvancedRedstoneConductor,
    IInsulatedRedstoneDevice, IAdvancedBundledConductor, IConnectionListener {

        private RedstoneConnectionCache connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
        private BundledConnectionCache bundledConnections = RedstoneApi.getInstance().createBundledConnectionCache(this);
        private boolean hasUpdated = false;
        private byte power = 0;
        private MinecraftColor color;

        public PartRedwireFreestandingInsulated(RedwireType type, MinecraftColor color) {

            super(4, type);
            this.color = color;

            connections.listen();
        }

        @Override
        public String getType() {

            return "wire.freestanding." + getRedwireType().getName() + "." + color.name().toLowerCase();
        }

        @Override
        protected boolean isConnected(ForgeDirection side) {

            if (getParent() == null || getWorld() == null)
                return true;

            return connections.getConnectionOnSide(side) != null;
        }

        @Override
        protected IIcon getWireIcon(ForgeDirection side) {

            return IconSupplier.wireInsulation1;
        }

        @Override
        protected int getColorMultiplier() {

            return color.getHex();
        }

        @Override
        public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

            if (type == ConnectionType.STRAIGHT)
                if (side == ForgeDirection.UNKNOWN && device instanceof DummyRedstoneDevice)
                    return false;
            if (type == ConnectionType.CLOSED_CORNER) {
                if (side == ForgeDirection.UNKNOWN)
                    return false;
            }

            if (device instanceof IInsulatedRedstoneDevice) {
                MinecraftColor c = ((IInsulatedRedstoneDevice) device).getInsulationColor(type == ConnectionType.STRAIGHT ? side
                        .getOpposite() : null);
                if (c != null && c != getInsulationColor(side))
                    return false;
            }

            if (device instanceof IRedwire && !getRedwireType().canConnectTo(((IRedwire) device).getRedwireType()))
                return false;

            if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.FACE_HOLLOW, 1, side))
                return false;

            return true;
        }

        @Override
        public boolean canConnect(ForgeDirection side, IBundledDevice device, ConnectionType type) {

            if (device instanceof IInsulatedRedstoneDevice)
                return false;

            if (device instanceof IRedwire)
                return true;

            return false;
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

            if (hasUpdated) {
                sendUpdatePacket();

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    IConnection<IRedstoneDevice> c = connections.getConnectionOnSide(dir);
                    if (c == null)
                        continue;
                    IRedstoneDevice dev = c.getB();
                    if (dev == null || dev instanceof DummyRedstoneDevice)
                        RedstoneHelper.notifyRedstoneUpdate(getWorld(), getX(), getY(), getZ(), dir, false);
                }

                hasUpdated = false;
            }
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
                            && ((IRedwire) c.getB()).getRedwireType() != getRedwireType()));
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
                            && ((IRedwire) c.getB()).getRedwireType() != getRedwireType()));
            }

            return l;
        }

        @Override
        public MinecraftColor getBundledColor(ForgeDirection side) {

            return null;
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
            ForgeDirection d = ForgeDirection.UNKNOWN;
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS)
                if (getRedstoneConnectionCache().getConnectionOnSide(dir) != null)
                    d = dir;
            RedstoneApi.getInstance().getRedstonePropagator(this, d).propagate();
        }

        // Rendering methods

        @Override
        public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

            power = (byte) 255;

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
        public void writeUpdateData(DataOutput buffer) throws IOException {

            super.writeUpdateData(buffer);
            buffer.writeByte(power);
        }

        @Override
        public void readUpdateData(DataInput buffer) throws IOException {

            super.readUpdateData(buffer);
            power = buffer.readByte();

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

            return true;
        }

        @Override
        public int getWeakPower(ForgeDirection side) {

            if (!RedstoneApi.getInstance().shouldWiresOutputPower(hasLoss(side)))
                return 0;

            if (new Vec3i(this).add(side).getBlock() instanceof BlockRedstoneWire)
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

    public static class PartRedwireFreestandingBundled extends PartRedwireFreestanding implements IAdvancedBundledConductor,
    IConnectionListener {

        private BundledConnectionCache bundledConnections = RedstoneApi.getInstance().createBundledConnectionCache(this);
        private byte[] power = new byte[16];
        private MinecraftColor color;

        public PartRedwireFreestandingBundled(RedwireType type, MinecraftColor color) {

            super(6, type);
            this.color = color;

            bundledConnections.listen();
        }

        @Override
        public String getType() {

            return "wire.freestanding." + getRedwireType().getName() + ".bundled"
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

            if (side == ForgeDirection.UNKNOWN) {
                boolean up = shouldRenderConnection(ForgeDirection.UP);
                boolean down = shouldRenderConnection(ForgeDirection.DOWN);
                boolean west = shouldRenderConnection(ForgeDirection.WEST);
                boolean east = shouldRenderConnection(ForgeDirection.EAST);
                boolean north = shouldRenderConnection(ForgeDirection.NORTH);
                boolean south = shouldRenderConnection(ForgeDirection.SOUTH);

                if (face == ForgeDirection.UP || face == ForgeDirection.DOWN) {
                    if ((west || east) != (north || south)) {
                        if (west || east)
                            return IconSupplier.wireBundledStraight2;
                        return IconSupplier.wireBundledStraight1;
                    }
                    if (west || east || north || south)
                        return IconSupplier.wireBundledCross;
                } else if (face == ForgeDirection.EAST || face == ForgeDirection.WEST) {
                    if ((up || down) != (north || south)) {
                        if (up || down)
                            return IconSupplier.wireBundledStraight1;
                        return IconSupplier.wireBundledStraight2;
                    }
                    if (up || down || north || south)
                        return IconSupplier.wireBundledCross;
                } else if (face == ForgeDirection.NORTH || face == ForgeDirection.SOUTH) {
                    if ((up || down) != (west || east)) {
                        if (up || down)
                            return IconSupplier.wireBundledStraight1;
                        return IconSupplier.wireBundledStraight2;
                    }
                    if (up || down || west || east)
                        return IconSupplier.wireBundledCross;
                }

                if (!shouldRenderConnection(face) && shouldRenderConnection(face.getOpposite()))
                    return IconSupplier.wireBundledConnection;

                return IconSupplier.wireBundledCross;
            }

            if (side == face)
                return IconSupplier.wireBundledConnection;

            return IconSupplier.wireBundledCross;
        }

        @Override
        protected int getColorMultiplier() {

            return 0xFFFFFF;
        }

        @Override
        public boolean canConnect(ForgeDirection side, IBundledDevice device, ConnectionType type) {

            if ((type == ConnectionType.STRAIGHT || type == ConnectionType.CLOSED_CORNER) && side == ForgeDirection.UNKNOWN)
                return false;

            if (device instanceof IRedwire && !getRedwireType().canConnectTo(((IRedwire) device).getRedwireType()))
                return false;

            if (!color.canConnect(device.getBundledColor(side.getOpposite())))
                return false;

            if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, side, side))
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
                            && ((IRedwire) c.getB()).getRedwireType() != getRedwireType()));
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
            RedstoneApi.getInstance().getBundledPropagator(this, ForgeDirection.DOWN).propagate();
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

        // @Override
        // public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {
        //
        // super.renderStatic(translation, renderer, renderBlocks, pass);
        //
        // ForgeDirection d1 = ForgeDirection.NORTH;
        // ForgeDirection d2 = ForgeDirection.SOUTH;
        // ForgeDirection d3 = ForgeDirection.WEST;
        // ForgeDirection d4 = ForgeDirection.EAST;
        //
        // if (getFace() == ForgeDirection.NORTH) {
        // d1 = ForgeDirection.UP;
        // d2 = ForgeDirection.DOWN;
        // } else if (getFace() == ForgeDirection.SOUTH) {
        // d1 = ForgeDirection.DOWN;
        // d2 = ForgeDirection.UP;
        // } else if (getFace() == ForgeDirection.WEST) {
        // d3 = ForgeDirection.UP;
        // d4 = ForgeDirection.DOWN;
        // } else if (getFace() == ForgeDirection.EAST) {
        // d3 = ForgeDirection.DOWN;
        // d4 = ForgeDirection.UP;
        // } else if (getFace() == ForgeDirection.UP) {
        // d3 = ForgeDirection.EAST;
        // d4 = ForgeDirection.WEST;
        // }
        //
        // if (getFace() == ForgeDirection.NORTH || getFace() == ForgeDirection.SOUTH) {
        // d1 = d1.getRotation(getFace());
        // d2 = d2.getRotation(getFace());
        // d3 = d3.getRotation(getFace());
        // d4 = d4.getRotation(getFace());
        // }
        //
        // boolean s1 = shouldRenderConnection(d1);
        // boolean s2 = shouldRenderConnection(d2);
        // boolean s3 = shouldRenderConnection(d3);
        // boolean s4 = shouldRenderConnection(d4);
        //
        // boolean[] openConnections = new boolean[6];
        //
        // double size = 1 / 64D;
        //
        // double width = 1 / 48D;
        // double height = getHeight() / 16D;
        //
        // renderer.setColor(WireHelper.getColorForPowerLevel(getRedwireType(), (byte) (255 / 2)/* power */));
        //
        // // Center
        // if ((s1 && s3) || (s3 && s2) || (s2 && s4) || (s4 && s1)) {
        // renderer.renderBox(new Vec3dCube(8 / 16D - width - size, height, 8 / 16D - width - size, 8 / 16D + width + size, height
        // + size, 8 / 16D + width + size), IconSupplier.wire);
        // } else {
        // renderer.renderBox(
        // new Vec3dCube(8 / 16D - width, height, 8 / 16D - width, 8 / 16D + width, height + size, 8 / 16D + width),
        // IconSupplier.wire);
        // }
        // // Sides
        // if (s4 || s3) {
        // if (s3 || (!s1 && !s2))
        // renderer.renderBox(new Vec3dCube(s3 ? (openConnections[d3.ordinal()] ? -height - size : 0) : 5 / 16D, height,
        // 8 / 16D - width, 8 / 16D - width, height + size, 8 / 16D + width), IconSupplier.wire);
        // if (s4 || (!s1 && !s2))
        // renderer.renderBox(new Vec3dCube(8 / 16D + width, height, 8 / 16D - width, s4 ? (openConnections[d4.ordinal()] ? 1
        // + height + size : 1) : 11 / 16D, height + size, 8 / 16D + width), IconSupplier.wire);
        // if (s1)
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, height, s1 ? (openConnections[d1.ordinal()] ? -height - size : 0)
        // : 4 / 16D, 8 / 16D + width, height + size, 8 / 16D - width), IconSupplier.wire);
        // if (s2)
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, height, 8 / 16D + width, 8 / 16D + width, height + size,
        // s2 ? (openConnections[d2.ordinal()] ? 1 + height + size : 1) : 12 / 16D), IconSupplier.wire);
        // } else {
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, height, s1 ? (openConnections[d1.ordinal()] ? -height - size : 0)
        // : 5 / 16D, 8 / 16D + width, height + size, 8 / 16D - width), IconSupplier.wire);
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, height, 8 / 16D + width, 8 / 16D + width, height + size,
        // s2 ? (openConnections[d2.ordinal()] ? 1 + height + size : 1) : 11 / 16D), IconSupplier.wire);
        // }
        // //
        // // double len = 1 / 16D;
        // // width = 1 / 16D;
        // //
        // // if (s4 || s3) {
        // // if (s3 || (!s1 && !s2))
        // // renderer.renderBox(new Vec3dCube(4 / 16D - len, 0, 8 / 16D - width, 4 / 16D, 2 / 16D, 8 / 16D + width),
        // // IconSupplier.wire);
        // //
        // // if (s4 || (!s1 && !s2)) {
        // // renderer.renderBox(new Vec3dCube(12 / 16D, 0, 8 / 16D - width, 12 / 16D + len, 2 / 16D, 8 / 16D + width),
        // // IconSupplier.wire);
        // // }
        // // } else {
        // // if (!s1)
        // // renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 4 / 16D - len, 8 / 16D + width, 2 / 16D, 4 / 16D),
        // // IconSupplier.wire);
        // // if (!s2)
        // // renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 12 / 16D, 8 / 16D + width, 2 / 16D, 12 / 16D + len),
        // // IconSupplier.wire);
        // // }
        //
        // return true;
        // }

        @Override
        public void writeUpdateData(DataOutput buffer) throws IOException {

            super.writeUpdateData(buffer);
        }

        @Override
        public void readUpdateData(DataInput buffer) throws IOException {

            super.readUpdateData(buffer);

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