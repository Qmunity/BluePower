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

package com.bluepowermod.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.init.CustomTabs;
import com.bluepowermod.references.Refs;

import java.util.Random;

public class BlockItemOre extends BlockBase {

    private Random rand = new Random();

    public BlockItemOre(String type) {

        super(Material.iron);
        this.setCreativeTab(CustomTabs.tabBluePowerBlocks);
        this.setStepSound(soundTypeStone);
        this.setHardness(2.5F);
        this.setResistance(10.0F);
        this.textureName = Refs.MODID + ":" + type;
        this.setBlockName(type);
    }

    @Override
    public int quantityDropped(Random rand) {

        return this == BPBlocks.nikolite_ore ? 4 + rand.nextInt(2) : 1;
    }

    @Override
    public Item getItemDropped(int par1, Random par2, int par3) {

        return getDropFromBlockName(this.getUnlocalizedName().substring(5));
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

    @Override
    public int getExpDrop(IBlockAccess par1, int par2, int par3) {

        if (this.getItemDropped(par2, rand, par3) != Item.getItemFromBlock(this)) {
            int j1 = 0;
            if (this == BPBlocks.amethyst_ore) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 3, 7);
            } else if (this == BPBlocks.ruby_ore) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 3, 7);
            } else if (this == BPBlocks.sapphire_ore) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 3, 7);
            } else if (this == BPBlocks.nikolite_ore) {
                j1 = MathHelper.getRandomIntegerInRange(rand, 2, 5);
            }
            return j1;
        }
        return 0;
    }

    public static Item getDropFromBlockName(String blockName) {

        if (blockName.equalsIgnoreCase(Refs.NIKOLITEORE_NAME)) {
            return BPItems.nikolite;
        } else if (blockName.equalsIgnoreCase(Refs.RUBYORE_NAME)) {
            return BPItems.ruby;
        } else if (blockName.equalsIgnoreCase(Refs.SAPPHIREORE_NAME)) {
            return BPItems.sapphire;
        } else if (blockName.equalsIgnoreCase(Refs.AMETHYSTORE_NAME)) {
            return BPItems.amethyst;
        } else {
            return null;
        }
    }
}
