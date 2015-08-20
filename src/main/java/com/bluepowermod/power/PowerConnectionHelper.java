package com.bluepowermod.power;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.power.IPowerBase;
import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.part.wire.ConnectionLogicHelper;
import com.bluepowermod.part.wire.ConnectionLogicHelper.IConnectableProvider;

public class PowerConnectionHelper {

    private static ConnectionLogicHelper<IPowerBase, PowerConnection> power = new ConnectionLogicHelper<IPowerBase, PowerConnection>(
            new IConnectableProvider<IPowerBase, PowerConnection>() {

                @Override
                public IPowerBase getConnectableAt(World world, int x, int y, int z, ForgeDirection face, ForgeDirection side) {

                    IPowered d = BPApi.getInstance().getPowerApi().getPoweredDeviceAt(world, x, y, z, face, side);
                    if (d == null)
                        return null;
                    return d.getPowerHandler(side);
                }

                @Override
                public IWorldLocation getLocation(IPowerBase o) {

                    return o.getDevice();
                }

                @Override
                public PowerConnection createConnection(IPowerBase a, IPowerBase b, ForgeDirection sideA, ForgeDirection sideB, ConnectionType type) {

                    return new PowerConnection(a, b, sideA, sideB, type);
                }

                @Override
                public boolean canConnect(IPowerBase from, IPowerBase to, ForgeDirection side, ConnectionType type) {

                    return from.getDevice().canConnectPower(side, to.getDevice(), type);
                }

                @Override
                public boolean isValidClosedCorner(IPowerBase o) {

                    return true;
                }

                @Override
                public boolean isValidOpenCorner(IPowerBase o) {

                    return true;
                }

                @Override
                public boolean isValidStraight(IPowerBase o) {

                    return true;
                }

                @Override
                public boolean isNormalFace(IPowerBase o, ForgeDirection face) {

                    return o.getDevice().isNormalFace(face);
                }
            });

    public static PowerConnection getNeighbor(IPowerBase device, ForgeDirection side) {

        return power.getNeighbor(device, side);
    }
}
