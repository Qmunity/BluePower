package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.ICustomModelBlock;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.state.IntegerProperty;
import net.minecraft.block.BlockState;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class BlockInsulatedAlloyWire extends BlockAlloyWire implements ICustomModelBlock{

    private final MinecraftColor color;

    public static final IntegerProperty STRAIGHT = IntegerProperty.create("straight", 0, 5);

    public BlockInsulatedAlloyWire(String type, MinecraftColor color) {
        super(type, Material.ROCK);
        this.color = color;
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.UP).with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false).with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false).with(STRAIGHT, 1).with(POWERED, false));
        setRegistryName(Refs.MODID + ":" + "wire." + type + "." + color.name().toLowerCase());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, STRAIGHT, POWERED);
    }

    @Override
    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld worldIn, BlockPos pos, BlockPos pos2, Hand hand) {
        Boolean connected_back = state.get(CONNECTED_BACK);
        Boolean connected_front = state.get(CONNECTED_FRONT);
        Boolean connected_left = state.get(CONNECTED_LEFT);
        Boolean connected_right = state.get(CONNECTED_RIGHT);

        int connections = 0;
        int straight = 1;

        for (Direction face : FACING.getAllowedValues()){
            switch (face){
                case NORTH:
                    connected_front = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    if(connected_front){
                        connections += 1;
                        straight = 4;
                    }
                    break;
                case SOUTH:
                    connected_back = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    if(connected_back){
                        connections += 1;
                        straight = 3;
                    }
                    break;
                case WEST:
                    connected_left = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    if(connected_left){
                        connections += 1;
                        straight = 5;
                    }
                    break;
                case EAST:
                    connected_right = worldIn.getBlockState(pos.offset(face)).getBlock().equals(this);
                    if(connected_right){
                        connections += 1;
                        straight = 2;
                    }
                    break;
            }
        }
        if(connections > 1){
            straight = 0;
        }

        return super.getStateForPlacement(state, facing, state2, worldIn, pos, pos2, hand)
                .with(CONNECTED_RIGHT, connected_right)
                .with(CONNECTED_LEFT, connected_left)
                .with(CONNECTED_FRONT, connected_front)
                .with(CONNECTED_BACK, connected_back)
                .with(STRAIGHT, straight);
    }

    @Override
    public int getColor(IBlockReader world, BlockPos pos, int tintIndex) {
        //Color for Block
        return tintIndex == 1 ? color.getHex() : tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

    @Override
    public int getColor(int tintIndex) {
        //Color for Block
        return tintIndex == 1 ? color.getHex() : tintIndex == 2 ? RedwireType.RED_ALLOY.getName().equals(type) ? MinecraftColor.RED.getHex() : MinecraftColor.BLUE.getHex() : -1;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initModel() {

    }
}
