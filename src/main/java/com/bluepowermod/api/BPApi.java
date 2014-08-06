package com.bluepowermod.api;

import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.IPartRegistry;
import com.bluepowermod.api.recipe.IAlloyFurnaceRegistry;

@SuppressWarnings({ "rawtypes", "unchecked" })
/**
 * This is then main hub where you can interface with BluePower as a modder. Note that the 'instance' in this class will be filled in BluePower's preInit.
 * This means this class is save to use from the init phase.
 */
public class BPApi {
    
    private static IBPApi instance;
    
    public static IBPApi getInstance() {
    
        return instance;
    }
    
    public static interface IBPApi {
        
        public IMultipartCompat getMultipartCompat();
        
        public IPartRegistry getPartRegistry();
        
        public IAlloyFurnaceRegistry getAlloyFurnaceRegistry();
        
    }
    
    /**
     * For internal use only, don't call it.
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
