package com.bluepowermod.compat.nei;

import com.bluepowermod.references.Refs;
import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

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
