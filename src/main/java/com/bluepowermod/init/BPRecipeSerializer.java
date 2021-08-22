/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.init;

import com.bluepowermod.api.recipe.IAlloyFurnaceRecipe;
import com.bluepowermod.recipe.AlloyFurnaceRecyclingRecipe;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import com.bluepowermod.recipe.MicroblockRecipe;
import com.bluepowermod.reference.Refs;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import net.minecraft.world.item.crafting.RecipeSerializer;

/**
 * @author MoreThanHidden
 */
@ObjectHolder(Refs.MODID)
public class BPRecipeSerializer {

    @ObjectHolder("alloy_smelting")
    public static RecipeSerializer<IAlloyFurnaceRecipe> ALLOYSMELTING;

    @ObjectHolder("alloy_recycling")
    public static RecipeSerializer<IAlloyFurnaceRecipe> ALLOY_RECYCLING;

    @ObjectHolder("micro_block")
    public static RecipeSerializer<MicroblockRecipe> MICROBLOCK;

    @Mod.EventBusSubscriber(modid = Refs.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class Registration {
        @SubscribeEvent
        public static void onRecipeSerializerRegistry(final RegistryEvent.Register<RecipeSerializer<?>> event) {
            IForgeRegistry<RecipeSerializer<?>> registry = event.getRegistry();
            registry.register(new AlloyFurnaceRegistry.Serializer().setRegistryName(Refs.MODID + ":alloy_smelting"));
            registry.register(new AlloyFurnaceRecyclingRecipe.Serializer().setRegistryName(Refs.MODID + ":alloy_recycling"));
            registry.register(new MicroblockRecipe.Serializer().setRegistryName(Refs.MODID + ":micro_block"));
        }
    }

}
