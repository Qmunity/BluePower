package com.bluepowermod.block;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import com.bluepowermod.reference.Refs;

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

    @Override
    public boolean canDisplace(IBlockAccess world, int x, int y, int z) {

        if (world.getBlock(x, y, z).getMaterial().isLiquid())
            return false;
        return super.canDisplace(world, x, y, z);
    }

    @Override
    public boolean displaceIfPossible(World world, int x, int y, int z) {

        if (world.getBlock(x, y, z).getMaterial().isLiquid())
            return false;
        return super.displaceIfPossible(world, x, y, z);
    }
}