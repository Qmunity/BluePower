package com.bluepowermod.client.render;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraftforge.client.model.geometry.IGeometryLoader;

public class BPWireModelLoader implements IGeometryLoader<BPWireModel> {
    @Override
    public BPWireModel read(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        jsonObject.remove("loader");
        UnbakedModel model = jsonDeserializationContext.deserialize(jsonObject, BlockModel.class);
        return new BPWireModel(model);
    }
}
