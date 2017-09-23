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
import net.minecraft.util.NonNullList;
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

public class PartBakedModel implements IBakedModel{
    private Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter;
    private List<ResourceLocation> sprite;
    private TextureAtlasSprite defaultTexture;
    private VertexFormat format;


    public PartBakedModel(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, Collection<ResourceLocation> locations) {
        this.format = format;
        this.sprite = new ArrayList<ResourceLocation>(locations);
        this.bakedTextureGetter = bakedTextureGetter;

        this.defaultTexture = bakedTextureGetter.apply(sprite.get(0));

    }


    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {

        if (side != null) {
            return Collections.emptyList();
        }

        IExtendedBlockState extendedBlockState = (IExtendedBlockState)state;
        List<Boolean> connected = NonNullList.withSize(18, false);
         try {
            for (int i = 0; i < 18; i++) {
                connected.set(i, extendedBlockState.getValue(CONNECTED.get(i)));
            }
         }catch (NullPointerException ignored){}

        List<BakedQuad> quads = new ArrayList<BakedQuad>();

/*         if (state.getBlock() instanceof  ITilePartHolder){
            for ( IPart part : ((ITilePartHolder)state.getBlock()).getParts()  ) {
                part.renderDynamic(null, 0, 0);
            }
        }*/
        return quads;
    }

    private BakedQuad createQuad(Vec3d v1, Vec3d v2, Vec3d v3, Vec3d v4, TextureAtlasSprite sprite) {
        Vec3d normal = v1.subtract(v2).crossProduct(v3.subtract(v2));

        UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
        builder.setTexture(sprite);
        putVertex(builder, normal, v1.x, v1.y, v1.z, 0, 16, sprite);
        putVertex(builder, normal, v2.x, v2.y, v2.z, 16, 16, sprite);
        putVertex(builder, normal, v3.x, v3.y, v3.z, 16, 0, sprite);
        putVertex(builder, normal, v4.x, v4.y, v4.z, 0, 0, sprite);
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
                    builder.put(e, (float) normal.x, (float) normal.y, (float) normal.z, 0f);
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
        return this.defaultTexture;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public ItemOverrideList getOverrides() {
        return ItemOverrideList.NONE;
    }

}
