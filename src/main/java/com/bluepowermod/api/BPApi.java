package com.bluepowermod.api;

import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.api.bluestone.IBluestoneApi;
import com.bluepowermod.api.cast.ICastRegistry;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.IPartRegistry;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;
import com.bluepowermod.api.tube.IPneumaticTube;

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

        public IMultipartCompat getMultipartCompat();

        public IPartRegistry getPartRegistry();

        public IPneumaticTube getPneumaticTube(TileEntity te);

        public IAlloyFurnaceRegistry getAlloyFurnaceRegistry();

        public IBluestoneApi getBluestoneApi();

        public ICastRegistry getCastRegistry();

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
