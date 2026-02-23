package com.bluepowermod.client.render;

import com.bluepowermod.block.gates.BlockGateBase;
import it.unimi.dsi.fastutil.Pair;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GateBakedModel implements BakedModel {
    private final List<Pair<IGateCondition, BakedModel>> models;
    private final TextureAtlasSprite particle;

    public GateBakedModel(List<Pair<IGateCondition, BakedModel>> models, TextureAtlasSprite particle) {
        this.models = models;
        this.particle = particle;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return List.of();
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = new ArrayList<>();
        Map<String, Object> redstoneStates = new HashMap<>();
        redstoneStates.put("powered_back", state.getValue(BlockGateBase.POWERED_BACK));
        redstoneStates.put("powered_front", state.getValue(BlockGateBase.POWERED_FRONT));
        redstoneStates.put("powered_left", state.getValue(BlockGateBase.POWERED_LEFT));
        redstoneStates.put("powered_right", state.getValue(BlockGateBase.POWERED_RIGHT));
        for(Pair<IGateCondition, BakedModel> pair : models){
            if (pair.key().test(redstoneStates)){
                quads.addAll(pair.value().getQuads(state, side, rand, data, renderType));
            }
        }
        return quads;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return false;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particle;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
