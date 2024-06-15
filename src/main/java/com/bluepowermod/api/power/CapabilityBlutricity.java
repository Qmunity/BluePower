package com.bluepowermod.api.power;

import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.tier3.TileBlulectricAlloyFurnace;
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
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.BLULECTRIC_ALLOY_FURNACE.get(), (furnace, side) -> furnace.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.BLULECTRIC_FURNACE.get(), (furnace, side) -> furnace.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.BLULECTRIC_CABLE.get(), (cable, side) -> cable.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.BATTERY.get(), (battery, side) -> battery.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.SOLAR_PANEL.get(), (solar, side) -> solar.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.ENGINE.get(), (engine, side) -> engine.storage);
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
