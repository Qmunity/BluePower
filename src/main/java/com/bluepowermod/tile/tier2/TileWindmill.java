/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */
package com.bluepowermod.tile.tier2;

import com.bluepowermod.init.BPBlockEntityType;
import com.bluepowermod.tile.TileBase;
import net.minecraft.client.renderer.texture.Tickable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TileWindmill extends TileBase implements Tickable {

	
	public int turbineTick;
	public TileWindmill(BlockPos pos, BlockState state){
		super(BPBlockEntityType.WINDMILL.get(), pos, state);
		turbineTick = 0;
	}

	@Override
	public void tick() {
		if(level.isClientSide){
			turbineTick++;
		}
	}
}
