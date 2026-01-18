package com.bluepowermod.api.power;

import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.tier3.TileBlulectricAlloyFurnace;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author MoreThanHidden
 */
public class CapabilityBlutricity {

    public static BlockCapability<IPowerBase, @Nullable Direction> BLUTRICITY_CAPABILITY = BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath("bluepower", "blutricity"), IPowerBase.class);

    public static void register(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.BLULECTRIC_ALLOY_FURNACE.get(), (furnace, side) -> furnace.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.BLULECTRIC_FURNACE.get(), (furnace, side) -> furnace.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.BLULECTRIC_CABLE.get(), (cable, side) -> cable.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.BATTERY.get(), (battery, side) -> battery.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.SOLAR_PANEL.get(), (solar, side) -> solar.storage);
        event.registerBlockEntity(BLUTRICITY_CAPABILITY, BPBlockEntityType.ENGINE.get(), (engine, side) -> engine.storage);
    }

    public static void saveEnergy(IPowerBase instance, ValueOutput valueOutput) {
        valueOutput.putDouble("blutricity", instance.getEnergy());
    }

    public static void loadEnergy(IPowerBase instance, ValueInput input) {
        double energy = input.getDoubleOr("blutricity", 0);
        instance.addEnergy(-(instance.getEnergy() - energy), false);
    }

}
