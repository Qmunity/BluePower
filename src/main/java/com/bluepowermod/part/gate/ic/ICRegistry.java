package com.bluepowermod.part.gate.ic;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import uk.co.qmunity.lib.client.render.RenderHelper;

import com.bluepowermod.api.gate.ic.IIntegratedCircuitHandler;
import com.bluepowermod.api.gate.ic.IIntegratedCircuitRegistry;
import com.bluepowermod.api.misc.ICachedBlock;

public class ICRegistry implements IIntegratedCircuitRegistry, IIntegratedCircuitHandler {

    public static final ICRegistry instance = new ICRegistry();

    private List<IIntegratedCircuitHandler> handlers = new ArrayList<IIntegratedCircuitHandler>();

    @Override
    public void registerHandler(IIntegratedCircuitHandler handler) {

        handlers.add(handler);
    }

    @Override
    public boolean canPlaceOnIntegratedCircuit(ItemStack stack) {

        for (IIntegratedCircuitHandler h : handlers)
            if (h.canPlaceOnIntegratedCircuit(stack))
                return true;

        return false;
    }

    @Override
    public boolean placeOnIC(World world, MovingObjectPosition hit, EntityPlayer player, ItemStack stack) {

        for (IIntegratedCircuitHandler h : handlers)
            if (h.canPlaceOnIntegratedCircuit(stack))
                return h.placeOnIC(world, hit, player, stack);

        return false;
    }

    @Override
    public boolean canRender(ICachedBlock block) {

        for (IIntegratedCircuitHandler h : handlers)
            if (h.canRender(block))
                return h.canRender(block);

        return false;
    }

    @Override
    public boolean renderStatic(int x, int y, ICachedBlock block, RenderHelper helper, int pass) {

        for (IIntegratedCircuitHandler h : handlers)
            if (h.canRender(block))
                return h.renderStatic(x, y, block, helper, pass);

        return false;
    }

    @Override
    public void renderDynamic(ICachedBlock block, RenderHelper helper, int pass, float frame) {

        for (IIntegratedCircuitHandler h : handlers) {
            if (h.canRender(block)) {
                h.renderDynamic(block, helper, pass, frame);
                return;
            }
        }
    }

    static {
        instance.registerHandler(new HandlerItemBlockTMP());
        instance.registerHandler(new HandlerBPPart());
    }
}
