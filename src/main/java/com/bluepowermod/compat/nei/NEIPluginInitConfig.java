package com.bluepowermod.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.bluepowermod.api.Refs;

/**
 * 
 * @author MineMaarten
 */

public class NEIPluginInitConfig implements IConfigureNEI {
    
    @Override
    public void loadConfig() {
    
        API.registerUsageHandler(new AlloyFurnaceHandler());
        API.registerRecipeHandler(new AlloyFurnaceHandler());
    }
    
    @Override
    public String getName() {
    
        return "BPNEIHandler";
    }
    
    @Override
    public String getVersion() {
    
        return Refs.fullVersionString();
    }
    
}
