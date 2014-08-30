package com.bluepowermod.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tileentities.TileCastPlate;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderCastPlate extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    private static RenderBlocks renderer = new RenderBlocks();

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        TileCastPlate te = (TileCastPlate) world.getTileEntity(x, y, z);

        renderer.setRenderAllFaces(true);
        renderer.setRenderFromInside(false);

        // Base
        renderer.setRenderBounds(0, 0, 0, 1, 1 / 16D, 1);
        renderer.renderStandardBlock(block, x, y, z);

        // North
        renderer.setRenderBounds(0, 1 / 16D, 0, 1, 2 / 16D, 2 / 16D);
        renderer.renderStandardBlock(block, x, y, z);

        // South
        renderer.setRenderBounds(0, 1 / 16D, 1 - (2 / 16D), 1, 2 / 16D, 1);
        renderer.renderStandardBlock(block, x, y, z);

        // West
        renderer.setRenderBounds(0, 1 / 16D, 2 / 16D, 2 / 16D, 2 / 16D, 1 - (2 / 16D));
        renderer.renderStandardBlock(block, x, y, z);

        // East
        renderer.setRenderBounds(1 - (2 / 16D), 1 / 16D, 2 / 16D, 1, 2 / 16D, 1 - (2 / 16D));
        renderer.renderStandardBlock(block, x, y, z);

        // Clay
        if (te.hasClay()) {
            renderer.setRenderBounds(2 / 16D, 1 / 16D, 2 / 16D, 1 - (2 / 16D), 2 / 16D - (1 / 64D), 1 - (2 / 16D));
            renderer.renderStandardBlock(Blocks.clay, x, y, z);
        }

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return false;
    }

    @Override
    public int getRenderId() {

        return RENDER_ID;
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float frame) {

        TileCastPlate te = (TileCastPlate) tile;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);

            // Template
            GL11.glPushMatrix();
            {
                ItemStack item = null;
                if (te.hasTemplate())
                    item = te.getTemplate();
                if (item != null) {

                    RenderHelper.enableStandardItemLighting();

                    GL11.glTranslated(0.5, 0.3, 0.5);
                    GL11.glScaled(1.5, 1.5, 1.5);
                    GL11.glTranslated(0, -0.128, -0.225);
                    GL11.glRotated(90, 1, 0, 0);

                    com.bluepowermod.client.renderers.RenderHelper.renderItem(item);

                    RenderHelper.disableStandardItemLighting();
                }
            }
            GL11.glPopMatrix();

            // Cast
            GL11.glPushMatrix();
            {
                ItemStack item = null;
                if (te.hasCast())
                    item = te.getCast();
                if (item != null) {

                    RenderHelper.enableStandardItemLighting();

                    GL11.glTranslated(0.5, 0.3, 0.5);
                    GL11.glScaled(1.5, 1.5, 1.5);
                    GL11.glTranslated(0, -0.128, -0.225);
                    GL11.glRotated(90, 1, 0, 0);

                    com.bluepowermod.client.renderers.RenderHelper.renderItem(item);

                    RenderHelper.disableStandardItemLighting();
                }
            }
            GL11.glPopMatrix();

            // Progress
            GL11.glPushMatrix();
            {
                MovingObjectPosition mop = Minecraft.getMinecraft().thePlayer.rayTrace(
                        Minecraft.getMinecraft().thePlayer.capabilities.isCreativeMode ? 5 : 4.5, 0);

                if (mop != null && mop.blockX == te.xCoord && mop.blockY == te.yCoord && mop.blockZ == te.zCoord && te.hasTemplate()
                        && (te.hasClay() || te.hasCast())) {
                    com.bluepowermod.client.renderers.RenderHelper.renderEntityName(((int) (te.getCookProgress() * 100)) + "%", 0.5, 0.75, 0.5);
                }
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();
    }

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

        Block block = BPBlocks.cast_plate;

        GL11.glPushMatrix();
        {

            Tessellator.instance.startDrawingQuads();

            renderer.blockAccess = Minecraft.getMinecraft().theWorld;
            renderer.setRenderAllFaces(true);

            // Base
            renderer.setRenderBounds(0, 0, 0, 1, 1 / 16D, 1);
            renderer.renderStandardBlock(block, 0, 0, 0);

            // North
            renderer.setRenderBounds(0, 1 / 16D, 0, 1, 2 / 16D, 2 / 16D);
            renderer.renderStandardBlock(block, 0, 0, 0);

            // South
            renderer.setRenderBounds(0, 1 / 16D, 1 - (2 / 16D), 1, 2 / 16D, 1);
            renderer.renderStandardBlock(block, 0, 0, 0);

            // West
            renderer.setRenderBounds(0, 1 / 16D, 2 / 16D, 2 / 16D, 2 / 16D, 1 - (2 / 16D));
            renderer.renderStandardBlock(block, 0, 0, 0);

            // East
            renderer.setRenderBounds(1 - (2 / 16D), 1 / 16D, 2 / 16D, 1, 2 / 16D, 1 - (2 / 16D));
            renderer.renderStandardBlock(block, 0, 0, 0);

            Tessellator.instance.draw();
        }
        GL11.glPopMatrix();
    }
}
