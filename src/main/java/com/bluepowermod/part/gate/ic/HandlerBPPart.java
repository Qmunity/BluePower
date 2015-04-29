package com.bluepowermod.part.gate.ic;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.part.IPart;
import uk.co.qmunity.lib.part.compat.PartUpdateManager;
import uk.co.qmunity.lib.tile.TileMultipart;
import uk.co.qmunity.lib.vec.Vec3i;

import com.bluepowermod.api.gate.ic.IIntegratedCircuitHandler;
import com.bluepowermod.api.misc.ICachedBlock;
import com.bluepowermod.item.ItemPart;

public class HandlerBPPart implements IIntegratedCircuitHandler {

    @Override
    public boolean canPlaceOnIntegratedCircuit(ItemStack stack) {

        return stack.getItem() instanceof ItemPart;// ItemMicroPart;
    }

    @Override
    public boolean placeOnIC(World world, MovingObjectPosition hit, EntityPlayer player, ItemStack stack) {

        if (world.isRemote)
            return true;

        PartUpdateManager.setUpdatesEnabled(false);
        boolean result = stack.getItem().onItemUse(stack, player, world, hit.blockX, hit.blockY, hit.blockZ, hit.sideHit,
                (float) hit.hitVec.xCoord - hit.blockX, (float) hit.hitVec.yCoord - hit.blockY, (float) hit.hitVec.zCoord - hit.blockZ);
        PartUpdateManager.setUpdatesEnabled(true);

        // System.out.println("HIT:");
        // System.out.println(" > " + hit.blockX + "(" + (hit.hitVec.xCoord - hit.blockX) + ") " + hit.blockZ + "("
        // + (hit.hitVec.zCoord - hit.blockZ) + ")");

        return result;
    }

    @Override
    public boolean canRender(ICachedBlock block) {

        return block.tile() != null && block.tile() instanceof TileMultipart;
    }

    @Override
    public boolean renderStatic(int x, int y, ICachedBlock block, RenderHelper helper, int pass) {

        for (IPart p : ((TileMultipart) block.tile()).getParts())
            p.renderStatic(new Vec3i(0, 0, 0), helper, RenderBlocks.getInstance(), pass);

        return true;
    }

    @Override
    public void renderDynamic(ICachedBlock block, RenderHelper helper, int pass, float frame) {

    }

}
