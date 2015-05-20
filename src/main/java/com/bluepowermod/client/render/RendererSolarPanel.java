package com.bluepowermod.client.render;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import uk.co.qmunity.lib.client.render.RenderHelper;
import uk.co.qmunity.lib.vec.Vec3dCube;

import com.bluepowermod.block.machine.BlockSolarPanel;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

/**
 * @author Koen Beckers (K4Unl)
 */
public class RendererSolarPanel implements ISimpleBlockRenderingHandler {

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
}
