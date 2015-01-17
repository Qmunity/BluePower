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

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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
import uk.co.qmunity.lib.part.IPartCustomPlacement;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartSolid;
import uk.co.qmunity.lib.part.IPartThruHole;
import uk.co.qmunity.lib.part.IPartWAILAProvider;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.PartPlacementDefault;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IFaceBundledDevice;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.client.render.IconSupplier;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.wire.PartWireFreestanding;
import com.bluepowermod.part.wire.redstone.propagation.BundledDeviceWrapper;
import com.bluepowermod.part.wire.redstone.propagation.WirePropagator;
import com.bluepowermod.util.DebugHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PartRedwireFreestanding extends PartWireFreestanding implements IRedstoneConductor, IBundledConductor, IPartRedstone,
        IPartWAILAProvider, IPartSolid, IPartThruHole, IPartCustomPlacement {

    protected IRedstoneDevice[] devices = new IRedstoneDevice[6];
    protected IBundledDevice[] bundledDevices = new IBundledDevice[6];
    protected boolean[] connections = new boolean[6];

    protected byte power = 0;
    protected byte[] bundledPower = new byte[16];

    protected RedwireType type;
    protected boolean bundled;
    protected MinecraftColor color;

    private boolean hasUpdated = false;
    private boolean disconnected = false;

    public PartRedwireFreestanding(RedwireType type, MinecraftColor color, Boolean bundled) {

        this.type = type;
        this.color = color;
        this.bundled = bundled;
    }

    @Override
    protected int getSize() {

        return bundled ? 6 : (color == MinecraftColor.NONE ? 2 : 4);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getWireIcon(ForgeDirection side) {

        return bundled ? IconSupplier.wireBundled : (color == MinecraftColor.NONE ? IconSupplier.wire : IconSupplier.wireInsulation1);
    }

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

        return connections[side.ordinal()];
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected IIcon getFrameIcon() {

        return Blocks.planks.getIcon(0, 0);
    }

    @Override
    protected int getColorMultiplier() {

        return bundled ? 0xFFFFFF : (color == MinecraftColor.NONE ? WireCommons.getColorForPowerLevel(type.getColor(), power) : color
                .getHex());
    }

    @Override
    protected int getFrameColorMultiplier() {

        return 0xFFFFFF;
    }

    @Override
    public int getHollowSize(ForgeDirection side) {

        return 8;
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
    @SideOnly(Side.CLIENT)
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

        for (int i = 0; i < 16; i++)
            tag.setByte("power_" + i, bundledPower[i]);
        tag.setByte("power", power);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);

        for (int i = 0; i < 6; i++)
            connections[i] = tag.getBoolean("connected_" + i);

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

        if (!(device instanceof IFaceRedstoneDevice))
            if (OcclusionHelper.microblockOcclusionTest(new Vec3i(this), MicroblockShape.FACE_HOLLOW, 8, side))
                return false;

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        if (!(device instanceof IFaceRedstoneDevice))
            if (OcclusionHelper.microblockOcclusionTest(new Vec3i(this), MicroblockShape.FACE_HOLLOW, 8, side))
                return false;

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
                if (dev != null && (dev instanceof DummyRedstoneDevice))
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
    public MinecraftColor getInsulationColor() {

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
                    devices.add(new Pair<IRedstoneDevice, ForgeDirection>(BundledDeviceWrapper.getWrapper(dev, getInsulationColor()),
                            ForgeDirection.getOrientation(i)));
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

    @Override
    public void onRemoved() {

        // if (getWorld().isRemote)
        // return;
        //
        // WireCommons.disconnect(this, this);
        super.onRemoved();
    }

    private byte lastInput = 1;

    @Override
    public void onUpdate() {

        if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
            return;

        super.onUpdate();

        if (getWorld().isRemote)
            return;

        WireCommons.refreshConnections(this, this);

        if (!bundled) {
            int input = 0;
            for (int i = 0; i < 6; i++) {
                IRedstoneDevice d = devices[i];
                if (d != null && !(d instanceof IRedstoneConductor)) {
                    input = Math.max(input, d.getRedstonePower(ForgeDirection.getOrientation(i).getOpposite()) & 0xFF);
                }
            }

            RedstoneApi.getInstance().setWiresHandleUpdates(false);
            WirePropagator.INSTANCE.onPowerLevelChange(this, ForgeDirection.DOWN, disconnected ? -1 : lastInput, (byte) -1);
            RedstoneApi.getInstance().setWiresHandleUpdates(true);

            lastInput = (byte) input;

        } else {
            for (MinecraftColor c : MinecraftColor.VALID_COLORS) {
                RedstoneApi.getInstance().setWiresHandleUpdates(false);
                WirePropagator.INSTANCE.onPowerLevelChange(BundledDeviceWrapper.getWrapper(this, c), ForgeDirection.DOWN, disconnected ? -1
                        : lastInput, (byte) -1);
                RedstoneApi.getInstance().setWiresHandleUpdates(true);
            }
        }
    }

    @Override
    public void onNeighborBlockChange() {

        super.onNeighborBlockChange();

    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        return false;// side != getFace();
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        if (devices[side.ordinal()] == null || !(devices[side.ordinal()] instanceof DummyRedstoneDevice))
            return 0;

        return (devices[side.ordinal()] != null && devices[side.ordinal()] instanceof DummyRedstoneDevice) ? ((DummyRedstoneDevice) devices[side
                .ordinal()]).getRedstoneOutput(MathHelper.map(power & 0xFF, 0, 255, 0, 15)) : 0;
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        if (devices[side.ordinal()] == null || !(devices[side.ordinal()] instanceof DummyRedstoneDevice))
            return 0;

        return (devices[side.ordinal()] != null && devices[side.ordinal()] instanceof DummyRedstoneDevice) ? ((DummyRedstoneDevice) devices[side
                .ordinal()]).getRedstoneOutput(MathHelper.map(power & 0xFF, 0, 255, 0, 15)) : 0;
    }

    @Override
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        // if (!(device instanceof IFaceBundledDevice))
        // if (OcclusionHelper.microblockOcclusionTest(new Vec3i(this), MicroblockShape.FACE_HOLLOW, 8, side))
        // return false;

        return isBundled();// WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        if (!(device instanceof IFaceBundledDevice))
            if (OcclusionHelper.microblockOcclusionTest(new Vec3i(this), MicroblockShape.FACE_HOLLOW, 8, side))
                return false;

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
    public byte[] getBundledOutput(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return new byte[16];

        return getBundledPower(side);
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        bundledPower = power;
        if (!bundled)
            this.power = power[getInsulationColor().ordinal()];
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
    public MinecraftColor getBundledColor() {

        return bundled ? color : (color == MinecraftColor.NONE ? null : MinecraftColor.NONE);
    }

    @Override
    public List<Pair<IBundledDevice, ForgeDirection>> propagateBundled(ForgeDirection fromSide) {

        List<Pair<IBundledDevice, ForgeDirection>> devices = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();

        for (int i = 0; i < 6; i++) {
            IBundledDevice d = bundledDevices[i];
            if (d != null) {
                if (d instanceof IRedstoneDevice && ((IRedstoneDevice) d).getInsulationColor() != null && getInsulationColor() != null)
                    continue;
                devices.add(new Pair<IBundledDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
            }
        }

        return devices;
    }

    @Override
    public boolean isBundled() {

        return bundled || getInsulationColor() != MinecraftColor.NONE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addWAILABody(List<String> text) {

        text.add("Power: " + (power & 0xFF) + "/255");
    }

    @Override
    public CreativeTabs getCreativeTab() {

        return BPCreativeTabs.wiring;
    }

    @Override
    public boolean isSideSolid(ForgeDirection face) {

        return false;
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

        return "wire.freestanding." + type.getName() + (bundled ? ".bundled" : "")
                + (color != MinecraftColor.NONE ? "." + color.name().toLowerCase() : "");
    }

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        if (!DebugHelper.isDebugModeEnabled() && (bundled || type == RedwireType.RED_ALLOY))
            return null;

        return new PartPlacementDefault();
    }

    @Override
    public void addTooltip(List<String> tip) {

        if (!DebugHelper.isDebugModeEnabled() && (bundled || type == RedwireType.RED_ALLOY))
            tip.add(MinecraftColor.RED + I18n.format("Disabled temporarily. Still not fully working."));
    }

}