package com.bluepowermod.client.render.model;

import com.bluepowermod.reference.Refs;
import com.google.common.base.Function;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author MoreThanHidden
 */
public class PartModel implements IModel {

        ResourceLocation location;

        public PartModel(ResourceLocation location){
            this.location = location;
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            return new ConnectedBakedModel(state, format, bakedTextureGetter, getTextures());
        }

        @Override
        public Collection<ResourceLocation> getDependencies() {
            return Collections.emptySet();
        }

        @Override
        public Collection<ResourceLocation> getTextures() {
            Collection<ResourceLocation> icons = new ArrayList<ResourceLocation>();

            for (int i = 0; i < 47; i++)
                icons.add(new ResourceLocation(Refs.MODID + ":" + "blocks/" + location.getResourcePath() + "/" + location.getResourcePath() + "_" + (i + 1)));
            return icons;


        }

        @Override
        public IModelState getDefaultState() {
            return TRSRTransformation.identity();
        }
}
