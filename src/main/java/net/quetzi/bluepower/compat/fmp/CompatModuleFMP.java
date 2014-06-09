/*
 * This file is part of Blue Power.
 *
 *     Blue Power is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     Blue Power is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with Blue Power.  If not, see <http://www.gnu.org/licenses/>
 */

package net.quetzi.bluepower.compat.fmp;

import codechicken.microblock.BlockMicroMaterial;
import codechicken.microblock.MicroMaterialRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.quetzi.bluepower.compat.CompatModule;
import net.quetzi.bluepower.init.BPBlocks;

public class CompatModuleFMP extends CompatModule {

    @Override
    public void preInit(FMLPreInitializationEvent ev) {

    }

    @Override
    public void init(FMLInitializationEvent ev) {

        RegisterMultiparts.register();

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
        registerBlockAsMicroblock(BPBlocks.tin_block);
    }

    private void registerBlockAsMicroblock(Block b) {

        MicroMaterialRegistry.registerMaterial(new BlockMicroMaterial(b, 0), b.getUnlocalizedName());
    }
}
