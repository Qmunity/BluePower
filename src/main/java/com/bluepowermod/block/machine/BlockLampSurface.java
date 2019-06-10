package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.AABBUtils;
import mcmultipart.RayTraceHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.properties.PropertyDirection;
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

public class BlockLampSurface extends BlockLamp {

    private final String name;
    private final Boolean isInverted;
    private final AxisAlignedBB size;

    public static final PropertyDirection FACING = PropertyDirection.create("facing");

    public BlockLampSurface(String name, boolean isInverted, MinecraftColor color, AxisAlignedBB size) {
        super(name, isInverted, color);
        this.name = name;
        this.isInverted = isInverted;
        this.size = size;
        this.setDefaultState(blockState.getBaseState().withProperty(POWER, isInverted ? 15 : 0).withProperty(FACING, Direction.UP));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initModel() {
        //All lamps need to use the same blockstate
        StateMapperBase stateMapper = new StateMapperBase() {
            @Override
            protected ModelResourceLocation getModelResourceLocation(BlockState iBlockState) {
                return new ModelResourceLocation(Refs.MODID + ":" + name, "facing=" + iBlockState.getValue(FACING).getName() + "," + ((!isInverted == iBlockState.getValue(POWER) > 0) ? "powered=true" : "powered=false"));
            }
        };
        ModelLoader.setCustomStateMapper(this, stateMapper);
        if(!isInverted()) {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Refs.MODID + ":" + name, "inventory"));
        }else {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(Refs.MODID + ":" + name, "inventory_glow"));
        }
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
        //Bounding box for the Caged Lamp
        return AABBUtils.rotate(size.equals(Refs.CAGELAMP_AABB) ? size.grow( 0.0625) : size, state.getValue(FACING));
    }

    @Override
    public AxisAlignedBB getSize() {
        return size;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, POWER, FACING);
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
