/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.tier1.TileWire;
import com.bluepowermod.util.MultipartUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Uses Multipart IModelData to create a model.
 * @author MoreThanHidden
 */
public class BPMultipartModel implements BakedModel {

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        Map<BlockState, ModelData> stateInfo = extraData.get(TileBPMultipart.STATE_INFO);

        if (stateInfo != null) {
            return stateInfo.keySet().stream().flatMap(
                    i -> {
                        BakedModel model = brd.getBlockModel(i);
                        List<BakedQuad> list = new ArrayList<>();
                        ModelData mData = stateInfo.get(i);
                        if (mData == null) mData = ModelData.EMPTY;
                        ModelData finalMData = mData;
                        for (RenderType rType : model.getRenderTypes(i, rand, mData)){
                            list.addAll(model.getQuads(i, side, rand, mData, rType).stream().map(
                                    q -> finalMData.has(TileWire.COLOR_INFO) ? transform(q, finalMData.get(TileWire.COLOR_INFO), finalMData.has(TileWire.LIGHT_INFO) ? finalMData.get(TileWire.LIGHT_INFO) : false) : q
                            ).toList());
                        }
                        return list.stream();
                    }
            ).collect(Collectors.toList());
        }else{
            return Collections.emptyList();
        }
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return Collections.emptyList();
    }


    private static BakedQuad transform(BakedQuad quad, Pair<Integer, Integer> colorPair, Boolean fullBright) {
        BakedQuad[] finalQuad = new BakedQuad[1];
        int color = quad.getTintIndex() == 2 ? colorPair.getSecond() : colorPair.getFirst();
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        final QuadBakingVertexConsumer consumer = new QuadBakingVertexConsumer(q -> finalQuad[0] = q);
        consumer.putBulkData(new PoseStack().last(), quad, r, g, b, 1, 0, OverlayTexture.NO_OVERLAY, true);
        if (fullBright){
            QuadTransformers.applyingLightmap(15, 15).processInPlace(quad);
        }
        return finalQuad[0];
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
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
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        HitResult rayTraceResult = Minecraft.getInstance().hitResult;
        if(Minecraft.getInstance().player != null && rayTraceResult instanceof BlockHitResult){
            BlockState state = MultipartUtils.getClosestState(Minecraft.getInstance().player, ((BlockHitResult)rayTraceResult).getBlockPos());
            if(state != null)
                return Minecraft.getInstance().getBlockRenderer().getBlockModel(state).getParticleIcon();
        }
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(new ResourceLocation("minecraft:stone"), "")).getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

}
