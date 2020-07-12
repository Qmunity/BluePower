package com.bluepowermod.block.machine;

import com.bluepowermod.api.misc.MinecraftColor;
import com.bluepowermod.api.wire.redstone.RedwireType;
import com.bluepowermod.client.render.ICustomModelBlock;
import com.bluepowermod.reference.Refs;
import net.minecraft.block.Block;
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
        super(type);
        this.color = color;
        this.setDefaultState(this.getStateContainer().getBaseState().with(FACING, Direction.UP).with(CONNECTED_FRONT, false).with(CONNECTED_BACK, false).with(CONNECTED_LEFT, false).with(CONNECTED_RIGHT, false).with(STRAIGHT, 1).with(POWERED, false));
        setRegistryName(Refs.MODID + ":" + "wire." + type + "." + color.name().toLowerCase());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder){
        builder.add(FACING, CONNECTED_FRONT, CONNECTED_BACK, CONNECTED_LEFT, CONNECTED_RIGHT, STRAIGHT, POWERED, WATERLOGGED);
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
