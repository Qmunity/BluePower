/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import uk.co.qmunity.lib.part.IPart;

import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;
import com.bluepowermod.api.wire.redstone.IRedstoneApi;

import cpw.mods.fml.common.Loader;

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

        // public IPneumaticTube getPneumaticTube(TileEntity te);

        public IAlloyFurnaceRegistry getAlloyFurnaceRegistry();

        /**
         * Should be called by an Block#onBlockAdded that implements ISilkyRemovable. It will get the TileEntity and load the tag "tileData" stored in
         * the supplied itemstack.
         *
         * @param world
         * @param x
         * @param y
         * @param z
         * @param stack
         */
        public void loadSilkySettings(World world, int x, int y, int z, ItemStack stack);

        /**
         * Should be called by a BPPart when the part gets added. It will load the NBT from the given stack.
         *
         * @param part
         * @param stack
         */
        public void loadSilkySettings(IPart part, ItemStack stack);

        public IRedstoneApi getRedstoneApi();
    }

    /**
     * For internal use only, don't call it.
     *
     * @param inst
     */
    public static void init(IBPApi inst) {

        if (instance == null && Loader.instance().activeModContainer().getModId().equals("bluepower")) {
            instance = inst;
        } else {
            throw new IllegalStateException("This method should be called from BluePower only!");
        }
    }
}
