package com.bluepowermod.api.power;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

import javax.annotation.Nullable;

/**
 * @author MoreThanHidden
 */
public class CapabilityBlutricity {

    public static void register(){
        CapabilityManager.INSTANCE.register(IPowerBase.class, new DefaultBlutricityStorage<>(), () -> new BlutricityStorage(320));
    }

    private static class DefaultBlutricityStorage<T extends IPowerBase> implements Capability.IStorage<T> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<T> capability, T instance, EnumFacing side) {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setDouble("blutricity", instance.getVoltage());
            return nbt;
        }

        @Override
        public void readNBT(Capability<T> capability, T instance, EnumFacing side, NBTBase nbt) {
            NBTTagCompound tags = (NBTTagCompound) nbt;
            double energy = tags.getDouble("blutricity");
            instance.addEnergy(-(instance.getVoltage() - energy), false);
        }
    }

}
