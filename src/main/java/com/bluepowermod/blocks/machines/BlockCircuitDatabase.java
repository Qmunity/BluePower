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

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import com.bluepowermod.helper.ItemStackDatabase;
import com.bluepowermod.network.NetworkHandler;
import com.bluepowermod.network.messages.MessageSendClientServerTemplates;
import com.bluepowermod.tileentities.tier3.TileCircuitDatabase;
import com.qmunity.lib.tileentity.TileBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockCircuitDatabase extends BlockProjectTable {
    
    public BlockCircuitDatabase(Class<? extends TileBase> tileClass) {
    
        super(tileClass);
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
    
        if (side < 2) {
            return super.getIcon(world, x, y, z, side);
        } else {
            return textureSide;
        }
        
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister) {
    
        textureTop = iconRegister.registerIcon(getTextureName() + "_top");
        textureBottom = iconRegister.registerIcon(getTextureName() + "_bottom");
        textureSide = iconRegister.registerIcon(getTextureName() + "_side");
    }
    
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
    
        if (super.onBlockActivated(world, x, y, z, player, par6, par7, par8, par9)) {
            TileCircuitDatabase database = (TileCircuitDatabase) world.getTileEntity(x, y, z);
            database.clientCurrentTab = 0;
            if (!world.isRemote) {
                NetworkHandler.sendTo(new MessageSendClientServerTemplates(new ItemStackDatabase().loadItemStacks()), (EntityPlayerMP) player);
            }
            return true;
        } else {
            return false;
        }
    }
    
}
