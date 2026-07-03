package com.bluepowermod.mixin;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.tile.TileBPMultipart;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.world.level.block.FaceAttachedHorizontalDirectionalBlock.FACE;
import static net.minecraft.world.level.block.HorizontalDirectionalBlock.FACING;
import static net.minecraft.world.level.block.LeverBlock.POWERED;

@Mixin(LeverBlock.class)
public abstract class LeverBlockMixin implements IBPPartBlock {
    @Shadow
    @Final
    protected static VoxelShape UP_AABB_X;

    @Shadow
    @Final
    protected static VoxelShape UP_AABB_Z;

    @Shadow
    @Final
    protected static VoxelShape EAST_AABB;

    @Shadow
    @Final
    protected static VoxelShape WEST_AABB;

    @Shadow
    @Final
    protected static VoxelShape SOUTH_AABB;

    @Shadow
    @Final
    protected static VoxelShape NORTH_AABB;

    @Shadow
    @Final
    protected static VoxelShape DOWN_AABB_X;

    @Shadow
    @Final
    protected static VoxelShape DOWN_AABB_Z;

    @Shadow
    protected abstract void updateNeighbours(BlockState state, Level level, BlockPos pos);

    @Inject(method = "pull", at = @At("HEAD"), cancellable = true)
    private void bluepower$injectPull(BlockState state, Level level, BlockPos pos, CallbackInfoReturnable<BlockState> cir){
        if (level.getBlockEntity(pos) instanceof TileBPMultipart multipart){
            BlockState newState = state.cycle(POWERED);
            multipart.changeState(state, newState);
            this.updateNeighbours(state, level, pos);
            cir.setReturnValue(newState);
        }
    }

    @Override
    public VoxelShape getOcclusionShape(BlockState state) {
        return switch (state.getValue(FACE)) {
            case FLOOR -> {
                if (state.getValue(FACING).getAxis() == Axis.X) {
                    yield UP_AABB_X;
                }
                yield UP_AABB_Z;
            }
            case WALL -> {
                switch (state.getValue(FACING)) {
                    case EAST -> {
                        yield EAST_AABB;
                    }
                    case WEST -> {
                        yield WEST_AABB;
                    }
                    case SOUTH -> {
                        yield SOUTH_AABB;
                    }
                    default -> {
                        yield NORTH_AABB;
                    }
                }
            }
            case CEILING -> {
                Axis axis = state.getValue(FACING).getAxis();
                if (axis == Axis.X) {
                    yield DOWN_AABB_X;
                }
                yield DOWN_AABB_Z;
            }
        };
    }
}
