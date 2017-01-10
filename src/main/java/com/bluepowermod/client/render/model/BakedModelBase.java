package com.bluepowermod.client.render.model;


import com.google.common.base.Function;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.property.IExtendedBlockState;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.bluepowermod.block.worldgen.BlockStoneOreConnected.CONNECTED;

/**
* @author MoreThanHidden
*/

public class BakedModelBase implements IBakedModel{
    private Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
    private List<ResourceLocation> sprite;
    private TextureAtlasSprite defaultTexture;
    private VertexFormat format;

    public static int[] iconRefByID = { 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2,
            27, 14, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 4, 4, 5, 5,
            4, 4, 5, 5, 17, 17, 22, 26, 17, 17, 22, 26, 16, 16, 20, 20, 16, 16, 28, 28, 21, 21, 46, 42, 21, 21, 43, 38, 4, 4, 5, 5, 4, 4,
            5, 5, 9, 9, 30, 12, 9, 9, 30, 12, 16, 16, 20, 20, 16, 16, 28, 28, 25, 25, 45, 37, 25, 25, 40, 32, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3,
            19, 15, 3, 3, 19, 15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 0, 0, 6, 6, 0, 0, 6, 6, 3, 3, 19, 15, 3, 3, 19,
            15, 1, 1, 18, 18, 1, 1, 13, 13, 2, 2, 23, 31, 2, 2, 27, 14, 4, 4, 5, 5, 4, 4, 5, 5, 17, 17, 22, 26, 17, 17, 22, 26, 7, 7, 24,
            24, 7, 7, 10, 10, 29, 29, 44, 41, 29, 29, 39, 33, 4, 4, 5, 5, 4, 4, 5, 5, 9, 9, 30, 12, 9, 9, 30, 12, 7, 7, 24, 24, 7, 7, 10,
            10, 8, 8, 36, 35, 8, 8, 34, 11 };

    public BakedModelBase(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, Collection<ResourceLocation> locations) {
        this.format = format;
        this.sprite = new ArrayList<ResourceLocation>(locations);
        this.bakedTextureGetter = bakedTextureGetter;

        defaultTexture = bakedTextureGetter.apply(sprite.get(0));

    }

