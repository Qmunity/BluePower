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

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;

import com.bluepowermod.block.BlockBluePowerBase;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;

public class BlockItemOre extends BlockBluePowerBase {

    protected Random rand = new Random();

    public BlockItemOre(String type) {

        super(Material.iron);
        this.setCreativeTab(BPCreativeTabs.blocks);
        this.setStepSound(soundTypeStone);
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.textureName = Refs.MODID + ":" + type;
        this.setBlockName(type);
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

        if (quantity > 0 && Item.getItemFromBlock(this) != this.getItemDropped(0, rand, quantity)) {
            int j = rand.nextInt(quantity + 2) - 1;
            if (j < 0) {
                j = 0;
            }
            return this.quantityDropped(rand) * (j + 1);
        } else {
            return this.quantityDropped(rand);
        }
    }
}
