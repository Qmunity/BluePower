package com.bluepowermod.part.gate.ic;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.client.render.RenderHelper;

import com.bluepowermod.api.gate.ic.IIntegratedCircuitHandler;
import com.bluepowermod.api.misc.ICachedBlock;

public class HandlerItemBlockTMP implements IIntegratedCircuitHandler {

    @Override
    public boolean canPlaceOnIntegratedCircuit(ItemStack stack) {

        return stack.getItem() instanceof ItemBlock;
    }

    @Override
    public boolean placeOnIC(World world, MovingObjectPosition hit, EntityPlayer player, ItemStack stack) {

        int y = (int) Math.floor(hit.hitVec.yCoord);

        if (world.getBlock(hit.blockX, y, hit.blockZ) != Blocks.air)
            return false;

        if (world.isRemote)
            return true;

        world.setBlock(hit.blockX, y, hit.blockZ, Block.getBlockFromItem(stack.getItem()));

        return true;
    }

    @Override
    public boolean canRender(ICachedBlock block) {

        return false;
    }

    @Override
    public boolean renderStatic(int x, int y, ICachedBlock block, RenderHelper helper, int pass) {

        return false;
    }

    @Override
    public void renderDynamic(ICachedBlock block, RenderHelper helper, int pass, float frame) {

    }

}
