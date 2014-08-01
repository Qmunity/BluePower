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

package com.bluepowermod.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;

import com.bluepowermod.blocks.BlockContainerBase;
import com.bluepowermod.client.renderers.RendererBlockBase.EnumFaceType;
import com.bluepowermod.references.GuiIDs;
import com.bluepowermod.references.Refs;
import com.bluepowermod.tileentities.tier1.TileBlockBreaker;

public class BlockBlockBreaker extends BlockContainerBase {
    
    public BlockBlockBreaker() {
    
        super(Material.rock);
        setBlockName(Refs.BLOCKBREAKER_NAME);
    }
    
    @Override
    protected Class<? extends TileEntity> getTileEntity() {
    
        return TileBlockBreaker.class;
    }
    
    @Override
    public GuiIDs getGuiID() {
    
        return GuiIDs.INVALID;
    }
    
    @Override
    protected String getIconName(EnumFaceType faceType, boolean ejecting, boolean powered) {
    
        String iconName = textureName + "_" + faceType.toString().toLowerCase();
        if (faceType == EnumFaceType.FRONT) {
            if (ejecting) iconName += "_active";
        }
        return iconName;
    }
    
}
