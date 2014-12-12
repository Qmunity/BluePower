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
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartCustomPlacement;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.part.IPartRedstone;
import uk.co.qmunity.lib.part.IPartSolid;
import uk.co.qmunity.lib.part.IPartThruHole;
import uk.co.qmunity.lib.part.IPartWAILAProvider;
import uk.co.qmunity.lib.part.PartNormallyOccluded;
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
import com.bluepowermod.client.renderers.IconSupplier;
import com.bluepowermod.part.PartPlacementNone;
import com.bluepowermod.part.wire.PartWireFreestanding;
import com.bluepowermod.part.wire.redstone.propagation.WirePropagator;

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
    protected IIcon getWireIcon(ForgeDirection side) {

        return bundled ? IconSupplier.wireBundled : (color == MinecraftColor.NONE ? IconSupplier.wire : IconSupplier.wireInsulation1);
    }

    @Override
    protected boolean shouldRenderConnection(ForgeDirection side) {

        return connections[side.ordinal()];
    }

    @Override
    protected IIcon getFrameIcon() {

        return Blocks.planks.getIcon(0, 0);
    }

    @Override
    protected int getColorMultiplier() {

        return bundled ? 0xFFFFFF : (color == MinecraftColor.NONE ? WireCommons.getColorForPowerLevel(type.getColor(), power) : color
                .getHex());
    }

    @Override
    public int getHollowSize(ForgeDirection side) {

        return 4;
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

        // IIcon normalIcon = IconSupplier.wire;
        // IIcon insulationIcon = IconSupplier.wireInsulation;
        // IIcon bundleIcon = IconSupplier.wireBundled;
        //
        // double size = 2 / 16D;
        // double separation = 4 / 16D;
        // double thickness = 1 / 16D;
        //
        // IIcon icon = normalIcon;
        // int color = WireCommons.getColorForPowerLevel(getColor(), power);
        //
        // if (insulationColor != MinecraftColor.NONE) {
        // size += 2 / 16D;
        // separation -= 2 / 16D;
        // icon = insulationIcon;
        // color = insulationColor.getHex();
        // }
        // if (isBundled()) {
        // size += 4 / 16D;
        // separation -= 4 / 16D;
        // icon = bundleIcon;
        // color = 0xFFFFFF;
        // }
        //
        // boolean isInWorld = getParent() != null;
        //
        // boolean down = connections[ForgeDirection.DOWN.ordinal()];
        // boolean up = connections[ForgeDirection.UP.ordinal()];
        // boolean north = connections[ForgeDirection.NORTH.ordinal()];
        // boolean south = connections[ForgeDirection.SOUTH.ordinal()];
        // boolean west = connections[ForgeDirection.WEST.ordinal()];
        // boolean east = connections[ForgeDirection.EAST.ordinal()];
        //
        // renderer.setColor(color);
        //
        // // Wire
        // renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2),
        // 0.5 + (size / 2)), icon);
        // if (up || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 + (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 1, 0.5 + (size / 2)),
        // icon);
        // if (down || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0, 0.5 - (size / 2), 0.5 + (size / 2), 0.5 - (size / 2), 0.5 + (size / 2)),
        // icon);
        // if (north || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0, 0.5 + (size / 2), 0.5 + (size / 2), 0.5 - (size / 2)),
        // icon);
        // if (south || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 0.5 + (size / 2), 1),
        // icon);
        // if (west || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0, 0.5 - (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 0.5 + (size / 2), 0.5 + (size / 2)),
        // icon);
        // if (east || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + (size / 2), 0.5 - (size / 2), 0.5 - (size / 2), 1, 0.5 + (size / 2), 0.5 + (size / 2)),
        // icon);
        //
        // renderer.setColor(0xFFFFFF);
        //
        // // Frame
        // {
        // IIcon planks = Blocks.planks.getIcon(0, 0);
        //
        // // Top
        // if (west == up || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2),
        // 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness,
        // 0.5 + ((size + separation) / 2)), planks);
        // if (east == up || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2),
        // 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2)
        // + thickness, 0.5 + ((size + separation) / 2)), planks);
        // if (south == up || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2),
        // 0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5
        // + ((size + separation) / 2) + thickness), planks);
        // if (north == up || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5
        // - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2)
        // + thickness, 0.5 - ((size + separation) / 2)), planks);
        // // Bottom
        // if (west == down || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2) - thickness,
        // 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2),
        // 0.5 + ((size + separation) / 2)), planks);
        // if (east == down || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness,
        // 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 - ((size + separation) / 2),
        // 0.5 + ((size + separation) / 2)), planks);
        // if (south == down || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness,
        // 0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5
        // + ((size + separation) / 2) + thickness), planks);
        // if (north == down || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness, 0.5
        // - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2),
        // 0.5 - ((size + separation) / 2)), planks);
        //
        // // Sides
        // if (north == west || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5
        // - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2),
        // 0.5 - ((size + separation) / 2)), planks);
        // if (south == west || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2),
        // 0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5
        // + ((size + separation) / 2) + thickness), planks);
        // if (north == east || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5
        // - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2) + thickness,
        // 0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2)), planks);
        // if (south == east || !isInWorld)
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2),
        // 0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2), 0.5
        // + ((size + separation) / 2) + thickness), planks);
        //
        // // Corners
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2), 0.5
        // - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness,
        // 0.5 - ((size + separation) / 2)), planks);
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2),
        // 0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5
        // + ((size + separation) / 2) + thickness), planks);
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2), 0.5
        // - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2)
        // + thickness, 0.5 - ((size + separation) / 2)), planks);
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2),
        // 0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 + ((size + separation) / 2)
        // + thickness, 0.5 + ((size + separation) / 2) + thickness), planks);
        //
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2) - thickness, 0.5
        // - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2),
        // 0.5 - ((size + separation) / 2)), planks);
        // renderer.renderBox(new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2) - thickness,
        // 0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2), 0.5
        // + ((size + separation) / 2) + thickness), planks);
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness, 0.5
        // - ((size + separation) / 2) - thickness, 0.5 + ((size + separation) / 2) + thickness, 0.5 - ((size + separation) / 2),
        // 0.5 - ((size + separation) / 2)), planks);
        // renderer.renderBox(new Vec3dCube(0.5 + ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness,
        // 0.5 + ((size + separation) / 2), 0.5 + ((size + separation) / 2) + thickness, 0.5 - ((size + separation) / 2), 0.5
        // + ((size + separation) / 2) + thickness), planks);
        //
        // if (isInWorld) {
        // // Connections
        // Vec3dCube box = new Vec3dCube(0.5 - ((size + separation) / 2) - thickness, 0, 0.5 - ((size + separation) / 2) - thickness,
        // 0.5 - ((size + separation) / 2), 0.5 - ((size + separation) / 2) - thickness, 0.5 - ((size + separation) / 2));
        // for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
        // if (!connections[d.ordinal()])
        // continue;
        // for (int i = 0; i < 4; i++)
        // renderer.renderBox(box.clone().rotate(0, 90 * i, 0, Vec3d.center).rotate(d, Vec3d.center), planks);
        // }
        // }
        // }
        // renderer.resetTransformations();
        //
        // return true;
        return super.renderStatic(translation, renderer, renderBlocks, pass);
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
            if (!getParent().canAddPart(
                    new PartNormallyOccluded(OcclusionHelper.getFaceHollowMicroblockBox(1, side.ordinal(), getHollowSize(side)))))
                return false;

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        if (!(device instanceof IFaceRedstoneDevice))
            if (!getParent().canAddPart(
                    new PartNormallyOccluded(OcclusionHelper.getFaceHollowMicroblockBox(1, side.ordinal(), getHollowSize(side)))))
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

    @Override
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        if (!(device instanceof IFaceBundledDevice))
            if (!getParent().canAddPart(
                    new PartNormallyOccluded(OcclusionHelper.getFaceHollowMicroblockBox(1, side.ordinal(), getHollowSize(side)))))
                return false;

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        if (!(device instanceof IFaceBundledDevice))
            if (!getParent().canAddPart(
                    new PartNormallyOccluded(OcclusionHelper.getFaceHollowMicroblockBox(1, side.ordinal(), getHollowSize(side)))))
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

        return bundled ? color : null;
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

        return bundled || getInsulationColor() != MinecraftColor.NONE;
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

        if (((PartRedwireFreestanding) part).bundled || type == RedwireType.RED_ALLOY)
            return new PartPlacementNone();

        return null;
    }

    @Override
    public void addTooltip(List<String> tip) {

        if (bundled || type == RedwireType.RED_ALLOY)
            tip.add(MinecraftColor.RED + I18n.format("Disabled temporarily. Still not fully working."));
    }

}