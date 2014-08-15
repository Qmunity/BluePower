package com.bluepowermod.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import com.bluepowermod.blocks.BlockBPFluid;
import com.bluepowermod.util.Refs;

import cpw.mods.fml.common.registry.GameRegistry;

public class BPFluid extends Fluid {

    private BlockBPFluid block;

    public BPFluid(String fluidName) {

        super(fluidName);

        FluidRegistry.registerFluid(this);

        block = new BlockBPFluid(this, Material.water, fluidName);

        GameRegistry.registerBlock(block, Refs.MODID + ".fluids." + fluidName);
    }

    @Override
    public IIcon getStillIcon() {

        return block.still;
    }

    @Override
    public IIcon getFlowingIcon() {

        return block.flowing;
    }

    @Override
    public String getUnlocalizedName() {

        return block.getUnlocalizedName() + ".name";
    }

}
