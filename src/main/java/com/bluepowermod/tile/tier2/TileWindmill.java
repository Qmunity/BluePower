/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import com.bluepowermod.tile.BPTileEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.client.renderer.texture.ITickable;

public class TileWindmill extends TileBase implements ITickable {

	
	public int turbineTick;
	public TileWindmill(){
		super(BPTileEntityType.WINDMILL);

		turbineTick = 0;
	}

	@Override
	public void tick() {
		if(level.isClientSide){
			turbineTick++;
		}
	}
}
