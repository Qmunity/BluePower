package com.bluepowermod.api.wire.redstone;


import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * @author MoreThanHidden
 */
public class CapabilityRedstoneDevice {

    public static BlockCapability<IInsulatedRedstoneDevice, @Nullable Direction> INSULATED_CAPABILITY = BlockCapability.createSided(new ResourceLocation("bluepower", "insulated_redstone"), IInsulatedRedstoneDevice.class);
    public static BlockCapability<IRedstoneDevice, @Nullable Direction> UNINSULATED_CAPABILITY = BlockCapability.createSided(new ResourceLocation("bluepower", "redstone"), IRedstoneDevice.class);

    public static void register(RegisterCapabilitiesEvent event){
    }

}