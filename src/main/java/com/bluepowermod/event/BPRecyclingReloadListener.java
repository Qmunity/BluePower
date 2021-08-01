/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.event;

import com.bluepowermod.init.BPConfig;
import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import net.minecraft.server.ServerResources;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.item.crafting.RecipeManager;

public class BPRecyclingReloadListener implements ResourceManagerReloadListener {
    private final ServerResources registries;
    public static MinecraftServer server;

    public BPRecyclingReloadListener(ServerResources registries){
        this.registries = registries;
    }

    /**
     * Generates the Dynamic Recycling recipes on a reload.
     */
    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        onResourceManagerReload(registries.getRecipeManager());
    }

    public static void onResourceManagerReload(RecipeManager recipeManager){
        if(server != null) {
            if (BPConfig.CONFIG.alloyFurnaceDatapackGenerator.get()) {
                AlloyFurnaceRegistry.getInstance().generateRecyclingRecipes(recipeManager);
                AlloyFurnaceRegistry.getInstance().generateRecipeDatapack(server);
            }
        }
    }

}
