package com.bluepowermod.block.power;

import com.bluepowermod.api.power.CapabilityBlutricity;
import com.bluepowermod.block.BlockContainerBase;
import com.bluepowermod.init.BPCreativeTabs;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.tile.tier3.TileBlulectricCable;
import com.bluepowermod.util.AABBUtils;
import mcmultipart.api.container.IPartInfo;
import mcmultipart.api.multipart.IMultipart;
import mcmultipart.api.slot.EnumCenterSlot;
import mcmultipart.api.slot.EnumFaceSlot;
import mcmultipart.api.slot.IPartSlot;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author MoreThanHidden
 */
public class BlockBlulectricCable extends BlockContainerBase implements IMultipart {

    public static final PropertyDirection FACING = PropertyDirection.create("facing");
    private static final PropertyBool CONNECTED_FRONT = PropertyBool.create("connected_front");
    private static final PropertyBool CONNECTED_BACK = PropertyBool.create("connected_back");
    private static final PropertyBool CONNECTED_LEFT = PropertyBool.create("connected_left");
    private static final PropertyBool CONNECTED_RIGHT = PropertyBool.create("connected_right");
    private static final PropertyBool JOIN_FRONT = PropertyBool.create("join_front");
    private static final PropertyBool JOIN_BACK = PropertyBool.create("join_back");
    private static final PropertyBool JOIN_LEFT = PropertyBool.create("join_left");
    private static final PropertyBool JOIN_RIGHT = PropertyBool.create("join_right");
    protected AxisAlignedBB[] shapes = makeShapes();

