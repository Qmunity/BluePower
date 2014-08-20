package com.bluepowermod.api.cast;

import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public interface ICastRegistry {

    public void registerCast(ICast cast);

    public void registerCastTemplate(ICast cast, ItemStack template);

    public void registerCastTemplate(Class<? extends ICast> cast, ItemStack template);

    public void registerCastTemplate(String castType, ItemStack template);

    public void registerCastRecipe(ICast cast, FluidStack input, ItemStack output);

    public void registerCastRecipe(Class<? extends ICast> cast, FluidStack input, ItemStack output);

    public void registerCastRecipe(String castType, FluidStack input, ItemStack output);

    public ICast getCast(String id);

    public List<ICast> getRegisteredCasts();

    public ICast getCreatedCast(ItemStack stack);

    public ICast getCastFromStack(ItemStack stack);

    public List<Entry<FluidStack, ItemStack>> getRecipes(ICast cast);

    public ItemStack getResult(ICast cast, Fluid fluid);

    public Entry<FluidStack, ItemStack> getRecipe(ICast cast, Fluid fluid);

    public void registerCookingHeatSource(Block block);

    public void registerCookingHeatSource(Block block, int meta);

    public boolean isCookingHeatSource(Block block, int meta);

}
