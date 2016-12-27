package com.bluepowermod.compat.fmp;

import net.minecraft.world.World;
import net.minecraft.util.EnumFacing;;
import uk.co.qmunity.lib.helper.MathHelper;
import codechicken.multipart.IFaceRedstonePart;
import codechicken.multipart.IRedstonePart;
import codechicken.multipart.TMultiPart;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;
import com.bluepowermod.redstone.RedstoneApi;
import com.bluepowermod.redstone.RedstoneConnectionCache;

public class FMPRedstoneDevice implements IRedstoneDevice, IFace {

    private TMultiPart part;
    private RedstoneConnectionCache connections = RedstoneApi.getInstance().createRedstoneConnectionCache(this);

    public FMPRedstoneDevice(TMultiPart part) {

        this.part = part;
    }

    @Override
    public World getWorld() {

        return part.world();
    }

    @Override
    public int getX() {

        return part.x();
    }

    @Override
    public int getY() {

        return part.y();
    }

    @Override
    public int getZ() {

        return part.z();
    }

    @Override
    public boolean canConnect(EnumFacing side, IRedstoneDevice device, ConnectionType type) {

        if (type == ConnectionType.STRAIGHT || type == ConnectionType.CLOSED_CORNER) {
            if (side == EnumFacing.UNKNOWN)
                return false;
            if (!(device instanceof IFace))
                return false;

            return ((IRedstonePart) part).canConnectRedstone(side.ordinal());
        }

        return false;
    }

    @Override
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {

        return connections;
    }

    @Override
    public byte getRedstonePower(EnumFacing side) {

        IFaceRedstonePart p = ((IFaceRedstonePart) part);

        if (part.tile() == null && part.world() == null)
            return 0;

        int pow = Math.max(p.weakPowerLevel(side.ordinal()), p.strongPowerLevel(side.ordinal()));

        return (byte) MathHelper.map(pow, 0, 15, 0, 255);
    }

    @Override
    public void setRedstonePower(EnumFacing side, byte power) {

    }

    @Override
    public void onRedstoneUpdate() {

        if (part.tile() != null && part.world() != null)
            part.onNeighborChanged();
    }

    @Override
    public EnumFacing getFace() {

        return EnumFacing.getOrientation(((IFaceRedstonePart) part).getFace());
    }

    @Override
    public boolean isNormalFace(EnumFacing side) {

        return false;
    }

}
