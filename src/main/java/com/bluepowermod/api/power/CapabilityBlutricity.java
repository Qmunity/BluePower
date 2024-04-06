package com.bluepowermod.api.power;

import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author MoreThanHidden
 */
public class CapabilityBlutricity {

    public static BlockCapability<IPowerBase, @Nullable Direction> BLUTRICITY_CAPABILITY = BlockCapability.createSided(new ResourceLocation("bluepower", "blutricity"), IPowerBase.class);

    public static void register(RegisterCapabilitiesEvent event){
    }

    public static Tag writeNBT(BlockCapability<IPowerBase, @Nullable Direction> capability, IPowerBase instance, @Nullable Direction direction) {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("blutricity", instance.getEnergy());
        return nbt;
    }

    public static void readNBT(BlockCapability<IPowerBase, @Nullable Direction> capability, IPowerBase instance, @Nullable Direction side, Tag nbt) {
        CompoundTag tags = (CompoundTag) nbt;
        double energy = tags.getDouble("blutricity");
        instance.addEnergy(-(instance.getEnergy() - energy), false);
    }

}
