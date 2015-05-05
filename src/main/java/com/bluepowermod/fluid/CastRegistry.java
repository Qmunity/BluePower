package com.bluepowermod.fluid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.bluepowermod.api.fluid.ICast;
import com.bluepowermod.api.fluid.ICastRegistry;
import com.bluepowermod.api.misc.IHeatSource;
import com.bluepowermod.api.misc.SimpleHeatSource;
import com.bluepowermod.init.BPFluids;
import com.bluepowermod.init.BPItems;

import cpw.mods.fml.common.Loader;

public class CastRegistry implements ICastRegistry {

    private static final CastRegistry inst = new CastRegistry();

    public static CastRegistry getInstance() {

        return inst;
    }

    public static void init() {

        CastRegistry r = getInstance();

        // Register casts
        {
            // Ingot cast
            {
                r.registerCast(CastBase.INGOT);
                for (String ore : OreDictionary.getOreNames())
                    if (ore.startsWith("ingot"))
                        for (ItemStack is : OreDictionary.getOres(ore))
                            r.registerCastTemplate(CastBase.INGOT, is);
                r.registerCastTemplate(CastBase.INGOT, new ItemStack(Items.brick));
                r.registerCastTemplate(CastBase.INGOT, new ItemStack(Items.netherbrick));
            }
            // Nugget cast
            {
                r.registerCast(CastBase.NUGGET);
                for (String ore : OreDictionary.getOreNames())
                    if (ore.startsWith("nugget"))
                        for (ItemStack is : OreDictionary.getOres(ore))
                            r.registerCastTemplate(CastBase.NUGGET, is);
            }
            // Plate cast
            {
                r.recipes.put(null, new ArrayList<Pair<FluidStack, ItemStack>>());
            }
        }

        // Register recipes
        {
            // Ingot cast
            {
                r.registerCastRecipe(CastBase.INGOT, new FluidStack(BPFluids.molten_gold, BPFluids.INGOT_AMOUNT), new ItemStack(Items.gold_ingot));
                r.registerCastRecipe(CastBase.INGOT, new FluidStack(BPFluids.molten_iron, BPFluids.INGOT_AMOUNT), new ItemStack(Items.iron_ingot));
                r.registerCastRecipe(CastBase.INGOT, new FluidStack(BPFluids.molten_brass, BPFluids.INGOT_AMOUNT), new ItemStack(BPItems.brass_ingot));
                r.registerCastRecipe(CastBase.INGOT, new FluidStack(BPFluids.molten_blue_alloy, BPFluids.INGOT_AMOUNT), new ItemStack(
                        BPItems.blue_alloy_ingot));
            }

            // Plates (no cast)
            {
                r.registerCastRecipe((ICast) null, new FluidStack(BPFluids.molten_galvanized_iron, BPFluids.INGOT_AMOUNT * 3), new ItemStack(
                        BPItems.zincplate));
            }
        }

        // Register heat sources
        {
            r.registerHeatSource(new SimpleHeatSource(Blocks.lava, 1));
            r.registerHeatSource(new SimpleHeatSource(Blocks.lava, 0.5));
            r.registerHeatSource(new SimpleHeatSource(Blocks.lava, 0.05));
            if (Loader.isModLoaded("Thaumcraft")) {
                r.registerHeatSource(new SimpleHeatSource((Block) Block.blockRegistry.getObject("Thaumcraft:blockAiry"), 1, 1));
            }
        }
    }

    private Map<ICast, List<ItemStack>> casts = new HashMap<ICast, List<ItemStack>>();
    private Map<ICast, List<Pair<FluidStack, ItemStack>>> recipes = new HashMap<ICast, List<Pair<FluidStack, ItemStack>>>();
    private List<IHeatSource> heatSources = new ArrayList<IHeatSource>();

    @Override
    public void registerCast(ICast cast) {

        if (cast == null)
            throw new NullPointerException("Attempted to register a null cast!");
        if (casts.containsKey(cast))
            throw new IllegalStateException("Attempted to register a cast that was already registered!");

        casts.put(cast, new ArrayList<ItemStack>());
        recipes.put(cast, new ArrayList<Pair<FluidStack, ItemStack>>());
    }

