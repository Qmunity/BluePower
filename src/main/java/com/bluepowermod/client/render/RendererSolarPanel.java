package com.bluepowermod.client.render;

import com.bluepowermod.block.machine.BlockSolarPanel;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import uk.co.qmunity.lib.vec.Vec3dCube;

/**
 * @author Koen Beckers (K4Unl)
 */
public class RendererSolarPanel implements ISimpleBlockRenderingHandler{


    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        double pixel = 1.0 / 16.0;
        BlockSolarPanel solarPanel = (BlockSolarPanel) block;
        Vec3dCube vector = new Vec3dCube(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

        // if (pass == 0) {
        Tessellator t = Tessellator.instance;
        t.addTranslation(x, y, z);
        IIcon iconBottom = solarPanel.getIcon(RendererBlockBase.EnumFaceType.BACK, false, false);
        IIcon iconTop = solarPanel.getIcon(RendererBlockBase.EnumFaceType.FRONT, false, false);
        IIcon iconSides = solarPanel.getIcon(RendererBlockBase.EnumFaceType.SIDE, false, false);

        // Bottom side
        t.setNormal(0, -1, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), iconBottom.getMinU(), iconBottom.getMaxV());
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), iconBottom.getMinU(), iconBottom.getMinV());
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), iconBottom.getMaxU(), iconBottom.getMinV());
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), iconBottom.getMaxU(), iconBottom.getMaxV());

        // Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), pixel * 4.0, vector.getMaxZ(), iconTop.getMinU(), iconTop.getMaxV());
        t.addVertexWithUV(vector.getMaxX(), pixel * 4.0, vector.getMaxZ(), iconTop.getMinU(), iconTop.getMinV());
        t.addVertexWithUV(vector.getMaxX(), pixel * 4.0, vector.getMinZ(), iconTop.getMaxU(), iconTop.getMinV());
        t.addVertexWithUV(vector.getMinX(), pixel * 4.0, vector.getMinZ(), iconTop.getMaxU(), iconTop.getMaxV());

        // Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), iconSides.getMinU(), iconSides.getMaxV());
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), iconSides.getMinU(), iconSides.getMinV());
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), iconSides.getMaxU(), iconSides.getMinV());
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), iconSides.getMaxU(), iconSides.getMaxV());

        // Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), iconSides.getMinU(), iconSides.getMaxV());
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), iconSides.getMinU(), iconSides.getMinV());
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), iconSides.getMaxU(), iconSides.getMinV());
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), iconSides.getMaxU(), iconSides.getMaxV());

        // Draw north side
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), iconSides.getMinU(), iconSides.getMaxV());
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), iconSides.getMinU(), iconSides.getMinV());
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), iconSides.getMaxU(), iconSides.getMinV());
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), iconSides.getMaxU(), iconSides.getMaxV());

        // Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), iconSides.getMaxU(), iconSides.getMaxV());
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), iconSides.getMinU(), iconSides.getMaxV());
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), iconSides.getMinU(), iconSides.getMinV());
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), iconSides.getMaxU(), iconSides.getMinV());


        t.addTranslation(-x, -y, -z);
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
