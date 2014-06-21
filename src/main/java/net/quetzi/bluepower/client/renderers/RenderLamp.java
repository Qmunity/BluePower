package net.quetzi.bluepower.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.quetzi.bluepower.api.vec.Vector3Cube;
import net.quetzi.bluepower.blocks.machines.BlockLamp;
import net.quetzi.bluepower.tileentities.tier1.TileLamp;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderLamp implements ISimpleBlockRenderingHandler {
    
    public static final int RENDER_ID = RenderingRegistry.getNextAvailableRenderId();
    
    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    
        BlockLamp bLamp = (BlockLamp) block;
        int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
        int r = (bLamp.getColor() & redMask) >> 16;
        int g = (bLamp.getColor() & greenMask) >> 8;
        int b = (bLamp.getColor() & blueMask);
        Vector3Cube vector = new Vector3Cube(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        
        // if (pass == 0) {
        Tessellator t = Tessellator.instance;
        t.setColorOpaque(r, g, b);
        IIcon iconToUse;
        int power = 0;
        if (power == 0) {
            iconToUse = IconSupplier.lampOff;
        } else {
            iconToUse = IconSupplier.lampOn;
        }
        
        double minU = iconToUse.getMinU();
        double maxU = iconToUse.getMaxU();
        double minV = iconToUse.getMinV();
        double maxV = iconToUse.getMaxV();
        
        // Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
        
        // Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        // Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        
        // Draw north side
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        // Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        // }
    }
    
    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
    
        BlockLamp bLamp = (BlockLamp) block;
        int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
        int r = (bLamp.getColor() & redMask) >> 16;
        int g = (bLamp.getColor() & greenMask) >> 8;
        int b = (bLamp.getColor() & blueMask);
        
        Vector3Cube vector = new Vector3Cube(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
        
        // if (pass == 0) {
        Tessellator t = Tessellator.instance;
        t.addTranslation((float) x, (float) y, (float) z);
        t.setColorOpaque(r, g, b);
        IIcon iconToUse;
        int power = ((TileLamp) world.getTileEntity(x, y, z)).getPower();
        
        if (bLamp.isInverted()) {
            power = 15 - power;
        }
        
        if (power == 0) {
            iconToUse = IconSupplier.lampOn;
        } else {
            iconToUse = IconSupplier.lampOff;
        }
        
        double minU = iconToUse.getMinU();
        double maxU = iconToUse.getMaxU();
        double minV = iconToUse.getMinV();
        double maxV = iconToUse.getMaxV();
        
        // Top side
        t.setNormal(0, 1, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, maxV);
        
        // Draw west side:
        t.setNormal(-1, 0, 0);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        // Draw east side:
        t.setNormal(1, 0, 0);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        
        // Draw north side
        t.setNormal(0, 0, -1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMinZ(), minU, maxV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMinZ(), minU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMinZ(), maxU, minV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMinZ(), maxU, maxV);
        
        // Draw south side
        t.setNormal(0, 0, 1);
        t.addVertexWithUV(vector.getMinX(), vector.getMinY(), vector.getMaxZ(), minU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMinY(), vector.getMaxZ(), maxU, maxV);
        t.addVertexWithUV(vector.getMaxX(), vector.getMaxY(), vector.getMaxZ(), maxU, minV);
        t.addVertexWithUV(vector.getMinX(), vector.getMaxY(), vector.getMaxZ(), minU, minV);
        // }
        
        if (power > 0) {
            /*
             * GL11.glEnable(GL11.GL_BLEND);
             * GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
             * GL11.glDisable(GL11.GL_TEXTURE_2D);
             * GL11.glDisable(GL11.GL_LIGHTING);
             * // GL11.glDisable(GL11.GL_CULL_FACE);
             * GL11.glDepthMask(false);
             * GL11.glBegin(GL11.GL_QUADS);
             * RenderHelper.drawColoredCube(vector.clone().expand(0.8 / 16D), r / 256D, g / 256D, b / 256D, (power / 15D) * 0.625);
             * GL11.glEnd();
             * GL11.glDepthMask(true);
             * GL11.glEnable(GL11.GL_CULL_FACE);
             * GL11.glEnable(GL11.GL_LIGHTING);
             * GL11.glEnable(GL11.GL_TEXTURE_2D);
             * GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
             * GL11.glDisable(GL11.GL_BLEND);
             */
        }
        t.addTranslation((float) -x, (float) -y, (float) -z);
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