    @Override
    public void registerCastTemplate(ICast cast, ItemStack template) {

        if (!casts.containsKey(cast))
            throw new IllegalStateException("Attempted to register a template for a cast that's not registered!");
        if (template == null)
            throw new NullPointerException("Attempted to add a null cast template!");
        if (casts.get(cast).contains(template))
            throw new IllegalStateException("Attempted to add an already registered cast template!");

        casts.get(cast).add(template);
    }

    @Override
    public void registerCastTemplate(String castType, ItemStack template) {

        ICast cast = getCast(castType);
        if (cast == null)
            throw new IllegalStateException("Attempted to register a template for a cast that's not registered!");
        if (template == null)
            throw new NullPointerException("Attempted to add a null cast template!");
        if (casts.get(cast).contains(template))
            throw new IllegalStateException("Attempted to add an already registered cast template!");

        casts.get(cast).add(template);
    }

    @Override
    public void registerCastRecipe(ICast cast, FluidStack input, ItemStack output) {

        if (input == null)
            throw new NullPointerException("Attempted to add a recipe with a null input!");
        if (output == null)
            throw new NullPointerException("Attempted to add a recipe with a null output!");

        recipes.get(cast).add(new ImmutablePair<FluidStack, ItemStack>(input, output));
    }

    @Override
    public void registerCastRecipe(String castType, FluidStack input, ItemStack output) {

        ICast cast = getCast(castType);
        if (cast == null)
            throw new IllegalStateException("Attempted to register a recipe for a cast that's not registered!");

    }

    @Override
    public ICast getCast(String id) {

        for (ICast cast : casts.keySet())
            if (cast.getType().equals(id))
                return cast;

        return null;
    }

    @Override
    public ICast getCreatedCast(ItemStack template) {

        for (Entry<ICast, List<ItemStack>> e : casts.entrySet())
            for (ItemStack is : e.getValue())
                if (ItemStack.areItemStacksEqual(is, template) && ItemStack.areItemStackTagsEqual(is, template))
                    return e.getKey();

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
    public List<ICast> getRegisteredCasts() {

        return new ArrayList<ICast>(casts.keySet());
    }

    @Override
    public List<Pair<FluidStack, ItemStack>> getRecipes(ICast cast) {

        return recipes.get(cast);
    }

    @Override
    public List<ItemStack> getTemplates(ICast cast) {

        if (!casts.containsKey(cast))
            throw new IllegalStateException("Attempted to get the templates for a cast that's not registered!");

        return casts.get(cast);
    }

    @Override
    public Pair<FluidStack, ItemStack> getRecipe(ICast cast, Fluid fluid) {

        if (!casts.containsKey(cast))
            throw new IllegalStateException("Attempted to get a recipe for a cast that's not registered!");

        for (Pair<FluidStack, ItemStack> p : getRecipes(cast))
            if (p.getKey().getFluid().equals(fluid))
                return p;

        return null;
    }

    @Override
    public void registerHeatSource(IHeatSource heatSource) {

        if (heatSource == null)
            throw new NullPointerException("Attempted to register a null heat source!");
        if (heatSources.contains(heatSource))
            throw new IllegalStateException("Attempted to register a heat source that was already registered!");

        heatSources.add(heatSource);
    }

    @Override
    public double getProducedHeat(IBlockAccess world, int x, int y, int z) {

        double h = 0;

        for (IHeatSource s : heatSources)
            h = Math.max(h, s.getProducedHeat(world, x, y, z));
        Block b = world.getBlock(x, y, z);
        if (b instanceof IHeatSource)
            h = Math.max(h, ((IHeatSource) b).getProducedHeat(world, x, y, z));
        TileEntity te = world.getTileEntity(x, y, z);
        if (te != null && te instanceof IHeatSource)
            h = Math.max(h, ((IHeatSource) te).getProducedHeat(world, x, y, z));

        return h;
    }

}
