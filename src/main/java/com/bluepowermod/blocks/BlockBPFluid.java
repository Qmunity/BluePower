package com.bluepowermod.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import com.bluepowermod.util.Refs;

public class BlockBPFluid extends BlockFluidClassic {

    public IIcon flowing;
    public IIcon still;
    private String name;

    public BlockBPFluid(Fluid fluid, Material material, String name) {

        super(fluid, material);
        this.name = name;

        setBlockName("fluid." + name);
    }

    @Override
    public void registerBlockIcons(IIconRegister reg) {

        flowing = reg.registerIcon(Refs.MODID + ":fluids/" + name.replace(".", "_") + "_flowing");
        blockIcon = still = reg.registerIcon(Refs.MODID + ":fluids/" + name.replace(".", "_") + "_still");
    }

    @Override
    public String getUnlocalizedName() {

        return "fluid." + name;
    }
}
