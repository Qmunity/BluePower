/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.bluestone;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.bluepowermod.api.vec.Vector3;
import com.jcraft.jorbis.Block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public interface IBluestoneApi {

    public void registerSpecialConnection(Block block, int metadata, ForgeDirection cableSide, int extraLength);

    public void registerSpecialConnection(TileEntity te, ForgeDirection cableSide, int extraLength);

    public void registerSpecialConnection(ABluestoneConnect connection);

    public int getExtraLength(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide);

    public boolean canConnect(Vector3 block, IBluestoneWire wire, ForgeDirection cableSide);

    @SideOnly(Side.CLIENT)
    public void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz);

    @SideOnly(Side.CLIENT)
    public void renderBox(int minx, int miny, int minz, int maxx, int maxy, int maxz, int textureSize);

    public void renderExtraCables(Vector3 block, IBluestoneWire wire, ForgeDirection side);

}
