package net.quetzi.bluepower.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import net.quetzi.bluepower.references.Refs;

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
