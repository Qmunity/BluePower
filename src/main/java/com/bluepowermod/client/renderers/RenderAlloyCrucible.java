package com.bluepowermod.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.tileentities.tier1.TileAlloyCrucible;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderAlloyCrucible implements ISimpleBlockRenderingHandler {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        TileAlloyCrucible te = (TileAlloyCrucible) world.getTileEntity(x, y, z);

        int thickness = 2;
        int depth = 9;

        renderer.setRenderAllFaces(true);

        // Base
        renderer.renderStandardBlock(block, x, y, z);
        renderer.renderFromInside = true;
        renderer.renderStandardBlock(block, x, y, z);
        renderer.renderFromInside = false;

        // Bottom
        renderer.setRenderBounds(thickness / 16D, 0, thickness / 16D, 1 - (thickness / 16D), 1 - (depth / 16D), 1 - (thickness / 16D));
        renderer.renderFaceYPos(block, x, y, z, block.getIcon(world, x, y, z, ForgeDirection.DOWN.ordinal()));

        // Walls
        {
            // East
            renderer.setRenderBounds(0, 1 - (depth / 16D), thickness / 16D, thickness / 16D, 1, 1 - (thickness / 16D));
            renderer.renderFaceXPos(block, x, y, z, block.getIcon(world, x, y, z, ForgeDirection.DOWN.ordinal()));
            // WEST
            renderer.setRenderBounds(1 - (thickness / 16D), 1 - (depth / 16D), thickness / 16D, 1, 1, 1 - (thickness / 16D));
            renderer.renderFaceXNeg(block, x, y, z, block.getIcon(world, x, y, z, ForgeDirection.DOWN.ordinal()));
            // SOUTH
            renderer.setRenderBounds(thickness / 16D, 1 - (depth / 16D), 0, 1 - (thickness / 16D), 1, thickness / 16D);
            renderer.renderFaceZPos(block, x, y, z, block.getIcon(world, x, y, z, ForgeDirection.DOWN.ordinal()));
            // NORTH
            renderer.setRenderBounds(thickness / 16D, 1 - (depth / 16D), 1 - (thickness / 16D), 1 - (thickness / 16D), 1, 1);
            renderer.renderFaceZNeg(block, x, y, z, block.getIcon(world, x, y, z, ForgeDirection.DOWN.ordinal()));
        }

        // Fluid
        if (te.getTank().getFluid() != null && te.getTank().getFluidAmount() > 0) {
            double percentage = Math.min(te.getTank().getFluidAmount() / (double) te.getTank().getCapacity(), 1);
            renderer.setRenderBounds(thickness / 16D, (16 - depth) / 16D, thickness / 16D, 1 - (thickness / 16D),
                    (((16 - (depth * (1 - percentage))) / 16D) * 0.9) + 0.05, 1 - (thickness / 16D));
            renderer.renderStandardBlock(te.getTank().getFluid().getFluid().getBlock(), x, y, z);
        }

        renderer.setRenderAllFaces(false);

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
