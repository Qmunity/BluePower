package net.quetzi.bluepower.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.quetzi.bluepower.init.BPBlocks;
import net.quetzi.bluepower.references.Refs;

public class BlockAlloyFurnace extends BlockBase {
    public boolean isActive = false;

    public BlockAlloyFurnace(boolean isActive) {
        super(Material.rock);
        this.isActive = isActive;
        this.setBlockName(Refs.ALLOYFURNACE_NAME);
    }

    public int getLightValue() {
        if (this.isActive) {
            return 13;
        }
        return 0;
    }

    public boolean isOpaqueCube() {
        return true;
    }

    public Item getItemDropped(int p_149650_1_, Random random, int p_149650_3_) {
        return Item.getItemFromBlock(BPBlocks.alloy_furnace);
    }

    public void onBlockAdded(World world, int x, int y, int z) {
        super.onBlockAdded(world, x, y, z);
        // Deal with rotation here
    }
}
