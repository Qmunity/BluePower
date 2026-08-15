package com.bluepowermod.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;

public class RedstoneHelper {

    public static int getVanillaSignalStrength(Level level, BlockPos pos, Direction side, Direction face) {
        if (face != Direction.DOWN && face != null)
            return 0;

        BlockState blockState = level.getBlockState(pos);

        if (blockState.getBlock() == Blocks.REDSTONE_WIRE) {
            if (side == Direction.DOWN)
                return blockState.getValue(RedStoneWireBlock.POWER);
            if (side ==Direction.UP)
                return 0;
        }
        if (blockState.getBlock() instanceof ComparatorBlock)
            if (!blockState.getValue(DiodeBlock.POWERED)) {
                return 0;
            }
        if (blockState.getBlock() == Blocks.REPEATER) {
            if (side == Direction.DOWN || side == Direction.UP)
                return 0;
        }
        return blockState.getSignal(level, pos, side);
    }

    public static boolean canConnectVanilla(Level world, BlockPos pos, Direction side, Direction face) {

        if (side == null)
            return false;

        Block block = world.getBlockState(pos).getBlock();
        if ((block == Blocks.REPEATER)
                && (face == Direction.UP || face == null))
            if (world.getBlockState(pos).getValue(RepeaterBlock.FACING) == side.getOpposite())
                return true;

        if (block instanceof LeverBlock) {
            if (face == null) return true;
            BlockState state = world.getBlockState(pos);
            AttachFace attachFace = state.getValue(LeverBlock.FACE);
            Direction facing = state.getValue(LeverBlock.FACING);
            return switch (face){
                case UP -> {
                    yield attachFace == AttachFace.FLOOR || attachFace == AttachFace.WALL;
                }
                case DOWN -> {
                    yield attachFace == AttachFace.CEILING || attachFace == AttachFace.WALL;
                }
                default -> {
                    yield (attachFace == AttachFace.WALL) ||
                            (attachFace == AttachFace.CEILING && side == Direction.DOWN) ||
                            (attachFace == AttachFace.FLOOR && side == Direction.UP);
                }
            };
        }

        if (block instanceof ComparatorBlock && (face == Direction.UP || face == null))
            return side != Direction.DOWN;

        if (block instanceof RedStoneWireBlock)
            return face == null || face == Direction.UP;

        return block instanceof DoorBlock || block instanceof RedstoneLampBlock || block instanceof TntBlock
                || block instanceof DispenserBlock || block instanceof NoteBlock
                || block instanceof PistonBaseBlock;// true;
    }

    private static boolean isVanillaBlock(Level world, BlockPos pos) {

        Block b = world.getBlockState(pos).getBlock();
        return b instanceof RepeaterBlock || b instanceof LeverBlock || b instanceof RedStoneWireBlock
                || b instanceof ComparatorBlock || b instanceof DoorBlock || b instanceof RedstoneLampBlock
                || b instanceof TntBlock || b instanceof DispenserBlock || b instanceof NoteBlock
                || b instanceof PistonBaseBlock;
    }

    public static boolean isVanillaRedstoneSink(BlockState state){
        Block b = state.getBlock();
        return b instanceof DoorBlock || b instanceof RedstoneLampBlock || b instanceof TntBlock || b instanceof DispenserBlock
                || b instanceof NoteBlock || b instanceof PistonBaseBlock;
    }

    public static boolean isVanillaSource(BlockState state){
        Block b = state.getBlock();
        return b instanceof LeverBlock || b instanceof RedstoneTorchBlock || b instanceof RedstoneWallTorchBlock || b == Blocks.REDSTONE_BLOCK;
    }

    @Deprecated
    public static int getOutputWeak(Level world, int x, int y, int z, Direction side, Direction face) {

        return getOutputWeak(world, new BlockPos(x, y, z), side, face);
    }

    public static int getOutputWeak(Level world, BlockPos pos, Direction side, Direction face) {
        BlockState blockState = world.getBlockState(pos);

        int power = blockState.getSignal(world, pos, side.getOpposite());
        if (power > 0)
            return power;

        if (blockState.getBlock().shouldCheckWeakPower(blockState, world, pos, side)) {
            for (Direction d : Direction.values()) {
                if (d == side)
                    continue;
                power = Math.max(power,
                        getOutputStrong(world, pos.relative(d), d));
            }
        }

        return power;
    }

    public static int getOutputStrong(Level world, BlockPos pos, Direction side, Direction face) {
        int power = getVanillaSignalStrength(world, pos, side, face);
        if (power > 0)
            return power;

        return world.getBlockState(pos).getDirectSignal(world, pos, side.getOpposite());
    }

    public static int getOutputWeak(Level world, BlockPos pos, Direction side) {

        return getOutputWeak(world, pos, side, null);
    }

    public static int getOutputStrong(Level world, BlockPos pos, Direction side) {

        return getOutputStrong(world, pos, side, null);
    }

    public static int getOutput(Level world, BlockPos pos, Direction side) {

        return Math.max(getOutputWeak(world, pos, side), getOutputStrong(world, pos, side));
    }

    public static int getOutput(Level world, BlockPos pos, Direction side, Direction face) {

        return Math.max(getOutputWeak(world, pos, side, face), getOutputStrong(world, pos, side, face));
    }

    public static int getOutput(Level world, BlockPos pos) {

        int power = 0;
        for (Direction side : Direction.values())
            power = Math.max(power, getOutput(world, pos, side));
        return power;
    }

    public static int getInputWeak(Level world, BlockPos pos, Direction side, Direction face) {

        return getOutputWeak(world, pos.relative(side), side.getOpposite(), face);
    }

    public static int getInputStrong(Level world, BlockPos pos, Direction side, Direction face) {

        return getOutputStrong(world, pos.relative(side), side.getOpposite(), face);
    }

    public static int getInputWeak(Level world, BlockPos pos, Direction side) {

        return getOutputWeak(world, pos.relative(side), side.getOpposite());
    }

    public static int getInputStrong(Level world, BlockPos pos, Direction side) {

        return getOutputStrong(world, pos.relative(side), side.getOpposite());
    }


    public static int getInput(Level world, BlockPos pos, Direction side) {

        return getOutput(world, pos.relative(side), side.getOpposite());
    }

    public static int getInput(Level world, BlockPos pos, Direction side, Direction face) {

        return getOutput(world, pos.relative(side), side.getOpposite(), face);
    }


    public static int getInput(Level world, BlockPos pos) {

        int power = 0;
        for (Direction side : Direction.values())
            power = Math.max(power, getInput(world, pos, side));
        return power;
    }

    public static boolean canConnect(Level world, BlockPos pos, Direction side, Direction face) {
        if (isVanillaBlock(world, pos))
            return canConnectVanilla(world, pos, side, face);

        try {
            return world.getBlockState(pos).canRedstoneConnectTo(world, pos, side);
        } catch (Exception ex) {
            // ex.printStackTrace();
        }
        return false;
    }

    public static boolean canConnect(Level world, BlockPos pos, Direction side) {
        return canConnect(world, pos, side, null);
    }

    public static void notifyRedstoneUpdate(Level world, BlockPos pos, Direction direction, boolean strong) {

        if (world == null)
            return;

        Block block = world.getBlockState(pos).getBlock();

        // Weak/strong
        world.updateNeighborsAt(pos, block);

        // Strong
        if (strong)
            for (Direction d : Direction.values())
                if (d != direction.getOpposite())
                    world.updateNeighborsAt(pos.relative(d), block);
    }
}