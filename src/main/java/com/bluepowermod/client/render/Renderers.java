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

    @SubscribeEvent
    public void onModelBakeEvent(ModelBakeEvent event) {
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

        //TODO: ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, new RenderLamp());
        //TODO: ClientRegistry.bindTileEntitySpecialRenderer(TileEngine.class, new RenderEngine());

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
