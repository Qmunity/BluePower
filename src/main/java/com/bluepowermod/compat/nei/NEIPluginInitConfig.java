/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.nei;

import codechicken.nei.api.API;
import codechicken.nei.api.IConfigureNEI;

import com.bluepowermod.client.gui.GuiProjectTable;
import com.bluepowermod.util.Refs;

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
        //  GuiContainerManager.addTooltipHandler(new DebugTooltipHandler());//Remove comments to show the GameData name of an item when hovering over it.
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
