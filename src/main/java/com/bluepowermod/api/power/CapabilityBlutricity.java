package com.bluepowermod.api.power;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
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
        CapabilityManager.INSTANCE.register(IPowerBase.class, new DefaultBlutricityStorage<>(), () -> {throw new UnsupportedOperationException();});
    }

    private static class DefaultBlutricityStorage<T extends IPowerBase> implements Capability.IStorage<T> {

        @Nullable
        @Override
        public Tag writeNBT(Capability<T> capability, T instance, Direction direction) {
            CompoundTag nbt = new CompoundTag();
            nbt.putDouble("blutricity", instance.getEnergy());
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, Direction side, INBT nbt) {
            CompoundTag tags = (CompoundTag) nbt;
            double energy = tags.getDouble("blutricity");
            instance.addEnergy(-(instance.getEnergy() - energy), false);
        }
    }

}