    public ResourceLocation getTexture(List<Boolean> connected, int side) {

        boolean[] bitMatrix = new boolean[8];

        if (side == 0 || side == 1) {
            bitMatrix[0] = connected.get(15); //nw
            bitMatrix[1] = connected.get(0);  //n
            bitMatrix[2] = connected.get(14); //ne
            bitMatrix[3] = connected.get(2);  //w
            bitMatrix[4] = connected.get(3);  //e
            bitMatrix[5] = connected.get(17); //sw
            bitMatrix[6] = connected.get(1);  //s
            bitMatrix[7] = connected.get(16); //se
        }
        if (side == 2 || side == 3) {
            bitMatrix[0] = side == 2 ? connected.get(6) : connected.get(7);     //ue or uw
            bitMatrix[1] = connected.get(4);                                    //u
            bitMatrix[2] = side == 3 ? connected.get(6) : connected.get(7);     //ue or uw
            bitMatrix[3] = side == 2 ? connected.get(3) : connected.get(2);     //e or w
            bitMatrix[4] = side == 3 ? connected.get(3) : connected.get(2);     //e or w
            bitMatrix[5] = side == 2 ? connected.get(10) : connected.get(11);   //de or dw
            bitMatrix[6] = connected.get(5);                                    //d
            bitMatrix[7] = side == 3 ? connected.get(10) : connected.get(11);   //de or dw
        }
        if (side == 4 || side == 5) {
            bitMatrix[0] = side == 5 ? connected.get(8) : connected.get(9);     //us or un
            bitMatrix[1] = connected.get(4);                                    //u
            bitMatrix[2] = side == 4 ? connected.get(8) : connected.get(9);     //us or un
            bitMatrix[3] = side == 5 ? connected.get(1) : connected.get(0);     //s or n
            bitMatrix[4] = side == 4 ? connected.get(1) : connected.get(0);     //s or n
            bitMatrix[5] = side == 5 ? connected.get(12) : connected.get(13);   //ds or dn
            bitMatrix[6] = connected.get(5);                                    //d
            bitMatrix[7] = side == 4 ? connected.get(12) : connected.get(13);   //ds or dn
        }

        int idBuilder = 0;

        for (int i = 0; i <= 7; i++)
            idBuilder = idBuilder
                    + (bitMatrix[i] ? (i == 0 ? 1 : (i == 1 ? 2 : (i == 2 ? 4 : (i == 3 ? 8 : (i == 4 ? 16 : (i == 5 ? 32 : (i == 6 ? 64
                    : 128))))))) : 0);

        return idBuilder > 255 || idBuilder < 0 ? sprite.get(0) : sprite.get(iconRefByID[idBuilder]);

    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {

        if (side != null) {
            return Collections.emptyList();
        }

        IExtendedBlockState extendedBlockState = (IExtendedBlockState)state;
        List<Boolean> connected = new ArrayList<Boolean>();
        for(int i = 0; i < 18; i++){
            connected.add(extendedBlockState.getValue(CONNECTED.get(i)));
        }
        List<BakedQuad> quads = new ArrayList<BakedQuad>();

        //Add side quads if not connected
        if(!connected.get(4)) {
            quads.add(createQuad(new Vec3d(0, 1, 1), new Vec3d(1, 1, 1), new Vec3d(1, 1, 0), new Vec3d(0, 1, 0), bakedTextureGetter.apply(getTexture(connected, 0))));
        }
        if(!connected.get(5)) {
            quads.add(createQuad(new Vec3d(1, 0, 1), new Vec3d(0, 0, 1), new Vec3d(0, 0, 0), new Vec3d(1, 0, 0), bakedTextureGetter.apply(getTexture(connected, 1))));
        }
        if(!connected.get(3)) {
            quads.add(createQuad(new Vec3d(1, 0, 1), new Vec3d(1, 0, 0), new Vec3d(1, 1, 0), new Vec3d(1, 1, 1), bakedTextureGetter.apply(getTexture(connected, 5))));
        }
        if(!connected.get(2)) {
            quads.add(createQuad(new Vec3d(0, 0, 0), new Vec3d(0, 0, 1), new Vec3d(0, 1, 1), new Vec3d(0, 1, 0), bakedTextureGetter.apply(getTexture(connected, 4))));
        }
        if(!connected.get(0)) {
            quads.add(createQuad(new Vec3d(1, 0, 0), new Vec3d(0, 0, 0), new Vec3d(0, 1, 0), new Vec3d(1, 1, 0), bakedTextureGetter.apply(getTexture(connected, 2))));
        }
        if(!connected.get(1)) {
            quads.add(createQuad(new Vec3d(0, 0, 1), new Vec3d(1, 0, 1), new Vec3d(1, 1, 1), new Vec3d(0, 1, 1), bakedTextureGetter.apply(getTexture(connected, 3))));
        }

        return quads;
    }

    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v1.subtract(v2).crossProduct(v3.subtract(v2));

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        putVertex(builder, normal, v1.xCoord, v1.yCoord, v1.zCoord, 0, 16, sprite);
        putVertex(builder, normal, v2.xCoord, v2.yCoord, v2.zCoord, 16, 16, sprite);
        putVertex(builder, normal, v3.xCoord, v3.yCoord, v3.zCoord, 16, 0, sprite);
        putVertex(builder, normal, v4.xCoord, v4.yCoord, v4.zCoord, 0, 0, sprite);
        return builder.build();
    }

    private void putVertex(UnpackedBakedQuad.Builder builder, Vec3d normal, double x, double y, double z, float u, float v, TextureAtlasSprite sprite) {
        for (int e = 0; e < format.getElementCount(); e++) {
            switch (format.getElement(e).getUsage()) {
                case POSITION:
                    builder.put(e, (float)x, (float)y, (float)z, 1.0f);
                    break;
                case COLOR:
                    builder.put(e, 1.0f, 1.0f, 1.0f, 1.0f);
                    break;
                case UV:
                    if (format.getElement(e).getIndex() == 0) {
                        u = sprite.getInterpolatedU(u);
                        v = sprite.getInterpolatedV(v);
                        builder.put(e, u, v, 0f, 1f);
                        break;
                    }
                case NORMAL:
                    builder.put(e, (float) normal.xCoord, (float) normal.yCoord, (float) normal.zCoord, 0f);
                    break;
                default:
                    builder.put(e);
                    break;
            }
        }
    }

    @Override
    public boolean isAmbientOcclusion() {
        return false;
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
        return defaultTexture;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return null;
    }
}
