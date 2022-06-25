/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.block.power;

import com.bluepowermod.block.BlockContainerHorizontalFacingBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.BPBlockEntityType;
import com.bluepowermod.tile.tier3.TileThermopile;
import net.minecraft.world.level.material.Material;

/**
 * @author MoreThanHidden
 */

public class BlockThermopile extends BlockContainerHorizontalFacingBase {

    public BlockThermopile() {
        super(Material.METAL, TileThermopile.class, BPBlockEntityType.THERMOPILE);
        setRegistryName(Refs.MODID, Refs.THERMOPILE_NAME);
    }

}
