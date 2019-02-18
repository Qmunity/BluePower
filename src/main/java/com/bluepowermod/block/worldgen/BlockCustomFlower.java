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

package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.SoundType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.EnumPlantType;

public class BlockCustomFlower extends BlockBush {

    public static final String[] field_149858_b = new String[] { "indigo_flower" };
    // private int meta;

    public BlockCustomFlower(String name) {

        super();
        this.setCreativeTab(BPCreativeTabs.blocks);
        this.setHardness(0.0F);
        this.setSoundType(SoundType.PLANT);
        this.setTranslationKey(name);
        this.setRegistryName(Refs.MODID, name);
        BPBlocks.blockList.add(this);
    }

    @Override
    public String getTranslationKey() {

        return String.format("tile.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getTranslationKey()));
    }

    String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    public static BlockFlower func_149857_e(String name) {

        String[] astring = field_149858_b;
        int i = astring.length;
        int j;
        String s1;

        for (j = 0; j < i; ++j) {
            s1 = astring[j];

            if (s1.equals(name)) {
                return (BlockFlower) BPBlocks.indigo_flower;
            }
        }
        return null;
    }

    public static int func_149856_f(String name) {

        int i;

        for (i = 0; i < field_149858_b.length; ++i) {
            if (field_149858_b[i].equals(name)) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public EnumPlantType getPlantType(IBlockAccess world, BlockPos pos) {
        return EnumPlantType.Plains;
    }
}
