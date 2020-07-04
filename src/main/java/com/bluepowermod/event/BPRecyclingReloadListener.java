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
import net.minecraft.resources.IResourceManager;
import net.minecraft.resources.IResourceManagerReloadListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;

public class BPRecyclingReloadListener implements IResourceManagerReloadListener {
    private final MinecraftServer server;

    public BPRecyclingReloadListener(MinecraftServer server){
        this.server = server;
    }

    /**
     * Generates the Dynamic Recycling recipes on a reload.
     */
    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
            if (BPConfig.CONFIG.alloyFurnaceDatapackGenerator.get()) {
                RecipeManager recipeManager = server.getRecipeManager();
                AlloyFurnaceRegistry.getInstance().generateRecyclingRecipes(recipeManager);
                AlloyFurnaceRegistry.getInstance().generateRecipeDatapack(server);
            } else {
                //If disabled remove any generated recipes
                String path = server.getPath(FolderName.DATAPACKS).toString();
                DatapackUtils.clearBPAlloyFurnaceDatapack(path);
            }
    }
}
