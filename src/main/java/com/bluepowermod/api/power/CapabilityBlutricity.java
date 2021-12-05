package com.bluepowermod.api.power;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;

/**
 * @author MoreThanHidden
 */
public class CapabilityBlutricity {

    public static Capability<IPowerBase> BLUTRICITY_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});

    public static void register(RegisterCapabilitiesEvent event)
    {
        event.register(IPowerBase.class);
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
