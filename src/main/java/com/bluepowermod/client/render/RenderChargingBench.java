package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.client.render.RenderUtils;
import uk.co.qmunity.lib.transform.Rotation;
import uk.co.qmunity.lib.vec.Vec3dCube;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.tier2.TileChargingBench;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * @author Amadornes
 */
public class RenderChargingBench extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        RenderHelper rh = RenderHelper.instance;

        rh.fullReset();
        rh.setRenderCoords(world, x, y, z);

        TileChargingBench tile = (TileChargingBench) world.getTileEntity(x, y, z);
        IIcon icon0 = block.getIcon(world, x, y, z, 0);
        IIcon icon1 = block.getIcon(world, x, y, z, 1);
        IIcon icon2 = block.getIcon(world, x, y, z, 2);
        IIcon icon3 = block.getIcon(world, x, y, z, 3);
        IIcon icon4 = block.getIcon(world, x, y, z, 4);
        IIcon icon5 = block.getIcon(world, x, y, z, 5);
        // IIcon wood = Blocks.planks.getIcon(0, 0);
        int rot = Direction.facingToDirection[tile.getFacingDirection().ordinal()];
        if (rot == 0 || rot == 2)
            rot = (rot + 2) % 4;

        double height = 10 / 16D;

        rh.renderBox(new Vec3dCube(0, 0, 0, 1, height, 1), icon0, icon1, icon4, icon5, icon2, icon3);

        rh.addTransformation(new Rotation(0, 90 * rot, 0));

        rh.renderBox(new Vec3dCube(0, height, 15 / 16D, 1, 1, 1), icon1);
        rh.renderBox(new Vec3dCube(0, height, 12 / 16D, 1 / 16D, 1, 15 / 16D), icon1);
        rh.renderBox(new Vec3dCube(15 / 16D, height, 12 / 16D, 1, 1, 15 / 16D), icon1);
        rh.renderBox(new Vec3dCube(0, height, 10 / 16D, 1 / 16D, 15 / 16D, 12 / 16D), icon1);
        rh.renderBox(new Vec3dCube(15 / 16D, height, 10 / 16D, 1, 15 / 16D, 12 / 16D), icon1);
        rh.renderBox(new Vec3dCube(0, height, 9 / 16D, 1 / 16D, 14 / 16D, 10 / 16D), icon1);
        rh.renderBox(new Vec3dCube(15 / 16D, height, 9 / 16D, 1, 14 / 16D, 10 / 16D), icon1);
        rh.renderBox(new Vec3dCube(0, height, 8 / 16D, 1 / 16D, 13 / 16D, 9 / 16D), icon1);
        rh.renderBox(new Vec3dCube(15 / 16D, height, 8 / 16D, 1, 13 / 16D, 9 / 16D), icon1);
        rh.renderBox(new Vec3dCube(0, height, 7 / 16D, 1 / 16D, 11 / 16D, 8 / 16D), icon1);
        rh.renderBox(new Vec3dCube(15 / 16D, height, 7 / 16D, 1, 11 / 16D, 8 / 16D), icon1);

        // rh.renderBox(new Vec3dCube(7 / 16D, height, 11 / 16D, 9 / 16D, 12 / 16D, 12 / 16D), wood);
        // rh.renderBox(new Vec3dCube(6 / 16D, 12 / 16D, 11 / 16D, 7 / 16D, 14 / 16D, 12 / 16D), wood);
        // rh.renderBox(new Vec3dCube(9 / 16D, 12 / 16D, 11 / 16D, 10 / 16D, 14 / 16D, 12 / 16D), wood);
        //
        // rh.renderBox(new Vec3dCube(3 / 16D, height, 5 / 16D, 5 / 16D, 12 / 16D, 6 / 16D), wood);
        // rh.renderBox(new Vec3dCube(2 / 16D, 12 / 16D, 5 / 16D, 3 / 16D, 14 / 16D, 6 / 16D), wood);
        // rh.renderBox(new Vec3dCube(5 / 16D, 12 / 16D, 5 / 16D, 6 / 16D, 14 / 16D, 6 / 16D), wood);
        //
        // rh.renderBox(new Vec3dCube(11 / 16D, height, 5 / 16D, 13 / 16D, 12 / 16D, 6 / 16D), wood);
        // rh.renderBox(new Vec3dCube(10 / 16D, 12 / 16D, 5 / 16D, 11 / 16D, 14 / 16D, 6 / 16D), wood);
        // rh.renderBox(new Vec3dCube(13 / 16D, 12 / 16D, 5 / 16D, 14 / 16D, 14 / 16D, 6 / 16D), wood);

        rh.fullReset();

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
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {

        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {

        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        Block block = BPBlocks.chargingBench;

        GL11.glPushMatrix();
        {
            if (type == ItemRenderType.ENTITY)
                GL11.glTranslated(0.5, 0, -0.5);
            else if (type == ItemRenderType.INVENTORY)
                GL11.glTranslated(0, -0.0625, 0);

            Tessellator.instance.startDrawingQuads();

            RenderHelper rh = RenderHelper.instance;

            rh.fullReset();

            IIcon icon0 = block.getIcon(0, 0);
            IIcon icon1 = block.getIcon(1, 0);
            IIcon icon2 = block.getIcon(2, 0);
            IIcon icon3 = block.getIcon(3, 0);
            IIcon icon4 = block.getIcon(4, 0);
            IIcon icon5 = block.getIcon(5, 0);
            // IIcon wood = Blocks.planks.getIcon(0, 0);
            double height = 10 / 16D;

            rh.renderBox(new Vec3dCube(0, 0, 0, 1, height, 1), icon0, icon1, icon4, icon5, icon2, icon3);

            rh.addTransformation(new Rotation(0, -90, 0));

            rh.renderBox(new Vec3dCube(0, height, 15 / 16D, 1, 1, 1), icon1);
            rh.renderBox(new Vec3dCube(0, height, 12 / 16D, 1 / 16D, 1, 15 / 16D), icon1);
            rh.renderBox(new Vec3dCube(15 / 16D, height, 12 / 16D, 1, 1, 15 / 16D), icon1);
            rh.renderBox(new Vec3dCube(0, height, 10 / 16D, 1 / 16D, 15 / 16D, 12 / 16D), icon1);
            rh.renderBox(new Vec3dCube(15 / 16D, height, 10 / 16D, 1, 15 / 16D, 12 / 16D), icon1);
            rh.renderBox(new Vec3dCube(0, height, 9 / 16D, 1 / 16D, 14 / 16D, 10 / 16D), icon1);
            rh.renderBox(new Vec3dCube(15 / 16D, height, 9 / 16D, 1, 14 / 16D, 10 / 16D), icon1);
            rh.renderBox(new Vec3dCube(0, height, 8 / 16D, 1 / 16D, 13 / 16D, 9 / 16D), icon1);
            rh.renderBox(new Vec3dCube(15 / 16D, height, 8 / 16D, 1, 13 / 16D, 9 / 16D), icon1);
            rh.renderBox(new Vec3dCube(0, height, 7 / 16D, 1 / 16D, 11 / 16D, 8 / 16D), icon1);
            rh.renderBox(new Vec3dCube(15 / 16D, height, 7 / 16D, 1, 11 / 16D, 8 / 16D), icon1);

            // rh.renderBox(new Vec3dCube(7 / 16D, height, 11 / 16D, 9 / 16D, 12 / 16D, 12 / 16D), wood);
            // rh.renderBox(new Vec3dCube(6 / 16D, 12 / 16D, 11 / 16D, 7 / 16D, 14 / 16D, 12 / 16D), wood);
            // rh.renderBox(new Vec3dCube(9 / 16D, 12 / 16D, 11 / 16D, 10 / 16D, 14 / 16D, 12 / 16D), wood);
            //
            // rh.renderBox(new Vec3dCube(3 / 16D, height, 5 / 16D, 5 / 16D, 12 / 16D, 6 / 16D), wood);
            // rh.renderBox(new Vec3dCube(2 / 16D, 12 / 16D, 5 / 16D, 3 / 16D, 14 / 16D, 6 / 16D), wood);
            // rh.renderBox(new Vec3dCube(5 / 16D, 12 / 16D, 5 / 16D, 6 / 16D, 14 / 16D, 6 / 16D), wood);
            //
            // rh.renderBox(new Vec3dCube(11 / 16D, height, 5 / 16D, 13 / 16D, 12 / 16D, 6 / 16D), wood);
            // rh.renderBox(new Vec3dCube(10 / 16D, 12 / 16D, 5 / 16D, 11 / 16D, 14 / 16D, 6 / 16D), wood);
            // rh.renderBox(new Vec3dCube(13 / 16D, 12 / 16D, 5 / 16D, 14 / 16D, 14 / 16D, 6 / 16D), wood);

            rh.fullReset();
            Tessellator.instance.draw();
        }
        GL11.glPopMatrix();
    }

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float frame) {

        TileChargingBench tile = (TileChargingBench) te;
        int rot = Direction.facingToDirection[tile.getFacingDirection().ordinal()];
        if (rot == 0 || rot == 2)
            rot = (rot + 2) % 4;
        rot--;

        ItemStack stack = null;
        for (int i = 0; i < tile.getSizeInventory(); i++) {
            ItemStack s = tile.getStackInSlot(i);
            if (s != null) {
                stack = s;
                break;
            }
        }

        if (stack == null)
            return;

        GL11.glPushMatrix();
        {
            GL11.glTranslated(x, y, z);
            // GL11.glTranslated(0.5, 0, 0.5);
            // GL11.glRotated(90 * rot, 0, 1, 0);
            // GL11.glTranslated(-0.5, 0, -0.5);
            // GL11.glTranslated(0.575, 0.7, 9.9 / 16D);
            // GL11.glRotated(90, 0, 1, 0);
            // GL11.glRotated(60, 1, 0, 0);
            // GL11.glRotated(-45, 0, 0, 1);
            // GL11.glScaled(0.75, 0.75, 0.75);

            if (stack.getItem() instanceof ItemBlock) {
                GL11.glTranslated(0.5, 10 / 16D, 0.5);
            } else {
                GL11.glTranslated(0.5, 0, 0.5);
                GL11.glRotated(90 + 90 * rot, 0, 1, 0);
                GL11.glTranslated(-0.5, 0, -0.5);
                GL11.glTranslated(8 / 16D, 10 / 16D, 4 / 16D);
                GL11.glRotated(90, 1, 0, 0);
            }

            RenderUtils.renderItem(stack);
        }
        GL11.glPopMatrix();
    }
}
