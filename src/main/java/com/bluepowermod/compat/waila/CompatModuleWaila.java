/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.waila;

import com.bluepowermod.tile.TileMachineBase;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.tileentity.BlockEntity;
/**
 * @author amadornes
 *
 */
@WailaPlugin
public class CompatModuleWaila implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerBlockDataProvider(new WailaProviderMachines(), TileMachineBase.class);
        registrar.registerBlockDataProvider(new WailaProviderParts(), BlockEntity.class);
    }
}
