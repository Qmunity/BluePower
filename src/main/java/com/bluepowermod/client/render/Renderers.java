/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.block.BlockBPMicroblock;
import com.bluepowermod.block.BlockBPMultipart;
import com.bluepowermod.block.gates.BlockGateBase;
import com.bluepowermod.block.lighting.BlockLampSurface;
import com.bluepowermod.block.power.BlockBattery;
import com.bluepowermod.block.worldgen.BlockBPGlass;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.BPBlockEntityType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


import net.minecraft.client.resources.model.ModelResourceLocation;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.registries.DeferredHolder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author MoreThanHidden
 */
@OnlyIn(Dist.CLIENT)
public class Renderers {

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

        //Register Multipart and Microblock Baked Models
        BPMultipartModel multipartModel = new BPMultipartModel();
        BPMicroblockModel microblockModel = new BPMicroblockModel();

        Map<ModelResourceLocation, BakedModel> models = new HashMap<>(event.getModels());
        models.forEach((key, model) -> {
            if(key.toString().contains("bluepower:cover") || key.toString().contains("bluepower:panel") || key.toString().contains("bluepower:half_block")){
                if(key.getVariant().contains("waterlogged=true")){
                    event.getModels().put(new ModelResourceLocation(key.id(), key.getVariant().replace("facing", "face").split(",")[0]), model);
                }
                event.getModels().put(key, microblockModel);
            }
            if(key.toString().contains("bluepower:multipart")){
                event.getModels().put(key, multipartModel);
            }
        });

        event.getModels().put(new ModelResourceLocation(ResourceLocation.parse("bluepower:half_block"), "inventory"), microblockModel);
        event.getModels().put(new ModelResourceLocation(ResourceLocation.parse("bluepower:panel"), "inventory"), microblockModel);
        event.getModels().put(new ModelResourceLocation(ResourceLocation.parse("bluepower:cover"), "inventory"), microblockModel);

    }

    public static void init() {

        BlockEntityRenderers.register(BPBlockEntityType.LAMP.get(), context -> new RenderLamp());
        BlockEntityRenderers.register(BPBlockEntityType.TUBE.get(), context -> new RenderTube());
        BlockEntityRenderers.register(BPBlockEntityType.ENGINE.get(), context -> new RenderEngine());

        for (DeferredHolder<Item, ? extends Item> item : BPItems.ITEMS.getEntries()) {
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
