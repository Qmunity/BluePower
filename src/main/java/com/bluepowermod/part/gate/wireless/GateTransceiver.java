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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartPlacement;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.util.Dir;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.IGateLogic;
import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.ConnectionType;
import com.bluepowermod.api.wire.IConnection;
import com.bluepowermod.api.wire.redstone.IBundledConductor.IAdvancedBundledConductor;
import com.bluepowermod.api.wire.redstone.IBundledDevice;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.api.wire.redstone.IRedwire;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IWirelessDevice;
import com.bluepowermod.client.gui.gate.GuiGateWireless;
import com.bluepowermod.network.BPNetworkHandler;
import com.bluepowermod.network.message.MessageWirelessFrequencySync;
import com.bluepowermod.part.IGuiButtonSensitive;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.gate.connection.GateConnectionAnalogue;
import com.bluepowermod.part.gate.connection.GateConnectionBase;
import com.bluepowermod.part.gate.connection.GateConnectionBundledAnalogue;
import com.bluepowermod.part.gate.connection.GateConnectionBundledDigital;
import com.bluepowermod.part.gate.connection.GateConnectionDigital;
import com.bluepowermod.util.DebugHelper;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GateTransceiver extends
        GateBase<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase>
        implements IGateLogic<GateTransceiver>, IWirelessDevice, IWirelessGate, IGuiButtonSensitive, IAdvancedRedstoneConductor,
        IAdvancedBundledConductor {

    private static final List<GateTransceiver> transceivers = new ArrayList<GateTransceiver>();

    private boolean isBundled;
    private boolean isAnalogue;

    private Frequency frequency = null;

    private WirelessMode mode = WirelessMode.BOTH;

    public GateTransceiver(Boolean isBundled, Boolean isAnalogue) {

        this.isBundled = isBundled;
        this.isAnalogue = isAnalogue;
    }

    @Override
    public void initConnections() {

        front(
                isBundled ? (isAnalogue ? new GateConnectionBundledAnalogue(this, Dir.FRONT) : new GateConnectionBundledDigital(this,
                        Dir.FRONT)) : (isAnalogue ? new GateConnectionAnalogue(this, Dir.FRONT)
                        : new GateConnectionDigital(this, Dir.FRONT))).setEnabled(true);
    }

    @Override
    public void initComponents() {

    }

    @Override
    public String getGateType() {

        return "wirelesstransceiver" + (isAnalogue ? ".analog" : "") + (isBundled ? ".bundled" : "");
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

        if (getParent() == null || getWorld() == null)
            renderer.addTransformation(new Rotation(0, -(System.currentTimeMillis() / 100D) % 360, 0));
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
    public void tick() {

    }

    @Override
    public boolean changeMode() {

        return false;
    }

    @Override
    public IGateLogic<? extends GateBase<GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase, GateConnectionBase>> logic() {

        return this;
    }

    @Override
    public GateTransceiver getGate() {

        return this;
    }

    @Override
    public void setFrequency(IFrequency freq) {

        frequency = (Frequency) freq;
    }

    @Override
    public Frequency getFrequency() {

        return frequency;
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
    public void writeUpdateData(DataOutput buffer) throws IOException {

        super.writeUpdateData(buffer);
        buffer.writeInt(mode.ordinal());

        buffer.writeBoolean(frequency != null);
        if (frequency != null)
            frequency.writeToBuffer(buffer);
    }

    @Override
    public void readUpdateData(DataInput buffer, int channel) throws IOException {

        super.readUpdateData(buffer, channel);
        mode = WirelessMode.values()[buffer.readInt()];

        if (buffer.readBoolean()) {
            if (frequency == null)
                frequency = new Frequency();
            frequency.readFromBuffer(buffer);
        } else {
            frequency = null;
        }
    }

    @Override
    protected void handleGUIServer(EntityPlayer player) {

        sendUpdatePacket();
        BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
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
    public boolean isBundled() {

        return isBundled;
    }

    @Override
    public void onButtonPress(EntityPlayer player, int messageId, int value) {

        if (messageId == 0)
            mode = WirelessMode.values()[value];
        if (messageId == 1) {
            setFrequency(null);
            BPNetworkHandler.INSTANCE.sendTo(new MessageWirelessFrequencySync(player), (EntityPlayerMP) player);
        }

        sendUpdatePacket();
    }

    @Override
    public boolean hasLoss(ForgeDirection side) {

        return false;
    }

    @Override
    public boolean isAnalogue(ForgeDirection side) {

        return isAnalogue;
    }

    @Override
    public boolean canPropagateFrom(ForgeDirection fromSide) {

        return !isBundled;
    }

    @Override
    public boolean canPropagateBundledFrom(ForgeDirection fromSide) {

        return isBundled;
    }

    @Override
    public Collection<Entry<IConnection<IRedstoneDevice>, Boolean>> propagate(ForgeDirection fromSide) {

        List<Entry<IConnection<IRedstoneDevice>, Boolean>> l = new ArrayList<Entry<IConnection<IRedstoneDevice>, Boolean>>();

        if (frequency == null)
            return l;

        l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(getRedstoneConnectionCache().getConnectionOnSide(fromSide), false));

        for (IWirelessDevice d : WirelessManager.COMMON_INSTANCE.getDevices()) {
            if (d != this && d.getFrequency() != null && d.getFrequency().equals(getFrequency())) {
                if (d instanceof GateTransceiver) {
                    IConnection<IRedstoneDevice> c = ((GateTransceiver) d).getRedstoneConnectionCache().getConnectionOnSide(
                            ((GateTransceiver) d).front().getForgeDirection());
                    if (c != null)
                        l.add(new Pair<IConnection<IRedstoneDevice>, Boolean>(c, false));
                }
            }
        }

        return l;
    }

    @Override
    public Collection<Entry<IConnection<IBundledDevice>, Boolean>> propagateBundled(ForgeDirection fromSide) {

        return Collections.emptyList();
    }

    @Override
    public boolean canConnect(ForgeDirection side, IRedstoneDevice device, ConnectionType type) {

        if (device instanceof IRedwire) {
            RedwireType rwt = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                    : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
            if (rwt == null)
                return false;
            if (rwt.isAnalogue() != isAnalogue(side))
                return false;
        }

        return !isBundled && super.canConnect(side, device, type);
    }

    @Override
    public boolean canConnect(ForgeDirection side, IBundledDevice device, ConnectionType type) {

        if (device instanceof IRedwire) {
            RedwireType rwt = ((IRedwire) device).getRedwireType(type == ConnectionType.STRAIGHT ? side.getOpposite()
                    : (type == ConnectionType.CLOSED_CORNER ? getFace() : getFace().getOpposite()));
            if (rwt == null)
                return false;
            if (rwt.isAnalogue() != isAnalogue(side))
                return false;
        }

        return isBundled && super.canConnect(side, device, type);
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
    public IPartPlacement getPlacement(IPart part, World world, Vec3i location, ForgeDirection face, MovingObjectPosition mop,
            EntityPlayer player) {

        if (!DebugHelper.isDebugModeEnabled())
            return null;

        return super.getPlacement(part, world, location, face, mop, player);
    }

    @Override
    public void addTooltip(ItemStack item, List<String> tip) {

        if (!DebugHelper.isDebugModeEnabled())
            tip.add(MinecraftColor.RED + I18n.format("Disabled temporarily. Still not fully working."));
    }

}
