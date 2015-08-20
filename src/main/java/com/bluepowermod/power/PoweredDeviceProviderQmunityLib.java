package com.bluepowermod.power;

import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.ITilePartHolder;
import uk.co.qmunity.lib.part.compat.MultipartCompatibility;

import com.bluepowermod.api.misc.IFace;
import com.bluepowermod.api.power.IPowered;
import com.bluepowermod.api.power.IPoweredDeviceProvider;

public class PoweredDeviceProviderQmunityLib implements IPoweredDeviceProvider {

    @Override
    public IPowered getPoweredDeviceAt(World world, int x, int y, int z, ForgeDirection side, ForgeDirection face) {

        ITilePartHolder holder = MultipartCompatibility.getPartHolder(world, x, y, z);
        if (holder != null) {
            for (IPart p : holder.getParts()) {
                if (p instanceof IPowered) {
                    if (p instanceof IFace) {
                        if (((IFace) p).getFace() == face && ((IPowered) p).getPowerHandler(side) != null)
                            return (IPowered) p;
                    } else {
                        if (face == ForgeDirection.UNKNOWN && ((IPowered) p).getPowerHandler(side) != null)
                            return (IPowered) p;
                    }
                }
            }
        }

        return null;
    }

}
