package com.bluepowermod.client.render.model;

import com.bluepowermod.reference.Refs;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

import java.util.Collection;
import java.util.Collections;

/**
 * @author MoreThanHidden
 */
public class ConnectedModel implements IModel {

        ResourceLocation location;

        public ConnectedModel(ResourceLocation location){
            this.location = location;
        }

        @Override
        public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
            return new ConnectedBakedModel(state, format, getIcons(location, bakedTextureGetter));
        }

        TextureAtlasSprite[] getIcons(ResourceLocation name, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter){
            TextureAtlasSprite[] icons = new TextureAtlasSprite[47];

            for (int i = 0; i < 47; i++)
                icons[i] = bakedTextureGetter.apply(new ResourceLocation(Refs.MODID + ":" + name.getResourcePath() + "/" + name.getResourcePath() + "_" + (i + 1)));
            return icons;
        }

        @Override
        public Collection<ResourceLocation> getDependencies() {
            return Collections.emptySet();
        }

        @Override
        public Collection<ResourceLocation> getTextures() {
            return ImmutableSet.of(location);
        }

        @Override
        public IModelState getDefaultState() {
            return TRSRTransformation.identity();
        }
}
