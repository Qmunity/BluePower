package com.bluepowermod.part.gate.wireless;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3d;
import uk.co.qmunity.lib.vec.Vec3dCube;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.Accessability;
import com.bluepowermod.api.redstone.IBundledConductor;
import com.bluepowermod.api.redstone.IBundledDevice;
import com.bluepowermod.api.redstone.IFaceBundledDevice;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.api.redstone.RedstoneColor;
import com.bluepowermod.api.wireless.IBundledFrequency;
import com.bluepowermod.api.wireless.IFrequency;
import com.bluepowermod.api.wireless.IRedstoneFrequency;
import com.bluepowermod.part.gate.GateBase;
import com.bluepowermod.part.wire.WireCommons;

public class GateTransceiver extends GateBase implements IFaceRedstoneDevice, IRedstoneConductor, IFaceBundledDevice, IBundledConductor {

    private static IFrequency mainFreq = WirelessManager.INSTANCE.registerRedstoneFrequency(null, "test", Accessability.PUBLIC);
    private static List<GateTransceiver> transceivers = new ArrayList<GateTransceiver>();

    private boolean isBundled;
    private boolean isAnalog;

    private IFrequency frequency = mainFreq;

    private IRedstoneDevice[] devices = new IRedstoneDevice[6];
    private IBundledDevice[] bundledDevices = new IBundledDevice[6];

    public GateTransceiver(Boolean isBundled, Boolean isAnalog) {

        this.isBundled = isBundled;
        this.isAnalog = isAnalog;
    }

    @Override
    public void initializeConnections() {

    }

    @Override
    public String getId() {

        return "wirelesstransceiver" + (isAnalog ? ".analog" : "") + (isBundled ? ".bundled" : "");
    }

    @Override
    protected void renderTop(float frame) {

    }

    @Override
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

        return true;
    }

    @Override
    public void doLogic() {

    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        return !isBundled;
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        return !isBundled;
    }

    @Override
    public boolean canConnectClosedCorner(ForgeDirection side, IRedstoneDevice device) {

        return !isBundled;
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

        if (isBundled)
            return 0;
        if (frequency == null)
            return 0;

        return ((IRedstoneFrequency) frequency).getSignal();
    }

    @Override
    public void setRedstonePower(ForgeDirection side, byte power) {

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
    public RedstoneColor getInsulationColor() {

        return RedstoneColor.NONE;
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

        for (GateTransceiver t : transceivers) {
            if (t == this)
                continue;
            if (t.frequency != frequency)
                continue;
            devices.add(new Pair<IRedstoneDevice, ForgeDirection>(t, ForgeDirection.UNKNOWN));
        }

        if (fromSide == ForgeDirection.UNKNOWN)
            for (int i = 0; i < 6; i++) {
                IRedstoneDevice d = this.devices[i];
                if (d != null)
                    devices.add(new Pair<IRedstoneDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
            }

        return devices;
    }

    @Override
    public boolean canConnectBundledStraight(ForgeDirection side, IBundledDevice device) {

        return true;
    }

    @Override
    public boolean canConnectBundledOpenCorner(ForgeDirection side, IBundledDevice device) {

        return true;
    }

    @Override
    public boolean canConnectBundledClosedCorner(ForgeDirection side, IBundledDevice device) {

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
    public byte[] getBundledPower(ForgeDirection side) {

        if (!isBundled)
            return new byte[16];
        if (frequency == null)
            return new byte[16];

        return ((IBundledFrequency) frequency).getSignal();
    }

    @Override
    public void setBundledPower(ForgeDirection side, byte[] power) {

        if (!isBundled)
            return;
        if (frequency == null)
            return;

        ((IBundledFrequency) frequency).setSignal(power);
    }

    @Override
    public void onBundledUpdate() {

    }

    @Override
    public RedstoneColor getBundleColor() {

        return RedstoneColor.NONE;
    }

    @Override
    public boolean isBundled() {

        return isBundled;
    }

    @Override
    public Collection<Pair<IBundledDevice, ForgeDirection>> propagateBundled(ForgeDirection fromSide) {

        List<Pair<IBundledDevice, ForgeDirection>> devices = new ArrayList<Pair<IBundledDevice, ForgeDirection>>();

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
    }

    @Override
    public void onLoaded() {

        super.onLoaded();
        if (!transceivers.contains(this))
            transceivers.add(this);
    }

    @Override
    public void onRemoved() {

        super.onRemoved();
        transceivers.remove(this);
    }

    @Override
    public void onUnloaded() {

        super.onUnloaded();
        transceivers.remove(this);
    }

}
