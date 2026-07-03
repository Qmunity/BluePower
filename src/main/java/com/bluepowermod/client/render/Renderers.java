/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.block.BlockBPMicroblock;
import com.bluepowermod.block.BlockBPMultipart;
import com.bluepowermod.block.gates.BlockGateBase;
import com.bluepowermod.block.lighting.BlockLampSurface;
import com.bluepowermod.block.power.BlockBattery;
import com.bluepowermod.block.worldgen.BlockBPGlass;
import com.bluepowermod.client.render.placement_preview.BlockPreviewRenderer;
import com.bluepowermod.client.render.placement_preview.PlacementPreviewReloadListener;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPClientConfig;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MoreThanHidden
 */
@Mod.EventBusSubscriber(modid = Refs.MODID, value = Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class Renderers {

    @SubscribeEvent
    public static void onRenderHighlightEvent(RenderHighlightEvent.Block event){
        if (BPClientConfig.CONFIG.renderPlacementPreview.get()){
            @SuppressWarnings("resource")
            LocalPlayer player = Minecraft.getInstance().player;
            if (player != null && player.level() != null) {
                InteractionHand hand = player.getUsedItemHand();
                ItemStack itemstack = player.getItemInHand(hand == null ? InteractionHand.MAIN_HAND : hand);
                Item item = itemstack.getItem();
                if (item instanceof BlockItem blockItem && PlacementPreviewReloadListener.INSTANCE.hasItem(item)) {
                    Block block = blockItem.getBlock();
                    Level world = player.level();
                    BlockHitResult rayTrace = event.getTarget();
                    Direction directionAwayFromTargetedBlock = rayTrace.getDirection();
                    BlockPos placePos = rayTrace.getBlockPos().relative(directionAwayFromTargetedBlock);

                    BlockState existingState = world.getBlockState(placePos);
                    boolean isMultipart = existingState.getBlock() instanceof IBPPartBlock || existingState.getBlock() instanceof BlockBPMultipart;
                    if (existingState.isAir() || existingState.canBeReplaced() ||
                            (block instanceof IBPPartBlock && isMultipart)) {
                        // only render the preview if we know it would make sense for the block to be placed where we expect it to be
                        BlockState state = block.getStateForPlacement(new BlockPlaceContext(world, player, hand, itemstack, rayTrace));
                        if (isMultipart && block instanceof IBPPartBlock partBlock){
                            if (AABBUtils.testOcclusion(partBlock.getOcclusionShape(state), existingState.getShape(world, placePos))){
                                return;
                            }
                        }
                        BlockPreviewRenderer.renderBlockPreview(placePos, state, world, event.getCamera().getPosition(), event.getPoseStack(), event.getMultiBufferSource());
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void registerModels(ModelEvent.RegisterAdditional evt){
        for (Block block : BPBlocks.blockList) {
            if ((block instanceof ICustomModelBlock)) {
                registerBakedModel(block);
            }
        }
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelEvent.ModifyBakingResult event) {
        PlacementPreviewReloadListener.BAKERY = event.getModelBakery();
        //Register Multipart and Microblock Baked Models
        BPMultipartModel multipartModel = new BPMultipartModel();
        BPMicroblockModel microblockModel = new BPMicroblockModel();

        Map<ResourceLocation, BakedModel> models = new HashMap<>(event.getModels());
        models.forEach((key, model) -> {
            if(key.toString().contains("bluepower:cover") || key.toString().contains("bluepower:panel") || key.toString().contains("bluepower:half_block")){
                if(((ModelResourceLocation)key).getVariant().contains("waterlogged=true")){
                    event.getModels().put(new ModelResourceLocation(key.getNamespace(), key.getPath(), ((ModelResourceLocation)key).getVariant().replace("facing", "face").split(",")[0]), model);
                }
                event.getModels().put(key, microblockModel);
            }
            if(key.toString().contains("bluepower:multipart")){
                event.getModels().put(key, multipartModel);
            }
        });

        event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:half_block"), "inventory"), microblockModel);
        event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:panel"), "inventory"), microblockModel);
        event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:cover"), "inventory"), microblockModel);

    }

    @SubscribeEvent
    public void onRegisterGeometryLoadersEvent(RegisterGeometryLoaders event){
        event.register("gate", new GateModelLoader());
        event.register("wire", new BPWireModelLoader());
    }

    @SubscribeEvent
    public void onClientReload(RegisterClientReloadListenersEvent event){
        event.registerReloadListener(PlacementPreviewReloadListener.INSTANCE);
    }

    public static void init() {

        BlockEntityRenderers.register(BPBlockEntityType.LAMP.get(), context -> new RenderLamp());
        BlockEntityRenderers.register(BPBlockEntityType.TUBE.get(), context -> new RenderTube());
        BlockEntityRenderers.register(BPBlockEntityType.ENGINE.get(), context -> new RenderEngine());

        for (RegistryObject<Item> item : BPItems.ITEMS.getEntries()) {
            if (item.get() instanceof IBPColoredItem) {
                Minecraft.getInstance().getItemColors().register(new BPItemColor(), item.get());
            }
        }
        for (Block block : BPBlocks.blockList) {
            if (block instanceof IBPColoredBlock) {
                Minecraft.getInstance().getBlockColors().register(new BPBlockColor(), block);
                Minecraft.getInstance().getItemColors().register(new BPBlockColor(), Item.byBlock(block));
            }
            if(block instanceof BlockLampSurface || block instanceof BlockGateBase || block instanceof BlockBattery)
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.cutout());
            if(block instanceof BlockBPGlass || block instanceof BlockBPMicroblock || block instanceof BlockBPMultipart)
                ItemBlockRenderTypes.setRenderLayer(block, RenderType.translucent());
        }

        ItemBlockRenderTypes.setRenderLayer(BPBlocks.blulectric_cable.get(), RenderType.translucent());

        ItemBlockRenderTypes.setRenderLayer(BPBlocks.indigo_flower.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.flax_crop.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.cracked_basalt_lava.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.cracked_basalt_decorative.get(), RenderType.cutout());
        //ItemBlockRenderTypes.setRenderLayer(BPBlocks.rubber_leaves.get(), RenderType.cutout());
        //ItemBlockRenderTypes.setRenderLayer(BPBlocks.rubber_sapling.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(BPBlocks.tube.get(), RenderType.cutout());

    }

    public static void registerBakedModel(Block block) {
        ((ICustomModelBlock) block).initModel();
    }

}
