package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.connect.ConnectionType;
import com.bluepowermod.api.connect.IConnectionCache;
import com.bluepowermod.api.misc.IWorldLocation;
import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public interface IRedstoneDevice extends IWorldLocation {

    /**
     * Returns whether the device passed as an argument can be connected to this device on the specified side. It also takes a ConnectionType,
     * which determines the type of connection to this device.
     */
    boolean canConnect(Direction side, IRedstoneDevice dev, ConnectionType type);

    /**
     * Returns a cache of all the connections of other devices with this one. Create an instance of this class by calling
     * {@link IRedstoneApi#createRedstoneConnectionCache(IRedstoneDevice)}
     */
    IConnectionCache<? extends IRedstoneDevice> getRedstoneConnectionCache();

    /**
     * Gets the output of this device on the specified side.
     */
    byte getRedstonePower(Direction side);


    /**
     * Gets the output of this device on the specified side, as a byte between 0 and 15.
     */
    byte getVanillaRedstonePower(Direction side);

    /**
     * Sets the power level on the specified side to a set power level.
     */
    void setRedstonePower(Direction side, byte power);

    void setInputSide(Direction side);

    /**
     * Notifies the device of a power change. (Usually called after propagation)
     */
    void onRedstoneUpdate();

    /**
     * Returns whether this is a full face (if face devices should be able to connect to it)
     */
    boolean isNormalFace(Direction side);


    static Tag writeNBT(Capability<IRedstoneDevice> capability, IRedstoneDevice instance, Direction direction) {
        CompoundTag nbt = new CompoundTag();
        nbt.putByte("power", instance.getRedstonePower(direction));
        return nbt;
    }

    static void readNBT(Capability<IRedstoneDevice> capability, IRedstoneDevice instance, Direction side, Tag nbt) {
        CompoundTag tags = (CompoundTag) nbt;
        byte power = tags.getByte("power");
        instance.setRedstonePower(side, power);
    }

}
