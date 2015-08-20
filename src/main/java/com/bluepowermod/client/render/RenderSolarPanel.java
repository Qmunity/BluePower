package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.IItemRenderer;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.transform.Translation;
import uk.co.qmunity.lib.vec.Vec3dCube;

import com.bluepowermod.block.machine.BlockSolarPanel;
import com.bluepowermod.init.BPBlocks;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * @author Koen Beckers (K4Unl)
 */
public class RenderSolarPanel implements ISimpleBlockRenderingHandler, IItemRenderer {

    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        RenderHelper rh = RenderHelper.instance;

        rh.fullReset();
        rh.setRenderCoords(world, x, y, z);

        BlockSolarPanel solarPanel = (BlockSolarPanel) block;
        IIcon iconBottom = solarPanel.getIcon(RendererBlockBase.EnumFaceType.BACK, false, false);
        IIcon iconTop = solarPanel.getIcon(RendererBlockBase.EnumFaceType.FRONT, false, false);
        IIcon iconSides = solarPanel.getIcon(RendererBlockBase.EnumFaceType.SIDE, false, false);

        rh.renderBox(new Vec3dCube(0, 0, 0, 1, 4 / 16D, 1), iconBottom, iconTop, iconSides, iconSides, iconSides, iconSides);

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

        RenderHelper rh = RenderHelper.instance;

        Tessellator.instance.startDrawingQuads();
        rh.fullReset();

        BlockSolarPanel solarPanel = (BlockSolarPanel) BPBlocks.solar_panel;
        IIcon iconBottom = solarPanel.getIcon(RendererBlockBase.EnumFaceType.BACK, false, false);
        IIcon iconTop = solarPanel.getIcon(RendererBlockBase.EnumFaceType.FRONT, false, false);
        IIcon iconSides = solarPanel.getIcon(RendererBlockBase.EnumFaceType.SIDE, false, false);

        rh.addTransformation(new Translation(0, 0.25D, 0));
        rh.renderBox(new Vec3dCube(0, 0, 0, 1, 4 / 16D, 1), iconBottom, iconTop, iconSides, iconSides, iconSides, iconSides);

        rh.fullReset();
        Tessellator.instance.draw();
    }
}
