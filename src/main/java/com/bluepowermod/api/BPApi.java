/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api;

import com.bluepowermod.api.bluepower.IPowerBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import com.bluepowermod.api.bluestone.IBluestoneApi;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.part.IPartRegistry;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;
import com.bluepowermod.api.tube.IPneumaticTube;

@SuppressWarnings({ "rawtypes", "unchecked" })
/**
 * This is then main hub where you can interface with BluePower as a modder. Note that the 'instance' in this class will be filled in BluePower's preInit.
 * This means this class is save to use from the init phase.
 * @author MineMaarten
 */
public class BPApi {
    
    private static IBPApi instance;
    
    public static IBPApi getInstance() {
    
        return instance;
    }
    
    public static interface IBPApi {
        
        public IMultipartCompat getMultipartCompat();
        
        public IPartRegistry getPartRegistry();
        
        public IPneumaticTube getPneumaticTube(TileEntity te);
        
        public IAlloyFurnaceRegistry getAlloyFurnaceRegistry();
        
        public IBluestoneApi getBluestoneApi();


        public IPowerBase getNewPowerHandler(BPPart part, float maxAmp);

        public IPowerBase getNewPowerHandler(TileEntity tileEntity, float maxAmp);
        
        /**
         * Should be called by an Block#onBlockAdded that implements ISilkyRemovable. It will get the TileEntity and load the tag "tileData" stored in
         * the supplied itemstack.
         * @param world
         * @param x
         * @param y
         * @param z
         * @param stack
         */
        public void loadSilkySettings(World world, int x, int y, int z, ItemStack stack);
        
        /**
         * Should be called by a BPPart when the part gets added. It will load the NBT from the given stack.
         * @param part
         * @param stack
         */
        public void loadSilkySettings(BPPart part, ItemStack stack);
    }
    
    /**
     * For internal use only, don't call it.
     * 
     * @param inst
     */
    public static void init(IBPApi inst) {
    
        if (instance == null) {
            instance = inst;
        } else {
            throw new IllegalStateException("This method should be called from BluePower only!");
        }
    }
}
