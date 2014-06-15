package net.quetzi.bluepower.client.renderers;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class RendererBlockBase implements ISimpleBlockRenderingHandler {

    public static final int   RENDER_ID         = RenderingRegistry.getNextAvailableRenderId();
    public static final Block FAKE_RENDER_BLOCK = new Block(Material.rock) {

        @Override
        public IIcon getIcon(int meta, int side) {

            return currentBlockToRender.getIcon(meta, side);
        }
    };

    public static Block currentBlockToRender = Blocks.stone;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

        currentBlockToRender = block;
        renderer.renderBlockAsItem(FAKE_RENDER_BLOCK, 1, 1.0F);
    }

    /*
    UV Deobfurscation derp:
    West = South
    East = North
    North = East
    South = West
     */
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        int meta = world.getBlockMetadata(x, y, z);
        switch (meta)
        {
            case 0:
                renderer.uvRotateSouth = 3;
                renderer.uvRotateNorth = 3;
                renderer.uvRotateEast = 3;
                renderer.uvRotateWest = 3;
                break;
            case 2:
                renderer.uvRotateSouth = 1;
                renderer.uvRotateNorth = 2;
                break;
            case 3:
                renderer.uvRotateSouth = 2;
                renderer.uvRotateNorth = 1;
                renderer.uvRotateTop = 3;
                renderer.uvRotateBottom = 3;
                break;
            case 4:
                renderer.uvRotateEast = 1;
                renderer.uvRotateWest = 2;
                renderer.uvRotateTop = 2;
                renderer.uvRotateBottom = 1;
                break;
            case 5:
                renderer.uvRotateEast = 2;
                renderer.uvRotateWest = 1;
                renderer.uvRotateTop = 1;
                renderer.uvRotateBottom = 2;
                break;
        }
        boolean ret = renderer.renderStandardBlock(block, x, y, z);
        renderer.uvRotateSouth = 0;
        renderer.uvRotateEast = 0;
        renderer.uvRotateWest = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;

        return ret;
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
