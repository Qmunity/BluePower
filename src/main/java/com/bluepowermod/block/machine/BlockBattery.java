package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier3.TileBattery;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;

/**
 * @author MoreThanHidden
 */

public class BlockBattery extends BlockContainerBase {

    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 6);

    public BlockBattery() {
        super(Material.IRON, TileBattery.class);
        setUnlocalizedName(Refs.BATTERYBLOCK_NAME);
        setRegistryName(Refs.MODID, Refs.BATTERYBLOCK_NAME);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LEVEL);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.blockState.getBaseState().withProperty(LEVEL, meta);
    }

}
