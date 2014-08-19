package com.bluepowermod.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
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
    private final RenderItem customRenderItem;

    public RenderCastPlate() {

        customRenderItem = new RenderItem() {

            @Override
            public boolean shouldSpreadItems() {

                return false;
            }
        };

        customRenderItem.setRenderManager(RenderManager.instance);
    }

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
            ItemStack torender = null;
            if (te.hasTemplate())
                torender = te.getTemplate();
            if (torender != null) {

                RenderHelper.enableStandardItemLighting();

                EntityItem ghostEntityItem = new EntityItem(te.getWorldObj());
                ghostEntityItem.hoverStart = 0.0F;
                ghostEntityItem.setEntityItemStack(torender);

                GL11.glTranslatef((float) x, (float) y, (float) z);

                GL11.glTranslatef(0.5F, 0.3F, 0.5F);
                GL11.glColor3f(1F, 1F, 1F);

                GL11.glScaled(1.5, 1.5, 1.5);

                GL11.glTranslated(0, -0.128, -0.225);
                GL11.glRotated(90, 1, 0, 0);

                if (torender.getItem() instanceof ItemBlock) {
                    ItemBlock testItem = (ItemBlock) torender.getItem();
                    Block testBlock = testItem.field_150939_a;
                    if (RenderBlocks.renderItemIn3d(testBlock.getRenderType())) {
                        GL11.glScalef(1.2F, 1.2F, 1.2F);
                    }
                }

                customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);

                RenderHelper.disableStandardItemLighting();
            }
        }
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        {
            ItemStack torender = null;
            if (te.hasCast())
                torender = te.getCast();
            if (torender != null) {

                RenderHelper.enableStandardItemLighting();

                EntityItem ghostEntityItem = new EntityItem(te.getWorldObj());
                ghostEntityItem.hoverStart = 0.0F;
                ghostEntityItem.setEntityItemStack(torender);

                GL11.glTranslatef((float) x, (float) y, (float) z);

                GL11.glTranslatef(0.5F, 0.3F, 0.5F);
                GL11.glColor3f(1F, 1F, 1F);

                GL11.glScaled(1.5, 1.5, 1.5);

                GL11.glTranslated(0, -0.128, -0.225);
                GL11.glRotated(90, 1, 0, 0);

                if (torender.getItem() instanceof ItemBlock) {
                    ItemBlock testItem = (ItemBlock) torender.getItem();
                    Block testBlock = testItem.field_150939_a;
                    if (RenderBlocks.renderItemIn3d(testBlock.getRenderType())) {
                        GL11.glScalef(1.2F, 1.2F, 1.2F);
                    }
                }

                customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);

                RenderHelper.disableStandardItemLighting();
            }
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

        RenderHelper.enableStandardItemLighting();

        Block block = BPBlocks.cast_plate;

        NBTTagCompound tag = item.stackTagCompound;

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

            // Clay
            if (tag != null && tag.hasKey("hasClay") && tag.getBoolean("hasClay")) {
                renderer.setRenderBounds(2 / 16D, 1 / 16D, 2 / 16D, 1 - (2 / 16D), 2 / 16D - (1 / 64D), 1 - (2 / 16D));
                renderer.renderStandardBlock(Blocks.clay, 0, 0, 0);
            }
            Tessellator.instance.draw();

            GL11.glPushMatrix();
            {
                ItemStack torender = null;
                if (tag != null && tag.hasKey("inventory0"))
                    torender = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("inventory0"));
                if (torender != null) {

                    EntityItem ghostEntityItem = new EntityItem(Minecraft.getMinecraft().theWorld);
                    ghostEntityItem.hoverStart = 0.0F;
                    ghostEntityItem.setEntityItemStack(torender);

                    GL11.glTranslatef(0.5F, 0.3F, 0.5F);
                    GL11.glColor3f(1F, 1F, 1F);

                    GL11.glScaled(1.5, 1.5, 1.5);

                    GL11.glTranslated(0, -0.128, -0.225);
                    GL11.glRotated(90, 1, 0, 0);

                    if (torender.getItem() instanceof ItemBlock) {
                        ItemBlock testItem = (ItemBlock) torender.getItem();
                        Block testBlock = testItem.field_150939_a;
                        if (RenderBlocks.renderItemIn3d(testBlock.getRenderType())) {
                            GL11.glScalef(1.2F, 1.2F, 1.2F);
                        }
                    }

                    try {
                        customRenderItem.doRender(ghostEntityItem, 0, 0, 0, 0, 0);
                    } catch (Exception ex) {
                    }
                }
            }
            GL11.glPopMatrix();
        }
        GL11.glPopMatrix();

        RenderHelper.enableStandardItemLighting();
    }
}
