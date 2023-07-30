package com.bluepowermod.helper;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.misc.IWorldLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class ConnectionLogicHelper<T extends IWorldLocation, C> {

    public static interface IConnectableProvider<T extends IWorldLocation, C> {

        public T getConnectableAt(Level level, BlockPos pos, Direction face, Direction side);

        public C createConnection(T a, T b, Direction sideA, Direction sideB, ConnectionType type);

        public boolean canConnect(T from, T to, Direction side, ConnectionType type);

        public boolean isValidClosedCorner(T o);

        public boolean isValidOpenCorner(T o);

        public boolean isValidStraight(T o);

        public boolean isNormalFace(T o, Direction face);
    }

    private IConnectableProvider<T, C> provider;

    public ConnectionLogicHelper(IConnectableProvider<T, C> provider) {

        this.provider = provider;
    }

    public C getNeighbor(T device, Direction side) {

        Direction face = null;
        if (device instanceof IFace)
            face = ((IFace) device).getFace();

        // In same block
        do {
            T dev = provider.getConnectableAt(device.getLevel(), device.getBlockPos(), face == null ? null : side == face.getOpposite() ? null
                    : side, face == null ? side.getOpposite() : face);
            if (dev == null || dev == device || !provider.isValidClosedCorner(dev))
                break;

            ConnectionType type = !(device instanceof IFace == dev instanceof IFace) ? ConnectionType.STRAIGHT
                    : ConnectionType.CLOSED_CORNER;
            if (provider.canConnect(device, dev, side, type) && provider.canConnect(dev, device, face, type))
                return provider.createConnection(device, dev, side, face, type);
        } while (false);

        // On same block
        if (face != null) {
            do {
                T dev = provider.getConnectableAt(device.getLevel(), device.getBlockPos(), side.getOpposite(), face.getOpposite());
                if (dev == null || dev == device || !provider.isValidOpenCorner(dev))
                    break;
                if (provider.canConnect(device, dev, side, ConnectionType.OPEN_CORNER)
                        && provider.canConnect(dev, device, face.getOpposite(), ConnectionType.OPEN_CORNER))
                    return provider.createConnection(device, dev, side, face.getOpposite(), ConnectionType.OPEN_CORNER);
            } while (false);
        }

        // Straight connection
        do {
            T dev = provider.getConnectableAt(device.getLevel(), device.getBlockPos(), face, side.getOpposite());
            if (dev == null) {
                dev = provider.getConnectableAt(device.getLevel(), device.getBlockPos(), side.getOpposite(), side.getOpposite());
                if (dev == null && face == null && provider.isNormalFace(device, side)) {
                    for (Direction d : Direction.values()) {
                        if (d != side && d != side.getOpposite()) {
                            dev = provider.getConnectableAt(device.getLevel(), device.getBlockPos(), d, side.getOpposite());
                            if (dev != null)
                                break;
                        }
                    }
                }
            }

            if (dev == null || dev == device || !provider.isValidStraight(dev))
                break;

            if (provider.canConnect(device, dev, side, ConnectionType.STRAIGHT)
                    && provider.canConnect(dev, device, side.getOpposite(), ConnectionType.STRAIGHT))
                return provider.createConnection(device, dev, side, side.getOpposite(), ConnectionType.STRAIGHT);
        } while (false);

        return null;
    }

}