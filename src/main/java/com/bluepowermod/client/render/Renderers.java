/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.block.machine.BlockLamp;
import com.bluepowermod.client.render.model.BakedModelLoader;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod.EventBusSubscriber(Side.CLIENT)
@SideOnly(Side.CLIENT)
public class Renderers {

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent evt){
        for (Item item : BPItems.itemList) {
            if (!(item instanceof IItemColor)) {
                registerItemModel(item, 0);
            } else {
                NonNullList<ItemStack> subitems = NonNullList.create();
                item.getSubItems(item.getCreativeTab(), subitems);
                for (ItemStack subitem : subitems) {
                    registerItemModel(item, item.getMetadata(subitem));
                }
            }
        }

        for (Block block : BPBlocks.blockList) {
            if (!(block instanceof ICustomModelBlock)) {
                registerItemModel(Item.getItemFromBlock(block), 0);
            } else {
                registerBakedModel(block);
            }
        }

    }

    public static void preinit() {
        ModelLoaderRegistry.registerLoader(new BakedModelLoader());

    }

    public static void init() {

        ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, new RenderLamp());

        for (Item item : BPItems.itemList) {
            if (item instanceof IItemColor) {
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler((IItemColor) item, item);
            }
        }
        for (final Block block : BPBlocks.blockList) {
            if (block instanceof BlockLamp) {
                Minecraft.getMinecraft().getBlockColors().registerBlockColorHandler((IBlockColor) block, block);
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler((IItemColor) block, Item.getItemFromBlock(block));
            }
        }

    }

    public static void registerItemModel(Item item, int metadata) {
        ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
        if(loc != null) {
            ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(loc, "inventory"));
        }
    }

    public static void registerBakedModel(Block block) {
        ((ICustomModelBlock) block).initModel();
    }





}
