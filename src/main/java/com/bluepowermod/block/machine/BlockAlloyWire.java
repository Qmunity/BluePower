package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.block.BlockBase;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.tier1.TileWire;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.BlockObserver;
import net.minecraft.block.BlockRedstoneDiode;
import net.minecraft.block.BlockRedstoneRepeater;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public class BlockAlloyWire extends BlockContainerBase implements IBlockColor, IItemColor{

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    public static final PropertyBool POWERED = PropertyBool.create("powered");
    public static final PropertyBool CONNECTED_FRONT = PropertyBool.create("connected_front");
    public static final PropertyBool CONNECTED_BACK = PropertyBool.create("connected_back");
    public static final PropertyBool CONNECTED_LEFT = PropertyBool.create("connected_left");
    public static final PropertyBool CONNECTED_RIGHT = PropertyBool.create("connected_right");
    private boolean canProvidePower = true;
    final String type;
    /** List of blocks to update with redstone. */
    private final Set<BlockPos> blocksNeedingUpdate = Sets.<BlockPos>newHashSet();

    public BlockAlloyWire(String type) {
        super(Material.CIRCUITS, TileWire.class);
        this.type = type;
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP).withProperty(CONNECTED_FRONT, false).withProperty(CONNECTED_BACK, false).withProperty(CONNECTED_LEFT, false).withProperty(CONNECTED_RIGHT, false).withProperty(POWERED, false));
        setTranslationKey("wire." + type);
        setCreativeTab(BPCreativeTabs.wiring);
        setRegistryName(Refs.MODID + ":" + type + "_wire");
    }

    public BlockAlloyWire(String type, Material material) {
        super(material, TileWire.class);
        this.type = type;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0,0,0,1,0.1875,1);
    }

    private IBlockState updateSurroundingWire(World worldIn, BlockPos pos, IBlockState state)
    {
        state = this.calculateCurrentChanges(worldIn, pos, pos, state);
        List<BlockPos> list = Lists.newArrayList(this.blocksNeedingUpdate);
        this.blocksNeedingUpdate.clear();

        for (BlockPos blockpos : list)
        {
            worldIn.notifyNeighborsOfStateChange(blockpos, this, false);
        }

        return state;
    }

    private IBlockState calculateCurrentChanges(World worldIn, BlockPos pos1, BlockPos pos2, IBlockState state)
    {
        IBlockState iblockstate = state;
        boolean i = state.getValue(POWERED);
        this.canProvidePower = false;
        int k = worldIn.getRedstonePowerFromNeighbors(pos1);
        this.canProvidePower = true;

        if (i != k > 0)
        {
            state = state.withProperty(POWERED, k > 0);

            if (worldIn.getBlockState(pos1) == iblockstate)
            {
                worldIn.setBlockState(pos1, state, 2);
            }

            this.blocksNeedingUpdate.add(pos1);

            for (EnumFacing enumfacing1 : EnumFacing.values())
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

            for (EnumFacing enumfacing : EnumFacing.values())
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }
        }
    }

    /**
     * Called after the block is set in the Chunk data, but before the Tile Entity is set
     */
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
    {
        if (!worldIn.isRemote)
        {
            this.updateSurroundingWire(worldIn, pos, state);

            for (EnumFacing enumfacing : EnumFacing.Plane.VERTICAL)
            {
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL)
            {
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL)
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
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
        super.breakBlock(worldIn, pos, state);

        if (!worldIn.isRemote){
            for (EnumFacing enumfacing : EnumFacing.values()){
                worldIn.notifyNeighborsOfStateChange(pos.offset(enumfacing), this, false);
            }

            this.updateSurroundingWire(worldIn, pos, state);

            for (EnumFacing enumfacing1 : EnumFacing.Plane.HORIZONTAL){
                this.notifyWireNeighborsOfStateChange(worldIn, pos.offset(enumfacing1));
            }

            for (EnumFacing enumfacing2 : EnumFacing.Plane.HORIZONTAL){
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
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
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
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos){
        return NULL_AABB;
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

    public boolean canProvidePower(IBlockState state)
    {
        return this.canProvidePower;
    }


    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        Boolean connected_back = state.getValue(CONNECTED_BACK);
        Boolean connected_front = state.getValue(CONNECTED_FRONT);
        Boolean connected_left = state.getValue(CONNECTED_LEFT);
        Boolean connected_right = state.getValue(CONNECTED_RIGHT);

        for (EnumFacing face : FACING.getAllowedValues()){
            IBlockState stateof = worldIn.getBlockState(pos.offset(face));
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
                .withProperty(CONNECTED_RIGHT, connected_right)
                .withProperty(CONNECTED_LEFT, connected_left)
                .withProperty(CONNECTED_FRONT, connected_front)
                .withProperty(CONNECTED_BACK, connected_back);
    }

    /**
     * @deprecated call via {@link IBlockState#getStrongPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getStrongPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
        return !this.canProvidePower ? 0 : blockState.getWeakPower(blockAccess, pos, side);
    }

    /**
     * @deprecated call via {@link IBlockState#getWeakPower(IBlockAccess,BlockPos,EnumFacing)} whenever possible.
     * Implementing/overriding is fine.
     */
    public int getWeakPower(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side){
        TileWire tile = (TileWire) blockAccess.getTileEntity(pos);
        if (!this.canProvidePower || !this.isPowerSourceAt(blockAccess, pos, side) || !tile.getIsRedstonePowered()){
            return 0;
        }else{
            return blockState.getValue(POWERED) ? 16 : 0;
        }
    }

    private boolean isPowerSourceAt(IBlockAccess worldIn, BlockPos pos, EnumFacing side){
        BlockPos blockpos = pos.offset(side);
        IBlockState iblockstate = worldIn.getBlockState(blockpos);
        boolean flag = iblockstate.isNormalCube();
        boolean flag1 = worldIn.getBlockState(pos.up()).isNormalCube();

        if (!flag1 && flag && canConnectUpwardsTo(worldIn, blockpos.up())){
            return true;
        }else if (canConnectTo(iblockstate, side, worldIn, pos)){
            return true;
        }else if (iblockstate.getBlock() == Blocks.POWERED_REPEATER && iblockstate.getValue(BlockRedstoneDiode.FACING) == side){
            return true;
        }else{
            return !flag && canConnectUpwardsTo(worldIn, blockpos.down());
        }
    }

    protected static boolean canConnectUpwardsTo(IBlockAccess worldIn, BlockPos pos){
        return canConnectTo(worldIn.getBlockState(pos), null, worldIn, pos);
    }

    protected static boolean canConnectTo(IBlockState blockState, @Nullable EnumFacing side, IBlockAccess world, BlockPos pos){
        if (Blocks.UNPOWERED_REPEATER.isSameDiode(blockState)){
            EnumFacing enumfacing = blockState.getValue(BlockRedstoneRepeater.FACING);
            return enumfacing == side || enumfacing.getOpposite() == side;
        }else if (Blocks.OBSERVER == blockState.getBlock()){
            return side == blockState.getValue(BlockObserver.FACING);
        }else{
            return blockState.getBlock().canConnectRedstone(blockState, world, pos, side);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(IBlockState state, @Nullable IBlockAccess world, @Nullable BlockPos pos, int tintIndex) {
        //Color for Block
        return tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public int colorMultiplier(ItemStack stack, int tintIndex) {
        //Color for Itemstack
        return tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }
}
