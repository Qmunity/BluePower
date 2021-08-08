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
        CapabilityManager.INSTANCE.register(IPowerBase.class);
    }

    public static Tag writeNBT(Capability<IPowerBase> capability, IPowerBase instance, Direction direction) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("blutricity", instance.getEnergy());
        return nbt;
    }

    public static void readNBT(Capability<IPowerBase> capability, IPowerBase instance, Direction side, Tag nbt) {
        CompoundTag tags = (CompoundTag) nbt;
        double energy = tags.getDouble("blutricity");
        instance.addEnergy(-(instance.getEnergy() - energy), false);
    }

}
