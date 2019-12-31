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
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.TransformationMatrix;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.World;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TransformationHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Uses Microblock IModelData to create a model.
 * @author MoreThanHidden
 */
public class BPMicroblockModel implements IBakedModel {
    private Block defBlock = BPBlocks.marble;
    private Block defSize = BPBlocks.half_block;
    BPMicroblockModel(){}

    private BPMicroblockModel(Block defBlock, Block defSize){
        this.defBlock = defBlock;
        this.defSize = defSize;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        Pair<Block, Integer> info = extraData.getData(TileBPMicroblock.PROPERTY_INFO);
        if (info != null) {
            IBakedModel typeModel = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(info.getKey().getDefaultState());
            IBakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getRegistryName(), "face=" + Direction.WEST));

            List<BakedQuad> bakedQuads = new ArrayList<>();

            if(state != null && state.getBlock() instanceof BlockBPMicroblock) {
                sizeModel = Minecraft.getInstance().getModelManager().getModel(
                        new ModelResourceLocation(state.getBlock().getRegistryName(), "face=" + state.get(BlockBPMicroblock.FACING))
                );
            }

            List<BakedQuad> sizeModelQuads = sizeModel.getQuads(state, side, rand);

            if (state != null) {
                TextureAtlasSprite sprite = typeModel.getParticleTexture();
                for (BakedQuad quad: sizeModelQuads) {
                    List<BakedQuad> typeModelQuads = typeModel.getQuads(info.getKey().getDefaultState(), quad.getFace(), rand);
                    if(typeModelQuads.size() > 0){
                        sprite = typeModelQuads.get(0).getSprite();
                    }
                    bakedQuads.add(transform(quad, sprite, state.get(BlockBPMicroblock.FACING)));
                }
                return bakedQuads;
            }

        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        List<BakedQuad> outquads = new ArrayList<>();
        IBakedModel typeModel = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(this.defBlock.getDefaultState());
        IBakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getRegistryName(), "face=" + Direction.WEST));

        if(state != null && state.getBlock() instanceof BlockBPMicroblock) {
            sizeModel = Minecraft.getInstance().getModelManager().getModel(
                    new ModelResourceLocation(state.getBlock().getRegistryName(), "face=" + state.get(BlockBPMicroblock.FACING))
            );
        }

        List<BakedQuad> sizeModelQuads = sizeModel.getQuads(state, side, rand);

        TextureAtlasSprite sprite = typeModel.getParticleTexture();
        for (BakedQuad quad: sizeModelQuads) {
            List<BakedQuad> typeModelQuads = typeModel.getQuads(this.defBlock.getDefaultState(), quad.getFace(), rand);
            if(typeModelQuads.size() > 0){
                sprite = typeModelQuads.get(0).getSprite();
            }
            outquads.add(transform(quad, sprite, Direction.EAST));
        }

        return outquads;
    }

    private static BakedQuad transform(BakedQuad sizeQuad, TextureAtlasSprite sprite, Direction dir) {
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.BLOCK);
        final IVertexConsumer consumer = new VertexTransformer(builder) {
            @Override
            public void put(int element, float... data) {
                VertexFormatElement e = this.getVertexFormat().func_227894_c_().get(element);
                if (e.getUsage() == VertexFormatElement.Usage.UV && e.getIndex() == 0) {
                    Vec2f vec = new Vec2f(data[0], data[1]);
                    float u = (vec.x - sizeQuad.getSprite().getMinU()) / (sizeQuad.getSprite().getMaxU() - sizeQuad.getSprite().getMinU()) * 16;
                    float v = (vec.y - sizeQuad.getSprite().getMinV()) / (sizeQuad.getSprite().getMaxV() - sizeQuad.getSprite().getMinV()) * 16;
                    builder.put(element, sprite.getInterpolatedU(u), sprite.getInterpolatedV(v));
                } else {
                    parent.put(element, data);
                }
            }
        };
        LightUtil.putBakedQuad(consumer, sizeQuad);
        return builder.build();
    }

    @Override
    public boolean doesHandlePerspectives() {
        return true;
    }

    @Override
    public IBakedModel handlePerspective(ItemCameraTransforms.TransformType type, MatrixStack stack) {
        IBakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getRegistryName(), "face=" + Direction.WEST));
        TransformationMatrix tr = TransformationHelper.toTransformation(sizeModel.getItemCameraTransforms().getTransform(type));
        if(!tr.isIdentity()) {
            tr.push(stack);
        }
        return this;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        IBakedModel typeModel = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(this.defBlock.getDefaultState());
        return typeModel.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return new BakedMicroblockOverrideHandler();
    }

    /**
     * Overwrites the model with NBT definition
     */
    private static final class BakedMicroblockOverrideHandler extends ItemOverrideList{
        @Override
        public IBakedModel getModelWithOverrides(IBakedModel originalModel, ItemStack stack, @Nullable World world, @Nullable LivingEntity entity){
            CompoundNBT nbt = stack.getTag();
            if(nbt != null && nbt.contains("block")){
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(nbt.getString("block")));
                return new BPMicroblockModel(block, Block.getBlockFromItem(stack.getItem()));
            }
            return new BPMicroblockModel(Blocks.STONE, Block.getBlockFromItem(stack.getItem()));
        }
    }

}