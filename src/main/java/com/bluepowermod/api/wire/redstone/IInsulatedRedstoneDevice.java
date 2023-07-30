package com.bluepowermod.api.wire.redstone;

import net.minecraft.core.Direction;

import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public interface IInsulatedRedstoneDevice extends IRedstoneDevice {

    /**
     * Gets the insulation color on the specified side. This usually determines whether things can connect to it.
     */
    MinecraftColor getInsulationColor(Direction side);

    /**
     * Sets the insulation color only for initialisation.
     */
    void setInsulationColor(MinecraftColor color);

    @Nullable
    default Tag writeNBT(Capability<IInsulatedRedstoneDevice> capability, IInsulatedRedstoneDevice instance, Direction direction) {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("color", instance.getInsulationColor(direction).name());
        nbt.putByte("power", instance.getRedstonePower(direction));
        return nbt;
    }

    default void readNBT(Capability<IInsulatedRedstoneDevice> capability, IInsulatedRedstoneDevice instance, Direction side, Tag nbt) {
        CompoundTag tags = (CompoundTag) nbt;
        byte power = tags.getByte("power");
        instance.setInsulationColor(MinecraftColor.valueOf(tags.getString("color")));
        instance.setRedstonePower(side, power);
    }

}
