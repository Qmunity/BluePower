package com.bluepowermod.api.wire.redstone;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
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
    public static Capability<IInsulatedRedstoneDevice> UNINSULATED_CAPABILITY = null;

    public static void register(){
        CapabilityManager.INSTANCE.register(IInsulatedRedstoneDevice.class, new DefaultInsulatedDevice<>(), () -> {throw new UnsupportedOperationException();});
        CapabilityManager.INSTANCE.register(IRedstoneDevice.class, new DefaultRedstoneDevice<>(), () -> {throw new UnsupportedOperationException();});
    }

    private static class DefaultInsulatedDevice<T extends IInsulatedRedstoneDevice> implements Capability.IStorage<T> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction direction) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putByte("power", instance.getRedstonePower(direction));
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            CompoundNBT tags = (CompoundNBT) nbt;
            byte power = tags.getByte("power");
            instance.setRedstonePower(side, power);
        }
    }

    private static class DefaultRedstoneDevice<T extends IRedstoneDevice> implements Capability.IStorage<T> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction direction) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putByte("power", instance.getRedstonePower(direction));
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            CompoundNBT tags = (CompoundNBT) nbt;
            byte power = tags.getByte("power");
            instance.setRedstonePower(side, power);
        }
    }

}
