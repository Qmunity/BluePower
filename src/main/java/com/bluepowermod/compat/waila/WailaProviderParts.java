/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.compat.waila;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.item.ItemStack;
import uk.co.qmunity.lib.part.IPartWAILAProvider;
import uk.co.qmunity.lib.raytrace.QMovingObjectPosition;

public class WailaProviderParts implements IWailaDataProvider {

    @Override
    public List<String> getWailaHead(ItemStack item, List<String> l, IWailaDataAccessor data, IWailaConfigHandler cfg) {

        return l;
    }

    @Override
    public List<String> getWailaBody(ItemStack item, List<String> l, IWailaDataAccessor data, IWailaConfigHandler cfg) {

        if (data.getPosition() instanceof QMovingObjectPosition) {
            QMovingObjectPosition mop = (QMovingObjectPosition) data.getPosition();
            if (mop.getPart() != null && mop.getPart() instanceof IPartWAILAProvider)
                ((IPartWAILAProvider) mop.getPart()).addWAILABody(l);
        }

        // ITilePartHolder h = MultipartCompatibility.getPartHolder(data.getWorld(), data.getPosition().blockX, data.getPosition().blockY,
        // data.getPosition().blockZ);
        //
        // if (h == null)
        // return l;
        //
        // QMovingObjectPosition mop = h.rayTrace(RayTracer.instance().getStartVector(data.getPlayer()),
        // RayTracer.instance().getEndVector(data.getPlayer()));
        // if (mop != null && mop.getPart() != null && mop.getPart() instanceof IPartWAILAProvider) {
        // if (mop.hitVec.distanceTo(data.getPosition().hitVec) < 0.01)
        // ((IPartWAILAProvider) mop.getPart()).addWAILABody(l);
        // }

        return l;
    }

    @Override
    public List<String> getWailaTail(ItemStack item, List<String> l, IWailaDataAccessor data, IWailaConfigHandler cfg) {

        return l;
    }

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor data, IWailaConfigHandler cfg) {

        return null;
    }

}
