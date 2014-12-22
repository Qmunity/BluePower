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
import uk.co.qmunity.lib.part.IPartPlacement;
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

import cpw.mods.fml.common.FMLCommonHandler;

public class PartRedwireFace extends PartWireFace implements IFaceRedstoneDevice, IRedstoneConductor, IFaceBundledDevice,
        IBundledConductor, IPartRedstone, IPartWAILAProvider {

    protected final IRedstoneDevice[] devices = new IRedstoneDevice[6];
    protected final IBundledDevice[] bundledDevices = new IBundledDevice[6];
    protected final boolean[] connections = new boolean[6];

    protected final RedwireType type;
    protected final boolean bundled;
    protected final MinecraftColor color;

    protected byte power = 0;
    protected byte[] bundledPower = new byte[16];

    private boolean hasUpdated = false;

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

        if (isBundled()) {
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
                    renderer.renderBox(new Vec3dCube(s3 ? 0 : 5 / 16D, height, 8 / 16D - width, 8 / 16D - width, height + size,
                            8 / 16D + width), IconSupplier.wire);
                if (s4 || (!s1 && !s2))
                    renderer.renderBox(new Vec3dCube(8 / 16D + width, height, 8 / 16D - width, s4 ? 1 : 11 / 16D, height + size,
                            8 / 16D + width), IconSupplier.wire);
                if (s1)
                    renderer.renderBox(new Vec3dCube(8 / 16D - width, height, s1 ? 0 : 4 / 16D, 8 / 16D + width, height + size,
                            8 / 16D - width), IconSupplier.wire);
                if (s2)
                    renderer.renderBox(new Vec3dCube(8 / 16D - width, height, 8 / 16D + width, 8 / 16D + width, height + size, s2 ? 1
                            : 12 / 16D), IconSupplier.wire);
            } else {
                renderer.renderBox(
                        new Vec3dCube(8 / 16D - width, height, s1 ? 0 : 5 / 16D, 8 / 16D + width, height + size, 8 / 16D - width),
                        IconSupplier.wire);
                renderer.renderBox(new Vec3dCube(8 / 16D - width, height, 8 / 16D + width, 8 / 16D + width, height + size, s2 ? 1
                        : 11 / 16D), IconSupplier.wire);
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
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        power = (byte) 255;
        double scale = 2;
        double translation = 0.5;
        double droppedTranslation = -0.5;

        if (bundled || getInsulationColor() != MinecraftColor.NONE) {
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

        RedstoneApi.getInstance().setWiresOutputPower(true);

        if (!bundled && hasUpdated) {
            sendUpdatePacket();
            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                try {
                    getWorld();
                } catch (Exception ex) {
                    System.out.println(FMLCommonHandler.instance().getEffectiveSide());
                }
                if ((devices[dir.ordinal()] != null && (devices[dir.ordinal()] instanceof DummyRedstoneDevice)) || dir == getFace())
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
        for (int i = 0; i < 6; i++) {
            IRedstoneDevice d = this.devices[i];
            if (d != null)
                devices.add(new Pair<IRedstoneDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
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

        if (!RedstoneApi.getInstance().shouldWiresHandleUpdates())
            return;

        super.onUpdate();

        if (getWorld().isRemote)
            return;

        WireCommons.refreshConnections(this, this);

        RedstoneApi.getInstance().setWiresOutputPower(false);
        int input = 0;
        for (int i = 0; i < 6; i++) {
            IRedstoneDevice d = devices[i];
            if (d != null && d instanceof DummyRedstoneDevice)
                input = Math.max(input, d.getRedstonePower(ForgeDirection.getOrientation(i).getOpposite()) & 0xFF);
        }
        RedstoneApi.getInstance().setWiresOutputPower(true);

        RedstoneApi.getInstance().setWiresHandleUpdates(false);
        WirePropagator.INSTANCE.onPowerLevelChange(this, getFace(), lastInput, (byte) input);
        RedstoneApi.getInstance().setWiresHandleUpdates(true);

        lastInput = (byte) input;
    }

    @Override
    public boolean canConnectRedstone(ForgeDirection side) {

        if (getWorld().isRemote) {
            System.out.println(Arrays.asList(devices));
        }

        return false;// side != getFace().getOpposite() && !bundled;
    }

    @Override
    public int getStrongPower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        if ((side == getFace().getOpposite() || devices[side.ordinal()] == null || !(devices[side.ordinal()] instanceof DummyRedstoneDevice))
                && side != getFace())
            return 0;

        return MathHelper.map(power & 0xFF, 0, 255, 0, 15);
    }

    @Override
    public int getWeakPower(ForgeDirection side) {

        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;

        if ((side == getFace().getOpposite() || devices[side.ordinal()] == null || !(devices[side.ordinal()] instanceof DummyRedstoneDevice))
                && side != getFace())
            return 0;

        return MathHelper.map(power & 0xFF, 0, 255, 0, 15);
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

    @Override
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        if (bundled || type == RedwireType.RED_ALLOY)
            return null;

        return super.getPlacement(part, world, location, face, mop, player);
    }

    @Override
    public void addTooltip(List<String> tip) {

        if (bundled || type == RedwireType.RED_ALLOY)
            tip.add(MinecraftColor.RED + I18n.format("Disabled temporarily. Still not fully working."));
    }

    @Override
    public void setFace(ForgeDirection face) {

        WireCommons.disconnect(this, this);

        super.setFace(face);

        if (getParent() != null)
            WireCommons.refreshConnections(this, this);
    }

}
