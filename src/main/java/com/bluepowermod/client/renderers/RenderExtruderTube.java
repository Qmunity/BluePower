package com.bluepowermod.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import com.bluepowermod.blocks.machines.extruder.BlockExtruderHopper;
import com.bluepowermod.blocks.machines.extruder.BlockExtruderTube;
import com.bluepowermod.init.BPBlocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderExtruderTube implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        renderer.renderAllFaces = true;

        // Render tube core
        renderer.setRenderBounds(0.25, 0.25, 0.25, 0.75, 0.75, 0.75);
        renderer.renderStandardBlock(block, x, y, z);

        // Render sides
        {
            // West
            if (world.getBlock(x - 1, y, z) != null && world.getBlock(x - 1, y, z) instanceof BlockExtruderTube) {
                renderer.setRenderBounds(0, 0.25, 0.25, 0.25, 0.75, 0.75);
                renderer.renderStandardBlock(block, x, y, z);
            }
            // East
            if (world.getBlock(x + 1, y, z) != null && world.getBlock(x + 1, y, z) instanceof BlockExtruderTube) {
                renderer.setRenderBounds(0.75, 0.25, 0.25, 1, 0.75, 0.75);
                renderer.renderStandardBlock(block, x, y, z);
            }
            // North
            if (world.getBlock(x, y, z - 1) != null && world.getBlock(x, y, z - 1) instanceof BlockExtruderTube) {
                renderer.setRenderBounds(0.25, 0.25, 0, 0.75, 0.75, 0.25);
                renderer.renderStandardBlock(block, x, y, z);
            }
            // South
            if (world.getBlock(x, y, z + 1) != null && world.getBlock(x, y, z + 1) instanceof BlockExtruderTube) {
                renderer.setRenderBounds(0.25, 0.25, 0.75, 0.75, 0.75, 1);
                renderer.renderStandardBlock(block, x, y, z);
            }
        }

        if (block instanceof BlockExtruderHopper) {
            // Render hopper
            IIcon texture = BPBlocks.machine_frame.getIcon(0, 0);
            Tessellator tess = Tessellator.instance;

            tess.setNormal(0, 1, 0);
            tess.addVertexWithUV(0, 1, 0, texture.getInterpolatedU(0), texture.getInterpolatedV(0));
            tess.addVertexWithUV(0, 1, 1, texture.getInterpolatedU(0), texture.getInterpolatedV(16));
            tess.addVertexWithUV(1, 1, 1, texture.getInterpolatedU(16), texture.getInterpolatedV(16));
            tess.addVertexWithUV(1, 1, 0, texture.getInterpolatedU(16), texture.getInterpolatedV(0));
        }

        renderer.renderAllFaces = false;

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return true;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

}
