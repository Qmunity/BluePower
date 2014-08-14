package com.bluepowermod.init;

import net.minecraftforge.fluids.Fluid;

import com.bluepowermod.helper.FluidHelper;

public class BPFluids {

    public static int INGOT_AMOUNT = 144;

    public static Fluid molten_iron;
    public static Fluid molten_gold;
    public static Fluid molten_brass;
    public static Fluid molten_zinc;

    public static Fluid molten_blue_alloy;

    public static Fluid molten_silicon;
    public static Fluid molten_red_silicon;
    public static Fluid molten_blue_silicon;

    public static void init() {

        molten_iron = FluidHelper.registerFluidIfNotExisting("moltenIron");
        molten_gold = FluidHelper.registerFluidIfNotExisting("moltenGold");
        molten_brass = FluidHelper.registerFluidIfNotExisting("moltenBrass");
        molten_zinc = FluidHelper.registerFluidIfNotExisting("moltenZinc");

        molten_blue_alloy = FluidHelper.registerFluidIfNotExisting("moltenBlueAlloy");

        molten_silicon = FluidHelper.registerFluidIfNotExisting("moltenSilicon");
        molten_red_silicon = FluidHelper.registerFluidIfNotExisting("moltenSiliconRed");
        molten_blue_silicon = FluidHelper.registerFluidIfNotExisting("moltenSiliconBlue");
    }

}
