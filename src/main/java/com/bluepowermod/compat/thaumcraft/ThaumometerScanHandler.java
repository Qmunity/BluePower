package com.bluepowermod.compat.thaumcraft;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import thaumcraft.api.research.IScanEventHandler;
import thaumcraft.api.research.ScanResult;

import com.bluepowermod.api.BPApi;
import com.bluepowermod.api.compat.IMultipartCompat;
import com.bluepowermod.api.part.BPPart;
import com.bluepowermod.api.vec.Vector3;

public class ThaumometerScanHandler implements IScanEventHandler {

    @Override
    public ScanResult scanPhenomena(ItemStack item, World world, EntityPlayer player) {

        MovingObjectPosition mop = player.rayTrace(player.capabilities.isCreativeMode ? 5 : 4.5, 0);

        if (mop == null)
            return null;

        IMultipartCompat compat = BPApi.getInstance().getMultipartCompat();

        if (!compat.isMultipart(world.getTileEntity(mop.blockX, mop.blockY, mop.blockZ)))
            return null;

        BPPart part = compat.getClickedPart(new Vector3(mop.blockX, mop.blockY, mop.blockZ, world),
                new Vector3(mop.hitVec, world).subtract(mop.blockX, mop.blockY, mop.blockZ), player, null);

        System.out.println(part);

        return null;
    }
}
