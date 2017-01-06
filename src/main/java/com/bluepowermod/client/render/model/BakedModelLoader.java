package com.bluepowermod.client.render.model;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.reference.Refs;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;

/**
 * @author MoreThanHidden
 */
public class BakedModelLoader implements ICustomModelLoader {

    public static final ConnectedModel MODEL = new ConnectedModel(BPBlocks.reinforced_sapphire_glass.getRegistryName());

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return modelLocation.getResourceDomain().equals(Refs.MODID) && Refs.REINFORCEDSAPPHIREGLASS_NAME.equals(modelLocation.getResourcePath());
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        return MODEL;
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {

    }



}


