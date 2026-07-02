package com.bluepowermod.client.render;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import java.util.List;
import java.util.function.Function;

public class GateModel implements IUnbakedGeometry<GateModel> {
    private final List<Pair<IGateCondition, UnbakedModel>> models;
    private final ResourceLocation particle;

    public GateModel(List<Pair<IGateCondition, UnbakedModel>> models, ResourceLocation particle) {
        this.models = models;
        this.particle = particle;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> function, ModelState modelState, ItemOverrides itemOverrides, ResourceLocation resourceLocation) {
        ImmutableList.Builder<Pair<IGateCondition, BakedModel>> builder = ImmutableList.builder();
        this.models.forEach((model) -> {
            builder.add(Pair.of(model.key(), model.value().bake(modelBaker, function, modelState, resourceLocation)));
        });
        return new GateBakedModel(builder.build(), function.apply(new Material(TextureAtlas.LOCATION_BLOCKS, particle)));
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
        for (Pair<IGateCondition, UnbakedModel> unbakedModel : models) {
            unbakedModel.value().resolveParents(modelGetter);
        }
    }
}
