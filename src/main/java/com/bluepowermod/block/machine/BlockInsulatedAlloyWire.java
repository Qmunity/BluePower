package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.ICustomModelBlock;
import com.bluepowermod.reference.Refs;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class BlockInsulatedAlloyWire extends BlockAlloyWire implements ICustomModelBlock{

    private final MinecraftColor color;
    protected final VoxelShape[] shapes = makeShapes();

    public BlockInsulatedAlloyWire(String type, MinecraftColor color) {
        super(type, Material.ROCK);
        this.color = color;
        setDefaultState(getStateContainer().getBaseState().with(FACING, Direction.UP).with(POWERED, false)
                .with(CONNECTED_FRONT, false)
                .with(CONNECTED_BACK, false)
                .with(CONNECTED_LEFT, false)
                .with(CONNECTED_RIGHT, false)
                .with(JOIN_FRONT, false)
                .with(JOIN_BACK, false)
                .with(JOIN_LEFT, false)
                .with(JOIN_RIGHT, false)
        );
        setRegistryName(Refs.MODID + ":" + "wire." + type + "." + color.name().toLowerCase());
    }

    private int getShapeIndex(BlockState state) {
        int i = 0;

        if(state.get(CONNECTED_FRONT))
            i |= getMask(Direction.NORTH);
        if(state.get(CONNECTED_BACK))
            i |= getMask(Direction.SOUTH);
        if(state.get(CONNECTED_LEFT))
            i |= getMask(Direction.WEST);
        if(state.get(CONNECTED_RIGHT))
            i |= getMask(Direction.EAST);

        return i;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        World worldIn = context.getWorld();
        BlockPos pos = context.getPos();
        boolean connection_back = false;
        boolean connection_front = false;
        boolean connection_left = false;
        boolean connection_right = false;

        for (Direction face : FACING.getAllowedValues()){
            switch (face){
                case NORTH:
                    if(worldIn.getBlockState(pos.offset(face)).getBlock().equals(this)){
                        connection_front = true;
                    }
                    break;
                case SOUTH:
                    if(worldIn.getBlockState(pos.offset(face)).getBlock().equals(this)){
                        connection_back = true;
                    }
                    break;
                case WEST:
                    if(worldIn.getBlockState(pos.offset(face)).getBlock().equals(this)){
                        connection_left = true;
                    }
                    break;
                case EAST:
                    if(worldIn.getBlockState(pos.offset(face)).getBlock().equals(this)){
                        connection_right = true;
                    }
                    break;
            }
        }

        return this.getDefaultState().with(CONNECTED_RIGHT, connection_right)
                .with(CONNECTED_LEFT, connection_left)
                .with(CONNECTED_FRONT, connection_front)
                .with(CONNECTED_BACK, connection_back);
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
