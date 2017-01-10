/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.client.render;

import com.bluepowermod.block.worldgen.BlockStoneOreConnected;
import com.bluepowermod.client.render.model.BakedModelLoader;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.tile.tier1.TileLamp;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class Renderers {

    public static int RenderIdLamps;

    public static void preinit() {

        for (Item item : BPItems.renderlist) {
            if (!(item instanceof IItemColor)) {
                registerItemModel(item, 0);
            }else {
                NonNullList<ItemStack> subitems = NonNullList.create();
                item.getSubItems(item, null, subitems);
                for (ItemStack subitem : subitems){
                    registerItemModel(item, item.getMetadata(subitem));
                }
            }
        }

        ModelLoaderRegistry.registerLoader(new BakedModelLoader());

        for (Block block : BPBlocks.renderlist) {
            if (!(block instanceof BlockStoneOreConnected)) {
                registerItemModel(Item.getItemFromBlock(block), 0);
            } else {
                registerBakedModel(block);
            }
        }


        ClientRegistry.bindTileEntitySpecialRenderer(TileLamp.class, new RenderLamp());
    }

    public static void init() {
        for (Item item : BPItems.renderlist) {
            if (item instanceof IItemColor) {
                Minecraft.getMinecraft().getItemColors().registerItemColorHandler((IItemColor) item, item);
            }
        }
    }

    public static void registerItemModel(Item item, int metadata) {
        ResourceLocation loc = Item.REGISTRY.getNameForObject(item);
        ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(loc, "inventory"));
    }

    public static void registerBakedModel(Block block) {
        ((BlockStoneOreConnected) block).initModel();
    }






}
