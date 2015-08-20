package com.bluepowermod.redstone;

import java.util.Collection;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnection;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.wire.redstone.IRedConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneConductor.IAdvancedRedstoneConductor;
import com.bluepowermod.api.wire.redstone.IRedstoneDevice;

public class RedstoneWrappers {

    public static IRedstoneDevice wrap(IRedstoneDevice dev, ForgeDirection face) {

        if (dev instanceof IAdvancedRedstoneConductor)
            return new AdvancedRedstoneConductorWrapper((IAdvancedRedstoneConductor) dev, face);
        else if (dev instanceof IAdvancedRedstoneConductor)
            return new RedstoneConductorWrapper((IRedstoneConductor) dev, face);
        else
            return new RedstoneDeviceWrapper(dev, face);
    }

    private static ForgeDirection computeDirection(Object obj, ForgeDirection dir) {

        ForgeDirection face = obj instanceof IFace ? ((IFace) obj).getFace() : ForgeDirection.UNKNOWN;

        if (face == ForgeDirection.UNKNOWN || face == ForgeDirection.DOWN)
            return dir;

        return dir;// ForgeDirection.UNKNOWN;
    }

    private static class RedstoneDeviceWrapper implements IRedstoneDevice, IFace {

        protected IRedstoneDevice dev;
        protected ForgeDirection face;

        public RedstoneDeviceWrapper(IRedstoneDevice dev, ForgeDirection face) {

            this.dev = dev;
            this.face = face;
        }

        @Override
        public World getWorld() {

            return dev.getWorld();
        }

        @Override
        public int getX() {

            return dev.getX();
        }

        @Override
        public int getY() {

            return dev.getY();
        }

        @Override
        public int getZ() {

            return dev.getZ();
        }

        @Override
        public ForgeDirection getFace() {

            return face;
        }

        @Override
        public boolean canConnect(ForgeDirection side, IRedstoneDevice dev, ConnectionType type) {

            return dev.canConnect(computeDirection(dev, side), dev, type);
        }

        @Override
        public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache() {

            return dev.getRedstoneConnectionCache();
        }

        @Override
        public byte getRedstonePower(ForgeDirection side) {

            return dev.getRedstonePower(computeDirection(dev, side));
        }

        @Override
        public void setRedstonePower(ForgeDirection side, byte power) {

            dev.setRedstonePower(computeDirection(dev, side), power);
        }

        @Override
        public void onRedstoneUpdate() {

            dev.onRedstoneUpdate();
        }

        @Override
        public boolean isNormalFace(ForgeDirection side) {

            return dev.isNormalFace(computeDirection(dev, side));
        }

    }

    private static class RedstoneConductorWrapper extends RedstoneDeviceWrapper implements IRedstoneConductor {

        public RedstoneConductorWrapper(IRedstoneConductor dev, ForgeDirection face) {

            super(dev, face);
        }

        @Override
        public boolean hasLoss(ForgeDirection side) {

            return ((IRedConductor) dev).hasLoss(computeDirection(dev, side));
        }

        @Override
        public boolean isAnalogue(ForgeDirection side) {

            return ((IRedConductor) dev).isAnalogue(computeDirection(dev, side));
        }

        @Override
        public boolean canPropagateFrom(ForgeDirection fromSide) {

            return ((IRedstoneConductor) dev).canPropagateFrom(computeDirection(dev, fromSide));
        }

    }

    private static class AdvancedRedstoneConductorWrapper extends RedstoneConductorWrapper implements IAdvancedRedstoneConductor {

        public AdvancedRedstoneConductorWrapper(IAdvancedRedstoneConductor dev, ForgeDirection face) {

            super(dev, face);
        }

        @Override
        public void propagate(ForgeDirection fromSide, Collection<IConnection<IRedstoneDevice>> propagation) {

            ((IAdvancedRedstoneConductor) dev).propagate(computeDirection(dev, fromSide), propagation);
        }

    }

}
