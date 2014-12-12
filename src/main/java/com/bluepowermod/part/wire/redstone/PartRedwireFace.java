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
import net.minecraft.creativetab.CreativeTabs;
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
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.PartNormallyOccluded;
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
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.helper.VectorHelper;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.part.wire.PartWireFace;
import com.bluepowermod.part.wire.redstone.propagation.WirePropagator;

public class PartRedwireFace extends PartWireFace implements IFaceRedstoneDevice, IRedstoneConductor, IFaceBundledDevice,
IBundledConductor, IPartRedstone, IPartWAILAProvider {

    protected IRedstoneDevice[] devices = new IRedstoneDevice[6];
    protected IBundledDevice[] bundledDevices = new IBundledDevice[6];
    protected boolean[] connections = new boolean[6];

    protected byte power = 0;
    protected byte[] bundledPower = new byte[16];

    protected RedwireType type;
    protected boolean bundled;
    protected MinecraftColor color;

    public PartRedwireFace(RedwireType type, MinecraftColor color, Boolean bundled) {

        this.type = type;
        this.color = color;
        this.bundled = bundled;
    }

    @Override
    protected int getWidth() {

        return bundled ? 6 : (color == MinecraftColor.NONE ? 2 : 4);
    }

    @Override
    protected int getHeight() {

        return bundled ? 4 : (color == MinecraftColor.NONE ? 2 : 3);
    }

    @Override
    protected IIcon getWireIcon(ForgeDirection side) {

        return bundled ? IconSupplier.wireBundled : (color == MinecraftColor.NONE ? IconSupplier.wire : (side == ForgeDirection.UP
                || side == ForgeDirection.DOWN ? IconSupplier.wireInsulation1 : IconSupplier.wireInsulation2));
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
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        // IIcon normalIcon = IconSupplier.wire;
        // IIcon insulationIcon = IconSupplier.wireInsulation;
        // IIcon bundleIcon = IconSupplier.wireBundled;
        //
        // double height = 2 / 16D;
        // double width = 1 / 16D;
        // IIcon icon = normalIcon;
        // int color = WireCommons.getColorForPowerLevel(getColor(), power);
        //
        // if (insulationColor != MinecraftColor.NONE) {
        // height += 1 / 16D;
        // width += 1 / 16D;
        // icon = insulationIcon;
        // color = insulationColor.getHex();
        // }
        // if (bundled) {
        // height += 2 / 16D;
        // width += 2 / 16D;
        // icon = bundleIcon;
        // color = 0xFFFFFF;
        // }
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
        // switch (getFace()) {
        // case DOWN:
        // break;
        // case UP:
        // renderer.addTransformation(new Rotation(180, 0, 0, Vec3d.center));
        // break;
        // case NORTH:
        // renderer.addTransformation(new Rotation(90, 90, 0, Vec3d.center));
        // d1 = d1.getRotation(getFace());
        // d2 = d2.getRotation(getFace());
        // d3 = d3.getRotation(getFace());
        // d4 = d4.getRotation(getFace());
        // break;
        // case SOUTH:
        // renderer.addTransformation(new Rotation(-90, 90, 0, Vec3d.center));
        // d1 = d1.getRotation(getFace());
        // d2 = d2.getRotation(getFace());
        // d3 = d3.getRotation(getFace());
        // d4 = d4.getRotation(getFace());
        // break;
        // case WEST:
        // renderer.addTransformation(new Rotation(0, 0, -90, Vec3d.center));
        // break;
        // case EAST:
        // renderer.addTransformation(new Rotation(0, 0, 90, Vec3d.center));
        // break;
        // default:
        // break;
        // }
        //
        // boolean north = connections[d1.ordinal()];
        // boolean northOpen = false;// connections[d1.ordinal()];
        // boolean south = connections[d2.ordinal()];
        // boolean southOpen = false;// connections[d2.ordinal()];
        // boolean west = connections[d3.ordinal()];
        // boolean westOpen = false;// connections[d3.ordinal()];
        // boolean east = connections[d4.ordinal()];
        // boolean eastOpen = false;// connections[d4.ordinal()];
        //
        // renderer.setColor(color);
        //
        // // Center
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D - width, 8 / 16D + width, height, 8 / 16D + width), icon);
        // // Sides
        // if (east || west) {
        // if (west || (!west && east && !north && !south))
        // renderer.renderBox(new Vec3dCube(west ? (westOpen ? -2 / 16D : 0) : 5 / 16D, 0, 8 / 16D - width, 8 / 16D - width, height,
        // 8 / 16D + width), icon);
        // if (east || (west && !east && !north && !south))
        // renderer.renderBox(new Vec3dCube(8 / 16D + width, 0, 8 / 16D - width, east ? (eastOpen ? 18 / 16D : 1) : 11 / 16D, height,
        // 8 / 16D + width), icon);
        // if (north)
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, north ? (northOpen ? -2 / 16D : 0) : 5 / 16D, 8 / 16D + width, height,
        // 8 / 16D - width), icon);
        // if (south)
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D + width, 8 / 16D + width, height,
        // south ? (southOpen ? 18 / 16D : 1) : 11 / 16D), icon);
        // } else {
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, north ? 0 : 5 / 16D, 8 / 16D + width, height, 8 / 16D - width), icon);
        // renderer.renderBox(new Vec3dCube(8 / 16D - width, 0, 8 / 16D + width, 8 / 16D + width, height, south ? 1 : 11 / 16D), icon);
        // }
        //
        // if (bundled) {
        // if (bundleColor != MinecraftColor.NONE) {
        // renderer.setColor(ItemDye.field_150922_c[15 - bundleColor.ordinal()]);
        // renderer.renderBox(new Vec3dCube(9 / 16D - width, height, 9 / 16D - width, 7 / 16D + width, height + 1 / 16D,
        // 7 / 16D + width), Blocks.wool.getIcon(0, 0));
        // renderer.setColor(WireCommons.getColorForPowerLevel(getColor(), (byte) 127));
        // renderer.renderBox(new Vec3dCube(10 / 16D - width, height + 1 / 16D, 10 / 16D - width, 6 / 16D + width, height + 2 / 16D,
        // 6 / 16D + width), normalIcon);
        // } else {
        // renderer.setColor(WireCommons.getColorForPowerLevel(getColor(), (byte) 127));
        // renderer.renderBox(new Vec3dCube(10 / 16D - width, height, 10 / 16D - width, 6 / 16D + width, height + 1 / 16D,
        // 6 / 16D + width), normalIcon);
        // }
        // }
        //
        // renderer.setColor(0xFFFFFF);
        // renderer.resetTransformations();
        //
        // return true;
        return super.renderStatic(translation, renderer, renderBlocks, pass);
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        power = (byte) 255;
        double scale = 2;
        double translation = 0.5;

        if (bundled || getInsulationColor() != MinecraftColor.NONE) {
            connections = new boolean[] { false, false, true, true, true, true };
            scale = 1.25;
            translation = 0.125;
        }

        RenderHelper rh = RenderHelper.instance;
        rh.setRenderCoords(null, 0, 0, 0);
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        {
            if (type == ItemRenderType.ENTITY)
                GL11.glTranslated(-0.5, 0, -0.5);
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

        if (side != getFace().getOpposite() && side != ForgeDirection.UNKNOWN) {
            int microblockLocation = getFace().ordinal() + (side.ordinal() << 4);
            if (!getParent().canAddPart(new PartNormallyOccluded(OcclusionHelper.getBox(MicroblockShape.EDGE, 1, microblockLocation))))
                return false;
        }

        return !bundled && side != getFace().getOpposite() && WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        if (side != getFace().getOpposite() && side != ForgeDirection.UNKNOWN) {
            int microblockLocation = getFace().ordinal() + (side.ordinal() << 4);
            if (!getParent().canAddPart(new PartNormallyOccluded(OcclusionHelper.getBox(MicroblockShape.EDGE, 1, microblockLocation))))
                return false;
        }

        return !bundled && WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        if (side != getFace().getOpposite() && side != ForgeDirection.UNKNOWN) {
            int microblockLocation = getFace().ordinal() + (side.ordinal() << 4);
            if (!getParent().canAddPart(new PartNormallyOccluded(OcclusionHelper.getBox(MicroblockShape.EDGE, 1, microblockLocation))))
                return false;
        }

        return !bundled && WireCommons.canConnect(this, device);
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

        if (!isBundled())
            sendUpdatePacket();
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
        for (int i = 0; i < 6; i++) {
            IRedstoneDevice d = this.devices[i];
            if (d != null)
                devices.add(new Pair<IRedstoneDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
        }

        return devices;
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

    @Override
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        if (device instanceof PartRedwireFace)
            if (!bundled && !((PartRedwireFace) device).bundled && !color.matches(((PartRedwireFace) device).color))
                return false;

        if (side != getFace().getOpposite() && side != ForgeDirection.UNKNOWN) {
            int microblockLocation = getFace().ordinal() + (side.ordinal() << 4);
            if (!getParent().canAddPart(new PartNormallyOccluded(OcclusionHelper.getBox(MicroblockShape.EDGE, 1, microblockLocation))))
                return false;
        }

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        if (device instanceof PartRedwireFace)
            if (!bundled && !((PartRedwireFace) device).bundled && !color.matches(((PartRedwireFace) device).color))
                return false;

        if (side != getFace().getOpposite() && side != ForgeDirection.UNKNOWN) {
            int microblockLocation = getFace().ordinal() + (side.ordinal() << 4);
            if (!getParent().canAddPart(new PartNormallyOccluded(OcclusionHelper.getBox(MicroblockShape.EDGE, 1, microblockLocation))))
                return false;
        }

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectBundledClosedCorner(ForgeDirection side, IBundledDevice device) {

        if (device instanceof PartRedwireFace)
            if (!bundled && !((PartRedwireFace) device).bundled && !color.matches(((PartRedwireFace) device).color))
                return false;

        if (side != getFace().getOpposite() && side != ForgeDirection.UNKNOWN) {
            int microblockLocation = getFace().ordinal() + (side.ordinal() << 4);
            if (!getParent().canAddPart(new PartNormallyOccluded(OcclusionHelper.getBox(MicroblockShape.EDGE, 1, microblockLocation))))
                return false;
        }

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
    public MinecraftColor getBundledColor() {

        return bundled ? color : (color == MinecraftColor.NONE ? null : MinecraftColor.NONE);
    }

    @Override
    public List<Pair<IBundledDevice, ForgeDirection>> propagateBundled(ForgeDirection fromSide) {

        List<Pair<IBundledDevice, ForgeDirection>> devices = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();

        if (bundled) {
            for (int i = 0; i < 6; i++) {
                IBundledDevice d = bundledDevices[i];
                if (d != null)
                    devices.add(new Pair<IBundledDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
            }
        }

        return devices;
    }

    @Override
    public boolean isBundled() {

        return bundled || color != MinecraftColor.NONE;
    }

    @Override
    public void addWAILABody(List<String> text) {

        text.add("Power: " + (power & 0xFF) + "/255");
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

}
