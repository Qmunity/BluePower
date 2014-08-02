/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.compat.waila;

import java.util.ArrayList;
import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;

import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.raytrace.RayTracer;

/**
 * @author amadornes
 * 
 */
public class WailaProviderPart implements IWailaDataProvider {

    private List<String> info = new ArrayList<String>();

    @Override
    public List<String> getWailaBody(ItemStack item, List<String> tip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        EntityPlayer p = accessor.getPlayer();
        MovingObjectPosition mop = p.rayTrace(p.capabilities.isCreativeMode ? 5 : 4.5, 0);
        if (mop == null)
            return tip;

        BPPart hovered = RayTracer.getSelectedPart(mop, p);
        if (hovered == null)
            return tip;

        hovered.addWailaInfo(info);
        tip.addAll(info);
        info.clear();

        return tip;
    }

    @Override
    public List<String> getWailaHead(ItemStack item, List<String> tip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        return tip;
    }

    @Override
    public List<String> getWailaTail(ItemStack item, List<String> tip, IWailaDataAccessor accessor, IWailaConfigHandler config) {

        return tip;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {

        return null;
    }
}
