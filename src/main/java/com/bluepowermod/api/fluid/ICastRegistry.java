package com.bluepowermod.api.fluid;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.bluepowermod.api.misc.IHeatSource;

public interface ICastRegistry {

    public void registerCast(ICast cast);

    public void registerCastTemplate(ICast cast, ItemStack template);

    public void registerCastTemplate(String castType, ItemStack template);

    public void registerCastRecipe(ICast cast, FluidStack input, ItemStack output);

    public void registerCastRecipe(String castType, FluidStack input, ItemStack output);

    public ICast getCast(String id);

    public ICast getCreatedCast(ItemStack template);

    public ICast getCastFromStack(ItemStack stack);

    public List<ICast> getRegisteredCasts();

    public List<Pair<FluidStack, ItemStack>> getRecipes(ICast cast);

    public List<ItemStack> getTemplates(ICast cast);

    public Pair<FluidStack, ItemStack> getRecipe(ICast cast, Fluid fluid);

    public void registerHeatSource(IHeatSource heatSource);

    public double getProducedHeat(IBlockAccess world, int x, int y, int z);
}
