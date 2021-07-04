/*
 * This file is part of Blue Power. Blue Power is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version. Blue Power is
 * distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General Public License along
 * with Blue Power. If not, see <http://www.gnu.org/licenses/>
 */

package com.bluepowermod.item;

import com.bluepowermod.api.multipart.IBPPartBlock;
import com.bluepowermod.block.BlockBPMultipart;
import com.bluepowermod.init.BPBlocks;
import com.bluepowermod.tile.TileBPMultipart;
import com.bluepowermod.util.AABBUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;

/**
 * IBPPartBlock's use this rather then BlockItem for their Items.
 * @author MoreThanHidden
 */
public class ItemBPPart extends BlockItem {
    public ItemBPPart(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public ActionResultType place(BlockItemUseContext context) {
        BlockState state = context.getLevel().getBlockState(context.getClickedPos());
        BlockState thisState = getBlock().getStateForPlacement(context);

        if(state.getBlock() instanceof IBPPartBlock && thisState != null && !AABBUtils.testOcclusion(((IBPPartBlock)thisState.getBlock()).getOcclusionShape(thisState), state.getShape(context.getLevel(), context.getClickedPos()))) {

            //Save the Tile Entity Data
            CompoundNBT nbt = new CompoundNBT();
            TileEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if(tileEntity != null){
                nbt = tileEntity.save(nbt);
            }

            //Replace with Multipart
            context.getLevel().setBlockAndUpdate(context.getClickedPos(), BPBlocks.multipart.defaultBlockState());
            tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if(tileEntity instanceof TileBPMultipart){
                //Add the original State to the Multipart
                ((TileBPMultipart) tileEntity).addState(state);

                //Restore the Tile Entity Data
                TileEntity tile = ((TileBPMultipart) tileEntity).getTileForState(state);
                if (tile != null)
                    tile.load(state, nbt);

                //Add the new State
                ((TileBPMultipart) tileEntity).addState(thisState);
                thisState.getBlock().setPlacedBy( context.getLevel(),context.getClickedPos(), thisState, context.getPlayer(), context.getItemInHand());
            }
            //Update Self
            state.neighborChanged(context.getLevel(), context.getClickedPos(), state.getBlock(), context.getClickedPos(), false);
            context.getItemInHand().shrink(1);
            //Place Sound
            context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            return ActionResultType.SUCCESS;

        }else if(state.getBlock() instanceof BlockBPMultipart && thisState != null && !AABBUtils.testOcclusion(((IBPPartBlock)thisState.getBlock()).getOcclusionShape(thisState), state.getShape(context.getLevel(), context.getClickedPos()))) {

            // Add to the Existing Multipart
            TileEntity tileEntity = context.getLevel().getBlockEntity(context.getClickedPos());
            if (tileEntity instanceof TileBPMultipart) {
                ((TileBPMultipart) tileEntity).addState(thisState);
                thisState.getBlock().setPlacedBy( context.getLevel(),context.getClickedPos(), thisState, context.getPlayer(), context.getItemInHand());
                //Update Neighbors
                for(Direction dir : Direction.values()){
                    context.getLevel().getBlockState(context.getClickedPos().relative(dir)).neighborChanged(context.getLevel(), context.getClickedPos().relative(dir), state.getBlock(), context.getClickedPos(), false);
                }
                //Update Self
                state.neighborChanged(context.getLevel(), context.getClickedPos(), state.getBlock(), context.getClickedPos(), false);
                context.getItemInHand().shrink(1);
                //Place Sound
                context.getLevel().playSound(null, context.getClickedPos(), SoundEvents.STONE_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F);
                return ActionResultType.SUCCESS;
            }

        }
        return super.place(context);
    }

    @Override
    protected boolean canPlace(BlockItemUseContext context, BlockState state) {
        return true;
    }
}