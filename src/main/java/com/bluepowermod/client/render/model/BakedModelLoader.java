package com.bluepowermod.client.render.model;

import com.bluepowermod.block.worldgen.BlockStoneOreConnected;
import com.bluepowermod.init.BPBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MoreThanHidden
 */
public class BakedModelLoader implements ICustomModelLoader {
    public static final Map<ResourceLocation, IModel> MODELS = new HashMap<ResourceLocation, IModel>();

    public BakedModelLoader(){
        for (Block block :  BPBlocks.blockList){
            if (block instanceof BlockStoneOreConnected){
                MODELS.put(block.getRegistryName(), new ConnectedModel(block.getRegistryName()));
            }
        }
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return MODELS.containsKey(new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath()));
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return MODELS.get(new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath()));
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }



}


