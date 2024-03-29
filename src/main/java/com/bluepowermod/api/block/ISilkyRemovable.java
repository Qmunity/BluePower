/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.api.block;

/**
 * This interface, when implemented by a block, will be called by a Silky Screwdriver upon right clicking.
 * It will get the BlockEntity / parts, write the NBT to the item in the 'tileData' tag, and break the block/parts.
 * 
 * Now make sure on block/parts placement to call BPApi.getInstance().loadSilkySettings(args) to load the tag back.
 * @author MineMaarten
 */
public interface ISilkyRemovable {
}
