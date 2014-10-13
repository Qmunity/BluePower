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

package com.bluepowermod.blocks.worldgen;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.util.Refs;
import com.qmunity.lib.block.BlockBase;

public class BlockItemOre extends BlockBase {

    protected Random rand = new Random();

    public BlockItemOre(String type) {

        super(Material.iron);
        setCreativeTab(CustomTabs.tabBluePowerBlocks);
        setStepSound(soundTypeStone);
        setHardness(2.5F);
        setResistance(10.0F);
        textureName = Refs.MODID + ":" + type;
        setBlockName(type);
        this.setHarvestLevel("pickaxe", 2);

    }

    @Override
    public int quantityDropped(Random rand) {

        return 1;
    }

    @Override
    protected boolean canSilkHarvest() {

        return true;
    }

    @Override
    public int quantityDroppedWithBonus(int quantity, Random rand) {

        if (quantity > 0 && Item.getItemFromBlock(this) != getItemDropped(0, rand, quantity)) {
            int j = rand.nextInt(quantity + 2) - 1;
            if (j < 0) {
                j = 0;
            }
            return this.quantityDropped(rand) * (j + 1);
        } else {
            return this.quantityDropped(rand);
        }
    }

    @Override
    protected String getModId() {
        return Refs.MODID;
    }
}
