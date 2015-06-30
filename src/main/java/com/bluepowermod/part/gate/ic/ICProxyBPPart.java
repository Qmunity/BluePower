package com.bluepowermod.part.gate.ic;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.IPartUpdateListener;
import uk.co.qmunity.lib.tile.TileMultipart;

import com.bluepowermod.api.gate.ic.IIntegratedCircuitProxy;
import com.bluepowermod.api.misc.ICachedBlock;

public class ICProxyBPPart implements IIntegratedCircuitProxy {

    @Override
    public boolean canHandlePlacement(ItemStack stack) {

        return true;// stack.getItem() instanceof ItemBlock;// ItemPart;
    }

    @Override
    public boolean placeOnIC(World world, MovingObjectPosition hit, EntityPlayer player, ItemStack stack) {

        return stack.getItem().onItemUse(stack, player, world, hit.blockX, hit.blockY, hit.blockZ, hit.sideHit,
                (float) (hit.hitVec.xCoord - hit.blockX), (float) (hit.hitVec.yCoord - hit.blockY), (float) (hit.hitVec.zCoord - hit.blockZ));
    }

    @Override
    public boolean canHandle(ICachedBlock block) {

        return block.tile() instanceof TileMultipart;
    }

    @Override
    public void onBlockUpdate(ICachedBlock block) {

        TileMultipart te = (TileMultipart) block.tile();
        for (IPart p : te.getParts())
            if (p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborBlockChange();
    }

    @Override
    public void onTileUpdate(ICachedBlock block) {

        TileMultipart te = (TileMultipart) block.tile();
        for (IPart p : te.getParts())
            if (p instanceof IPartUpdateListener)
                ((IPartUpdateListener) p).onNeighborTileChange();
    }

    @Override
    public boolean onActivated(ICachedBlock block, MovingObjectPosition mop, EntityPlayer player, ItemStack item) {

        return ((TileMultipart) block.tile()).onActivated(player);
    }

    @Override
    public void onClicked(ICachedBlock block, EntityPlayer player, ItemStack item) {

        ((TileMultipart) block.tile()).onClicked(player);
    }

}
