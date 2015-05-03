package com.bluepowermod.init;

import net.minecraftforge.fluids.Fluid;

import com.bluepowermod.fluid.BPFluid;
import com.bluepowermod.helper.FluidHelper;

public class BPFluids {

    public static int INGOT_AMOUNT = 144;

    public static Fluid molten_iron;
    public static Fluid molten_gold;
    public static Fluid molten_brass;
    public static Fluid molten_galvanized_iron;

    public static Fluid molten_blue_alloy;

    public static Fluid molten_silicon;
    public static Fluid molten_red_silicon;
    public static Fluid molten_blue_silicon;

    public static void init() {

        molten_iron = FluidHelper.registerFluidIfNotExisting("iron.molten");
        if (molten_iron instanceof BPFluid)
            molten_iron.setLuminosity(12).setDensity(3000).setViscosity(6000).setTemperature(1300);
        molten_gold = FluidHelper.registerFluidIfNotExisting("gold.molten");
        if (molten_gold instanceof BPFluid)
            molten_gold.setLuminosity(12).setDensity(3000).setViscosity(6000).setTemperature(1300);
        molten_brass = FluidHelper.registerFluidIfNotExisting("brass.molten");
        if (molten_brass instanceof BPFluid)
            molten_brass.setLuminosity(12).setDensity(3000).setViscosity(6000).setTemperature(1300);
        molten_galvanized_iron = FluidHelper.registerFluidIfNotExisting("iron.galvanized.molten");
        if (molten_galvanized_iron instanceof BPFluid)
            molten_galvanized_iron.setLuminosity(12).setDensity(3000).setViscosity(6000).setTemperature(1300);

        molten_blue_alloy = FluidHelper.registerFluidIfNotExisting("bluealloy.molten");

        molten_silicon = FluidHelper.registerFluidIfNotExisting("silicon.molten");
        molten_red_silicon = FluidHelper.registerFluidIfNotExisting("silicon.red.molten");
        molten_blue_silicon = FluidHelper.registerFluidIfNotExisting("silicon.blue.molten");
    }

}