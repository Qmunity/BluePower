package com.bluepowermod.api.power;

import net.minecraft.nbt.INBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class CapabilityBlutricity {

    @CapabilityInject(IPowerBase.class)
    public static Capability<IPowerBase> BLUTRICITY_CAPABILITY = null;

    public static void register(){
        //TODO: Add factory
        CapabilityManager.INSTANCE.register(IPowerBase.class, new DefaultBlutricityStorage<>(), () -> {throw new UnsupportedOperationException();});
    }

    private static class DefaultBlutricityStorage<T extends IPowerBase> implements Capability.IStorage<T> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<T> capability, T instance, Direction direction) {
            CompoundNBT nbt = new CompoundNBT();
            nbt.putDouble("blutricity", instance.getVoltage());
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            CompoundNBT tags = (CompoundNBT) nbt;
            double energy = tags.getDouble("blutricity");
            instance.addEnergy(-(instance.getVoltage() - energy), false);
        }
    }

}
