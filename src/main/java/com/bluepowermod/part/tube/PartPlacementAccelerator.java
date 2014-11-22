package com.bluepowermod.part.tube;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.qmunity.lib.misc.ForgeDirectionUtils;
import com.qmunity.lib.part.IPart;
import com.qmunity.lib.part.PartPlacementDefault;
import com.qmunity.lib.part.compat.IMultipartCompat;
import com.qmunity.lib.vec.Vec3i;

public class PartPlacementAccelerator extends PartPlacementDefault {

    private ForgeDirection rotation;

    public PartPlacementAccelerator(EntityPlayer player) {

        rotation = ForgeDirectionUtils.getDirectionFacing(player, true);
    }

    @Override
    public boolean placePart(IPart part, World world, Vec3i location, IMultipartCompat multipartSystem, boolean simulated) {

        if (part instanceof Accelerator)
            ((Accelerator) part).setRotation(rotation);

        return super.placePart(part, world, location, multipartSystem, simulated);
    }

}
