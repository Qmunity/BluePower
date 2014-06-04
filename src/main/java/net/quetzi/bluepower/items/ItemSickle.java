package net.quetzi.bluepower.items;

import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.CustomTabs;

import com.google.common.collect.Sets;

import net.quetzi.bluepower.references.Refs;

public class ItemSickle extends ItemTool {
    private static final Set toolBlocks = Sets.newHashSet(new Block[] { Blocks.leaves,
            Blocks.leaves2, Blocks.wheat, Blocks.potatoes, Blocks.carrots });
    private int cropRadius = 2;
    private int leafRadius = 1;

    public ItemSickle(ToolMaterial material, String name) {
        super(1.0F, material, toolBlocks);
        this.setUnlocalizedName(name);
        this.setCreativeTab(CustomTabs.tabBluePowerTools);
        this.setTextureName(Refs.MODID + ":" + name);
    }

    public float func_150893_a(ItemStack itemStack, Block block) {
        return this.toolBlocks.contains(block) ? this.efficiencyOnProperMaterial : 1.0F;
    }

    public boolean hitEntity(ItemStack itemStack, EntityLivingBase par2EntityLivingBase,
            EntityLivingBase par3EntityLivingBase) {
        itemStack.damageItem(2, par3EntityLivingBase);
        return true;
    }

    public boolean onBlockDestroyed(ItemStack itemStack, World world, Block block, int x, int y,
            int z, EntityLivingBase entityLiving) {
        boolean used = false;

        if (!(entityLiving instanceof EntityPlayer))
            return false;
        EntityPlayer player = (EntityPlayer) entityLiving;

        if ((block != null) && (block.isLeaves(world, x, y, z))) {
            for (int i = -this.leafRadius; i <= this.leafRadius; i++) {
                for (int j = -this.leafRadius; j <= this.leafRadius; j++)
                    for (int k = -this.leafRadius; k <= this.leafRadius; k++) {
                        Block blockToCheck = world.getBlock(x + i, y + j, z + k);
                        int meta = world.getBlockMetadata(x + i, y + j, z + k);
                        if ((blockToCheck != null)
                                && (blockToCheck.isLeaves(world, x + i, y + j, z + k))) {
                            if (blockToCheck.canHarvestBlock(player, meta)) {
                                blockToCheck.harvestBlock(world, player, x + i, y + j, z + k, meta);
                            }
                            world.setBlock(x + i, y + j, z + k, Blocks.air);
                            used = true;
                        }
                    }
            }
            if (used) {
                itemStack.damageItem(1, entityLiving);
            }
            return used;
        }

        for (int i = -this.cropRadius; i <= this.cropRadius; i++)
            for (int j = -this.cropRadius; j <= this.cropRadius; j++) {
                Block blockToCheck = world.getBlock(x + i, y, z + j);
                int meta = world.getBlockMetadata(x + i, y, z + j);
                if (blockToCheck != null) {
                    if ((blockToCheck instanceof BlockCrops) || (blockToCheck instanceof BlockBush)) {
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
