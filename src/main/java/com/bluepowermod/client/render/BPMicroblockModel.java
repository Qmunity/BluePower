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
import javafx.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.World;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.pipeline.IVertexConsumer;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.VertexTransformer;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import javax.vecmath.Vector4f;
import java.util.*;

/**
 * Uses Microblock IModelData to create a model.
 * @author MoreThanHidden
 */
public class BPMicroblockModel implements IBakedModel {
    private Block defBlock = Blocks.STONE;
    private Vector3f defVec = new Vector3f(1,0.5f,1);
    BPMicroblockModel(){}

    private BPMicroblockModel(Block defBlock, Vector3f defVec){
        this.defBlock = defBlock;
        this.defVec = defVec;
    }

    @Nonnull
    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @Nonnull Random rand, @Nonnull IModelData extraData) {
        Pair<Block, Integer> info = extraData.getData(TileBPMicroblock.PROPERTY_INFO);
        if (info != null) {
            IBakedModel origModel = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(info.getKey().getDefaultState());
            List<BakedQuad> quads = origModel.getQuads(info.getKey().getDefaultState(), side, rand);
            List<BakedQuad> outquads = new ArrayList<>();

            if(state != null && state.getBlock() instanceof BlockBPMicroblock) {
                VoxelShape size = ((BlockBPMicroblock) state.getBlock()).getSize();
                this.defVec = new Vector3f((float) size.getBoundingBox().getXSize(), (float)size.getBoundingBox().getYSize(), (float)size.getBoundingBox().getZSize());
            }

            TRSRTransformation transformation;
            if (state != null) {
                switch (state.get(BlockBPMicroblock.FACING)) {
                    case UP:
                        transformation = new TRSRTransformation(new Vector3f(0,-(0.5f - (defVec.y / 2)),0), null, defVec, null);
                        break;
                    case DOWN:
                        transformation = new TRSRTransformation(new Vector3f(0,(0.5f - (defVec.y / 2)),0), null, defVec, null);
                        break;
                    case NORTH:
                        transformation = new TRSRTransformation(new Vector3f(0,0,(0.5f - (defVec.y / 2))), null, new Vector3f(defVec.x,defVec.z,defVec.y), null);
                        break;
                    case SOUTH:
                        transformation = new TRSRTransformation(new Vector3f(0,0,-(0.5f - (defVec.y / 2))), null, new Vector3f(defVec.x,defVec.z,defVec.y), null);
                        break;
                    case EAST:
                        transformation = new TRSRTransformation(new Vector3f(-(0.5f - (defVec.y / 2)),0,0), null, new Vector3f(defVec.y,defVec.x,defVec.z), null);
                        break;
                    default:
                        transformation = new TRSRTransformation(new Vector3f((0.5f - (defVec.y / 2)),0,0), null, new Vector3f(defVec.y,defVec.x,defVec.z), null);
                }

                for (BakedQuad quad: quads) {
                    outquads.add(transform(quad, TRSRTransformation.blockCenterToCorner(transformation), state.get(BlockBPMicroblock.FACING)));
                }
                return outquads;
            }

        }
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        List<BakedQuad> outquads = new ArrayList<>();
        IBakedModel origModel = Minecraft.getInstance().getBlockRendererDispatcher().getModelForState(this.defBlock.getDefaultState());
        List<BakedQuad> quads = origModel.getQuads(this.defBlock.getDefaultState(), side, rand);

        for (BakedQuad quad: quads) {
            outquads.add(transform(quad, TRSRTransformation.blockCenterToCorner(new TRSRTransformation(null, null, new Vector3f(defVec.y, defVec.x, defVec.z), null)), Direction.EAST));
        }
        return outquads;
    }

    @Override
    public org.apache.commons.lang3.tuple.Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType type) {
        return PerspectiveMapWrapper.handlePerspective(this, ForgeBlockStateV1.Transforms.get("forge:default-block").get(), type);
    }

    protected static BakedQuad transform(BakedQuad quad, final TRSRTransformation transform, Direction dir) {
        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(DefaultVertexFormats.ITEM);
        final IVertexConsumer consumer = new VertexTransformer(builder) {
            @Override
            public void put(int element, float... data) {
                VertexFormatElement formatElement = DefaultVertexFormats.ITEM.getElement(element);
                switch(formatElement.getUsage()) {
                    case POSITION: {
                        float[] newData = new float[4];
                        Vector4f vec = new Vector4f(data);
                        transform.getMatrixVec().transform(vec);
                        vec.y = vec.y + transform.getTranslation().y;
                        vec.x = vec.x + transform.getTranslation().x;
                        vec.z = vec.z + transform.getTranslation().z;
                        vec.get(newData);
                        parent.put(element, newData);
                        break;
                    }
                    case UV: {
                        Vector4f vec = new Vector4f(data);
                        float u = quad.getSprite().getUnInterpolatedU(vec.x);
                        float v = quad.getSprite().getUnInterpolatedV(vec.y);
                        switch (dir){
                            case UP:
                                if(Direction.UP != quad.getFace() && Direction.DOWN != quad.getFace() && v < 1){v = 16 * (1.0f - transform.getScale().y);}
                                break;
                            case DOWN:
                                if(Direction.UP != quad.getFace() && Direction.DOWN != quad.getFace() && v > 15){v = 16 * (1.0f - transform.getScale().y);}
                                break;
                            case EAST:
                                if((Direction.UP == quad.getFace() || Direction.DOWN == quad.getFace()) && u > 15){u = 16 * transform.getScale().x;}
                                if((Direction.NORTH == quad.getFace() || Direction.SOUTH == quad.getFace()) && u > 15){u = 16 * transform.getScale().x;}
                                break;
                            case WEST:
                                if((Direction.UP == quad.getFace() || Direction.DOWN == quad.getFace()) && u < 1){u = 16 * (1.0f - transform.getScale().x);}
                                if((Direction.NORTH == quad.getFace() || Direction.SOUTH == quad.getFace()) && u > 15){u = 16 * transform.getScale().x;}
                                break;
                            case SOUTH:
                                if((Direction.UP == quad.getFace() || Direction.DOWN == quad.getFace()) && v > 15){v = 16 * transform.getScale().z;}
                                if((Direction.EAST == quad.getFace() || Direction.WEST == quad.getFace()) && u > 15){u = 16 * transform.getScale().z;}
                                break;
                            case NORTH:
                                if((Direction.UP == quad.getFace() || Direction.DOWN == quad.getFace()) && v < 1){v = 16 * (1.0f - transform.getScale().z);}
                                if((Direction.EAST == quad.getFace() || Direction.WEST == quad.getFace()) && u > 15){u = 16 * transform.getScale().z;}
                                break;
                        }


                        builder.put(element,quad.getSprite().getInterpolatedU(u),quad.getSprite().getInterpolatedV(v),0,1);
                        break;
                    }
                    default: {
                        parent.put(element, data);
                        break;
                    }
                }
            }
        };
        quad.pipe(consumer);
        return builder.build();
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
        return Minecraft.getInstance().getTextureMap().getAtlasSprite("minecraft:block/stone");
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
                Vector3f vec = new Vector3f(1,0.5f,1);
                if(Block.getBlockFromItem(stack.getItem()) instanceof BlockBPMicroblock) {
                    VoxelShape size = ((BlockBPMicroblock) Block.getBlockFromItem(stack.getItem())).getSize();
                    vec = new Vector3f((float) size.getBoundingBox().getXSize(), (float)size.getBoundingBox().getYSize(), (float)size.getBoundingBox().getZSize());
                }
                return new BPMicroblockModel(block, vec);
            }
            Vector3f vec = new Vector3f(1,0.5f,1);
            if(Block.getBlockFromItem(stack.getItem()) instanceof BlockBPMicroblock) {
                VoxelShape size = ((BlockBPMicroblock) Block.getBlockFromItem(stack.getItem())).getSize();
                vec = new Vector3f((float) size.getBoundingBox().getXSize(), (float)size.getBoundingBox().getYSize(), (float)size.getBoundingBox().getZSize());
            }
            return new BPMicroblockModel(Blocks.STONE, vec);
        }
    }

}
