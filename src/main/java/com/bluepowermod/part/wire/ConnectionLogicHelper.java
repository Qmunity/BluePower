package com.bluepowermod.part.wire;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.misc.IFace;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.co.qmunity.lib.vec.IWorldLocation;

;

public class ConnectionLogicHelper<T extends IWorldLocation, C> {

    public static interface IConnectableProvider<T extends IWorldLocation, C> {

        public T getConnectableAt(World world, BlockPos pos, EnumFacing face, EnumFacing side);

        public C createConnection(T a, T b, EnumFacing sideA, EnumFacing sideB, ConnectionType type);

        public boolean canConnect(T from, T to, EnumFacing side, ConnectionType type);

        public boolean isValidClosedCorner(T o);

        public boolean isValidOpenCorner(T o);

        public boolean isValidStraight(T o);

        public boolean isNormalFace(T o, EnumFacing face);
    }

    private IConnectableProvider<T, C> provider;

    public ConnectionLogicHelper(IConnectableProvider<T, C> provider) {

        this.provider = provider;
    }

    public C getNeighbor(T device, EnumFacing side) {

        EnumFacing face = null;
        if (device instanceof IFace)
            face = ((IFace) device).getFace();

        // In same block
        do {
            T dev = provider.getConnectableAt(device.getWorld(), new BlockPos(device.getX(), device.getY(), device.getZ()), side == face.getOpposite() ? null
                    : side, face == null ? side.getOpposite() : face);
            if (dev == null || dev == device || !provider.isValidClosedCorner(dev))
                break;

            ConnectionType type = (device instanceof IFace || dev instanceof IFace) && !(device instanceof IFace == dev instanceof IFace) ? ConnectionType.STRAIGHT
                    : ConnectionType.CLOSED_CORNER;
            if (provider.canConnect(device, dev, side, type) && provider.canConnect(dev, device, face, type))
                return provider.createConnection(device, dev, side, face, type);
        } while (false);

        // On same block
        if (face != null) {
            do {
                BlockPos loc = new BlockPos(device.getX(), device.getY(), device.getZ()).offset(face).offset(side);
                T dev = provider.getConnectableAt(device.getWorld(), loc, side.getOpposite(), face.getOpposite());
                if (dev == null || dev == device || !provider.isValidOpenCorner(dev))
                    break;

                BlockPos block = new BlockPos(device.getX(), device.getY(), device.getZ()).offset(side);
                Block b = device.getWorld().getBlockState(block).getBlock();
                // Full block check
                if (b.isNormalCube(device.getWorld().getBlockState(block)) || b == Blocks.REDSTONE_BLOCK)
                    break;
                // Microblock check
                if (!OcclusionHelper.microblockOcclusionTest(block, MicroblockShape.EDGE, 2, face, side.getOpposite()))
                    break;

                if (provider.canConnect(device, dev, side, ConnectionType.OPEN_CORNER)
                        && provider.canConnect(dev, device, face.getOpposite(), ConnectionType.OPEN_CORNER))
                    return provider.createConnection(device, dev, side, face.getOpposite(), ConnectionType.OPEN_CORNER);
            } while (false);
        }

        // Straight connection
        do {
            BlockPos loc = new BlockPos(device.getX(), device.getY(), device.getZ()).offset(side);
            T dev = provider.getConnectableAt(device.getWorld(), loc, face, side.getOpposite());
            if (dev == null) {
                dev = provider.getConnectableAt(device.getWorld(), loc, side.getOpposite(), side.getOpposite());
                if (dev == null && face == null && provider.isNormalFace(device, side)) {
                    for (EnumFacing d : EnumFacing.VALUES) {
                        if (d != side && d != side.getOpposite()) {
                            dev = provider.getConnectableAt(device.getWorld(), loc, d, side.getOpposite());
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
