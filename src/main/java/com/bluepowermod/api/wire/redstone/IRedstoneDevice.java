package com.bluepowermod.api.wire.redstone;

import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.vec.IWorldLocation;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;

public interface IRedstoneDevice extends IWorldLocation {

    /**
     * Returns whether or not the device passed as an argument can be connected to this device on the specified side. It also takes a ConnectionType,
     * which determines the type of connection to this device.
     */
    public boolean canConnect(ForgeDirection side, IRedstoneDevice dev, ConnectionType type);

    /**
     * Returns a cache of all the connections of other devices with this one. Create an instance of this class by calling
     * {@link IRedstoneApi#createRedstoneConnectionCache(IRedstoneDevice)}
     */
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache();

    /**
     * Gets the output of this device on the specified side.
     */
    public byte getRedstonePower(ForgeDirection side);

    /**
     * Sets the power level on the specified side to a set power level.
     */
    public void setRedstonePower(ForgeDirection side, byte power);

    /**
     * Notifies the device of a power change. (Usually called after propagation)
     */
    public void onRedstoneUpdate();

    /**
     * Returns whether or not this is a full face (if face devices should be able to connect to it)
     */
    public boolean isNormalFace(ForgeDirection side);

}
