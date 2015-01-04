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

package com.bluepowermod.part.gate.wireless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.transform.Rotation;
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
import com.bluepowermod.api.wireless.IBundledFrequency;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IRedstoneFrequency;
import com.bluepowermod.api.wireless.IWirelessDevice;
import com.bluepowermod.client.gui.gate.GuiGateWireless;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.message.MessageWirelessFrequencySync;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.wire.redstone.RedstoneApi;
import com.bluepowermod.part.wire.redstone.WireCommons;
import com.bluepowermod.part.wire.redstone.propagation.WirePropagator;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateTransceiver extends GateBase implements IWirelessDevice, IFaceRedstoneDevice, IRedstoneConductor, IFaceBundledDevice,
IBundledConductor, IGuiButtonSensitive, IWirelessGate {

    private static final List<GateTransceiver> transceivers = new ArrayList<GateTransceiver>();

    private boolean isBundled;
    private boolean isAnalog;

    private Frequency frequency = null;

    private IRedstoneDevice[] devices = new IRedstoneDevice[6];
    private IBundledDevice[] bundledDevices = new IBundledDevice[6];

    private WirelessMode mode = WirelessMode.BOTH;

    public GateTransceiver(Boolean isBundled, Boolean isAnalog) {

        this.isBundled = isBundled;
        this.isAnalog = isAnalog;
    }

    @Override
    public void initializeConnections() {

        front().setEnabled(true);
    }

    @Override
    public String getId() {

        return "wirelesstransceiver" + (isAnalog ? ".analog" : "") + (isBundled ? ".bundled" : "");
    }

    @Override
    protected String getTextureName() {

        return "wirelesstransceiver";
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected void renderTop(float frame) {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean renderStatic(Vec3i translation, RenderHelper renderer, RenderBlocks renderBlocks, int pass) {

        super.renderStatic(translation, renderer, renderBlocks, pass);

        if (getParent() != null)
            renderer.addTransformation(new Rotation(0, 180, 0));

        IIcon obsidian = Blocks.obsidian.getIcon(0, 0);
        IIcon quartz = Blocks.quartz_block.getIcon(0, 0);
        IIcon iron = Blocks.iron_block.getIcon(0, 0);
        IIcon gold = Blocks.gold_block.getIcon(0, 0);

        // Base
        renderer.renderBox(new Vec3dCube(7 / 16D, 2 / 16D, 7 / 16D, 9 / 16D, 8 / 16D, 9 / 16D), obsidian);

        if (rendering != null) {
            renderer.addTransformation(new Rotation(0, (System.currentTimeMillis() / 100D) % 360, 0));
        }
        renderer.addTransformation(new Rotation(45, 0, 0));

        // Post
        renderer.renderBox(new Vec3dCube(15 / 32D, 9 / 16D, 15 / 32D, 17 / 32D, 10 / 16D, 17 / 32D), iron);
        // Ball thingy
        renderer.renderBox(new Vec3dCube(7 / 16D, 10 / 16D, 7 / 16D, 9 / 16D, 12 / 16D, 9 / 16D), gold);

        renderer.renderBox(new Vec3dCube(6 / 16D, 8 / 16D, 5 / 16D, 10 / 16D, 9 / 16D, 11 / 16D), quartz);
        renderer.renderBox(new Vec3dCube(5 / 16D, 8 / 16D, 6 / 16D, 11 / 16D, 9 / 16D, 10 / 16D), quartz);

        for (int i = 0; i < 4; i++) {
            renderer.renderBox(new Vec3dCube(5 / 16D, 9 / 16D, 10 / 16D, 6 / 16D, 10 / 16D, 11 / 16D).rotate(0, i * 90, 0, Vec3d.center),
                    quartz);

            renderer.renderBox(new Vec3dCube(4 / 16D, 9 / 16D, 6 / 16D, 5 / 16D, 10 / 16D, 10 / 16D).rotate(0, i * 90, 0, Vec3d.center),
                    quartz);
            renderer.renderBox(new Vec3dCube(4 / 16D, 10 / 16D, 5 / 16D, 5 / 16D, 11 / 16D, 11 / 16D).rotate(0, i * 90, 0, Vec3d.center),
                    quartz);

            renderer.renderBox(new Vec3dCube(4 / 16D, 11 / 16D, 11 / 16D, 5 / 16D, 12 / 16D, 12 / 16D).rotate(0, i * 90, 0, Vec3d.center),
                    quartz);

            renderer.renderBox(new Vec3dCube(3 / 16D, 11 / 16D, 4 / 16D, 4 / 16D, 12 / 16D, 12 / 16D).rotate(0, i * 90, 0, Vec3d.center),
                    quartz);
        }

        renderer.resetTransformations();

        return true;
    }

    @Override
    public void doLogic() {

    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        if (!super.canConnectStraight(side, device))
            return false;

        return side == front().getDirection().toForgeDirection(getFace(), getRotation());
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        if (!super.canConnectOpenCorner(side, device))
            return false;

        return side == front().getDirection().toForgeDirection(getFace(), getRotation());
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        if (!super.canConnectClosedCorner(side, device))
            return false;

        return side == front().getDirection().toForgeDirection(getFace(), getRotation());
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

        if (mode == WirelessMode.SEND)
            return 0;
        if (!RedstoneApi.getInstance().shouldWiresOutputPower())
            return 0;
        if (isBundled)
            return 0;
        if (frequency == null)
            return 0;

        return ((IRedstoneFrequency) frequency).getSignal();
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

        if (mode == WirelessMode.RECEIVE)
            return;
        if (isBundled)
            return;
        if (frequency == null)
            return;

        ((IRedstoneFrequency) frequency).setSignal(power);
    }

    @Override
    public void onRedstoneUpdate() {

    }

    @Override
    public MinecraftColor getInsulationColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public boolean hasLoss() {

        return false;
    }

    @Override
    public boolean isAnalog() {

        return isAnalog;
    }

    @Override
    public Collection<Pair<IRedstoneDevice, ForgeDirection>> propagate(ForgeDirection fromSide) {

        List<Pair<IRedstoneDevice, ForgeDirection>> devices = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();

        if (frequency == null)
            return devices;

        if (mode != WirelessMode.RECEIVE) {
            for (GateTransceiver t : transceivers) {
                if (t == this)
                    continue;
                if (t.frequency != frequency)
                    continue;
                devices.add(new Pair<IRedstoneDevice, ForgeDirection>(t, ForgeDirection.UNKNOWN));
            }
        }

        if (mode != WirelessMode.SEND) {
            if (fromSide == ForgeDirection.UNKNOWN) {
                for (int i = 0; i < 6; i++) {
                    IRedstoneDevice d = this.devices[i];
                    if (d != null)
                        devices.add(new Pair<IRedstoneDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
                }
            }
        }

        return devices;
    }

    @Override
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        if (!isBundled)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return true;
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        if (!isBundled)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return true;
    }

    @Override
    public boolean canConnectBundledClosedCorner(ForgeDirection side, IBundledDevice device) {

        if (!isBundled)
            return false;

        if (OcclusionHelper.microblockOcclusionTest(getParent(), MicroblockShape.EDGE, 1, getFace(), side))
            return false;

        return true;
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

        if (mode == WirelessMode.SEND)
            return new byte[16];
        if (!isBundled)
            return new byte[16];
        if (frequency == null)
            return new byte[16];

        return ((IBundledFrequency) frequency).getSignal();
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        if (mode == WirelessMode.RECEIVE)
            return;
        if (!isBundled)
            return;
        if (frequency == null)
            return;

        ((IBundledFrequency) frequency).setSignal(power);
    }

    @Override
    public byte[] getBundledPower(ForgeDirection side) {

        return getBundledOutput(side);
    }

    @Override
    public void onBundledUpdate() {

    }

    @Override
    public MinecraftColor getBundledColor() {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isBundled() {

        return isBundled;
    }

    @Override
    public Collection<Pair<IBundledDevice, ForgeDirection>> propagateBundled(ForgeDirection fromSide) {

        List<Pair<IBundledDevice, ForgeDirection>> devices = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();

        if (frequency == null)
            return devices;

        for (GateTransceiver t : transceivers) {
            if (t == this)
                continue;
            if (t.frequency != frequency)
                continue;
            devices.add(new Pair<IBundledDevice, ForgeDirection>(t, ForgeDirection.UNKNOWN));
        }

        if (fromSide == ForgeDirection.UNKNOWN)
            for (int i = 0; i < 6; i++) {
                IBundledDevice d = bundledDevices[i];
                if (d != null)
                    devices.add(new Pair<IBundledDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
            }

        return devices;
    }

    @Override
    public void onUpdate() {

        super.onUpdate();

        WireCommons.refreshConnections(this, this);
    }

    @Override
    public void onAdded() {

        super.onAdded();
        if (!transceivers.contains(this))
            transceivers.add(this);
        WirelessManager.COMMON_INSTANCE.registerWirelessDevice(this);
    }

    @Override
    public void onLoaded() {

        super.onLoaded();
        if (!transceivers.contains(this))
            transceivers.add(this);
        WirelessManager.COMMON_INSTANCE.registerWirelessDevice(this);
    }

    @Override
    public void onRemoved() {

        super.onRemoved();
        transceivers.remove(this);
        WirelessManager.COMMON_INSTANCE.unregisterWirelessDevice(this);
    }

    @Override
    public void onUnloaded() {

        super.onUnloaded();
        transceivers.remove(this);
        WirelessManager.COMMON_INSTANCE.unregisterWirelessDevice(this);
    }

    @Override
    public void setFrequency(IFrequency freq) {

        if (getWorld().isRemote)
            return;
        if (!(freq instanceof Frequency) && freq != null)
            return;

        transceivers.remove(this);
        WireCommons.refreshConnections(this, this);
        WirePropagator.INSTANCE.onPowerLevelChange(this, ForgeDirection.UNKNOWN, (byte) 0, (byte) 0);
        frequency = (Frequency) freq;
        transceivers.add(this);
        WirePropagator.INSTANCE.onPowerLevelChange(this, ForgeDirection.UNKNOWN, (byte) 0, (byte) 0);

        sendUpdatePacket();
    }

    @Override
    public Frequency getFrequency() {

        return frequency;
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {

        super.writeToNBT(tag);
        tag.setInteger("mode", mode.ordinal());

        if (frequency != null)
            frequency.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {

        super.readFromNBT(tag);
        mode = WirelessMode.values()[tag.getInteger("mode")];

        if (tag.hasKey("freq_name")) {
            Frequency f = new Frequency();
            f.readFromNBT(tag);
            frequency = (Frequency) WirelessManager.COMMON_INSTANCE.getFrequency(f.getAccessibility(), f.getFrequencyName(), f.getOwner());
        } else {
            frequency = null;
        }
    }

    @Override
    public void writeUpdateToNBT(NBTTagCompound tag) {

        super.writeUpdateToNBT(tag);
        tag.setInteger("mode", mode.ordinal());

        if (frequency != null)
            frequency.writeToNBT(tag);
    }

    @Override
    public void readUpdateFromNBT(NBTTagCompound tag) {

        super.readUpdateFromNBT(tag);
        mode = WirelessMode.values()[tag.getInteger("mode")];

        if (tag.hasKey("freq_name")) {
            if (frequency == null)
                frequency = new Frequency();
            frequency.readFromNBT(tag);
        } else {
            frequency = null;
        }
    }

    @Override
    protected void handleGUIServer(EntityPlayer player) {

        sendUpdatePacket();
        NetworkHandler.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected GuiScreen getGui(EntityPlayer player) {

        return new GuiGateWireless(this, isBundled, mode);
    }

    @Override
    protected boolean hasGUI() {

        return true;
    }

    @Override
    public WirelessMode getMode() {

        return mode;
    }

    @Override
    public void setMode(WirelessMode mode) {

        this.mode = mode;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 0)
            mode = WirelessMode.values()[value];
        if (messageId == 1) {
            setFrequency(null);
            NetworkHandler.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
        }

        sendUpdatePacket();
    }

}
