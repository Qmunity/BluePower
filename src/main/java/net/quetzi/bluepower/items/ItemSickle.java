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

package net.quetzi.bluepower.items;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.CustomTabs;
import net.quetzi.bluepower.references.Refs;

import java.util.Set;

public class ItemSickle extends ItemTool {

    private static final Set toolBlocks = Sets.newHashSet(new Block[] { Blocks.leaves, Blocks.leaves2, Blocks.wheat, Blocks.potatoes, Blocks.carrots,
            Blocks.nether_wart, Blocks.red_mushroom, Blocks.brown_mushroom, Blocks.reeds, Blocks.tallgrass, Blocks.vine, Blocks.waterlily,
            Blocks.red_flower, Blocks.yellow_flower });

    public ItemSickle(ToolMaterial material, String name) {

        super(1.0F, material, toolBlocks);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerTools);
        this.setTextureName(Refs.MODID + ":" + name);
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("item.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    public float func_150893_a(ItemStack itemStack, Block block) {

        if ((block.getMaterial() == Material.leaves) || (block.getMaterial() == Material.plants) || (block.getMaterial() == Material.grass)
                || toolBlocks.contains(block)) {

            return this.efficiencyOnProperMaterial;
        }
        return 1.0F;
    }

    public boolean hitEntity(ItemStack itemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {

        itemStack.damageItem(2, par3EntityLivingBase);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y, int z, EntityLivingBase entityLiving) {

        boolean used = false;

        if (!(entityLiving instanceof EntityPlayer)) return false;
        EntityPlayer player = (EntityPlayer) entityLiving;

        if ((block != null) && (block.isLeaves(world, x, y, z))) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        Block blockToCheck = world.getBlock(x + i, y + j, z + k);
                        int meta = world.getBlockMetadata(x + i, y + j, z + k);
                        if ((blockToCheck != null) && (blockToCheck.isLeaves(world, x + i, y + j, z + k))) {
                            if (blockToCheck.canHarvestBlock(player, meta)) {
                                blockToCheck.harvestBlock(world, player, x + i, y + j, z + k, meta);
                            }
                            world.setBlock(x + i, y + j, z + k, Blocks.air);
                            used = true;
                        }
                    }
                }
            }
            if (used) {
                itemStack.damageItem(1, entityLiving);
            }
            return used;
        }

        for (int i = -2; i <= 2; i++)
            for (int j = -2; j <= 2; j++) {
                Block blockToCheck = world.getBlock(x + i, y, z + j);
                int meta = world.getBlockMetadata(x + i, y, z + j);
                if (blockToCheck != null) {
                    if (blockToCheck instanceof BlockBush) {
                        if (blockToCheck.canHarvestBlock(player, meta)) {
                            blockToCheck.harvestBlock(world, player, x + i, y, z + j, meta);
                        }
                        world.setBlock(x + i, y, z + j, Blocks.air);
                        used = true;
                    }
                }
            }
        if (used) {
            itemStack.damageItem(1, entityLiving);
        }
        return used;
    }
}
