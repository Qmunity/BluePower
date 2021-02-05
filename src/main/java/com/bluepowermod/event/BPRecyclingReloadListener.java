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
import com.bluepowermod.util.DatapackUtils;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.resources.DataPackRegistries;
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;

public class BPRecyclingReloadListener implements IResourceManagerReloadListener {
    private final DataPackRegistries registries;
    public static MinecraftServer server;

    public BPRecyclingReloadListener(DataPackRegistries registries){
        this.registries = registries;
    }

    /**
     * Generates the Dynamic Recycling recipes on a reload.
     */
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
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
