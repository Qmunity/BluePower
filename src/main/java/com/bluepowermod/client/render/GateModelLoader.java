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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;
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
                    Function<JsonPrimitive, Object> mappingFunction = j -> {
                        if (j.isBoolean()) return j.getAsBoolean();
                        if (j.isNumber()) return j.getAsNumber();
                        if (j.isString()) return j.getAsString();
                        throw new JsonParseException("When condition must be a string, a number, or a boolean: " + j);
                    };
                    JsonObject when = object.getAsJsonObject("when");
                    List<Predicate<Map<String, Object>>> conditionsList = new ArrayList<>();
                    if (when.has("OR") && when.get("OR").isJsonArray()){
                        for (JsonElement element : when.getAsJsonArray("OR")){
                            if (element instanceof JsonObject conditionObject){
                                Map<String, Object> conditions = conditionObject.entrySet().stream()
                                        .filter(e -> e.getValue() instanceof JsonPrimitive)
                                        .collect(Collectors.toMap(Entry::getKey, e -> mappingFunction.apply(e.getValue().getAsJsonPrimitive())));
                                conditionsList.add(m -> {
                                    boolean[] bool = new boolean[1];
                                    bool[0] = true;
                                    conditions.forEach((s, o) -> {
                                        bool[0] &= m.containsKey(s) && m.get(s).equals(o);
                                    });
                                    return bool[0];
                                });
                            }
                        }
                    } else {
                        Map<String, Object> conditions = when.asMap().entrySet().stream()
                                .filter(e -> e.getValue() instanceof JsonPrimitive)
                                .collect(Collectors.toMap(Entry::getKey, e -> mappingFunction.apply(e.getValue().getAsJsonPrimitive())));
                        conditionsList.add(m -> {
                            boolean[] bool = new boolean[1];
                            bool[0] = true;
                            conditions.forEach((s, o) -> {
                                bool[0] &= m.containsKey(s) && m.get(s).equals(o);
                            });
                            return bool[0];
                        });
                    }
                    condition = m ->{
                        if (conditionsList.isEmpty()) return true;
                        boolean test = conditionsList.get(0).test(m);
                        for (int i = 1; i < conditionsList.size(); i++) {
                            test |= conditionsList.get(i).test(m);
                        }
                        return test;
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
