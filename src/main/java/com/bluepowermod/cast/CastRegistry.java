package com.bluepowermod.cast;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.bluepowermod.api.cast.ICast;
import com.bluepowermod.api.cast.ICastRegistry;
import com.bluepowermod.init.BPFluids;
import com.bluepowermod.init.BPItems;

import cpw.mods.fml.common.Loader;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class CastRegistry implements ICastRegistry {

    private static CastRegistry instance;

    public static ICastRegistry getInstance() {

        return instance != null ? instance : (instance = new CastRegistry());
    }

    private CastRegistry() {

    }

    private List<ICast> casts = new ArrayList<ICast>();
    private List<Entry<ICast, ItemStack>> templates = new ArrayList<Entry<ICast, ItemStack>>();
    private List<Entry<ICast, Entry<FluidStack, ItemStack>>> recipes = new ArrayList<Entry<ICast, Entry<FluidStack, ItemStack>>>();
    private List<Entry<Block, Integer>> heatSources = new ArrayList<Entry<Block, Integer>>();

    @Override
    public void registerCast(ICast cast) {

        if (cast == null)
            return;
        if (getCast(cast.getCastType()) != null)
            return;

        casts.add(cast);
    }

    @Override
    public void registerCastTemplate(ICast cast, ItemStack template) {

        if (cast == null)
            return;

        if (template == null)
            return;

        templates.add(new AbstractMap.SimpleEntry(cast, template));

    }

    @Override
    public void registerCastTemplate(Class<? extends ICast> castClass, ItemStack template) {

        ICast cast = getCast(castClass);

        registerCastTemplate(cast, template);
    }

    @Override
    public void registerCastTemplate(String castType, ItemStack template) {

        ICast cast = getCast(castType);

        registerCastTemplate(cast, template);
    }

    @Override
    public void registerCastRecipe(ICast cast, FluidStack input, ItemStack output) {

        if (input == null)
            return;
        if (input.getFluid() == null)
            return;
        if (input.amount <= 0)
            return;

        if (output == null)
            return;
        if (output.stackSize <= 0)
            return;

        Entry<FluidStack, ItemStack> recipe = new AbstractMap.SimpleEntry(input, output);
        recipes.add(new AbstractMap.SimpleEntry(cast, recipe));

    }

    @Override
    public void registerCastRecipe(Class<? extends ICast> castClass, FluidStack input, ItemStack output) {

        ICast cast = getCast(castClass);
        registerCastRecipe(cast, input, output);
    }

    @Override
    public void registerCastRecipe(String castType, FluidStack input, ItemStack output) {

        ICast cast = getCast(castType);
        registerCastRecipe(cast, input, output);
    }

    @Override
    public ICast getCast(String id) {

        for (ICast c : casts)
            if (c.getCastType().equals(id))
                return c;

        return null;
    }

    public ICast getCast(Class<? extends ICast> clazz) {

        for (ICast c : getRegisteredCasts())
            if (c.getClass() == clazz)
                return c;

        return null;
    }

    @Override
    public List<ICast> getRegisteredCasts() {

        return Collections.unmodifiableList(casts);
    }

    @Override
    public ICast getCreatedCast(ItemStack stack) {

        if (stack == null)
            return null;

        for (Entry<ICast, ItemStack> t : templates)
            if (ItemStack.areItemStackTagsEqual(t.getValue(), stack) && t.getValue().getItem() == stack.getItem()
                    && t.getValue().getItemDamage() == stack.getItemDamage())
                return t.getKey();

        return null;
    }

    @Override
    public ICast getCastFromStack(ItemStack stack) {

        if (stack == null)
            return null;
        if (stack.getTagCompound() == null)
            return null;

        return getCast(stack.getTagCompound().getString("type"));
    }

    @Override
    public List<Entry<FluidStack, ItemStack>> getRecipes(ICast cast) {

        List<Entry<FluidStack, ItemStack>> recipes = new ArrayList<Entry<FluidStack, ItemStack>>();

        for (Entry<ICast, Entry<FluidStack, ItemStack>> r : this.recipes)
            if (r.getKey() == cast)
                recipes.add(r.getValue());

        return recipes;
    }

    @Override
    public ItemStack getResult(ICast cast, Fluid fluid) {

        Entry<FluidStack, ItemStack> recipe = getRecipe(cast, fluid);
        if (recipe != null)
            return recipe.getValue();

        return null;
    }

    @Override
    public Entry<FluidStack, ItemStack> getRecipe(ICast cast, Fluid fluid) {

        for (Entry<ICast, Entry<FluidStack, ItemStack>> r : recipes)
            if (r.getKey() == cast && r.getValue().getKey().getFluid() == fluid)
                return r.getValue();

        return null;
    }

    @Override
    public void registerCookingHeatSource(Block block) {

        heatSources.add(new AbstractMap.SimpleEntry(block, -1));
    }

    @Override
    public void registerCookingHeatSource(Block block, int meta) {

        heatSources.add(new AbstractMap.SimpleEntry(block, meta));
    }

    @Override
    public boolean isCookingHeatSource(Block block, int meta) {

        for (Entry<Block, Integer> source : heatSources)
            if (source.getKey() == block && (source.getValue().intValue() == -1 || source.getValue().intValue() == meta))
                return true;
        if (block instanceof BlockFluidBase) {
            Fluid f = ((BlockFluidBase) block).getFluid();
            if (f.getTemperature() - 273 > 50) // More than 50ºC
                return true;
        }

        return false;
    }

    public static ICast ingotCast;

    public static void init() {

        ICastRegistry r = getInstance();

        // Register casts
        {
            // Ingot cast
            {
                ingotCast = new DefaultCast("ingot");
                r.registerCast(ingotCast);
                for (String ore : OreDictionary.getOreNames())
                    if (ore.startsWith("ingot"))
                        for (ItemStack is : OreDictionary.getOres(ore))
                            r.registerCastTemplate(ingotCast, is);
                r.registerCastTemplate(ingotCast, new ItemStack(Items.brick));
                r.registerCastTemplate(ingotCast, new ItemStack(Items.netherbrick));
            }
        }

        // Register recipes
        {
            // Ingot cast
            {
                r.registerCastRecipe(ingotCast, new FluidStack(BPFluids.molten_gold, BPFluids.INGOT_AMOUNT), new ItemStack(Items.gold_ingot));
                r.registerCastRecipe(ingotCast, new FluidStack(BPFluids.molten_iron, BPFluids.INGOT_AMOUNT), new ItemStack(Items.iron_ingot));
                r.registerCastRecipe(ingotCast, new FluidStack(BPFluids.molten_brass, BPFluids.INGOT_AMOUNT), new ItemStack(BPItems.brass_ingot));
                r.registerCastRecipe(ingotCast, new FluidStack(BPFluids.molten_blue_alloy, BPFluids.INGOT_AMOUNT), new ItemStack(
                        BPItems.blue_alloy_ingot));
            }

            // Plates (no cast)
            {
                r.registerCastRecipe((ICast) null, new FluidStack(BPFluids.molten_galvanized_iron, BPFluids.INGOT_AMOUNT * 3), new ItemStack(
                        BPItems.galvanized_iron_plate));
            }
        }

        // Register heat sources
        {
            r.registerCookingHeatSource(Blocks.lava);
            r.registerCookingHeatSource(Blocks.fire);
            r.registerCookingHeatSource(Blocks.torch);
            if (Loader.isModLoaded("Thaumcraft")) {
                r.registerCookingHeatSource((Block) Block.blockRegistry.getObject("Thaumcraft:blockAiry"), 1);
            }
        }
    }
}
