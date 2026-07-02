package com.bluepowermod.item;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.block.BlockBPMultipart;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.StandingAndWallBlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class RedstoneTorchItem extends StandingAndWallBlockItem {
    final Block wallBlock;
    final Direction attachmentDirection;

    public RedstoneTorchItem(Block block, Block wallBlock, Properties properties, Direction attachmentDirection) {
        super(block, wallBlock, properties, attachmentDirection);
        this.wallBlock = wallBlock;
        this.attachmentDirection = attachmentDirection;
    }

    protected boolean canPlace(LevelReader level, BlockState state, BlockPos pos) {
        return state.canSurvive(level, pos);
    }

    public BlockState getTorchPlacementState(BlockPlaceContext context) {
        BlockState blockstate = this.wallBlock.getStateForPlacement(context);
        BlockState blockstate1 = null;
        LevelReader levelreader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();

        for(Direction direction : context.getNearestLookingDirections()) {
            if (direction != this.attachmentDirection.getOpposite()) {
                BlockState blockstate2 = direction == this.attachmentDirection ? this.getBlock().getStateForPlacement(context) : blockstate;
                if (blockstate2 != null && this.canPlace(levelreader, blockstate2, blockpos)) {
                    blockstate1 = blockstate2;
                    break;
                }
            }
        }

        return blockstate1;
    }

    public BlockState getPartPlacementState(BlockPlaceContext context) {
        BlockState state = getTorchPlacementState(context);
        return state.getBlock() == wallBlock ? state : null;
    }

    @Override
    protected @Nullable BlockState getPlacementState(BlockPlaceContext context) {
        BlockState state = getTorchPlacementState(context);
        return state != null && context.getLevel().isUnobstructed(state, context.getClickedPos(), CollisionContext.empty()) ? state : null;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        BlockState thisState = getPartPlacementState(context);

        if(state.getBlock() instanceof IBPPartBlock && thisState != null && !AABBUtils.testOcclusion(((IBPPartBlock)thisState.getBlock()).getOcclusionShape(thisState), state.getShape(context.getLevel(), context.getClickedPos()))) {

            //Save the Tile Entity Data
            CompoundTag nbt = new CompoundTag();
            BlockEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if(tileEntity != null){
                nbt = tileEntity.saveWithoutMetadata();
            }

            //Replace with Multipart
            context.getLevel().setBlockAndUpdate(context.getClickedPos(), BPBlocks.multipart.get().defaultBlockState());
            tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if(tileEntity instanceof TileBPMultipart multipart){
                //Add the original State to the Multipart
                multipart.addState(state);

                //Restore the Tile Entity Data
                BlockEntity tile = multipart.getTileForState(state);
                if (tile != null)
                    tile.load(nbt);

                //Add the new State
                multipart.addState(thisState);
                thisState.getBlock().setPlacedBy( context.getLevel(),context.getClickedPos(), thisState, context.getPlayer(), context.getItemInHand());
            }
            //Update Self
            state.neighborChanged(context.getLevel(), context.getClickedPos(), state.getBlock(), context.getClickedPos(), false);
            context.getItemInHand().shrink(1);
            //Place Sound
            context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            return InteractionResult.SUCCESS;

        }else if(state.getBlock() instanceof BlockBPMultipart && thisState != null && !AABBUtils.testOcclusion(((IBPPartBlock)thisState.getBlock()).getOcclusionShape(thisState), state.getShape(context.getLevel(), context.getClickedPos()))) {

            // Add to the Existing Multipart
            BlockEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if (tileEntity instanceof TileBPMultipart multipart) {
                multipart.addState(thisState);
                thisState.getBlock().setPlacedBy( context.getLevel(),context.getClickedPos(), thisState, context.getPlayer(), context.getItemInHand());
                //Update Neighbors
                for(Direction dir : Direction.values()){
                    context.getLevel().getBlockState(context.getClickedPos().relative(dir)).neighborChanged(context.getLevel(), context.getClickedPos().relative(dir), state.getBlock(), context.getClickedPos(), false);
                }
                //Update Self
                state.neighborChanged(context.getLevel(), context.getClickedPos(), state.getBlock(), context.getClickedPos(), false);
                context.getItemInHand().shrink(1);
                //Place Sound
                context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.STONE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

        }
        return super.place(context);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        return true;
    }

    @Override
    public void registerBlocks(Map<Block, Item> blockToItemMap, Item item) {
        super.registerBlocks(blockToItemMap, item);
        blockToItemMap.put(this.wallBlock, item);
    }
}
