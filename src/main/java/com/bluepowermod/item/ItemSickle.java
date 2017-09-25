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

package com.bluepowermod.item;

import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.init.BPItems;
import com.bluepowermod.reference.Refs;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ItemSickle extends ItemTool {

    public    Item    customCraftingMaterial = Items.AIR;
    protected boolean canRepair              = true;

    private static final Set toolBlocks = Sets.newHashSet(Blocks.LEAVES, Blocks.LEAVES2, Blocks.WHEAT, Blocks.POTATOES, Blocks.CARROTS,
            Blocks.NETHER_WART, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM, Blocks.REEDS, Blocks.TALLGRASS, Blocks.VINE, Blocks.WATERLILY,
            Blocks.RED_FLOWER, Blocks.YELLOW_FLOWER);

    public ItemSickle(ToolMaterial material, String name, Item repairItem) {
        super(material, toolBlocks);
        this.setUnlocalizedName(name);
        this.setCreativeTab(BPCreativeTabs.tools);
        this.setRegistryName(Refs.MODID + ":" + name);
        this.customCraftingMaterial = repairItem;
        BPItems.itemList.add(this);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {

        return String.format("item.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    @Override
    public String getUnlocalizedName() {

        return String.format("item.%s:%s", Refs.MODID, getUnwrappedUnlocalizedName(super.getUnlocalizedName()));
    }

    protected String getUnwrappedUnlocalizedName(String name) {

        return name.substring(name.indexOf(".") + 1);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if ((state.getMaterial() == Material.LEAVES) || (state.getMaterial() == Material.PLANTS) || toolBlocks.contains(state)) {
            return this.efficiencyOnProperMaterial;
        }
        return 1.0F;
    }

    @Override
    public boolean hitEntity(ItemStack itemStack, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase) {

        itemStack.damageItem(2, par3EntityLivingBase);
        return true;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {

        boolean used = false;

        if (!(entityLiving instanceof EntityPlayer)) return false;
        EntityPlayer player = (EntityPlayer) entityLiving;

        if ((state != null) && (state.getBlock().isLeaves(state, world, pos))) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    for (int k = -1; k <= 1; k++) {
                        Block blockToCheck = world.getBlockState(pos.add(i,j,k)).getBlock();
                        IBlockState meta = world.getBlockState(pos.add(i,j,k));
                        if ((blockToCheck != null) && (blockToCheck.isLeaves(meta, world, pos.add(i,j,k)))) {
                            if (blockToCheck.canHarvestBlock(world, pos.add(i,j,k), player)) {
                                blockToCheck.harvestBlock(world, player, pos.add(i,j,k), meta, null, stack);
                            }
                            world.setBlockToAir(pos.add(i,j,k));
                            used = true;
                        }
                    }
                }
            }
            if (used) {
                stack.damageItem(1, entityLiving);
            }
            return used;
        }

        if ((state != null) && (state.getBlock() instanceof BlockLilyPad)) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Block blockToCheck = world.getBlockState(pos.add(i,0,j)).getBlock();
                    IBlockState meta = world.getBlockState(pos.add(i,0,j));
                    if (blockToCheck != null && blockToCheck instanceof BlockLilyPad) {
                        if (blockToCheck.canHarvestBlock(world, pos.add(i,0,j), player)) {
                            blockToCheck.harvestBlock(world, player, pos.add(i,0,j), meta, null, stack);
                        }
                        world.setBlockToAir(pos.add(i,0,j));
                        used = true;
                    }
                }
            }
        }
        if ((state != null) && !(state.getBlock() instanceof BlockLilyPad)) {
            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    Block blockToCheck = world.getBlockState(pos.add(i,0,j)).getBlock();
                    IBlockState meta = world.getBlockState(pos.add(i,0,j));
                    if (blockToCheck != null) {
                        if (blockToCheck instanceof BlockBush && !(blockToCheck instanceof BlockLilyPad)) {
                            if (blockToCheck.canHarvestBlock(world,  pos.add(i,0,j), player)) {
                                blockToCheck.harvestBlock(world, player, pos.add(i,0,j), meta, null, stack);
                            }
                            world.setBlockToAir(pos.add(i,0,j));
                            used = true;
                        }
                    }
                }
            }
        }
        if (used) {
            stack.damageItem(1, entityLiving);
        }
        return used;
    }

    @Override
    public boolean isRepairable() {

        return canRepair && isDamageable();
    }

    @Override
    public boolean getIsRepairable(ItemStack is1, ItemStack is2) {

        return ((is1.getItem() == this || is2.getItem() == this) && (is1.getItem() == this.customCraftingMaterial || is2.getItem() == this.customCraftingMaterial));
    }
}
