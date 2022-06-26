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
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.mojang.math.Transformation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.BakedQuadBuilder;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.LightUtil;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TransformationHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;

/**
 * Uses Microblock IModelData to create a model.
 * @author MoreThanHidden
 */
public class BPMicroblockModel implements BakedModel {
    private RegistryObject<Block> defBlock = BPBlocks.marble;
    private RegistryObject<Block> defSize = BPBlocks.half_block;
    BPMicroblockModel(){}

    private BPMicroblockModel(RegistryObject<Block> defBlock, RegistryObject<Block> defSize){
        this.defBlock = defBlock;
        this.defSize = defSize;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull RandomSource rand, @Nonnull IModelData extraData) {
        Pair<Block, Integer> info = extraData.getData(TileBPMicroblock.PROPERTY_INFO);
        if (info != null) {
            BakedModel typeModel = Minecraft.getInstance().getBlockRenderer().getBlockModel(info.getKey().defaultBlockState());
            BakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getId(), "face=" + Direction.WEST));

            List<BakedQuad> bakedQuads = new ArrayList<>();

            if(state != null && state.getBlock() instanceof BlockBPMicroblock) {
                sizeModel = Minecraft.getInstance().getModelManager().getModel(
                        new ModelResourceLocation(ForgeRegistries.BLOCKS.getKey(state.getBlock()), "face=" + state.getValue(BlockBPMicroblock.FACING))
                );
            }

            List<BakedQuad> sizeModelQuads = sizeModel.getQuads(state, side, rand);

            if (state != null) {
                TextureAtlasSprite sprite = typeModel.getParticleIcon();
                for (BakedQuad quad: sizeModelQuads) {
                    List<BakedQuad> typeModelQuads = typeModel.getQuads(info.getKey().defaultBlockState(), quad.getDirection(), rand);
                    if(typeModelQuads.size() > 0){
                        sprite = typeModelQuads.get(0).getSprite();
                    }

                    bakedQuads.add(transform(quad, sprite, state.getValue(BlockBPMicroblock.FACING), defBlock.get()));
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
                    new ModelResourceLocation(ForgeRegistries.BLOCKS.getKey(state.getBlock()), "face=" + state.getValue(BlockBPMicroblock.FACING))
            );
        }

        List<BakedQuad> sizeModelQuads = sizeModel.getQuads(state, side, rand);

        TextureAtlasSprite sprite = typeModel.getParticleIcon();
        for (BakedQuad quad: sizeModelQuads) {
            List<BakedQuad> typeModelQuads = typeModel.getQuads(this.defBlock.get().defaultBlockState(), quad.getDirection(), rand);
            if(typeModelQuads.size() > 0){
                sprite = typeModelQuads.get(0).getSprite();
            }

            outquads.add(transform(quad, sprite, Direction.EAST , defBlock.get()));
        }

        return outquads;
    }

    private static BakedQuad transform(BakedQuad sizeQuad, TextureAtlasSprite sprite, Direction dir, Block block) {
        BakedQuadBuilder builder = new BakedQuadBuilder();
        final IVertexConsumer consumer = new VertexTransformer(builder) {
            @Override
            public void put(int element, float... data) {
                VertexFormatElement e = this.getVertexFormat().getElements().get(element);
                if (e.getUsage() == VertexFormatElement.Usage.UV && e.getIndex() == 0) {
                    Vec2 vec = new Vec2(data[0], data[1]);
                    float u = (vec.x - sizeQuad.getSprite().getU0()) / (sizeQuad.getSprite().getU1() - sizeQuad.getSprite().getU0()) * 16;
                    float v = (vec.y - sizeQuad.getSprite().getV0()) / (sizeQuad.getSprite().getV1() - sizeQuad.getSprite().getV0()) * 16;
                    builder.put(element, sprite.getU(u), sprite.getV(v));
                }else if(e.getUsage() == VertexFormatElement.Usage.COLOR){

                    int color;
                    try {
                        color = Minecraft.getInstance().getBlockColors().getColor(block.defaultBlockState(), null, null, sizeQuad.getTintIndex());
                    } catch(Exception ex){
                        try {
                            color = Minecraft.getInstance().getBlockColors().getColor(block.defaultBlockState(), null, BlockPos.ZERO, sizeQuad.getTintIndex());
                        } catch (Exception ex2){
                            color = 0;
                        }
                    }
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
        LightUtil.putBakedQuad(consumer, sizeQuad);
        return builder.build();
    }

    @Override
    public boolean doesHandlePerspectives() {
        return true;
    }

    @Override
    public BakedModel handlePerspective(ItemTransforms.TransformType type, PoseStack stack) {
        BakedModel sizeModel = Minecraft.getInstance().getModelManager().getModel(new ModelResourceLocation(defSize.getId(), "face=" + Direction.WEST));
        Transformation tr = TransformationHelper.toTransformation(sizeModel.getTransforms().getTransform(type));
        if(!tr.isIdentity()) {
            tr.push(stack);
        }
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
    public TextureAtlasSprite getParticleIcon(@Nonnull IModelData data) {
        Pair<Block, Integer> info = data.getData(TileBPMicroblock.PROPERTY_INFO);
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
            CompoundTag nbt = stack.getTag();
            if(nbt != null && nbt.contains("block")){
                RegistryObject<Block> block = RegistryObject.create(new ResourceLocation(nbt.getString("block")),ForgeRegistries.BLOCKS);
                return new BPMicroblockModel(block, RegistryObject.create(ForgeRegistries.BLOCKS.getKey(Block.byItem(stack.getItem())), ForgeRegistries.BLOCKS));
            }
            return new BPMicroblockModel(RegistryObject.create(new ResourceLocation("minecraft:stone"), ForgeRegistries.BLOCKS), RegistryObject.create(ForgeRegistries.BLOCKS.getKey(Block.byItem(stack.getItem())), ForgeRegistries.BLOCKS));
        }
    }

}
