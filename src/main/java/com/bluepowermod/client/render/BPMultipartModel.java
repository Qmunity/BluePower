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
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.ModelResourceLocation;
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
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.VertexTransformer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Uses Multipart IModelData to create a model.
 * @author MoreThanHidden
 */
public class BPMultipartModel implements BakedModel {


    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData) {
        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        Map<BlockState, IModelData> stateInfo = extraData.getData(TileBPMultipart.STATE_INFO);

        if (stateInfo != null) {
            return stateInfo.keySet().stream().flatMap(
                    i -> brd.getBlockModel(i).getQuads(i, side, rand, stateInfo.get(i)).stream().map(
                            q -> stateInfo.get(i).hasProperty(TileWire.COLOR_INFO) ? transform(q, stateInfo.get(i).getData(TileWire.COLOR_INFO), stateInfo.get(i).hasProperty(TileWire.LIGHT_INFO) ? stateInfo.get(i).getData(TileWire.LIGHT_INFO) : false): q
                    )
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
        BakedQuadBuilder builder = new BakedQuadBuilder();
        final IVertexConsumer consumer = new VertexTransformer(builder) {
            @Override
            public void put(int element, float... data) {
                VertexFormatElement e = this.getVertexFormat().getElements().get(element);
                if(e.getUsage() == VertexFormatElement.Usage.COLOR){

                    int color = quad.getTintIndex() == 2 ? colorPair.getSecond() : colorPair.getFirst();
                    int redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
                    int r = (color & redMask) >> 16;
                    int g = (color & greenMask) >> 8;
                    int b = (color & blueMask);

                    parent.put(element, r/255F, g/255F, b/255F, 1);
                }else {
                    parent.put(element, data);
                }
            }
        };

        LightUtil.putBakedQuad(consumer, quad);
        BakedQuad finalQuad = builder.build();
        if(fullBright)
            LightUtil.setLightData(finalQuad, 240);
        return finalQuad;
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
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation("minecraft:stone", "")).getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

}
