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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.core.Direction;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.registries.RegistryObject;

/**
 * @author MoreThanHidden
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class Renderers {

    @SubscribeEvent
    public static void registerModels(ModelEvent.RegisterAdditional evt){
        for (Block block : BPBlocks.blockList) {
            if ((block instanceof ICustomModelBlock)) {
                registerBakedModel(block);
            }
        }
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelEvent.ModifyBakingResult event) {

        //Register Multipart Model
        BPMultipartModel multipartModel = new BPMultipartModel();
        event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:multipart"),"waterlogged=false"), multipartModel);
        event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:multipart"),"waterlogged=true"), multipartModel);

        BPMicroblockModel microblockModel = new BPMicroblockModel();

        for(Direction dir : Direction.values()) {
            //Register Microblock Models
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:half_block"), "face=" + dir.getName()), event.getModels().get(new ModelResourceLocation(new ResourceLocation("bluepower:half_block"), "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:half_block"), "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:half_block"), "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:panel"), "face=" + dir.getName()), event.getModels().get(new ModelResourceLocation(new ResourceLocation("bluepower:panel"), "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:panel"), "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:panel"), "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:cover"), "face=" + dir.getName()), event.getModels().get(new ModelResourceLocation(new ResourceLocation("bluepower:cover"), "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:cover"), "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:cover"), "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);
        }

        event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:half_block"), "inventory"), microblockModel);
        event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:panel"), "inventory"), microblockModel);
        event.getModels().put(new ModelResourceLocation(new ResourceLocation("bluepower:cover"), "inventory"), microblockModel);

    }

    public static void init() {

        BlockEntityRenderers.register(BPBlockEntityType.LAMP.get(), context -> new RenderLamp());
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
