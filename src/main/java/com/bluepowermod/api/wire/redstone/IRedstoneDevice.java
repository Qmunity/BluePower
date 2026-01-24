package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.IWorldLocation;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapability;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface IRedstoneDevice extends IWorldLocation {

    /**
     * Returns whether the device passed as an argument can be connected to this device on the specified side. It also takes a ConnectionType,
     * which determines the type of connection to this device.
     */
    public boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type);

    /**
     * Returns a cache of all the connections of other devices with this one. Create an instance of this class by calling
     * {@link IRedstoneApi#createRedstoneConnectionCache(IRedstoneDevice)}
     */
    public IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache();

    /**
     * Gets the output of this device on the specified side.
     */
    public byte getRedstonePower(Direction side);

    /**
     * Sets the power level on the specified side to a set power level.
     */
    public void setRedstonePower(Direction side, byte power);

    /**
     * Notifies the device of a power change. (Usually called after propagation)
     */
    public void onRedstoneUpdate();

    /**
     * Returns whether this is a full face (if face devices should be able to connect to it)
     */
    public boolean isNormalFace(Direction side);


    static void storeValue(BlockCapability<IRedstoneDevice, @Nullable Direction> capability, IRedstoneDevice instance, Direction direction, ValueOutput output) {
        output.putByte("power", instance.getRedstonePower(direction));
    }

    static void loadValue(BlockCapability<IRedstoneDevice, @Nullable Direction> capability, IRedstoneDevice instance, Direction side, ValueInput input) {
        byte power = input.getByteOr("power", (byte) 0);
        instance.setRedstonePower(side, power);
    }

}
