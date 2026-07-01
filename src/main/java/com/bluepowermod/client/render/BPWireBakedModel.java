package com.bluepowermod.client.render;

import com.bluepowermod.tile.tier1.TileWire;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class BPWireBakedModel implements BakedModel {
    private final BakedModel off;
    private final BakedModel on;

    public BPWireBakedModel(BakedModel off, BakedModel on) {
        this.off = off;
        this.on = on;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        boolean powered = Boolean.TRUE.equals(data.get(TileWire.LIGHT_INFO));
        List<BakedQuad> quads = (powered ? on : off).getQuads(state, side, rand, data, renderType);
        if (powered){
            for (BakedQuad quad : quads){
                QuadTransformers.applyingLightmap(15, 15).processInPlace(quad);
            }
        }
        return quads;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return List.of();
    }

    @Override
    public boolean useAmbientOcclusion() {
        return off.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return off.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return off.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return off.isCustomRenderer();
    }


    @Override
    public TextureAtlasSprite getParticleIcon() {
        return off.getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return off.getOverrides();
    }
}
