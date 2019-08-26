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

import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class WailaProviderParts implements IServerDataProvider<TileEntity>{

    @Override
    public void appendServerData(CompoundNBT compoundNBT, ServerPlayerEntity serverPlayerEntity, World world, TileEntity tileEntity) {
        // ITilePartHolder h = MultipartCompatibility.getPartHolder(data.getWorld(), data.getPosition().blockX, data.getPosition().blockY,
        // data.getPosition().blockZ);
        //
        // if (h == null)
        // return l;
        //
        // QRayTraceResult mop = h.rayTrace(RayTracer.instance().getStartVector(data.getPlayer()),
        // RayTracer.instance().getEndVector(data.getPlayer()));
        // if (mop != null && mop.getPart() != null && mop.getPart() instanceof IPartWAILAProvider) {
        // if (mop.hitVec.distanceTo(data.getPosition().hitVec) < 0.01)
        // ((IPartWAILAProvider) mop.getPart()).addWAILABody(l);
        // }
    }

}
