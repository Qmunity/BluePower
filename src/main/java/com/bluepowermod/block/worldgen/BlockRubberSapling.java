package com.bluepowermod.block.worldgen;

import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.world.WorldGenRubberTree;
import net.minecraft.block.*;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemGroup;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;

import java.util.Random;

public class BlockRubberSapling extends SaplingBlock {

    public BlockRubberSapling() {
        this.setDefaultState(this.blockState.getBaseState().withProperty(STAGE, 0));
        this.setRegistryName(Refs.MODID + ":" + Refs.RUBBERSAPLING_NAME);
        this.setTranslationKey(Refs.MODID + ":" + Refs.RUBBERSAPLING_NAME);
        this.setCreativeTab(BPCreativeTabs.blocks);
        BPBlocks.blockList.add(this);
    }

    @Override
    public String getLocalizedName() {
        return I18n.translateToLocal(this.getTranslationKey() + ".name");
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(BlockState state)
    {
        return 0;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(ItemGroup itemIn, NonNullList<ItemStack> items) {
        items.add(new ItemStack(this));
    }

    /**
     * Check whether the given BlockPos has a Sapling of the given type
     */
    @Override
    public boolean isTypeAt(World worldIn, BlockPos pos, BlockPlanks.EnumType type){
        return false;
    }

    @Override
    public void generateTree(World worldIn, BlockPos pos, BlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(worldIn, rand, pos)) return;
        int i = 0;
        int j = 0;
        Feature worldgenerator = new WorldGenRubberTree(true);

        BlockState air = Blocks.AIR.getDefaultState();

            worldIn.setBlockState(pos.add(i, 0, j), air, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j), air, 4);
            worldIn.setBlockState(pos.add(i, 0, j + 1), air, 4);
            worldIn.setBlockState(pos.add(i + 1, 0, j + 1), air, 4);

        if (!worldgenerator.generate(worldIn, rand, pos.add(i, 0, j)))
        {
                worldIn.setBlockState(pos.add(i, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j), state, 4);
                worldIn.setBlockState(pos.add(i, 0, j + 1), state, 4);
                worldIn.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
        }

    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    @Override
    public BlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(STAGE, (meta & 8) >> 3);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    @Override
    public int getMetaFromState(BlockState state)
    {
        int i = 0;
        i = i | state.getValue(STAGE) << 3;
        return i;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, STAGE, TYPE);
    }


}
