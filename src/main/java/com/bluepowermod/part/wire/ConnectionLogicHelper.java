package com.bluepowermod.part.wire;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.MicroblockShape;
import uk.co.qmunity.lib.part.compat.OcclusionHelper;
import uk.co.qmunity.lib.vec.IWorldLocation;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.misc.IFace;

public class ConnectionLogicHelper<T, C> {

    public static interface IConnectableProvider<T, C> {

        public T getConnectableAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side);

        public IWorldLocation getLocation(T o);

        public C createConnection(T a, T b, ForgeDirection sideA, ForgeDirection sideB, ConnectionType type);

        public boolean canConnect(T from, T to, ForgeDirection side, ConnectionType type);

        public boolean isValidClosedCorner(T o);

        public boolean isValidOpenCorner(T o);

        public boolean isValidStraight(T o);

        public boolean isNormalFace(T o, ForgeDirection face);
    }

    private IConnectableProvider<T, C> provider;

    public ConnectionLogicHelper(IConnectableProvider<T, C> provider) {

        this.provider = provider;
    }

    public C getNeighbor(T device, ForgeDirection side) {

        ForgeDirection face = ForgeDirection.UNKNOWN;
        if (device instanceof IFace)
            face = ((IFace) device).getFace();

        // In same block
        do {
            Vec3i loc = new Vec3i(provider.getLocation(device));
            T dev = provider.getConnectableAt(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), side == face.getOpposite() ? ForgeDirection.UNKNOWN
                    : side, face == ForgeDirection.UNKNOWN ? side.getOpposite() : face);
            if (dev == null || dev == device || !provider.isValidClosedCorner(dev))
                break;

            ConnectionType type = (device instanceof IFace || dev instanceof IFace) && !(device instanceof IFace == dev instanceof IFace) ? ConnectionType.STRAIGHT
                    : ConnectionType.CLOSED_CORNER;
            if (provider.canConnect(device, dev, side, type) && provider.canConnect(dev, device, face, type)) {
                return provider.createConnection(device, dev, side, face, type);
            }
        } while (false);

        // On same block
        if (face != ForgeDirection.UNKNOWN) {
            do {
                Vec3i loc = new Vec3i(provider.getLocation(device)).add(face).add(side);
                T dev = provider.getConnectableAt(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), side.getOpposite(), face.getOpposite());
                if (dev == null || dev == device || !provider.isValidOpenCorner(dev))
                    break;

                Vec3i block = new Vec3i(provider.getLocation(device)).add(side);
                Block b = block.getBlock();
                // Full block check
                if (b.isNormalCube() || b == Blocks.redstone_block)
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
            Vec3i loc = new Vec3i(provider.getLocation(device)).add(side);
            T dev = provider.getConnectableAt(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), face, side.getOpposite());
            if (dev == null) {
                dev = provider.getConnectableAt(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), side.getOpposite(), side.getOpposite());
                if (dev == null && face == ForgeDirection.UNKNOWN && provider.isNormalFace(device, side)) {
                    for (ForgeDirection d : ForgeDirection.VALID_DIRECTIONS) {
                        if (d != side && d != side.getOpposite()) {
                            dev = provider.getConnectableAt(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ(), d, side.getOpposite());
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
