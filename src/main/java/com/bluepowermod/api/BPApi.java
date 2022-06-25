/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api;

import com.bluepowermod.recipe.AlloyFurnaceRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.fml.ModList;

/**
 * This is then main hub where you can interface with BluePower as a modder. Note that the 'instance' in this class will be filled in BluePower's
 * preInit. This means this class is save to use from the init phase.
 *
 * @author MineMaarten
 */
public class BPApi {

    private static IBPApi instance;

    public static IBPApi getInstance() {

        return instance;
    }

    public static interface IBPApi {

        // public IPneumaticTube getPneumaticTube(BlockEntity te);

        public AlloyFurnaceRegistry getAlloyFurnaceRegistry();

        /**
         * Should be called by an Block#onBlockAdded that implements ISilkyRemovable. It will get the BlockEntity and load the tag "tileData" stored in
         * the supplied itemstack.
         *
         * @param world
         * @param pos
         * @param stack
         */
        public void loadSilkySettings(Level world, BlockPos pos, ItemStack stack);

    }

    /**
     * For internal use only, don't call it.
     *
     * @param inst
     */
    public static void init(IBPApi inst) {

        if (instance == null && ModList.get().isLoaded("bluepower")) {
            instance = inst;
        } else {
            throw new IllegalStateException("This method should be called from BluePower only!");
        }
    }
}
