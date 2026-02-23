package com.bluepowermod.client.render;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class GateModelLoader implements IGeometryLoader<GateModel> {
    @Override
    public GateModel read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray models = jsonObject.getAsJsonArray("models");
        ResourceLocation particle = jsonObject.has("particle") ? new ResourceLocation(jsonObject.get("particle").getAsString()) : MissingTextureAtlasSprite.getLocation();
        ImmutableList.Builder<Pair<IGateCondition, UnbakedModel>> builder = ImmutableList.builder();
        for (JsonElement model : models) {
            String modelName = null;
            IGateCondition condition = IGateCondition.ALWAYS_TRUE;
            if (model instanceof JsonPrimitive primitive && primitive.isString()){
                modelName = primitive.getAsString();
            } else if (model instanceof JsonObject object) {
                modelName = object.getAsJsonPrimitive("model").getAsString();
                if (object.has("when")){
                    JsonObject when = object.getAsJsonObject("when");
                    Map<String, Boolean> conditions = when.asMap().entrySet().stream()
                            .filter(e -> e.getValue() instanceof JsonPrimitive primitive && primitive.isBoolean())
                            .collect(Collectors.toMap(Entry::getKey, e -> e.getValue().getAsBoolean()));
                    condition = m -> {
                        boolean[] bool = new boolean[1];
                        bool[0] = true;
                        conditions.forEach((s, o) -> {
                            bool[0] &= m.containsKey(s) && m.get(s).equals(o);
                        });
                        return bool[0];
                    };
                }
            }
            if (modelName != null){
                JsonObject modelObject = new JsonObject();
                modelObject.addProperty("parent", modelName);
                builder.add(Pair.of(condition, jsonDeserializationContext.deserialize(modelObject, BlockModel.class)));
            }
        }
        return new GateModel(builder.build(), particle);
    }
}
