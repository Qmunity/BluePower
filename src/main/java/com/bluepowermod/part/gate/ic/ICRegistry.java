package com.bluepowermod.part.gate.ic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import uk.co.qmunity.lib.client.render.RenderHelper;

import com.bluepowermod.api.gate.ic.IIntegratedCircuitProxy;
import com.bluepowermod.api.gate.ic.IIntegratedCircuitRegistry;
import com.bluepowermod.api.gate.ic.IIntegratedCircuitRenderingProxy;
import com.bluepowermod.api.misc.ICachedBlock;

public class ICRegistry implements IIntegratedCircuitRegistry {

    public static final ICRegistry instance = new ICRegistry();

    private List<IIntegratedCircuitProxy> proxies = new ArrayList<IIntegratedCircuitProxy>();

    @Override
    public void registerProxy(IIntegratedCircuitProxy proxy) {

        if (proxy == null)
            throw new NullPointerException("Attempted to register a null IC proxy!");
        proxies.add(proxy);
    }

    @Override
    public boolean placeOnIC(World world, MovingObjectPosition hit, EntityPlayer player, ItemStack stack) {

        for (IIntegratedCircuitProxy p : proxies)
            if (p.canHandlePlacement(stack))
                return p.placeOnIC(world, hit, player, stack);
        return false;
    }

    @Override
    public void onBlockUpdate(ICachedBlock block) {

        for (IIntegratedCircuitProxy p : proxies)
            if (p.canHandle(block))
                p.onBlockUpdate(block);
    }

    @Override
    public void onTileUpdate(ICachedBlock block) {

        for (IIntegratedCircuitProxy p : proxies)
            if (p.canHandle(block))
                p.onTileUpdate(block);
    }

    @Override
    public boolean onActivated(ICachedBlock block, MovingObjectPosition mop, EntityPlayer player, ItemStack item) {

        for (IIntegratedCircuitProxy p : proxies)
            if (p.canHandle(block))
                return p.onActivated(block, mop, player, item);
        return false;
    }

    @Override
    public void onClicked(ICachedBlock block, EntityPlayer player, ItemStack item) {

        for (IIntegratedCircuitProxy p : proxies)
            if (p.canHandle(block))
                p.onClicked(block, player, item);
    }

    @Override
    public boolean renderStatic(ICachedBlock block, RenderHelper helper, int pass) {

        RenderBlocks rb = RenderBlocks.getInstance();
        IBlockAccess oldBA = rb.blockAccess;
        rb.blockAccess = block.getWorld();

        if (block.block().canRenderInPass(pass))
            rb.renderBlockByRenderType(block.block(), block.getX(), block.getY(), block.getZ());

        rb.blockAccess = oldBA;

        for (IIntegratedCircuitProxy p : proxies)
            if (p instanceof IIntegratedCircuitRenderingProxy)
                if (((IIntegratedCircuitRenderingProxy) p).canRender(block))
                    return ((IIntegratedCircuitRenderingProxy) p).renderStatic(block, helper, pass);
        return false;
    }

    @Override
    public void renderDynamic(ICachedBlock block, RenderHelper helper, int pass, float frame) {

        for (IIntegratedCircuitProxy p : proxies)
            if (p instanceof IIntegratedCircuitRenderingProxy)
                if (((IIntegratedCircuitRenderingProxy) p).canRender(block))
                    ((IIntegratedCircuitRenderingProxy) p).renderDynamic(block, helper, pass, frame);
    }
}
