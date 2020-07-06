package com.bluepowermod.client.render;

import com.bluepowermod.block.machine.BlockAlloyWire;
import com.bluepowermod.tile.tier1.TileWire;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class RenderWire extends TileEntityRenderer<TileWire> {

    RenderWire(TileEntityRendererDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(TileWire te, float v, MatrixStack matrixStack, IRenderTypeBuffer iRenderTypeBuffer, int combinedLightIn, int combinedOverlayIn) {
        BlockState stateWire = te.getBlockState();
        BlockAlloyWire bWire = (BlockAlloyWire) stateWire.getBlock();
        if(te.getWorld() == null) {return;}
        int color = bWire.getColor(te.getWorld(), te.getPos(), 2);
        boolean[] renderFaces = new boolean[] { true, true, true, true, true, true };
        int redMask = 0xED0000, greenMask = 0xED00, blueMask = 0xED;
        int r = (color & redMask) >> 16;
        int g = (color & greenMask) >> 8;
        int b = (color & blueMask);

        matrixStack.push();
        //Draw the base wire
        int light = stateWire.get(BlockAlloyWire.POWERED) ? 255 : combinedLightIn;
        stateWire.getShape(te.getWorld(), te.getPos()).toBoundingBoxList().forEach(box -> RenderHelper.drawColoredCube(box, iRenderTypeBuffer.getBuffer(RenderType.getSolid()), matrixStack, r, g, b, 255, light, renderFaces));
        matrixStack.pop();
    }
}
