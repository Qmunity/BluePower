package com.bluepowermod.part.tube;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.misc.Pair;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.redstone.IFaceRedstoneDevice;
import com.bluepowermod.api.redstone.IRedstoneConductor;
import com.bluepowermod.api.redstone.IRedstoneDevice;
import com.bluepowermod.part.wire.redstone.RedstoneApi;
import com.bluepowermod.part.wire.redstone.WireCommons;

public class RedstoneConductorTube implements IRedstoneConductor {

    private static final List<RedstoneConductorTube> tubes = new ArrayList<RedstoneConductorTube>();

    public static RedstoneConductorTube getDevice(PneumaticTube tube) {

        for (RedstoneConductorTube dev : new ArrayList<RedstoneConductorTube>(tubes))
            if (dev.tube != null && dev.tube.equals(tube))
                return dev;

        RedstoneConductorTube dev = new RedstoneConductorTube(tube);
        tubes.add(dev);
        return dev;
    }

    private IRedstoneDevice[] devices = new IRedstoneDevice[6];
    private PneumaticTube tube;

    private byte power = 0;

    private RedstoneConductorTube(PneumaticTube tube) {

        this.tube = tube;
    }

    @Override
    public World getWorld() {

        return tube.getWorld();
    }

    @Override
    public int getX() {

        return tube.getX();
    }

    @Override
    public int getY() {

        return tube.getY();
    }

    @Override
    public int getZ() {

        return tube.getZ();
    }

    @Override
    public boolean canConnectStraight(ForgeDirection side, IRedstoneDevice device) {

        if (tube.getRedwireType() == null)
            return false;

        if (device instanceof IFaceRedstoneDevice)
            return false;
        if (OcclusionHelper.microblockOcclusionTest(new Vec3i(this), MicroblockShape.FACE_HOLLOW, 8, side))
            return false;
        if (device instanceof RedstoneConductorTube)
            if (((RedstoneConductorTube) device).tube instanceof MagTube != tube instanceof MagTube)
                return false;

        return WireCommons.canConnect(this, device);
    }

    @Override
    public boolean canConnectOpenCorner(ForgeDirection side, IRedstoneDevice device) {

        return false;
    }

    @Override
    public void onConnect(ForgeDirection side, IRedstoneDevice device) {

        devices[side.ordinal()] = device;
        tube.sendUpdatePacket();
    }

    @Override
    public void onDisconnect(ForgeDirection side) {

        devices[side.ordinal()] = null;
        tube.sendUpdatePacket();
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

        this.power = power;
    }

    @Override
    public void onRedstoneUpdate() {

        tube.sendUpdatePacket();
    }

    @Override
    public MinecraftColor getInsulationColor(ForgeDirection side) {

        return MinecraftColor.NONE;
    }

    @Override
    public boolean isNormalBlock() {

        return false;
    }

    @Override
    public boolean hasLoss() {

        if (tube.getRedwireType() == null)
            return false;

        return tube.getRedwireType().hasLoss();
    }

    @Override
    public boolean isAnalog() {

        if (tube.getRedwireType() == null)
            return false;

        return tube.getRedwireType().isAnalog();
    }

    @Override
    public Collection<Pair<IRedstoneDevice, ForgeDirection>> propagate(ForgeDirection fromSide) {

        List<Pair<IRedstoneDevice, ForgeDirection>> devices = new ArrayList<Pair<IRedstoneDevice, ForgeDirection>>();

        for (int i = 0; i < 6; i++) {
            IRedstoneDevice d = this.devices[i];
            if (d != null)
                devices.add(new Pair<IRedstoneDevice, ForgeDirection>(d, ForgeDirection.getOrientation(i)));
        }

        return devices;
    }

    public byte getPower() {

        return power;
    }

}
