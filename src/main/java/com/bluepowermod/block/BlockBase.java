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

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.init.BPBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockBase extends Block {

    private boolean wip = false;
    
    public BlockBase(Material material) {
        super(Properties.of(material).sound(SoundType.STONE).strength(3.0F));
        BPBlocks.blockList.add(this);
    }

    public BlockBase(Properties properties) {
        super(properties);
        BPBlocks.blockList.add(this);
    }

    public boolean getWIP(){
        return wip;
    }

    public BlockBase setWIP(boolean wip) {

        this.wip = wip;
        return this;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable IBlockReader world, List<ITextComponent> tooltip, ITooltipFlag advanced) {
        super.appendHoverText(stack, world, tooltip, advanced);
        if(wip){
            tooltip.add(new StringTextComponent(MinecraftColor.RED.getChatColor() + "WIP") );
        }
    }
    
}
