package com.bluepowermod.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;
import codechicken.nei.guihook.GuiContainerManager;

import com.bluepowermod.client.gui.GuiProjectTable;
import com.bluepowermod.util.Refs;

/**
 * 
 * @author MineMaarten
 */

public class NEIPluginInitConfig implements IConfigureNEI {
    
    @Override
    public void loadConfig() {
    
        AlloyFurnaceHandler handler = new AlloyFurnaceHandler();
        API.registerUsageHandler(handler);
        API.registerRecipeHandler(handler);
        GuiContainerManager.drawHandlers.add(handler);
        GuiContainerManager.tooltipHandlers.add(handler);
        
        API.registerGuiOverlayHandler(GuiProjectTable.class, new ProjectTableOverlayHandler(), "crafting");
        
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
