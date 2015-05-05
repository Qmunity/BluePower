package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;

import com.bluepowermod.init.BPBlocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderAlloyCrucible implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        RenderHelper rh = RenderHelper.instance;

        rh.fullReset();

        rh.setRenderCoords(world, x, y, z);

        rh.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), block.getIcon(world, x, y, z, 0), block.getIcon(world, x, y, z, 1),
                block.getIcon(world, x, y, z, 4), block.getIcon(world, x, y, z, 5), block.getIcon(world, x, y, z, 2),
                block.getIcon(world, x, y, z, 3));

        rh.setRenderFromInside(true);
        rh.renderBox(new Vec3dCube(2 / 16D, 4 / 16D, 2 / 16D, 1, 1, 1), block.getIcon(0, 0), null, block.getIcon(-1, 0), block.getIcon(-1, 0),
                block.getIcon(-1, 0), block.getIcon(-1, 0));

        rh.fullReset();

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

        GL11.glPushMatrix();
        {
            switch (type) {
            case ENTITY:
                GL11.glTranslated(-0.5, 0, -0.5);
                break;
            case EQUIPPED:
                break;
            case EQUIPPED_FIRST_PERSON:
                break;
            case FIRST_PERSON_MAP:
                break;
            case INVENTORY:
                GL11.glTranslated(0, -0.125, -1 / 32D);
                break;
            default:
                break;
            }

            Tessellator t = Tessellator.instance;
            RenderHelper rh = RenderHelper.instance;
            Block block = BPBlocks.alloycrucible;

            t.startDrawingQuads();
            {
                rh.fullReset();

                rh.renderBox(new Vec3dCube(0, 0, 0, 1, 1, 1), block.getIcon(0, 0), block.getIcon(1, 0), block.getIcon(4, 0), block.getIcon(5, 0),
                        block.getIcon(2, 0), block.getIcon(3, 0));

                rh.setRenderFromInside(true);
                rh.renderBox(new Vec3dCube(2 / 16D, 4 / 16D, 2 / 16D, 1, 1, 1), block.getIcon(0, 0), null, block.getIcon(-1, 0),
                        block.getIcon(-1, 0), block.getIcon(-1, 0), block.getIcon(-1, 0));

                rh.fullReset();
            }
            t.draw();
        }
        GL11.glPopMatrix();
    }

}
