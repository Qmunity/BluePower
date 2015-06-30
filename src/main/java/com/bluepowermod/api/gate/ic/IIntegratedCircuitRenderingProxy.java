package com.bluepowermod.api.gate.ic;

import uk.co.qmunity.lib.client.render.RenderHelper;

import com.bluepowermod.api.misc.ICachedBlock;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IIntegratedCircuitRenderingProxy extends IIntegratedCircuitProxy {

    @SideOnly(Side.CLIENT)
    public boolean canRender(ICachedBlock block);

    @SideOnly(Side.CLIENT)
    public boolean renderStatic(ICachedBlock block, RenderHelper helper, int pass);

    @SideOnly(Side.CLIENT)
    public void renderDynamic(ICachedBlock block, RenderHelper helper, int pass, float frame);

}
