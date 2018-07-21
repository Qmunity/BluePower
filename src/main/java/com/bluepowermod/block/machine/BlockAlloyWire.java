package com.bluepowermod.block.machine;

import com.bluepowermod.block.BlockBase;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAlloyWire extends BlockBase{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool CONNECTED_FRONT = PropertyBool.create("connected_front");
    public static final PropertyBool CONNECTED_BACK = PropertyBool.create("connected_back");
    public static final PropertyBool CONNECTED_LEFT = PropertyBool.create("connected_left");
    public static final PropertyBool CONNECTED_RIGHT = PropertyBool.create("connected_right");

    public BlockAlloyWire(String type) {
        super(Material.CIRCUITS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(CONNECTED_FRONT, false).withProperty(CONNECTED_BACK, false).withProperty(CONNECTED_LEFT, false).withProperty(CONNECTED_RIGHT, false).withProperty(POWERED, false));
        setTranslationKey("wire." + type);
        setCreativeTab(BPCreativeTabs.wiring);
        setRegistryName(Refs.MODID + ":" + type + "_wire");
    }

    public BlockAlloyWire(Material material) {
        super(material);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0,0,0,1,0.1875,1);
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, POWERED);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(POWERED) ? 1 : 0;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty( POWERED,meta > 0);
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        Boolean connected_back = state.getValue(CONNECTED_BACK);
        Boolean connected_front = state.getValue(CONNECTED_FRONT);
        Boolean connected_left = state.getValue(CONNECTED_LEFT);
        Boolean connected_right = state.getValue(CONNECTED_RIGHT);

        for (EnumFacing face : FACING.getAllowedValues()){
            switch (face){
                case NORTH:
                    connected_front = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    break;
                case SOUTH:
                    connected_back = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    break;
                case WEST:
                    connected_left = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    break;
                case EAST:
                    connected_right = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    break;
            }
        }

        return super.getActualState(state, worldIn, pos)
                .withProperty(CONNECTED_RIGHT, connected_right)
                .withProperty(CONNECTED_LEFT, connected_left)
                .withProperty(CONNECTED_FRONT, connected_front)
                .withProperty(CONNECTED_BACK, connected_back);
    }

    @Override
    public boolean canProvidePower(IBlockState state) {
        return true;
    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(world, pos, neighbor);
    }
}
