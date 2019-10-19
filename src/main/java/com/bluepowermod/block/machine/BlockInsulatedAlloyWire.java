package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.ICustomModelBlock;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.IntegerProperty;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockInsulatedAlloyWire extends BlockAlloyWire implements ICustomModelBlock{

    private final MinecraftColor color;
    protected final VoxelShape[] shapes = makeShapes();

    //0 = No Connection | 1 = Normal Connection  | 2 = Join Connection | 3 = Straight Connection
    private static final IntegerProperty CONNECTION_FRONT = IntegerProperty.create("connection_front", 0, 3);
    private static final IntegerProperty CONNECTION_BACK = IntegerProperty.create("connection_back", 0, 3);
    private static final IntegerProperty CONNECTION_LEFT = IntegerProperty.create("connection_left", 0, 3);
    private static final IntegerProperty CONNECTION_RIGHT = IntegerProperty.create("connection_right", 0, 3);

    public BlockInsulatedAlloyWire(String type, MinecraftColor color) {
        super(type, Material.ROCK);
        this.color = color;
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.UP).with(POWERED, false)
                .with(CONNECTION_FRONT, 0).with(CONNECTION_BACK, 0)
                .with(CONNECTION_LEFT, 0).with(CONNECTION_RIGHT, 0));
        setRegistryName(Refs.MODID + ":" + "wire." + type + "." + color.name().toLowerCase());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, POWERED, CONNECTION_FRONT, CONNECTION_BACK, CONNECTION_LEFT, CONNECTION_RIGHT);
    }


    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        int connection_back = 0;
        int connection_front = 0;
        int connection_left = 0;
        int connection_right = 0;

        int connections = 0;
        boolean straight_front = true;
        boolean straight_back = true;
        boolean straight_left = true;
        boolean straight_right = true;

        for (Direction face : FACING.getAllowedValues()){
            switch (face){
                case NORTH:
                    if(worldIn.getBlockState(pos.offset(face)).getBlock().equals(this)){
                        connection_front = 1;
                        connections += 1;
                        straight_front = false;
                        straight_left = false;
                        straight_right = false;
                    }
                    break;
                case SOUTH:
                    if(worldIn.getBlockState(pos.offset(face)).getBlock().equals(this)){
                        connection_back = 1;
                        connections += 1;
                        straight_back = false;
                        straight_left = false;
                        straight_right = false;
                    }
                    break;
                case WEST:
                    if(worldIn.getBlockState(pos.offset(face)).getBlock().equals(this)){
                        connection_left = 1;
                        connections += 1;
                        straight_left = false;
                        straight_front = false;
                        straight_back = false;
                    }
                    break;
                case EAST:
                    if(worldIn.getBlockState(pos.offset(face)).getBlock().equals(this)){
                        connection_right = 1;
                        connections += 1;
                        straight_right = false;
                        straight_front = false;
                        straight_back = false;
                    }
                    break;
            }
        }

        return this.getDefaultState().with(CONNECTION_RIGHT, straight_right ? 3 : connection_right)
                .with(CONNECTION_LEFT, straight_left ? 3 : connection_left)
                .with(CONNECTION_FRONT, straight_front ? 3 : connection_front)
                .with(CONNECTION_BACK, straight_back ? 3 : connection_back);
    }

    protected VoxelShape[] makeShapes() {

        float width = 2;
        float gap = 0;
        float height = 3;

        float f = 8.0F - width;
        float f1 = 8.0F + width;
        float f2 = 8.0F - width;
        float f3 = 8.0F + width;

        VoxelShape voxelshape = Block.makeCuboidShape((double)f, 0.0D, (double)f, (double)f1, (double)height, (double)f1);
        VoxelShape voxelshape1 = Block.makeCuboidShape((double)f2, (double)gap, 0.0D, (double)f3, (double)height, (double)f3);
        VoxelShape voxelshape2 = Block.makeCuboidShape((double)f2, (double)gap, (double)f2, (double)f3, (double)height, 16.0D);
        VoxelShape voxelshape3 = Block.makeCuboidShape(0.0D, (double)gap, (double)f2, (double)f3, (double)height, (double)f3);
        VoxelShape voxelshape4 = Block.makeCuboidShape((double)f2, (double)gap, (double)f2, 16.0D, (double)height, (double)f3);
        VoxelShape voxelshape5 = VoxelShapes.or(voxelshape1, voxelshape4);
        VoxelShape voxelshape6 = VoxelShapes.or(voxelshape2, voxelshape3);

        VoxelShape[] avoxelshape = new VoxelShape[]{
                VoxelShapes.empty(), voxelshape2, voxelshape3, voxelshape6, voxelshape1,
                VoxelShapes.or(voxelshape2, voxelshape1), VoxelShapes.or(voxelshape3, voxelshape1),
                VoxelShapes.or(voxelshape6, voxelshape1), voxelshape4, VoxelShapes.or(voxelshape2, voxelshape4),
                VoxelShapes.or(voxelshape3, voxelshape4), VoxelShapes.or(voxelshape6, voxelshape4), voxelshape5,
                VoxelShapes.or(voxelshape2, voxelshape5), VoxelShapes.or(voxelshape3, voxelshape5),
                VoxelShapes.or(voxelshape6, voxelshape5)
        };

        for(int i = 0; i < 16; ++i) {
            avoxelshape[i] = VoxelShapes.or(voxelshape, avoxelshape[i]);
        }

        return avoxelshape;
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return AABBUtils.rotate(this.shapes[this.getShapeIndex(state)], state.get(FACING));
    }

    private int getShapeIndex(BlockState state) {
        int i = 0;

        if(state.get(CONNECTION_FRONT) == 1 || state.get(CONNECTION_FRONT) == 2)
            i |= getMask(Direction.NORTH);
        if(state.get(CONNECTION_BACK) == 1 || state.get(CONNECTION_BACK) == 2)
            i |= getMask(Direction.SOUTH);
        if(state.get(CONNECTION_LEFT) == 1 || state.get(CONNECTION_LEFT) == 2)
            i |= getMask(Direction.WEST);
        if(state.get(CONNECTION_RIGHT) == 1 || state.get(CONNECTION_RIGHT) == 2)
            i |= getMask(Direction.EAST);

        return i;
    }

    private static int getMask(Direction facing) {
        return 1 << facing.getHorizontalIndex();
    }


    @Override
    public int getColor(IBlockReader world, BlockPos pos, int tintIndex) {
        //Color for Block
        return tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : color.getHex();
    }

    @Override
    public int getColor(int tintIndex) {
        //Color for Block
        return tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() :  color.getHex();
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initModel() {

    }
}
