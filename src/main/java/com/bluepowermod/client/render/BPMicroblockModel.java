/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.block.BlockBPMicroblock;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBPMicroblock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Uses Microblock IModelData to create a model.
 * @author MoreThanHidden
 */
public class BPMicroblockModel implements BakedModel {
    private DeferredHolder<Block, Block> defBlock = BPBlocks.marble;
    private DeferredHolder<Block, Block> defSize = BPBlocks.half_block;
    BPMicroblockModel(){}

    private BPMicroblockModel(DeferredHolder<Block, Block> defBlock, DeferredHolder<Block, Block> defSize){
        this.defBlock = defBlock;
        this.defSize = defSize;
    }


    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData extraData, @Nullable RenderType renderType) {
        Pair<Block, Integer> info = extraData.get(TileBPMicroblock.PROPERTY_INFO);
        if (info != null) {
            BakedModel typeModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(info.getKey().defaultBlockState());
            BakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getId(), "face=" + Direction.WEST));

            List<BakedQuad> bakedQuads = new ArrayList<>();

            if(state != null && state.getBlock() instanceof BlockBPMicroblock) {
                sizeModel = Minecraft.getInstance().getModelManager().getModel(
                        new ModelResourceLocation(BuiltInRegistries.BLOCK.getKey(state.getBlock()), "face=" + state.getValue(BlockBPMicroblock.FACING))
                );
            }

            List<BakedQuad> sizeModelQuads = sizeModel.getQuads(state, side, rand);

            if (state != null) {
                TextureAtlasSprite sprite = typeModel.getParticleIcon();
                int tintIndex = -1;
                for (BakedQuad quad: sizeModelQuads) {
                    List<BakedQuad> typeModelQuads = typeModel.getQuads(info.getKey().defaultBlockState(), quad.getDirection(), rand);
                    if(!typeModelQuads.isEmpty()){
                        sprite = typeModelQuads.getFirst().getSprite();
                        tintIndex = typeModelQuads.getFirst().getTintIndex();
                    }

                    bakedQuads.add(transform(quad, sprite, tintIndex, info.getKey()));
                }
                return bakedQuads;
            }

        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        List<BakedQuad> outquads = new ArrayList<>();
        BakedModel typeModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(this.defBlock.get().defaultBlockState());
        BakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getId(), "face=" + Direction.WEST));

        if(state != null && state.getBlock() instanceof BlockBPMicroblock) {
            sizeModel = Minecraft.getInstance().getModelManager().getModel(
                    new ModelResourceLocation(BuiltInRegistries.BLOCK.getKey(state.getBlock()), "face=" + state.getValue(BlockBPMicroblock.FACING))
            );
        }

        List<BakedQuad> sizeModelQuads = sizeModel.getQuads(state, side, rand);

        TextureAtlasSprite sprite = typeModel.getParticleIcon();
        int tintIndex = -1;
        for (BakedQuad quad: sizeModelQuads) {
            List<BakedQuad> typeModelQuads = typeModel.getQuads(this.defBlock.get().defaultBlockState(), quad.getDirection(), rand, ModelData.EMPTY, RenderType.cutout());
            if(!typeModelQuads.isEmpty()){
                sprite = typeModelQuads.getFirst().getSprite();
                tintIndex = typeModelQuads.getFirst().getTintIndex();
            }

            outquads.add(transform(quad, sprite, tintIndex, defBlock.get()));
        }

        return outquads;
    }

    private static BakedQuad transform(BakedQuad sizeQuad, TextureAtlasSprite sprite, int tintIndex, Block block) {
        int[] vertexData = sizeQuad.getVertices().clone();
        for (int i = 0; i < 4; i++) {

            //UVs
            int offset = i * IQuadTransformer.STRIDE + IQuadTransformer.UV0;
            //swap the sizeQuds UV with Sprite UV
            vertexData[offset] = Float.floatToRawIntBits(sprite.getU(sizeQuad.getSprite().getUOffset(Float.intBitsToFloat(vertexData[offset]))));
            vertexData[offset + 1] = Float.floatToRawIntBits(sprite.getV(sizeQuad.getSprite().getVOffset(Float.intBitsToFloat(vertexData[offset + 1]))));

            //Color
            offset = i * IQuadTransformer.STRIDE + IQuadTransformer.COLOR;
            int color;
            try {
                color = Minecraft.getInstance().getBlockColors().getColor(block.defaultBlockState(), null, null, tintIndex);
            } catch (Exception ex) {
                    color = -1;
            }
            int alphaMask = 0xFF000000, redMask = 0xFF0000, greenMask = 0xFF00, blueMask = 0xFF;
            int a = (color & alphaMask) >> 24;
            int r = (color & redMask) >> 16;
            int g = (color & greenMask) >> 8;
            int b = (color & blueMask);

            if(color != -1 && !(block instanceof GrassBlock && tintIndex == -1))
                vertexData[offset] = (a & 255) << 24 | (b & 255) << 16 | (g & 255) << 8 | r & 255;
        }
        return new BakedQuad(vertexData, sizeQuad.getTintIndex(), sizeQuad.getDirection(), sprite, sizeQuad.isShade(), sizeQuad.hasAmbientOcclusion());
    }

    @Override
    public BakedModel applyTransform(ItemDisplayContext type, PoseStack stack, boolean applyLeftHandTransform) {
        BakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getId(), "face=" + Direction.WEST));
        sizeModel.getTransforms().getTransform(type).apply(applyLeftHandTransform, stack);
        return this;
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
        BakedModel typeModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(this.defBlock.get().defaultBlockState());
        return typeModel.getParticleIcon();
    }

    @Override
    public TextureAtlasSprite getParticleIcon(@NotNull ModelData data) {
        Pair<Block, Integer> info = data.get(TileBPMicroblock.PROPERTY_INFO);
        if(info != null)
            return Minecraft.getInstance().getBlockRenderer().getBlockModel(info.getKey().defaultBlockState()).getParticleIcon();
        return getParticleIcon();
    }
    
    @Override
    public ItemOverrides getOverrides() {
        return new BakedMicroblockOverrideHandler();
    }

    /**
     * Overwrites the model with NBT definition
     */
    private static final class BakedMicroblockOverrideHandler extends ItemOverrides{


        @Nullable
        @Override
        public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int par1){
            CompoundTag nbt = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            if(nbt.contains("block")){
                DeferredHolder<Block, Block> block = DeferredHolder.create(ResourceLocation.parse("block"), ResourceLocation.parse(nbt.getString("block")));
                return new BPMicroblockModel(block, DeferredHolder.create(ResourceLocation.parse("block"), BuiltInRegistries.BLOCK.getKey(Block.byItem(stack.getItem()))));
            }
            return new BPMicroblockModel(BPBlocks.marble, DeferredHolder.create(ResourceLocation.parse("block"), BuiltInRegistries.BLOCK.getKey(Block.byItem(stack.getItem()))));
        }
    }

}
