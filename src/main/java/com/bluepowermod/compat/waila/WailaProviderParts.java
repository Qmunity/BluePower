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
