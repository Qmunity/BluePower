package com.bluepowermod.client.renderers;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;

import com.bluepowermod.api.vec.Vector3;
import com.bluepowermod.tileentities.BPTileMultipart;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;

public class RenderMultipart extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    public static int renderId;

    public static void init() {

        RenderMultipart inst = new RenderMultipart();
        renderId = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderId, inst);
        ClientRegistry.bindTileEntitySpecialRenderer(BPTileMultipart.class, inst);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float frame) {

        if (tile == null || !(tile instanceof BPTileMultipart))
            return;
        BPTileMultipart te = (BPTileMultipart) tile;

        te.renderDynamic(new Vector3(x, y, z), MinecraftForgeClient.getRenderPass(), frame);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {

    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile == null || !(tile instanceof BPTileMultipart))
            return false;
        BPTileMultipart te = (BPTileMultipart) tile;

        te.renderStatic(new Vector3(x, y, z), MinecraftForgeClient.getRenderPass());

        return true;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {

        return false;
    }

    @Override
    public int getRenderId() {

        return renderId;
    }

}
