package com.bluepowermod.api.wire.redstone;

import com.bluepowermod.api.misc.MinecraftColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class CapabilityRedstoneDevice {

    @CapabilityInject(IInsulatedRedstoneDevice.class)
    public static Capability<IInsulatedRedstoneDevice> INSULATED_CAPABILITY = null;

    @CapabilityInject(IRedstoneDevice.class)
    public static Capability<IRedstoneDevice> UNINSULATED_CAPABILITY = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IInsulatedRedstoneDevice.class, new DefaultInsulatedDevice<>(), () -> {throw new UnsupportedOperationException();});
        CapabilityManager.INSTANCE.register(IRedstoneDevice.class, new DefaultRedstoneDevice<>(), () -> {throw new UnsupportedOperationException();});
    }

    private static class DefaultInsulatedDevice<T extends IInsulatedRedstoneDevice> implements Capability.IStorage<T> {

        @Nullable
        @Override
        public Tag writeNBT(Capability<T> capability, T instance, Direction direction) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString("color", instance.getInsulationColor(direction).name());
            nbt.putByte("power", instance.getRedstonePower(direction));
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            CompoundTag tags = (CompoundTag) nbt;
            byte power = tags.getByte("power");
            instance.setInsulationColor(MinecraftColor.valueOf(tags.getString("color")));
            instance.setRedstonePower(side, power);
        }
    }

    private static class DefaultRedstoneDevice<T extends IRedstoneDevice> implements Capability.IStorage<T> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction direction) {
            CompoundTag nbt = new CompoundTag();
            nbt.putByte("power", instance.getRedstonePower(direction));
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            CompoundTag tags = (CompoundTag) nbt;
            byte power = tags.getByte("power");
            instance.setRedstonePower(side, power);
        }
    }

}