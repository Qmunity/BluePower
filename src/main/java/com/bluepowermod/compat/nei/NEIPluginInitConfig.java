package com.bluepowermod.compat.nei;

import codechicken.nei.api.IConfigureNEI;

import com.bluepowermod.util.Refs;

/**
 * 
 * @author MineMaarten
 */

public class NEIPluginInitConfig implements IConfigureNEI {

    @Override
    public void loadConfig() {

        // FIXME API.registerUsageHandler(new AlloyFurnaceHandler());
        // FIXME API.registerRecipeHandler(new AlloyFurnaceHandler());
        // FIXME API.registerGuiOverlayHandler(GuiProjectTable.class, new ProjectTableOverlayHandler(), "crafting");

        // GuiContainerManager.addTooltipHandler(new DebugTooltipHandler());//Remove comments to show the GameData name of an item when hovering over
        // it.
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
