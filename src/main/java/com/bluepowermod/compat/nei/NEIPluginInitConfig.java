package com.bluepowermod.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.bluepowermod.api.Refs;
import com.bluepowermod.client.gui.GuiProjectTable;

/**
 * 
 * @author MineMaarten
 */

public class NEIPluginInitConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {

        API.registerUsageHandler(new AlloyFurnaceHandler());
        API.registerRecipeHandler(new AlloyFurnaceHandler());
        API.registerGuiOverlayHandler(GuiProjectTable.class, new ProjectTableOverlayHandler(), "crafting");
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
