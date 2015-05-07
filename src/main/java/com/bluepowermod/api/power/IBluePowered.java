package com.bluepowermod.api.power;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.connect.ConnectionType;

public interface IBluePowered extends IWorldLocation {

    /**
     * Returns whether or not the device passed as an argument can be connected to this device on the specified side. It also takes a ConnectionType,
     * which determines the type of connection to this device.
     */
    public boolean canConnectPower(ForgeDirection side, IBluePowered dev, ConnectionType type);

    /**
     * Returns whether or not this is a full face (if face devices should be able to connect to it)
     */
    public boolean isNormalFace(ForgeDirection side);

    /**
     * Returns an object that will handle this device's power (network connections/disconnections and connection cache). When showing a tooltip in
     * WAILA the requested side will be {@link ForgeDirection#UNKNOWN}. This value will also be used when debugging with the multimeter. Be sure to
     * handle it if you want WAILA and the multimeter to work properly.
     */
    public IPowerHandler getPowerHandler(ForgeDirection side);

    /**
     * Gets the power tier this device uses
     */
    public PowerTier getPowerTier();

}
