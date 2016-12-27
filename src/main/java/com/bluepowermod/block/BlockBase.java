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

package com.bluepowermod.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;

public abstract class BlockBase extends Block {
    
    public BlockBase(Material material) {
    
        super(material);
        setSoundType(SoundType.STONE);
        setCreativeTab(BPCreativeTabs.machines);
        blockHardness = 3.0F;
    }
    
    @Override
    public String getUnlocalizedName() {
    
        return String.format("tile.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }
    
    protected String getUnwrappedUnlocalizedName(String name) {
    
        return name.substring(name.indexOf(".") + 1);
    }
    
}
