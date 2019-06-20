package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.client.render.IBPColoredBlock;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileWire;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.block.RepeaterBlock;
import net.minecraft.block.ObserverBlock;
import net.minecraft.block.material.Material;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

public class BlockAlloyWire extends BlockContainerBase implements IBPColoredBlock{

    public static final DirectionProperty FACING = DirectionProperty.create("facing");
    public static final BooleanProperty POWERED = BooleanProperty.create("powered");
    public static final BooleanProperty CONNECTED_FRONT = BooleanProperty.create("connected_front");
    public static final BooleanProperty CONNECTED_BACK = BooleanProperty.create("connected_back");
    public static final BooleanProperty CONNECTED_LEFT = BooleanProperty.create("connected_left");
    public static final BooleanProperty CONNECTED_RIGHT = BooleanProperty.create("connected_right");
    private boolean canProvidePower = true;
    final String type;
    /** List of blocks to update with redstone. */
    private final Set<BlockPos> blocksNeedingUpdate = Sets.<BlockPos>newHashSet();

    public BlockAlloyWire(String type) {
        super(Material.ROCK, TileWire.class);
        this.type = type;
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false).with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false).with(POWERED, false));
        setRegistryName(Refs.MODID + ":" + type + "_wire");
    }

    public BlockAlloyWire(String type, Material material) {
        super(material, TileWire.class);
        this.type = type;
    }

    private BlockState updateSurroundingWire(World worldIn, BlockPos pos, BlockState state)
    {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.notifyNeighborsOfStateChange(blockpos, this);
        }

        return state;
    }

    private BlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, BlockState state)
    {
        BlockState iblockstate = state;
        boolean i = state.get(POWERED);
        this.canProvidePower = false;
        int k = worldIn.getRedstonePowerFromNeighbors(pos1);
        this.canProvidePower = true;

        if (i != k > 0)
        {
            state = state.with(POWERED, k > 0);

            if (worldIn.getBlockState(pos1) == iblockstate)
            {
                worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);

            for (Direction enumfacing1 : Direction.values())
            {
                this.blocksNeedingUpdate.add(pos1.offset(enumfacing1));
            }
        }

        return state;
    }

    /**
     * Calls World.notifyNeighborsOfStateChange() for all neighboring blocks, but only if the given block is a redstone
     * wire.
     */
    private void notifyWireNeighborsOfStateChange(World worldIn, BlockPos pos)
    {
        if (worldIn.getBlockState(pos).getBlock() == this)
        {
            worldIn.notifyNeighborsOfStateChange(pos, this, false);

            for (Direction enumfacing : Direction.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, BlockState state)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingWire(worldIn, pos, state);

            for (Direction enumfacing : Direction.Plane.VERTICAL)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            for (Direction enumfacing1 : Direction.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (Direction enumfacing2 : Direction.Plane.HORIZONTAL)
            {
                BlockPos blockpos = pos.offset(enumfacing2);

                if (worldIn.getBlockState(blockpos).isNormalCube())
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }
                else
                {
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }

    /**
     * Called serverside after this block is replaced with another in Chunk, but before the Tile Entity is updated
     */
    public void breakBlock(World worldIn, BlockPos pos, BlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote){
            for (Direction enumfacing : Direction.values()){
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            this.updateSurroundingWire(worldIn, pos, state);

            for (Direction enumfacing1 : Direction.Plane.HORIZONTAL){
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (Direction enumfacing2 : Direction.Plane.HORIZONTAL){
                BlockPos blockpos = pos.offset(enumfacing2);

                if (worldIn.getBlockState(blockpos).isNormalCube()){
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.up());
                }else{
                    this.notifyWireNeighborsOfStateChange(worldIn, blockpos.down());
                }
            }
        }
    }

    /**
     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
     * block, etc.
     */
    public void neighborChanged(BlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!worldIn.isRemote){
            if (this.canPlaceBlockAt(worldIn, pos)){
                this.updateSurroundingWire(worldIn, pos, state);
            }else{
                this.dropBlockAsItem(worldIn, pos, state, 0);
                worldIn.setBlockToAir(pos);
            }
        }
    }

    /**
     * Checks if this block can be placed exactly at the given position.
     */
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos){
        return worldIn.getBlockState(pos.down()).isTopSolid() || worldIn.getBlockState(pos.down()).getBlock() == Blocks.GLOWSTONE;
    }


    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(BlockState blockState, IBlockAccess worldIn, BlockPos pos){
        return NULL_AABB;
    }

    @Override
    protected BlockStateContainer createBlockState(){
        return new BlockStateContainer(this, FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, POWERED);
    }

    @Override
    public int getMetaFromState(BlockState state) {
        return state.get(POWERED) ? 1 : 0;
    }

    @Override
    public BlockState getStateFromMeta(int meta) {
        return getDefaultState().with( POWERED,meta > 0);
    }

    public boolean canProvidePower(BlockState state)
    {
        return this.canProvidePower;
    }


    @Override
    public BlockState getActualState(BlockState state, IBlockAccess worldIn, BlockPos pos) {
        Boolean connected_back = state.get(CONNECTED_BACK);
        Boolean connected_front = state.get(CONNECTED_FRONT);
        Boolean connected_left = state.get(CONNECTED_LEFT);
        Boolean connected_right = state.get(CONNECTED_RIGHT);

        for (Direction face : FACING.getAllowedValues()){
            BlockState stateof = worldIn.getBlockState(pos.offset(face));
            switch (face){
                case NORTH:
                    connected_front = stateof.getBlock().canConnectRedstone(stateof, worldIn, pos, face);
                    break;
                case SOUTH:
                    connected_back = stateof.getBlock().canConnectRedstone(stateof, worldIn, pos, face);
                    break;
                case WEST:
                    connected_left = stateof.getBlock().canConnectRedstone(stateof, worldIn, pos, face);
                    break;
                case EAST:
                    connected_right = stateof.getBlock().canConnectRedstone(stateof, worldIn, pos, face);
                    break;
            }
        }

        return super.getActualState(state, worldIn, pos)
                .with(CONNECTED_RIGHT, connected_right)
                .with(CONNECTED_LEFT, connected_left)
                .with(CONNECTED_FRONT, connected_front)
                .with(CONNECTED_BACK, connected_back);
    }

    /**
     * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos, Direction)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getStrongPower(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side){
        return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
    }

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos, Direction)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(BlockState blockState, IBlockAccess blockAccess, BlockPos pos, Direction side){
        TileWire tile = (TileWire) blockAccess.getTileEntity(pos);
        if (!this.canProvidePower || !this.isPowerSourceAt(blockAccess, pos, side) || !tile.getIsRedstonePowered()){
            return 0;
        }else{
            return blockState.getValue(POWERED) ? 16 : 0;
        }
    }

    private boolean isPowerSourceAt(IBlockAccess worldIn, BlockPos pos, Direction side){
        BlockPos blockpos = pos.offset(side);
        BlockState iblockstate = worldIn.getBlockState(blockpos);
        boolean flag = iblockstate.isNormalCube();
        boolean flag1 = worldIn.getBlockState(pos.up()).isNormalCube();

        if (!flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up())){
            return true;
        }else if (canConnectTo(iblockstate, side, worldIn, pos)){
            return true;
        }else if (iblockstate.getBlock() == Blocks.POWERED_REPEATER && iblockstate.get(RedstoneDiodeBlock.FACING) == side){
            return true;
        }else{
            return !flag && canConnectUpwardsTo(worldIn, blockpos.down());
        }
    }

    protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos){
        return canConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
    }

    protected static boolean canConnectTo(BlockState blockState, @Nullable Direction side, IBlockAccess world, BlockPos pos){
        if (Blocks.UNPOWERED_REPEATER.isSameDiode(blockState)){
            Direction enumfacing = blockState.getValue(RepeaterBlock.FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        }else if (Blocks.OBSERVER == blockState.getBlock()){
            return side == blockState.getValue(ObserverBlock.FACING);
        }else{
            return blockState.getBlock().canConnectRedstone(blockState, world, pos, side);
        }
    }


    @Override
    public int getColor(IBlockAccess w, BlockPos pos, int tint) {
        return tint == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

    @Override
    public int getColor(int tint) {
        return tint == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

}
