/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.tile.tier1.TileLamp;
import com.bluepowermod.tile.tier3.TileEngine;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
@OnlyIn(Dist.CLIENT)
public class Renderers {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt){
        for (Block block : BPBlocks.blockList) {
            if ((block instanceof ICustomModelBlock)) {
                registerBakedModel(block);
            }
        }

    }

    //TODO: Remove all this when it gets reimplemented in forge

    @SubscribeEvent
    public void onTextureEvent(TextureStitchEvent.Pre event){
        event.addSprite(new ResourceLocation("bluepower:blocks/models/engineoff"));
        event.addSprite(new ResourceLocation("bluepower:blocks/models/engineon"));
    }

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {

        //Store the baked models for the inventory model
        List<IBakedModel> engine = new ArrayList<>();

        IUnbakedModel baseModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation("bluepower:block/engine/engine_base.obj"));
        if (baseModel instanceof OBJModel) {
            IBakedModel bakedModel = baseModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ForgeBlockStateV1.Transforms.get("forge:default-block").get(), false), DefaultVertexFormats.ITEM);
            engine.add(bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=down,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=down,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = baseModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X180_Y0, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=up,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=up,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = baseModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X270_Y90, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=east,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=east,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = baseModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X90_Y90, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=west,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=west,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = baseModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X270_Y0, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=north,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=north,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = baseModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X90_Y0, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=south,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=south,gear=false,glider=false,waterlogged=false"), bakedModel);
        }

        IUnbakedModel onModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation("bluepower:block/engine/engine_base_on.obj"));
        if (onModel instanceof OBJModel) {
            IBakedModel bakedModel = onModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ForgeBlockStateV1.Transforms.get("forge:default-block").get(), false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=down,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=down,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = onModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X180_Y0, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=up,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=up,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = onModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X270_Y90, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=east,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=east,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = onModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X90_Y90, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=west,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=west,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = onModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X270_Y0, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=north,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=north,gear=false,glider=false,waterlogged=false"), bakedModel);
            bakedModel = onModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(ModelRotation.X90_Y0, false), DefaultVertexFormats.ITEM);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=south,gear=false,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=true,facing=south,gear=false,glider=false,waterlogged=false"), bakedModel);
        }

        IUnbakedModel gearModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation("bluepower:block/engine/engine_gear.obj"));
        if (gearModel instanceof OBJModel) {
            IBakedModel bakedModel = gearModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(gearModel.getDefaultState(), false), DefaultVertexFormats.ITEM);
            engine.add(bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=down,gear=true,glider=false,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=down,gear=true,glider=false,waterlogged=false"), bakedModel);
        }

        IUnbakedModel gliderModel = ModelLoaderRegistry.getModelOrMissing(new ResourceLocation("bluepower:block/engine/engine_glider.obj"));
        if (gliderModel instanceof OBJModel) {
            IBakedModel bakedModel = gliderModel.bake(event.getModelLoader(), ModelLoader.defaultTextureGetter(), new BasicState(gliderModel.getDefaultState(), false), DefaultVertexFormats.ITEM);
            engine.add(bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=down,gear=false,glider=true,waterlogged=true"), bakedModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "active=false,facing=down,gear=false,glider=true,waterlogged=false"), bakedModel);
        }

        //Register Engine Inventory Model
        event.getModelRegistry().put(new ModelResourceLocation("bluepower:engine", "inventory"), new MergedBakedModel(engine));

        //Register Multipart Model
        event.getModelRegistry().put(new ModelResourceLocation("bluepower:multipart"), new BPMultipartModel());


        BPMicroblockModel microblockModel = new BPMicroblockModel();
        //Register Microblock Models
        //Register Microblock Models
        for(Direction dir : Direction.values()) {
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:half_block", "face=" + dir.getName()), event.getModelRegistry().get(new ModelResourceLocation("bluepower:half_block", "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:half_block", "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:half_block", "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:panel", "face=" + dir.getName()), event.getModelRegistry().get(new ModelResourceLocation("bluepower:panel", "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:panel", "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:panel", "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:cover", "face=" + dir.getName()), event.getModelRegistry().get(new ModelResourceLocation("bluepower:cover", "facing=" + dir.getName() + ",waterlogged=true")));
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:cover", "facing=" + dir.getName() + ",waterlogged=true"), microblockModel);
            event.getModelRegistry().put(new ModelResourceLocation("bluepower:cover", "facing=" + dir.getName() + ",waterlogged=false"), microblockModel);
        }
        event.getModelRegistry().put(new ModelResourceLocation("bluepower:half_block", "inventory"), microblockModel);
        event.getModelRegistry().put(new ModelResourceLocation("bluepower:panel", "inventory"), microblockModel);
        event.getModelRegistry().put(new ModelResourceLocation("bluepower:cover", "inventory"), microblockModel);

    }

    public static void init() {

        ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, new RenderLamp());
        ClientRegistry.bindTileEntitySpecialRenderer(TileEngine.class, new RenderEngine());

        for (Item item : BPItems.itemList) {
            if (item instanceof IBPColoredItem) {
                Minecraft.getInstance().getItemColors().register(new BPItemColor(), item);
            }
        }
        for (Block block : BPBlocks.blockList) {
            if (block instanceof IBPColoredBlock) {
                Minecraft.getInstance().getBlockColors().register(new BPBlockColor(), block);
                Minecraft.getInstance().getItemColors().register(new BPBlockColor(), Item.getItemFromBlock(block));
            }
        }

    }

    public static void registerBakedModel(Block block) {
        ((ICustomModelBlock) block).initModel();
    }





}
