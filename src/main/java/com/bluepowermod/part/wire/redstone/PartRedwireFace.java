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
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.helper.RedstoneHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.IConnectionListener;
import com.bluepowermod.api.wire.redstone.IRedConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.part.wire.PartWireFace;
import com.bluepowermod.redstone.DummyRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;

public abstract class PartRedwireFace extends PartWireFace implements IRedwire, IRedConductor {

    private RedwireType type;

    public PartRedwireFace(int width, int height, RedwireType type) {

        this.width = width;
        this.height = height;
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

    private int width, height;
    private boolean connections[] = new boolean[6];

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

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
        double d = 0.25;

        boxes.add(new Vec3dCube(d, 0, d, 1 - d, h, 1 - d));

        VectorHelper.rotateBoxes(boxes, getFace(), 0);
        return boxes;
    }

    // Conductor

    @Override
    public boolean hasLoss(ForgeDirection side) {

        return getRedwireType().hasLoss();
    }

    @Override
    public boolean isAnalogue(ForgeDirection side) {

        return getRedwireType().isAnalog();
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

    public static class PartRedwireFaceUninsulated extends PartRedwireFace implements IAdvancedRedstoneConductor, IConnectionListener {

        private RedstoneConnectionCache connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);
        private boolean hasUpdated = false;
        private byte power = 0;

        public PartRedwireFaceUninsulated(RedwireType type) {

            super(2, 2, type);
            connections.listen();
        }

        @Override
        public String getType() {

            return "wire." + getRedwireType().getName();
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

            return WireCommons.getColorForPowerLevel(getRedwireType().getColor(), power);
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

            if (device instanceof IRedwire && !getRedwireType().canConnectTo(((IRedwire) device).getRedwireType()))
                return false;

            if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
                return false;

            return true;// FIXME WireCommons.canConnect(this, device, side, getFace().getOpposite());
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
        public boolean canPropagateFrom(ForgeDirection fromSide) {

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
        public void onUpdate() {

            super.onUpdate();

            // Refresh connections
            connections.recalculateConnections();
            // Add bottom device (forced)
            IRedstoneDevice drd = DummyRedstoneDevice.getDeviceAt(new Vec3i(this).add(getFace()));
            connections.onConnect(getFace(), drd, getFace().getOpposite(), ConnectionType.STRAIGHT);
            drd.getRedstoneConnectionCache().onConnect(getFace().getOpposite(), null, getFace(), ConnectionType.STRAIGHT);

            RedstoneApi.getInstance().getRedstonePropagator(this, getFace()).propagate();
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

    }

}