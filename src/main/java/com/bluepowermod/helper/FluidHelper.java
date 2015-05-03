package com.bluepowermod.helper;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.bluepowermod.fluid.BPFluid;

public class FluidHelper {

    public static Fluid getFluid(String id) {

        return FluidRegistry.getFluid(id);
    }

    public static boolean isFluidRegistered(String id) {

        return FluidRegistry.isFluidRegistered(id);
    }

    public static Fluid registerFluidIfNotExisting(String id) {

        if (isFluidRegistered(id))
            return getFluid(id);

        BPFluid f = new BPFluid(id);
        return f;
    }

    public static FluidStack createStack(String id, int amt) {

        return new FluidStack(getFluid(id), amt);
    }

}