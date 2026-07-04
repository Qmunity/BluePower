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
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer.AmbientOcclusionFace;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Uses Multipart IModelData to create a model.
 * @author MoreThanHidden
 */
public class BPMultipartModel implements BakedModel {

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        Map<BlockState, ModelData> stateInfo = extraData.get(TileBPMultipart.STATE_INFO);
        BlockAndTintGetter level = extraData.get(TileBPMultipart.LEVEL);
        BlockPos pos = extraData.get(TileBPMultipart.POS);
        float[] shape = new float[Direction.values().length * 2];
        BitSet bitSet = new BitSet(3);

        if (stateInfo != null) {
            return stateInfo.keySet().stream().flatMap(
                    i -> {
                        BakedModel model = brd.getBlockModel(i);
                        ModelData mData = stateInfo.get(i);
                        if (mData == null) mData = ModelData.EMPTY;
                        ChunkRenderTypeSet renderTypes = model.getRenderTypes(i, rand, mData);
                        if (!renderTypes.contains(renderType)) return Stream.of();
                        ModelData finalMData = mData;
                        List<BakedQuad> list = new ArrayList<>(model.getQuads(i, side, rand, mData, renderType).stream().map(
                                q -> finalMData.has(TileWire.COLOR_INFO) ? transform(level, i, pos, side, q, finalMData.get(TileWire.COLOR_INFO), finalMData.has(TileWire.LIGHT_INFO) ? finalMData.get(TileWire.LIGHT_INFO) : false, shape, bitSet) : q
                        ).toList());
                        return list.stream();
                    }
            ).collect(Collectors.toList());
        }else{
            return Collections.emptyList();
        }
    }

    @Override
    public ChunkRenderTypeSet getRenderTypes(@NotNull BlockState state, @NotNull RandomSource rand, @NotNull ModelData data) {
        BlockRenderDispatcher brd = Minecraft.getInstance().getBlockRenderer();
        Set<RenderType> renderTypes = new HashSet<>();
        Map<BlockState, ModelData> stateInfo = data.get(TileBPMultipart.STATE_INFO);
        if (stateInfo == null) return ItemBlockRenderTypes.getRenderLayers(state);
        stateInfo.forEach((s, d) -> {
            BakedModel model = brd.getBlockModel(s);
            renderTypes.addAll(model.getRenderTypes(s, rand, d).asList());
        });
        return ChunkRenderTypeSet.of(renderTypes);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        return Collections.emptyList();
    }


    private static BakedQuad transform(BlockAndTintGetter level, BlockState state, BlockPos pos, Direction side, BakedQuad quad, Pair<Integer, Integer> colorPair, Boolean fullBright, float[] shape, BitSet shapeFlags) {
        BakedQuad[] finalQuad = new BakedQuad[1];
        AmbientOcclusionFace aoFace = new AmbientOcclusionFace();
        if (side != null) pos = pos.relative(quad.getDirection());
        int color = quad.getTintIndex() == 2 ? colorPair.getSecond() : colorPair.getFirst();
        calculateShape(level, state, pos, quad.getVertices(), quad.getDirection(), shape, shapeFlags);
        if (!ForgeHooksClient.calculateFaceWithoutAO(level, state, pos, quad, shapeFlags.get(0), aoFace.brightness, aoFace.lightmap)) {
            aoFace.calculate(level, state, pos, quad.getDirection(), shape, shapeFlags, quad.isShade());
        }
        float r = (float)(color >> 16 & 255) / 255.0F;
        float g = (float)(color >> 8 & 255) / 255.0F;
        float b = (float)(color & 255) / 255.0F;
        final QuadBakingVertexConsumer consumer = new QuadBakingVertexConsumer(q -> finalQuad[0] = q);
        consumer.putBulkData(new PoseStack().last(), quad, new float[]{aoFace.brightness[0], aoFace.brightness[1], aoFace.brightness[2], aoFace.brightness[3]}, r, g, b, new int[]{aoFace.lightmap[0], aoFace.lightmap[1], aoFace.lightmap[2], aoFace.lightmap[3]}, OverlayTexture.NO_OVERLAY, true);
        if (fullBright){
            QuadTransformers.applyingLightmap(15, 15).processInPlace(finalQuad[0]);
        }
        return finalQuad[0];
    }

    private static void calculateShape(BlockAndTintGetter level, BlockState state, BlockPos pos, int[] vertices, Direction direction, @javax.annotation.Nullable float[] shape, BitSet shapeFlags) {
        float f = 32.0F;
        float f1 = 32.0F;
        float f2 = 32.0F;
        float f3 = -32.0F;
        float f4 = -32.0F;
        float f5 = -32.0F;

        for(int i = 0; i < 4; ++i) {
            float f6 = Float.intBitsToFloat(vertices[i * 8]);
            float f7 = Float.intBitsToFloat(vertices[i * 8 + 1]);
            float f8 = Float.intBitsToFloat(vertices[i * 8 + 2]);
            f = Math.min(f, f6);
            f1 = Math.min(f1, f7);
            f2 = Math.min(f2, f8);
            f3 = Math.max(f3, f6);
            f4 = Math.max(f4, f7);
            f5 = Math.max(f5, f8);
        }

        if (shape != null) {
            shape[Direction.WEST.get3DDataValue()] = f;
            shape[Direction.EAST.get3DDataValue()] = f3;
            shape[Direction.DOWN.get3DDataValue()] = f1;
            shape[Direction.UP.get3DDataValue()] = f4;
            shape[Direction.NORTH.get3DDataValue()] = f2;
            shape[Direction.SOUTH.get3DDataValue()] = f5;
            int j = Direction.values().length;
            shape[Direction.WEST.get3DDataValue() + j] = 1.0F - f;
            shape[Direction.EAST.get3DDataValue() + j] = 1.0F - f3;
            shape[Direction.DOWN.get3DDataValue() + j] = 1.0F - f1;
            shape[Direction.UP.get3DDataValue() + j] = 1.0F - f4;
            shape[Direction.NORTH.get3DDataValue() + j] = 1.0F - f2;
            shape[Direction.SOUTH.get3DDataValue() + j] = 1.0F - f5;
        }

        float f9 = 1.0E-4F;
        float f10 = 0.9999F;
        switch (direction) {
            case DOWN:
                shapeFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
                shapeFlags.set(0, f1 == f4 && (f1 < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case UP:
                shapeFlags.set(1, f >= 1.0E-4F || f2 >= 1.0E-4F || f3 <= 0.9999F || f5 <= 0.9999F);
                shapeFlags.set(0, f1 == f4 && (f4 > 0.9999F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case NORTH:
                shapeFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
                shapeFlags.set(0, f2 == f5 && (f2 < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case SOUTH:
                shapeFlags.set(1, f >= 1.0E-4F || f1 >= 1.0E-4F || f3 <= 0.9999F || f4 <= 0.9999F);
                shapeFlags.set(0, f2 == f5 && (f5 > 0.9999F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case WEST:
                shapeFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
                shapeFlags.set(0, f == f3 && (f < 1.0E-4F || state.isCollisionShapeFullBlock(level, pos)));
                break;
            case EAST:
                shapeFlags.set(1, f1 >= 1.0E-4F || f2 >= 1.0E-4F || f4 <= 0.9999F || f5 <= 0.9999F);
                shapeFlags.set(0, f == f3 && (f3 > 0.9999F || state.isCollisionShapeFullBlock(level, pos)));
        }

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
        return Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(ResourceLocation.parse("minecraft:stone"), "")).getParticleIcon();
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

}
