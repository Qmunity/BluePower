/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.block;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * Implemented by blocks/parts which need more control over the Silky removing. Like setting a flag so inventory contents don't get dropped.
 *
 * @author MineMaarten
 */
public interface IAdvancedSilkyRemovable extends ISilkyRemovable {

    /**
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @return return false to prevent silky removing.
     */
    public boolean preSilkyRemoval(World world, int x, int y, int z);

    public void postSilkyRemoval(World world, int x, int y, int z);

    /**
     *
     * @param world
     * @param x
     * @param y
     * @param z
     * @param tag
     * @return Return true if you want the "Has silky data" tooltip to be hidden
     */
    public boolean writeSilkyData(World world, int x, int y, int z, NBTTagCompound tag);

    public void readSilkyData(World world, int x, int y, int z, NBTTagCompound tag);
}