    public BlockBlulectricCable() {
        super(Material.IRON, TileBlulectricCable.class);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.UP)
                .withProperty(CONNECTED_FRONT, false).withProperty(CONNECTED_BACK, false)
                .withProperty(CONNECTED_LEFT, false).withProperty(CONNECTED_RIGHT, false)
                .withProperty(JOIN_FRONT, false).withProperty(JOIN_BACK, false)
                .withProperty(JOIN_LEFT, false).withProperty(JOIN_RIGHT, false));
        setTranslationKey(Refs.BLULECTRICCABLE_NAME);
        setCreativeTab(BPCreativeTabs.wiring);
        setRegistryName(Refs.MODID + ":" + Refs.BLULECTRICCABLE_NAME);
    }

    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT;
    }

    /*@Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
        super.onReplaced(state, worldIn, pos, newState, isMoving);
        FACING.getAllowedValues().forEach(f ->{
            BlockPos neighborPos = pos.offset(f).offset(state.getValue(FACING).getOpposite());
            worldIn.getBlockState(neighborPos).neighborChanged(worldIn, neighborPos, state.getBlock(), pos, isMoving);
        });
    }*/

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, @Nullable EntityLivingBase entityLiving, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, entityLiving, stack);
        FACING.getAllowedValues().forEach(f -> {
            BlockPos neighborPos = pos.offset(f).offset(state.getValue(FACING).getOpposite());
            worldIn.getBlockState(neighborPos).neighborChanged(worldIn, neighborPos, state.getBlock(), pos);
        });
    }

    @Override
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return worldIn.getBlockState(pos).isSideSolid(worldIn, pos, side);
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return super.isFullCube(state);
    }

    AxisAlignedBB[] makeShapes() {

        float width = 2;
        float gap = 0;
        float height = 2;

        float f = 8.0F - width;
        float f1 = 8.0F + width;
        float f2 = 8.0F - width;
        float f3 = 8.0F + width;


       AxisAlignedBB voxelshape = new AxisAlignedBB(f, 0.0D, f, f1, height, f1);
       AxisAlignedBB voxelshape1 = new AxisAlignedBB(f2, gap, 0.0D, f3, height, f3);
       AxisAlignedBB voxelshape2 = new AxisAlignedBB(f2, gap, f2, f3, height, 16.0D);
       AxisAlignedBB voxelshape3 = new AxisAlignedBB(0.0D, gap, f2, f3, height, f3);
       AxisAlignedBB voxelshape4 = new AxisAlignedBB(f2, gap, f2, 16.0D, height, f3);
       AxisAlignedBB voxelshape5 = AxisAlignedBB.or(voxelshape1, voxelshape4);
       AxisAlignedBB voxelshape6 = AxisAlignedBB.or(voxelshape2, voxelshape3);

       AxisAlignedBB[] avoxelshape = new AxisAlignedBB[][]{
                NULL_AABB, voxelshape2, voxelshape3, voxelshape6, voxelshape1,
                AxisAlignedBB.or(voxelshape2, voxelshape1), AxisAlignedBB.or(voxelshape3, voxelshape1),
                AxisAlignedBB.or(voxelshape6, voxelshape1), voxelshape4, AxisAlignedBB.or(voxelshape2, voxelshape4),
                AxisAlignedBB.or(voxelshape3, voxelshape4), AxisAlignedBB.or(voxelshape6, voxelshape4), voxelshape5,
                AxisAlignedBB.or(voxelshape2, voxelshape5), AxisAlignedBB.or(voxelshape3, voxelshape5),
                AxisAlignedBB.or(voxelshape6, voxelshape5)
        };

        for(int i = 0; i < 16; ++i) {
            avoxelshape[i] = AxisAlignedBB.or(voxelshape, avoxelshape[i]);
        }

        return avoxelshape;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AABBUtils.rotate(this.shapes[this.getShapeIndex(state)], state.getValue(FACING));
    }

    private int getShapeIndex(IBlockState state) {
        int i = 0;

        if(state.getValue(CONNECTED_FRONT))
            i |= getMask(EnumFacing.NORTH);
        if(state.getValue(CONNECTED_BACK))
            i |= getMask(EnumFacing.SOUTH);
        if(state.getValue(CONNECTED_LEFT))
            i |= getMask(EnumFacing.WEST);
        if(state.getValue(CONNECTED_RIGHT))
            i |= getMask(EnumFacing.EAST);

        return i;
    }

    private static int getMask(EnumFacing facing) {
        return 1 << facing.getHorizontalIndex();
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
        TileEntity te = world.getTileEntity(pos);
        //Get new state based on surrounding capabilities
        IBlockState newState = getStateForPos(world, pos, getDefaultState().withProperty(FACING, state.getValue(FACING)), state.getValue(FACING));

        if (!(te instanceof TileBPMultipart)){
            //Change the block state
            world.setBlockState(pos, newState, 2);
        }else{
            //Update the state in the Multipart
            ((TileBPMultipart) te).changeState(state, newState);
        }
        state = newState;
        //If not placed on a solid block break off
        if (!world.getBlockState(pos).isSideSolid(world, pos, state.getValue(FACING).getOpposite())) {
            if(te instanceof TileBPMultipart){
                ((TileBPMultipart)te).removeState(state);
                if(world instanceof WorldServer) {
                    NonNullList<ItemStack> drops = NonNullList.create();
                    drops.add(new ItemStack(this));
                    InventoryHelper.dropItems(world, pos, drops);
                }
            }else {
                world.destroyBlock(pos, true);
            }
        }
    }

    private IBlockState getStateForPos(World world, BlockPos pos, IBlockState state, EnumFacing face){
        List<EnumFacing> directions = new ArrayList<>(FACING.getAllowedValues());
        //Make sure the side we are trying to connect on isn't blocked.
        TileEntity ownTile = world.getTileEntity(pos);
        if(ownTile instanceof TileBPMultipart)
            directions.removeIf(d -> ((TileBPMultipart) ownTile).isSideBlocked(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d));
        //Make sure the cable is on the same side of the block
        directions.removeIf(d -> world.getBlockState(pos.offset(d)).getBlock() instanceof BlockBlulectricCable
                && world.getBlockState(pos.offset(d)).getValue(FACING) != face);

        //Populate all directions
        for (EnumFacing d : directions) {
            TileEntity tileEntity = world.getTileEntity(pos.offset(d));

            boolean join = false;
            //If Air or Water look for a change in Direction
            Block dBlock = world.getBlockState(pos.offset(d)).getBlock();
            if ((dBlock == Blocks.AIR || dBlock == Blocks.WATER) &&
                    world.getBlockState(pos.offset(d).offset(face.getOpposite())).getBlock() instanceof BlockBlulectricCable &&
                    world.getBlockState(pos.offset(d).offset(face.getOpposite())).getValue(FACING) == d) {
                tileEntity = world.getTileEntity(pos.offset(d).offset(face.getOpposite()));
                join = true;
            }

            //Check Capability for Direction
            if (tileEntity != null)
                switch (state.getValue(FACING)) {
                    case UP:
                    case DOWN:
                        switch (d) {
                            case EAST:
                                state = state.withProperty(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case WEST:
                                state = state.withProperty(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case NORTH:
                                state = state.withProperty(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case SOUTH:
                                state = state.withProperty(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                        break;
                    case NORTH:
                        switch (d) {
                            case WEST:
                                state = state.withProperty(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case EAST:
                                state = state.withProperty(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case UP:
                                state = state.withProperty(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case DOWN:
                                state = state.withProperty(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                        break;
                    case SOUTH:
                        switch (d) {
                            case EAST:
                                state = state.withProperty(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case WEST:
                                state = state.withProperty(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case UP:
                                state = state.withProperty(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case DOWN:
                                state = state.withProperty(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                        break;
                    case EAST:
                        switch (d) {
                            case NORTH:
                                state = state.withProperty(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case SOUTH:
                                state = state.withProperty(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case UP:
                                state = state.withProperty(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case DOWN:
                                state = state.withProperty(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                        break;
                    case WEST:
                        switch (d) {
                            case SOUTH:
                                state = state.withProperty(CONNECTED_RIGHT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_RIGHT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case NORTH:
                                state = state.withProperty(CONNECTED_LEFT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_LEFT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case UP:
                                state = state.withProperty(CONNECTED_FRONT, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_FRONT, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                            case DOWN:
                                state = state.withProperty(CONNECTED_BACK, tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                state = state.withProperty(JOIN_BACK, join && tileEntity.getCapability(CapabilityBlutricity.BLUTRICITY_CAPABILITY, d.getOpposite()).isPresent());
                                break;
                        }
                }
        }
        return state;
    }

    @Override
    public IPartSlot getSlotForPlacement(World world, BlockPos blockPos, IBlockState iBlockState, EnumFacing enumFacing, float v, float v1, float v2, EntityLivingBase entityLivingBase) {
        return EnumFaceSlot.fromFace(enumFacing);
    }

    @Override
    public IPartSlot getSlotFromWorld(IBlockAccess iBlockAccess, BlockPos blockPos, IBlockState state) {
        return EnumFaceSlot.fromFace(state.getValue(FACING));
    }
}
