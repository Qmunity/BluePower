/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.compat.fmp;

import net.minecraft.block.Block;
import net.minecraftforge.client.MinecraftForgeClient;
import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;

import com.bluepowermod.compat.CompatModule;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class CompatModuleFMP extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

    }

    @Override
    public void init(FMLInitializationEvent ev) {

        registerBlocksAsMicroblock();
    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    private void registerBlocksAsMicroblock() {

        registerBlockAsMicroblock(BPBlocks.basalt);
        registerBlockAsMicroblock(BPBlocks.basalt_brick);
        registerBlockAsMicroblock(BPBlocks.basalt_brick_small);
        registerBlockAsMicroblock(BPBlocks.basalt_cobble);
        registerBlockAsMicroblock(BPBlocks.basalt_tile);
        registerBlockAsMicroblock(BPBlocks.basalt_paver);

        registerBlockAsMicroblock(BPBlocks.fancy_basalt);
        registerBlockAsMicroblock(BPBlocks.fancy_marble);

        registerBlockAsMicroblock(BPBlocks.marble);
        registerBlockAsMicroblock(BPBlocks.marble_brick);
        registerBlockAsMicroblock(BPBlocks.marble_brick_small);
        registerBlockAsMicroblock(BPBlocks.marble_tile);
        registerBlockAsMicroblock(BPBlocks.marble_paver);

        registerBlockAsMicroblock(BPBlocks.amethyst_block);
        registerBlockAsMicroblock(BPBlocks.ruby_block);
        registerBlockAsMicroblock(BPBlocks.sapphire_block);
        registerBlockAsMicroblock(BPBlocks.copper_block);
        registerBlockAsMicroblock(BPBlocks.silver_block);
        registerBlockAsMicroblock(BPBlocks.zinc_block);
    }

    private void registerBlockAsMicroblock(Block b) {

        MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(b, 0), b.getUnlocalizedName());
    }

    @Override
    public void registerBlocks() {

    }

    @Override
    public void registerItems() {

    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerRenders() {

        SawRenderFMP sawRender = new SawRenderFMP();
        MinecraftForgeClient.registerItemRenderer(BPItems.ruby_saw, sawRender);
        MinecraftForgeClient.registerItemRenderer(BPItems.amethyst_saw, sawRender);
        MinecraftForgeClient.registerItemRenderer(BPItems.sapphire_saw, sawRender);
    }

}
