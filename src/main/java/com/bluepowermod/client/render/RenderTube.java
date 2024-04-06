package com.bluepowermod.client.render;

import com.bluepowermod.container.stack.TubeStack;
import com.bluepowermod.tile.tier2.TileTube;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderTube implements BlockEntityRenderer<TileTube> {
    @Override
    public void render(TileTube blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        for(TubeStack tubeStack : blockEntity.tubeStacks){
            tubeStack.render(partialTick, poseStack, bufferSource, packedLight, packedOverlay);
        }
    }
}
