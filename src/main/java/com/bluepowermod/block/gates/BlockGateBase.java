package com.bluepowermod.block.gates;

import com.bluepowermod.block.BlockBase;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RedstoneWireBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BlockGateBase extends BlockBase {

    private final String name;
    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyInteger ROTATION = PropertyInteger.create("rotation", 0, 3);
    public static final PropertyBool POWERED_FRONT = PropertyBool.create("powered_front");
    public static final PropertyBool POWERED_BACK = PropertyBool.create("powered_back");
    public static final PropertyBool POWERED_LEFT = PropertyBool.create("powered_left");
    public static final PropertyBool POWERED_RIGHT = PropertyBool.create("powered_right");

    public BlockGateBase(String name) {
        super(Material.CIRCUITS);
        this.name = name;
        this.setDefaultState(blockState.getBaseState()
                .withProperty(FACING, Direction.UP)
                .withProperty(POWERED_BACK, false)
                .withProperty(POWERED_FRONT, false)
                .withProperty(POWERED_LEFT, false)
                .withProperty(POWERED_RIGHT, false)
                .withProperty(ROTATION, 0));
        setTranslationKey(name);
        setWIP(true);
    }

    @OnlyIn(Dist.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0,
                new ModelResourceLocation(Refs.MODID + ":" + name, "inventory"));

    }

    @Override
    public boolean canConnectRedstone(BlockState state, IBlockAccess world, BlockPos pos, @Nullable Direction side) {
        return state.getValue(FACING) != side && (state.getValue(FACING).getOpposite() != side);
    }

    @Override
    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos) {

        Map<String, Byte> map = getSidePower(worldIn, state, pos);

        return super.getActualState(state, worldIn, pos)
                .withProperty(POWERED_FRONT, map.get("front") > 0)
                .withProperty(POWERED_BACK, map.get("back") > 0)
                .withProperty(POWERED_LEFT, map.get("left") > 0)
                .withProperty(POWERED_RIGHT, map.get("right") > 0);
    }

    public int getWeakPower(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side){
        if(side == Direction.byHorizontalIndex(blockState.getValue(ROTATION))) {
            Map<String, Byte> map = getSidePower(blockAccess, blockState, pos);
            return map.get("front");
        }
        return 0;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side) {
        if(side == Direction.byHorizontalIndex(blockState.getValue(ROTATION))) {
            Map<String, Byte> map = getSidePower(blockAccess, blockState, pos);
            return map.get("front");
        }
        return 0;
    }

    private Map<String, Byte> getSidePower(IBlockAccess worldIn, BlockState state, BlockPos pos){
         Map<String, Byte> map = new HashMap<>();
         Direction side_left = Direction.byHorizontalIndex(state.getValue(ROTATION) == 3 ? 0 : state.getValue(ROTATION) + 1);
         Direction side_right = side_left.getOpposite();
         Direction side_back = Direction.byHorizontalIndex(state.getValue(ROTATION));
         BlockPos pos_left = pos.offset(side_left);
         BlockPos pos_right = pos.offset(side_right);
         BlockPos pos_back = pos.offset(side_back);
         BlockState state_left = worldIn.getBlockState(pos_left);
         BlockState state_right = worldIn.getBlockState(pos_right);
         BlockState state_back = worldIn.getBlockState(pos_back);
         byte left = (byte) state_left.getWeakPower(worldIn, pos_left, side_right);
         byte right = (byte) state_right.getWeakPower(worldIn, pos_right, side_left);
         byte back = (byte) state_back.getWeakPower(worldIn, pos_back, side_back.getOpposite());
         if(state_left.getBlock() instanceof RedstoneWireBlock){left = state_left.getValue(RedstoneWireBlock.POWER).byteValue();}
         if(state_right.getBlock() instanceof RedstoneWireBlock){right = state_right.getValue(RedstoneWireBlock.POWER).byteValue();}
         if(state_back.getBlock() instanceof RedstoneWireBlock){back = state_back.getValue(RedstoneWireBlock.POWER).byteValue();}
         map.put("left", left);
         map.put("right", right);
         map.put("back", back);
         map.put("front", computeRedstone(back, left, right));
         return map;
    }

    public byte computeRedstone(byte back, byte left, byte right){

        if (left > 0 && right > 0 ){
            return (byte)(back > 0 ? 16 : 0);
        }
        return 0;
    }

    @Override
    public boolean isOpaqueCube(BlockState state) {
        return false;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
    public AxisAlignedBB getBoundingBox(BlockState state, IBlockAccess source, BlockPos pos) {
        return AABBUtils.rotate(Refs.GATE_AABB, state.getValue(FACING));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, ROTATION, POWERED_BACK, POWERED_FRONT, POWERED_LEFT, POWERED_RIGHT);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.getValue(FACING).getIndex();
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, Direction.byIndex(meta));
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        super.neighborChanged(state, world, pos, blockIn, fromPos);
        if(!world.getBlockState(pos.offset(state.getValue(FACING).getOpposite())).isFullBlock()){
            world.setBlockToAir(pos);
            this.dropBlockAsItem(world, pos, state, 0);
        }
    }

    @Override
    public BlockState getStateForPlacement(World world, BlockPos pos, Direction facing, float hitX, float hitY, float hitZ, int meta, LivingEntity placer, Hand hand) {
        return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand).withProperty(FACING, facing);
    }
}
